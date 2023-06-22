# Lizard-Engine

This project consists of:
```
|__ lizard-engine-api     the api between the core and the editor 
|__ lizard-engine-core    the simple finite state machine (today)
|__ lizard-engine-editor  the editor for a description of a process
   \__ editor-api         the api for foreign plugins is based on pf4j 
   |__ editor-ui          the user interface based on javafx
   |__ gviz-fx            the javafx-based library for displaying graphs
   |
   |__ planetbot-lizard-editor-plugin  the example of a plugin for the lizard-engine-editor   
```

Lizard Editor is the graph editor that is based on a plugin architecture. This project uses [pf4j](https://pi4j.com/) as a plugin system.

![lizard-editor with plugin](https://github.com/burningrain/lizard-engine/blob/main/pics/editor_1.png?raw=true)
*the lizard editor with the plugin for a game 'planet bot'*

![lizard-editor with plugin](https://github.com/burningrain/lizard-engine/blob/main/pics/editor_2.png?raw=true)
*the lizard editor with the plugin for the library 'libgdx-simple-animation'*

### Starting from IDE

* compile the project by using the next command:
```mvn clean package```

* Choose the class ```com.github.burningrain.lizard.editor.ui.LizardEditorApplication.java```

* Add to VM Options next:
```shell
--module-path
"PATH_TO_JDK/lib"
--add-modules
javafx.controls,javafx.fxml,javafx.base,javafx.web
--add-exports
java.base/sun.util.logging=ALL-UNNAMED
--add-exports
javafx.base/com.sun.javafx=ALL-UNNAMED
--add-exports
javafx.base/com.sun.javafx.binding=ALL-UNNAMED
--add-exports
javafx.base/com.sun.javafx.collections=ALL-UNNAMED
--add-exports
javafx.base/com.sun.javafx.event=ALL-UNNAMED
--add-exports
javafx.base/com.sun.javafx.property=ALL-UNNAMED
--add-exports
javafx.base/javafx.beans.property=ALL-UNNAMED
--add-exports
javafx.graphics/com.sun.javafx.application=ALL-UNNAMED
--add-exports
javafx.graphics/com.sun.javafx.font=ALL-UNNAMED
--add-exports
javafx.graphics/com.sun.javafx.geom=ALL-UNNAMED
--add-exports
javafx.graphics/com.sun.javafx.geom.transform=ALL-UNNAMED
--add-exports
javafx.graphics/com.sun.javafx.perf=ALL-UNNAMED
--add-exports
javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED
--add-exports
javafx.graphics/com.sun.javafx.scene.input=ALL-UNNAMED
--add-exports
javafx.graphics/com.sun.javafx.scene.text=ALL-UNNAMED
--add-exports
javafx.graphics/com.sun.javafx.stage=ALL-UNNAMED
--add-exports
javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED
--add-exports
javafx.graphics/com.sun.javafx.util=ALL-UNNAMED
--add-exports
javafx.graphics/com.sun.prism=ALL-UNNAMED
--add-exports
javafx.graphics/com.sun.prism.paint=ALL-UNNAMED
--add-exports
javafx.graphics/com.sun.javafx.sg.prism=ALL-UNNAMED
--add-exports
javafx.graphics/com.sun.scenario=ALL-UNNAMED
--add-exports
javafx.graphics/com.sun.scenario.animation=ALL-UNNAMED
--add-exports
javafx.controls/com.sun.javafx.charts=ALL-UNNAMED
--add-exports
javafx.controls/com.sun.javafx.scene.control=ALL-UNNAMED
--add-exports
javafx.controls/com.sun.javafx.scene.control.behavior=ALL-UNNAMED
--add-exports
javafx.controls/com.sun.javafx.scene.control.inputmap=ALL-UNNAMED
--add-exports
javafx.controls/com.sun.javafx.scene.control.skin=ALL-UNNAMED
--add-exports
javafx.controls/javafx.scene.chart=ALL-UNNAMED
--add-exports
javafx.swing/javafx.embed.swing=ALL-UNNAMED
--add-exports
javafx.swing/com.sun.javafx.embed.swing=ALL-UNNAMED
```

* If you want to use a plugin you should put it in the folder ```plugins``` and restart the application.