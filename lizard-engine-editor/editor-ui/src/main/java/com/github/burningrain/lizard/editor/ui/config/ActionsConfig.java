package com.github.burningrain.lizard.editor.ui.config;

import com.github.burningrain.lizard.editor.ui.HotKeysImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("com.github.burningrain.lizard.editor.ui.actions")
@Configuration
public class ActionsConfig {

    @Bean
    public HotKeysImpl hotKeys() {
        return new HotKeysImpl();
    }


}
