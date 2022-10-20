package com.github.burningrain.lizard.editor.ui.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ComponentScan("com.github.burningrain.lizard.editor.ui.components")
@Import(UiConfig.class)
@Configuration
public class ComponentsConfig {


}
