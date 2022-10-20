package com.github.burningrain.lizard.editor.ui.core.config;

import com.github.burningrain.lizard.editor.ui.core.HotKeys;
import com.github.burningrain.lizard.editor.ui.core.HotKeysManager;
import com.github.burningrain.lizard.editor.ui.core.action.ActionFactory;
import com.github.burningrain.lizard.editor.ui.core.action.ActionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoreConfig {

    @Bean
    ActionManager actionManager() {
        return new ActionManager();
    }

    @Bean
    ActionFactory actionFactory() {
        return new ActionFactory();
    }

    @Bean
    HotKeysManager hotKeysHandler(ActionManager actionManager, ActionFactory actionFactory, HotKeys hotKeys) {
        return new HotKeysManager(actionManager, actionFactory, hotKeys);
    }


}
