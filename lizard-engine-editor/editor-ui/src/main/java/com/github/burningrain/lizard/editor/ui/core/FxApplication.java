package com.github.burningrain.lizard.editor.ui.core;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import com.github.burningrain.lizard.editor.ui.core.config.CoreConfig;

import java.util.Arrays;
import java.util.stream.Stream;

@SpringBootApplication
public abstract class FxApplication extends Application {

    public static String[] arguments;

    public void start(Stage primaryStage) {
        Class[] embeddedConfigs = new Class[]{CoreConfig.class};

        SpringApplicationBuilder builder = new SpringApplicationBuilder()
                .sources(
                        Stream.concat(Arrays.stream(sources()), Arrays.stream(embeddedConfigs)).toArray(Class[]::new)
                );

        // спринг сам силой ставит безголовый режим для swing. см. org.springframework.boot.SpringApplication#configureHeadlessProperty
        System.setProperty("java.awt.headless", System.getProperty("java.awt.headless", "false"));
        ConfigurableApplicationContext context;
        if (arguments != null && arguments.length != 0) {
            context = builder.run(arguments);
        } else {
            context = builder.run();
        }

        StageManager bean = context.getBean(StageManager.class);
        bean.start(primaryStage);
        bean.activate();
        bean.afterActivation();
    }

    protected abstract Class[] sources();

}
