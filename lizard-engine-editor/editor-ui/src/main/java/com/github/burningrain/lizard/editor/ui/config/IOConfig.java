package com.github.burningrain.lizard.editor.ui.config;

import com.github.burningrain.lizard.editor.ui.io.ExportImportInnerConverter;
import com.github.burningrain.lizard.editor.ui.io.ProcessIOConverter;
import com.github.burningrain.lizard.editor.ui.io.ProjectConverter;
import com.github.burningrain.lizard.editor.ui.model.Store;
import org.pf4j.PluginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(ModelConfig.class)
@Configuration
public class IOConfig {

    @Bean
    public ProcessIOConverter processIO(Store store) {
        return new ProcessIOConverter(store);
    }

    @Bean
    public ExportImportInnerConverter exportImportService(Store store) {
        return new ExportImportInnerConverter(store);
    }

    @Bean
    public ProjectConverter projectConverter(Store store, PluginManager pluginManager, ProcessIOConverter ioConverter) {
        return new ProjectConverter(store, pluginManager, ioConverter);
    }

}
