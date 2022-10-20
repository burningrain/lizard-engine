package com.github.burningrain.lizard.editor.ui.config;

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
    public LizardUiApi lizardUiApi(UiUtils uiUtils) {
        return new LizardUiApiImpl(uiUtils);
    }

}
