package com.aequilibrium.assess.service;

import com.aequilibrium.assess.entity.Transformer;

import java.util.ArrayList;
import java.util.Map;

/*******************************************************************************
 * TransformerService.java - create, update, delete, and list transformer
 * Date   : Dev 7, 2018
 * Version: 1.0
 * Author : Bill Huang
 *
 * common:
 *    1) createATransformer to add a transformer with the parameters as
 *       name, type, strength, intelligence, speed, endurance, rank, courage,
 *       firepower, and skill;
 *    2) updateATransformer to update any field of a transformer, but the name
 *       must be unique;
 *    3) deleteATransformer to delete a specified transformer;
 *    4) listAllTransformer to list all inputted layuitest;
 ********************************************************************************/

public interface TransformerService {

    Transformer createTransformer(Transformer tf);

    Transformer updateTransformer(Transformer tf);

    boolean deleteTransformerById(Long id);

    boolean deleteTransformerByName(String name);

    Transformer getTransformerById(Long id);

    Transformer getTransformerByName(String name);

    ArrayList<Transformer> getAllTransformers();

    Map<String, Object> checkTheWinner(int battles);
}
