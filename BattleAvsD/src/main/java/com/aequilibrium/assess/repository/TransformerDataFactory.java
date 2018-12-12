package com.aequilibrium.assess.repository;

import com.aequilibrium.assess.entity.Transformer;

import java.util.ArrayList;
import java.util.Map;

public interface TransformerDataFactory {

    Transformer createTransformerInMap(Transformer tf);

    Transformer updateTransformerInMap(Transformer tf);

    boolean deleteTransformerToMap(Long id);

    boolean deleteTransformerByNameToMap(String name);

    Transformer getTransformerByIdFromMap(Long id);

    Transformer getTransformerByNameFromMap(String name);

    ArrayList<Transformer> getAllTransformerFromMap();

    Map<String, Object> checkWinner(int battles);
}
