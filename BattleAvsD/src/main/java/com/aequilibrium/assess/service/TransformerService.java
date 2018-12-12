package com.aequilibrium.assess.service;

import com.aequilibrium.assess.entity.Transformer;
import com.aequilibrium.assess.repository.TransformerDataFactoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

/*******************************************************************************
 * TransformerService.java - create, update, delete, and list transformer
 * Date   : Dev 7, 2018
 * Version: 1.0
 * Author : Bill Huang
 *
 * Common:
 *    1) createATransformer to add a transformer with the parameters as
 *       name, type, strength, intelligence, speed, endurance, rank, courage,
 *       firepower, and skill;
 *    2) updateATransformer to update any field of a transformer, but the name
 *       must be unique;
 *    3) deleteATransformer to delete a specified transformer;
 *    4) listAllTransformer to list all inputted transformers;
 ********************************************************************************/

@Service
public class TransformerService {

    @Autowired
    private TransformerDataFactoryImpl tfsDataFactoryImpl;

    public Transformer createTransformerService(Transformer tf) {
        return tfsDataFactoryImpl.getInstance().createTransformerInMap(tf);
    }

    public Transformer updateTransformerService(Transformer tf) {
        return tfsDataFactoryImpl.getInstance().updateTransformerInMap(tf);
    }

    public boolean deleteATransformerService(Long id) {
        return tfsDataFactoryImpl.getInstance().deleteTransformerToMap(id);
    }

    public boolean deleteATransformerByNameService(String name) {
        return tfsDataFactoryImpl.getInstance().deleteTransformerByNameToMap(name);
    }

    public ArrayList<Transformer> getListAllTransformerService() {
        return tfsDataFactoryImpl.getInstance().getAllTransformerFromMap();
    }

    public Transformer getTransformerByIdService(Long id) {
        return tfsDataFactoryImpl.getInstance().getTransformerByIdFromMap(id);
    }

    public Transformer getTransformerByNameService(String name) {
        return tfsDataFactoryImpl.getInstance().getTransformerByNameFromMap(name);
    }

    public Map<String, Object> getBattlesResult(int battles) {
        return tfsDataFactoryImpl.getInstance().checkWinner(battles);
    }

}
