package com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.edge;

import com.github.br.gdx.simple.animation.io.dto.TransitionDto;
import com.github.br.gdx.simple.animation.io.interpret.OperatorEnum;
import com.github.burningrain.gdx.simple.animation.lizard.editor.plugin.ImportExportExtPointImpl;
import com.github.burningrain.lizard.editor.api.ElementDataConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Map;

public class EdgeElementDataConverterImpl implements ElementDataConverter<SimpleAnimationEdgeModel> {

    @Override
    public Map<String, String> exportNodeData(SimpleAnimationEdgeModel model) {
        TransitionDto transitionDto = new TransitionDto();
        transitionDto.setFrom(model.getFrom());
        transitionDto.setTo(model.getTo());

        ObservableList<PredicateModel> fsmPredicates = model.getFsmPredicates();
        String[] predicates = new String[fsmPredicates.size()];
        int i = 0;
        for (PredicateModel fsmPredicate : fsmPredicates) {
            predicates[i] = createPredicate(fsmPredicate);
            i++;
        }
        transitionDto.setFsmPredicates(predicates);
        return ImportExportExtPointImpl.createTransitionAttributes(transitionDto);
    }

    private String createPredicate(PredicateModel fsmPredicate) {
        // порядок и пробелы очень важны, увы
        return fsmPredicate.getVariableName() + " " + fsmPredicate.getOperator() + " " + fsmPredicate.getValue();
    }

    private ObservableList<PredicateModel> createPredicates(String[] fsmPredicates) {
        ArrayList<PredicateModel> list = new ArrayList<>(fsmPredicates.length);
        for (String fsmPredicate : fsmPredicates) {
            PredicateModel model = new PredicateModel();
            String[] split = fsmPredicate.split(" ");
            model.setVariableName(split[0]);
            model.setOperator(OperatorEnum.getByValue(split[1]).getValue());
            model.setValue(split[2]);

            list.add(model);
        }

        return FXCollections.observableArrayList(list);
    }

    @Override
    public SimpleAnimationEdgeModel importNodeData(Map<String, String> data) {
        TransitionDto transitionDto = ImportExportExtPointImpl.createTransitionDto(data);
        SimpleAnimationEdgeModel result = new SimpleAnimationEdgeModel();
        result.setFrom(transitionDto.getFrom());
        result.setTo(transitionDto.getTo());
        result.setFsmPredicates(createPredicates(transitionDto.getFsmPredicates()));

        return result;
    }



}
