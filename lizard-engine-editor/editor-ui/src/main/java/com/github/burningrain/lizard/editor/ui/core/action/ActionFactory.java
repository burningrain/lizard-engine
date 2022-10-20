package com.github.burningrain.lizard.editor.ui.core.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class ActionFactory {

    @Autowired
    private ApplicationContext applicationContext;

    //достаем прототип
    public <A extends Action> A createAction(Class<A> clazz) {
        return applicationContext.getBean(clazz);
    }

}
