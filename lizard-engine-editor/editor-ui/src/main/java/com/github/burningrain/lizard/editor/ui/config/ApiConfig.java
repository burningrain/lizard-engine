package com.github.burningrain.lizard.editor.ui.config;

import com.github.burningrain.gvizfx.GraphView;
import com.github.burningrain.lizard.editor.api.LizardPluginApi;
import com.github.burningrain.lizard.editor.ui.api.impl.LizardPluginApiImpl;
import com.github.burningrain.lizard.editor.ui.api.impl.LizardUiApiImpl;
import com.github.burningrain.lizard.editor.ui.utils.UiUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.github.burningrain.lizard.editor.api.LizardUiApi;

@Import(UiConfig.class)
@Configuration
public class ApiConfig {

    @Bean
    LizardUiApi lizardUiApi(UiUtils uiUtils) {
        return new LizardUiApiImpl(uiUtils);
    }

    @Bean
    LizardPluginApi pluginApi(GraphView graphView) {
        return new LizardPluginApiImpl(graphView);
    }

}
