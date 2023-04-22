package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.br.gdx.simple.animation.io.dto.AnimationDto;
import com.github.br.gdx.simple.animation.io.dto.AnimatorDto;
import com.github.br.gdx.simple.animation.io.dto.FsmDto;
import com.github.br.gdx.simple.animation.io.dto.TransitionDto;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.process.SubFsmsDto;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.process.VariablesDto;
import com.github.burningrain.lizard.editor.api.LizardPluginApi;
import com.github.burningrain.lizard.editor.api.ext.ImportExportExtPoint;
import com.github.burningrain.lizard.engine.api.data.NodeData;
import com.github.burningrain.lizard.engine.api.data.ProcessData;
import com.github.burningrain.lizard.engine.api.data.TransitionData;
import org.pf4j.Extension;
import com.github.br.gdx.simple.animation.io.JsonConverter;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Extension
public class ImportExportExtPointImpl implements ImportExportExtPoint {

    public static final Json JSON = new Json(JsonWriter.OutputType.json);
    private final JsonConverter jsonConverter = new JsonConverter();

    @Override
    public String getExtension() {
        return "afsm";
    }

    @Override
    public ProcessData read(LizardPluginApi pluginApi, String name, byte[] bytes) {
        AnimationDto animationDto = jsonConverter.from(new String(bytes, StandardCharsets.UTF_8));

        List<NodeData> nodes = createNodes(animationDto);
        HashMap<String, NodeData> nodesMap = new HashMap<>();
        for (NodeData node : nodes) {
            nodesMap.put(node.getAttributes().get(Constants.NAME), node);
        }
        return new ProcessData(
                animationDto.getName(), //String title,
                "", //String description,
                createProcessAttributes(animationDto), //Map<String, String> attributes,
                nodes, //List< NodeData > elements,
                createTransitions(nodesMap, animationDto), //List< TransitionData > transitions,
                0 //int startElementId
        );
    }

    private List<TransitionData> createTransitions(Map<String, NodeData> nodeData, AnimationDto animationDto) {
        ArrayList<TransitionData> result = new ArrayList<>();

        //todo потом добавить subfsm
        int id = 0;
        FsmDto fsm = animationDto.getFsm();
        for (TransitionDto transition : fsm.getTransitions()) {
            result.add(createTransition(id, transition, nodeData));
            id += 10;
        }

        return result;
    }

    private TransitionData createTransition(int id, TransitionDto transition, Map<String, NodeData> nodeData) {
        return new TransitionData(
                id,                                         // int id,
                Constants.PLUGIN_ID,                        // String pluginId,
                Constants.ANIMATION_TRANSITION,             // String title,
                "",                                         // String description,
                createTransitionAttributes(transition),     // Map<String, String> attributes,
                nodeData.get(transition.getFrom()).getId(), // int sourceId,
                nodeData.get(transition.getTo()).getId(),   // int targetId,
                true,
                ""                                          // String tag
        );
    }

    public static  Map<String, String> createTransitionAttributes(TransitionDto transition) {
        HashMap<String, String> result = new HashMap<>();
        result.put(Constants.TRANSITION_ATTRIBUTES, JSON.toJson(transition));

        return result;
    }

    public static TransitionDto createTransitionDto(Map<String, String> attributes) {
        String json = attributes.get(Constants.TRANSITION_ATTRIBUTES);
        return JSON.fromJson(TransitionDto.class, json);
    }

    private List<NodeData> createNodes(AnimationDto animationDto) {
        ArrayList<NodeData> result = new ArrayList<>();

        HashMap<String, AnimatorDto> animatorMap = new HashMap<>();
        for (AnimatorDto animator : animationDto.getAnimators()) {
            animatorMap.put(animator.getName(), animator);
        }

        //todo потом добавить subfsm
        int id = 0;
        FsmDto fsm = animationDto.getFsm();
        for (String state : fsm.getStates()) {
            String titleOfType = getVertexType(fsm.getStartState(), fsm.getEndState(), state);
            result.add(toNodeData(id, titleOfType, animatorMap.get(state)));
            id += 10;
        }
        result.add( createAnyStateNode(id + 10));

        return result;
    }

    private String getVertexType(String startState, String endState, String state) {
        if(Constants.ANY_STATE.equals(state)) {
            return Constants.ANY_STATE;
        }
        if(startState.equals(state)) {
            return Constants.START_STATE;
        }
        if(endState.equals(state)) {
            return Constants.END_STATE;
        }

        return Constants.INTERMEDIATE_STATE;
    }

    private NodeData createAnyStateNode(int id) {
        HashMap<String, String> attributes = new HashMap<>();
        attributes.put(Constants.NAME, Constants.ANY_STATE);
        return new NodeData(
                id, //int id,
                Constants.PLUGIN_ID, //String pluginId,
                Constants.ANY_STATE, //String title,
                attributes, //Map<String, String> attributes,
                id, //float x,
                id, //float y,
                "", //String className,
                "" //String description
        );
    }

