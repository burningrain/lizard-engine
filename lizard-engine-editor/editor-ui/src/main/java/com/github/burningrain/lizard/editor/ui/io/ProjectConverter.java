package com.github.burningrain.lizard.editor.ui.io;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.burningrain.lizard.editor.api.ProcessPropertiesInspectorBinder;
import com.github.burningrain.lizard.editor.api.project.ProcessViewModel;
import com.github.burningrain.lizard.editor.api.project.ProjectModel;
import com.github.burningrain.lizard.editor.api.project.model.ProcessElementType;
import com.github.burningrain.lizard.editor.api.project.model.descriptor.ElementType;
import com.github.burningrain.lizard.editor.api.project.model.descriptor.PluginDependency;
import com.github.burningrain.lizard.editor.api.project.model.descriptor.PluginDescriptor;
import com.github.burningrain.lizard.editor.ui.model.ProcessElementsWrapper;
import com.github.burningrain.lizard.editor.ui.model.ProcessViewModelImpl;
import com.github.burningrain.lizard.editor.ui.model.Store;
import com.github.burningrain.lizard.editor.ui.model.defaultmodel.DefaultProcessElementsExtPoint;
import com.github.burningrain.lizard.editor.ui.utils.ProjectUtils;
import org.jgrapht.nio.ImportException;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

//todo прицепить к сохранению/открытию файлика проекта .lzd
public class ProjectConverter {

    private static final String DESCRIPTOR_JSON = "descriptor.json";
    private static final String PROCESS_GRAPHML = "process.graphml";

    private final PluginManager pluginManager;
    private final ProcessIOConverter processIOConverter;
    private final Store store;

    private final ObjectMapper ioObjectMapper;

    public ProjectConverter(Store store, PluginManager pluginManager, ProcessIOConverter processIOConverter) {
        this.store = store;
        this.pluginManager = pluginManager;
        this.processIOConverter = processIOConverter;
        this.ioObjectMapper = new ObjectMapper();
    }

    public ProjectModelImpl importProject(File file) throws ImportException, IOException {
        ZipFile zipFile = new ZipFile(file);
        ZipEntry entryDescription = zipFile.getEntry(DESCRIPTOR_JSON);
        ZipEntry entryProcess = zipFile.getEntry(PROCESS_GRAPHML);

        ProjectDescriptorImpl descriptor = this.ioObjectMapper.readValue(zipFile.getInputStream(entryDescription), ProjectDescriptorImpl.class);
        ProcessViewModelImpl processViewModel = processIOConverter.importProcess(zipFile.getInputStream(entryProcess));
        processViewModel.setProcessName(descriptor.getTitle());
        processViewModel.setDescription(descriptor.getDescription());

        Map<String, ProcessPropertiesInspectorBinder> processPropertyBinders = store.getProcessPropertyBinders();
        descriptor.getPluginsProcessData().forEach((pluginId, pluginProcessData) -> {
            ProcessPropertiesInspectorBinder inspectorBinder = processPropertyBinders.get(pluginId);
            if (inspectorBinder == null) {
                store.processElementsProperty().put(
                        pluginId,
                        new ProcessElementsWrapper(pluginId, Collections.singletonList(new DefaultProcessElementsExtPoint()))
                );
                inspectorBinder = store.getProcessPropertyBinders().get(pluginId);
            }
            processViewModel.putDatum(pluginId,
                    ObjectMapperUtils.createDataFromString(
                            inspectorBinder.getElementDataConverter(), pluginProcessData));
        });

        return new ProjectModelImpl(
                ProjectUtils.generateProjectId(),
                descriptor,
                processViewModel
        );
    }

