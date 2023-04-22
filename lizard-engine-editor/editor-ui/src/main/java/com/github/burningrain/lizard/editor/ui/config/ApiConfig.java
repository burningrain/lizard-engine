package com.github.burningrain.lizard.editor.ui.config;

import com.github.burningrain.gvizfx.GraphView;
import com.github.burningrain.lizard.editor.api.LizardPluginApi;
import com.github.burningrain.lizard.editor.ui.api.impl.LizardPluginApiImpl;
import com.github.burningrain.lizard.editor.ui.api.impl.LizardUiApiImpl;
import com.github.burningrain.lizard.editor.ui.model.Store;
import com.github.burningrain.lizard.editor.ui.utils.UiUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.github.burningrain.lizard.editor.api.LizardUiApi;

@Import({UiConfig.class, ModelConfig.class})
@Configuration
public class ApiConfig {

    @Bean
    LizardUiApi lizardUiApi(UiUtils uiUtils, Store store, GraphView graphView) {
        return new LizardUiApiImpl(uiUtils, store, graphView);
    }

    @Bean
    LizardPluginApi pluginApi(GraphView graphView) {
        return new LizardPluginApiImpl(graphView);
    }

}
