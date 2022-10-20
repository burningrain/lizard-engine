package com.github.burningrain.lizard.editor.ui.config;

import com.github.burningrain.lizard.editor.ui.model.Store;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelConfig {

    @Bean
    Store store() {
        return new Store();
    }

}