    public void saveProject(String path, ProjectModel projectModel) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(path);
             ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)
        ) {
            zipOutputStream.putNextEntry(new ZipEntry(DESCRIPTOR_JSON));
            zipOutputStream.write(this.ioObjectMapper.writeValueAsBytes(createNewDescriptor(projectModel)));
            zipOutputStream.putNextEntry(new ZipEntry(PROCESS_GRAPHML));
            zipOutputStream.write(processIOConverter.exportProcess((ProcessViewModelImpl) projectModel.getProcessViewModel()));
        }
    }

    public ProjectDescriptorImpl createNewDescriptor(ProcessViewModel processViewModel) {
        return createNewDescriptor(processViewModel.getProcessName(), processViewModel.getDescription(), null);
    }

    private ProjectDescriptorImpl createNewDescriptor(ProjectModel projectModel) {
        Set<String> pluginIds = projectModel.getDescriptor().getPluginDescriptors()
                .stream()
                .map(PluginDescriptor::getPluginId)
                .collect(Collectors.toSet());
        ProcessViewModel processViewModel = projectModel.getProcessViewModel();
        return createNewDescriptor(
                processViewModel.getProcessName(),
                processViewModel.getDescription(),
                createPluginDescriptorsList(pluginIds, projectModel)
        );
    }

    public ProjectDescriptorImpl createNewDescriptor(String title, String description, Collection<PluginDescriptor> pluginDescriptors) {
        ProjectDescriptorImpl newDescriptor = new ProjectDescriptorImpl();
        newDescriptor.setTitle(title);
        newDescriptor.setDescription(description);
        newDescriptor.setAuthor(System.getProperty("user.name"));
        newDescriptor.setDate(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX").format(new Date()));
        newDescriptor.setPluginsProcessData(createPluginsProcessData());
        newDescriptor.setPluginDescriptors(pluginDescriptors);

        return newDescriptor;
    }

    private Map<String, String> createPluginsProcessData() {
        ProjectModel currentProjectModel = store.getCurrentProjectModel();
        if(currentProjectModel == null) {
            return Collections.emptyMap();
        }

        HashMap<String, Serializable> data = (HashMap<String, Serializable>) currentProjectModel.getProcessViewModel().getData();
        Map<String, ProcessPropertiesInspectorBinder> processPropertyBinders = store.getProcessPropertyBinders();
        return data.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> ObjectMapperUtils.createDataFormModel(
                        processPropertyBinders.get(entry.getKey()).getElementDataConverter(),
                        entry.getValue()
                )
        ));
    }

    private Map<String, Collection<ElementType>> createElementTypes(Collection<ProcessElementsWrapper> processElementsWrappers) {
        LinkedHashSet<ElementType> elements = new LinkedHashSet<>();
        processElementsWrappers.forEach(processElementsWrapper -> {
            elements.addAll(
                    processElementsWrapper.getVertexFactories().values().stream().map(vertexFactoryWrapper -> {
                        ProcessElementType type = vertexFactoryWrapper.getType();
                        return new ElementType(type.getPluginId(), type.getElementName(), type.getDescription());
                    }).collect(Collectors.toSet())
            );
            elements.addAll(
                    processElementsWrapper.getEdgeFactories().values().stream().map(edgeFactoryWrapper -> {
                        ProcessElementType type = edgeFactoryWrapper.getType();
                        return new ElementType(type.getPluginId(), type.getElementName(), type.getDescription());
                    }).collect(Collectors.toSet())
            );
        });

        HashMap<String, Collection<ElementType>> result = new HashMap<>();
        elements.forEach(elementType -> {
            Collection<ElementType> elementTypes = result.computeIfAbsent(elementType.getPluginId(), k -> new LinkedHashSet<>());
            elementTypes.add(elementType);
        });

        return result;
    }

    public List<PluginDescriptor> createPluginDescriptorsList(Set<String> pluginIdsSet, ProjectModel projectModel) {
        Map<String, PluginDescriptor> oldPluginDescriptors = projectModel == null ?
                Collections.emptyMap() : projectModel
                .getDescriptor()
                .getPluginDescriptors()
                .stream()
                .collect(Collectors.toMap(
                        PluginDescriptor::getPluginId,
                        pluginDescriptor -> pluginDescriptor
                ));
        Map<String, org.pf4j.PluginDescriptor> actualPluginDescriptors = pluginManager.getStartedPlugins().stream()
                .filter(wrapper -> pluginIdsSet.contains(wrapper.getPluginId()))
                .collect(
                        Collectors.toMap(
                                PluginWrapper::getPluginId,
                                PluginWrapper::getDescriptor)
                );

        Collection<ProcessElementsWrapper> processElementsWrappers = pluginIdsSet
                .stream()
                .map(pluginId -> store.getProcessElements().get(pluginId))
                .toList();
        Map<String, Collection<ElementType>> elementTypes = createElementTypes(processElementsWrappers);

        ArrayList<PluginDescriptor> pluginDescriptorsResult = new ArrayList<>();
        pluginIdsSet.forEach(pluginId -> {
            //todo вот здесь подумать над сравнением версией и какую модель версионирования брать
            PluginDescriptor result = null;
            org.pf4j.PluginDescriptor pluginDescriptor = actualPluginDescriptors.get(pluginId);
            if (pluginDescriptor != null) {
                result = createPluginDescriptor(pluginDescriptor, elementTypes.get(pluginId));
            } else {
                PluginDescriptor oldPluginDescriptor = oldPluginDescriptors.get(pluginId);
                if (oldPluginDescriptor != null) {
                    result = createPluginDescriptor(oldPluginDescriptor);
                }
                //todo мы не рассматриваем случай, когда не найден нигде. А вот на всякий случай можно
            }
            pluginDescriptorsResult.add(result);
        });
        return pluginDescriptorsResult;
    }

    private PluginDescriptor createPluginDescriptor(org.pf4j.PluginDescriptor pluginDescriptor, Collection<ElementType> elements) {
        PluginDescriptor result = new PluginDescriptor();
        result.setPluginId(pluginDescriptor.getPluginId());
        result.setPluginDescription(pluginDescriptor.getPluginDescription());
        result.setPluginClass(pluginDescriptor.getPluginClass());
        result.setVersion(pluginDescriptor.getVersion());
        result.setRequires(pluginDescriptor.getRequires());
        result.setProvider(pluginDescriptor.getProvider());
        result.setDependencies(createDependencies(pluginDescriptor.getDependencies()));
        result.setLicense(pluginDescriptor.getLicense());
        result.setElements(elements);

        return result;
    }

    private List<PluginDependency> createDependencies(List<org.pf4j.PluginDependency> dependencies) {
        return dependencies.stream().map(this::createPluginDependency).collect(Collectors.toList());
    }

    private PluginDependency createPluginDependency(org.pf4j.PluginDependency dependency) {
        PluginDependency pluginDependency = new PluginDependency();
        pluginDependency.setPluginId(dependency.getPluginId());
        pluginDependency.setVersion(dependency.getPluginVersionSupport());

        return pluginDependency;
    }

    private PluginDescriptor createPluginDescriptor(PluginDescriptor oldPluginDescriptor) {
        return oldPluginDescriptor;
    }

}