    private NodeData toNodeData(int id, String title, AnimatorDto animator) {
        return new NodeData(
                id, //int id,
                Constants.PLUGIN_ID, //String pluginId,
                title, //String title,
                createNodeAttribute(animator), //Map<String, String> attributes,
                id, //float x,
                id, //float y,
                "", //String className,
                "" //String description
        );
    }

    private Map<String, String> createNodeAttribute(AnimatorDto animator) {
        HashMap<String, String> result = new HashMap<>();
        result.put(Constants.NAME, animator.getName());
        result.put(Constants.FROM, String.valueOf(animator.getFrom()));
        result.put(Constants.TO, String.valueOf(animator.getTo()));
        result.put(Constants.FRAME_FREQUENCY, String.valueOf(animator.getFrameFrequency()));
        result.put(Constants.PLAY_MODE, animator.getMode().name());
        result.put(Constants.LOOPING, String.valueOf(animator.isLooping()));

        return result;
    }

    private Map<String, String> createProcessAttributes(AnimationDto animationDto) {
        HashMap<String, String> result = new HashMap<>();
        FsmDto fsm = animationDto.getFsm();
        result.put(Constants.VARIABLES, JSON.toJson(new VariablesDto(fsm.getVariables())));
        result.put(Constants.START_STATE, fsm.getStartState());
        result.put(Constants.END_STATE, fsm.getEndState());
        result.put(Constants.SUBFSMS, JSON.toJson(new SubFsmsDto(animationDto.getSubFsm())));

        return result;
    }

    @Override
    public byte[] write(LizardPluginApi pluginApi, ProcessData processData) {
        String json = jsonConverter.to(createAnimationDto(processData));
        return json.getBytes(StandardCharsets.UTF_8);
    }

    private AnimationDto createAnimationDto(ProcessData processData) {
        AnimationDto animationDto = new AnimationDto();
        animationDto.setName(processData.getTitle());
        animationDto.setAnimators(createAnimators(processData.getElements()));
        animationDto.setFsm(createFsm(processData));
        animationDto.setSubFsm(createSubFsm(processData));

        return animationDto;
    }

    private ObjectMap<String, FsmDto> createSubFsm(ProcessData processData) {
        String json = processData.getAttributes().get(Constants.SUBFSMS);
        if(json == null) {
            return null;
        }
        return JSON.fromJson(SubFsmsDto.class, json).getSubFsm();
    }

    private FsmDto createFsm(ProcessData processData) {
        Map<String, String> attributes = processData.getAttributes();

        FsmDto result = new FsmDto();
        result.setStartState(attributes.get(Constants.START_STATE));
        result.setEndState(attributes.get(Constants.END_STATE));
        result.setVariables(createVariables(attributes.get(Constants.VARIABLES)));
        result.setStates(createStates(processData.getElements()));
        result.setTransitions(createFsmTransitions(processData.getTransitions()));

        return result;
    }

    private TransitionDto[] createFsmTransitions(List<TransitionData> transitions) {
        TransitionDto[] result = new TransitionDto[transitions.size()];
        int i = 0;
        for (TransitionData transition : transitions) {
            result[i] = createTransitionDto(transition.getAttributes());
            i++;
        }
        return result;
    }

    private String[] createStates(List<NodeData> elements) {
        ArrayList<String> result = new ArrayList<>(elements.size());
        for (NodeData element : elements) {
            String name = element.getAttributes().get(Constants.NAME);
            if(Constants.ANY_STATE.equals(name)) {
                continue;
            }
            result.add(name);
        }
        return result.toArray(new String[0]);
    }

    private ObjectMap<String, String> createVariables(String variables) {
        VariablesDto variablesDto = JSON.fromJson(VariablesDto.class, variables);
        return variablesDto.getVariables();
    }

    private AnimatorDto[] createAnimators(List<NodeData> elements) {
        ArrayList<AnimatorDto> result = new ArrayList<>(elements.size());
        for (NodeData element : elements) {
            if(Constants.ANY_STATE.equals(element.getTitle())) {
                continue;
            }
            result.add(createAnimator(element));
        }

        return result.toArray(new AnimatorDto[0]);
    }

    private AnimatorDto createAnimator(NodeData element) {
        Map<String, String> attributes = element.getAttributes();

        AnimatorDto result = new AnimatorDto();
        String name = attributes.get(Constants.NAME);
        result.setName(name);
        result.setFrameFrequency(Integer.parseInt(attributes.get(Constants.FRAME_FREQUENCY)));
        result.setMode(Animation.PlayMode.valueOf(attributes.get(Constants.PLAY_MODE)));
        result.setLooping(Boolean.parseBoolean(attributes.get(Constants.LOOPING)));
        result.setFrom(Integer.parseInt(attributes.get(Constants.FROM)));
        result.setTo(Integer.parseInt(attributes.get(Constants.TO)));

        return result;
    }

}
