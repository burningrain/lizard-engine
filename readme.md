# Lizard-Engine

![lizard-editor with plugin](https://github.com/burningrain/lizard-engine/pics/editor_1.png?raw=true)


### Запуск из-под IDE
* добавить в VM Options следующие строки:
```shell
--module-path
"C:\javafx-sdk-18.0.2\lib"
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
```