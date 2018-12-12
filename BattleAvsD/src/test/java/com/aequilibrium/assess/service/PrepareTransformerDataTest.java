package com.aequilibrium.assess.service;

import com.aequilibrium.assess.entity.Transformer;
import com.aequilibrium.assess.repository.TransformerDataFactoryImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class PrepareTransformerDataTest {

    @Test
    public void prepareTransformerDataInMap() {

        TransformerDataFactoryImpl tfdf = TransformerDataFactoryImpl.getInstance();

        Transformer tf1 = new Transformer();
        Long lg = TransformerDataFactoryImpl.getCounter().getAndIncrement();
        tf1.setId(lg);
        tf1.setName("Soundwave");
        tf1.setType("D");
        tf1.setStrength(8);
        tf1.setIntelligence(9);
        tf1.setSpeed(2);
        tf1.setEndurance(6);
        tf1.setRank(7);
        tf1.setCourage(5);
        tf1.setFirepower(6);
        tf1.setSkill(10);
        tf1 = tfdf.createTransformerInMap(tf1);

        Transformer tf2 = new Transformer();
        lg = TransformerDataFactoryImpl.getCounter().getAndIncrement();
        tf2.setId(lg);
        tf2.setName("Bluestreak");
        tf2.setType("A");
        tf2.setStrength(6);
        tf2.setIntelligence(6);
        tf2.setSpeed(7);
        tf2.setEndurance(9);
        tf2.setRank(5);
        tf2.setCourage(2);
        tf2.setFirepower(9);
        tf2.setSkill(7);
        tf2 = tfdf.createTransformerInMap(tf2);

        Transformer tf3 = new Transformer();
        lg = TransformerDataFactoryImpl.getCounter().getAndIncrement();
        tf3.setId(lg);
        tf3.setName("Hubcap");
        tf3.setType("A");
        tf3.setStrength(4);
        tf3.setIntelligence(4);
        tf3.setSpeed(4);
        tf3.setEndurance(4);
        tf3.setRank(4);
        tf3.setCourage(4);
        tf3.setFirepower(4);
        tf3.setSkill(4);
        tf3 = tfdf.createTransformerInMap(tf3);

        for (Long key : tfdf.getTransformerMap().keySet()) {
            System.out.println("value=" + tfdf.getTransformerMap().get(key) + "\tID=" + tfdf.getTransformerMap().get(key).getId());
        }

    }

}
