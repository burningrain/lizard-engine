package com.github.burningrain.lizard.editor.ui.config;

import com.github.burningrain.gvizfx.GraphView;
import com.github.burningrain.lizard.editor.ui.Dashboard;
import com.github.burningrain.lizard.editor.ui.MenuInitializerImpl;
import com.github.burningrain.lizard.editor.ui.core.MenuInitializer;
import com.github.burningrain.lizard.editor.ui.core.StageManager;
import com.github.burningrain.lizard.editor.ui.core.action.ActionFactory;
import com.github.burningrain.lizard.editor.ui.core.action.ActionManager;
import com.github.burningrain.lizard.editor.ui.model.Store;
import com.github.burningrain.lizard.editor.ui.utils.UiUtils;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.github.burningrain.lizard.editor.ui.utils.FxUtils;

@Configuration
public class UiConfig {

    //todo убрать это отсюда
    @Bean
    PluginManager pluginManager() {
        DefaultPluginManager manager = new DefaultPluginManager();
        manager.loadPlugins(); // todo загружать не здесь
        manager.startPlugins();

        return manager;
    }

    @Bean
    FxUtils fxUtils() {
        return new FxUtils();
    }

    @Bean
    StageManager dashboard() {
        return new Dashboard();
    }

    @Bean
    MenuInitializer menuInitializer(ActionManager actionManager, ActionFactory actionFactory) {
        return new MenuInitializerImpl(actionManager, actionFactory);
    }

    @Bean
    UiUtils uiUtils(Store store) {
        return new UiUtils(store);
    }

    @Bean
    GraphView graphView() {
        return new GraphView();
    }

}
