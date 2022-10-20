package com.github.burningrain.lizard.engine.api.nodes;

import com.github.burningrain.lizard.engine.api.data.NodeData;

import java.util.List;

public interface NodeDataListener {

    void update(List<NodeData> nodeData);

}
