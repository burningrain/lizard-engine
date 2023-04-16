package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.process;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.br.gdx.simple.animation.io.dto.FsmDto;

import java.io.Serializable;

public class SubFsmsDto implements Serializable {

    private ObjectMap<String, FsmDto> subFsm;

    public SubFsmsDto(ObjectMap<String, FsmDto> subFsm) {
        this.subFsm = subFsm;
    }

    public SubFsmsDto() {
    }

    public ObjectMap<String, FsmDto> getSubFsm() {
        return subFsm;
    }

    public void setSubFsm(ObjectMap<String, FsmDto> subFsm) {
        this.subFsm = subFsm;
    }

}
