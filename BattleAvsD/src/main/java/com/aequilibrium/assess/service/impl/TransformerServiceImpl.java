package com.aequilibrium.assess.service.impl;

import com.aequilibrium.assess.entity.Transformer;
import com.aequilibrium.assess.service.TransformerService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Comparator.comparing;

@Service
public class TransformerServiceImpl implements TransformerService {

    private static TransformerServiceImpl instance = null;

    private TransformerServiceImpl() {
        prepareTransformerDataInMap();
    }

    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new TransformerServiceImpl();
        }
    }

    public static TransformerServiceImpl getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }

    // simulate repository, and cached the data in the memory based on ConcurrentMap
    private final ConcurrentMap<Long, Transformer> transformerMap = new ConcurrentHashMap<>();

    // count, increase for each new created Transformer; thread safe
    private static AtomicLong counter = new AtomicLong(1);

    private void prepareTransformerDataInMap() {
        Transformer tf = new Transformer();
        Long lg = counter.getAndIncrement();
        tf.setId(lg);
        tf.setName("Soundwave");
        tf.setType("D");
        tf.setStrength(8);
        tf.setIntelligence(9);
        tf.setSpeed(2);
        tf.setEndurance(6);
        tf.setRank(7);
        tf.setCourage(5);
        tf.setFirepower(6);
        tf.setSkill(10);
        this.transformerMap.put(lg, tf);

        tf = new Transformer();
        lg = counter.getAndIncrement();
        tf.setId(lg);
        tf.setName("Bluestreak");
        tf.setType("A");
        tf.setStrength(6);
        tf.setIntelligence(6);
        tf.setSpeed(7);
        tf.setEndurance(9);
        tf.setRank(5);
        tf.setCourage(2);
        tf.setFirepower(9);
        tf.setSkill(7);
        this.transformerMap.put(lg, tf);

        tf = new Transformer();
        lg = counter.getAndIncrement();
        tf.setId(lg);
        tf.setName("Hubcap");
        tf.setType("A");
        tf.setStrength(4);
        tf.setIntelligence(4);
        tf.setSpeed(4);
        tf.setEndurance(4);
        tf.setRank(4);
        tf.setCourage(4);
        tf.setFirepower(4);
        tf.setSkill(4);
        this.transformerMap.put(lg, tf);
    }

    @Override
    public Transformer createTransformer(Transformer tf) {
        Long id = counter.getAndIncrement();
        tf.setId(id);
        this.transformerMap.put(id, tf);
        return tf;
    }

    @Override
    public Transformer updateTransformer(Transformer tf) {
        Long id = tf.getId();
        this.transformerMap.put(id, tf);
        return tf;
    }

    @Override
    public boolean deleteTransformerById(Long id) {
        boolean successDelete = false;
        if (id != null) {
            transformerMap.remove(id);
            successDelete = true;
        }
        return successDelete;
    }

    @Override
    public boolean deleteTransformerByName(String name) {
        boolean successDelete = false;
        for (Long key : transformerMap.keySet()) {
            if (transformerMap.get(key).getName().toUpperCase().trim().equals(name.toUpperCase().trim())) {
                transformerMap.remove(transformerMap.get(key).getId());
                successDelete = true;
                break;
            }
        }
        return successDelete;
    }

    @Override
    public Transformer getTransformerById(Long id) {
        Transformer tf = null;
        for (Long key : transformerMap.keySet()) {
            if (key.equals(id)) {
                tf = transformerMap.get(key);
                break;
            }
        }
        return tf;
    }

    @Override
    public Transformer getTransformerByName(String name) {
        Transformer tf = null;
        for (Long key : transformerMap.keySet()) {
            if (transformerMap.get(key).getName().trim().equalsIgnoreCase(name.trim())) {
                tf = transformerMap.get(key);
                break;
            }
        }
        return tf;
    }

    @Override
    public ArrayList<Transformer> getAllTransformers() {
        ArrayList<Transformer> transformers = new ArrayList<>();
        for (Long key : transformerMap.keySet()) {
            Transformer value = transformerMap.get(key);
            transformers.add(value);
        }
        return transformers;
    }

    /*
     * Process orders:
     * 1. split according to the type A/D; type A as groupA, type D as groupD;
     * 2. For groupA and groupD, sorted by rank; and according to the number of battles,
     *    make how many Transformers will be selected. If there are 2 battles, the two layuitest of
     *    groupA and groupD should be selected.
     * 3. If (courageOfTn <= courageOfTm - 4) and (strengthOfTn <= strengthOfTm -3)
     *    then do not check the overallRating, Tm wins;
     * 4. overallRating=strength+intelligence+speed+endurance+firepower, the highest Tj is winner;
     * 5. if Tj and Tk overallRating are equal, both destroyed;
     * 6. Unselected T in groupA and groupD, are skipped (outSkippedTransformers);
     * 7. The team who eliminated the largest number of the opposing team is the winner
     *
     * notes：
     *    flag: W-Winner; L-Loser; S-Survivor; V-Optimus Prime/Predaking;
     *          E-Special rules the game immediately ends; T-tie
     */
    @Override
    public Map<String, Object> checkTheWinner(int battles) {

//        int battles = 1;
        String msg = null;
        List<Transformer> groupA = new ArrayList<>();
        List<Transformer> groupD = new ArrayList<>();

        while (true) {   // flight start hear

            // 1. split according to the type A/D; type A as groupA, type D as groupD;
            //    1.1 check if there are enough of the layuitest to be battles
            //    1.2 check the special rules
            //------------------------------------------------------------------------
            int optimusPrimeOfGroupA = 0, predakingOfGroupA = 0;
            int optimusPrimeOfGroupD = 0, predakingOfGroupD = 0;
            for (Long key : transformerMap.keySet()) {
                int overallRatingTf = transformerMap.get(key).getStrength() +
                        transformerMap.get(key).getIntelligence() +
                        transformerMap.get(key).getSpeed() +
                        transformerMap.get(key).getEndurance() +
                        transformerMap.get(key).getFirepower();
                transformerMap.get(key).setOverallRating(overallRatingTf);  // used by next check
                transformerMap.get(key).setFlag("L");                       // set all default flag as Loser
                if (transformerMap.get(key).getType().equals("A")) {
                    if (transformerMap.get(key).getName().toUpperCase().equals("OPTIMUSPRIME")) {
                        optimusPrimeOfGroupA++;
                        transformerMap.get(key).setFlag("V");
                        msg = "000 Autobots wins by Optimus Prime!";
                    }
                    if (transformerMap.get(key).getName().toUpperCase().equals("PREDAKING")) {
                        predakingOfGroupA++;
                        transformerMap.get(key).setFlag("V");
                        msg = "000 Autobots wins by Predaking!";
                    }
                    groupA.add(transformerMap.get(key));
                } else {
                    if (transformerMap.get(key).getName().toUpperCase().equals("OPTIMUSPRIME")) {
                        optimusPrimeOfGroupD++;
                        transformerMap.get(key).setFlag("V");
                        msg = "000 Decepticons wins by Optimus Prime!";
                    }
                    if (transformerMap.get(key).getName().toUpperCase().equals("PREDAKING")) {
                        predakingOfGroupD++;
                        transformerMap.get(key).setFlag("V");
                        msg = "000 Decepticons wins by Predaking!";
                    }
                    groupD.add(transformerMap.get(key));
                }
            }

            // 1.1 check the special rules
            if (optimusPrimeOfGroupA > 1 || predakingOfGroupA > 1 ||
                    optimusPrimeOfGroupD > 1 || predakingOfGroupD > 1 ||
                    optimusPrimeOfGroupA + optimusPrimeOfGroupD > 1 ||
                    predakingOfGroupA + predakingOfGroupD > 1) {
                msg = "001 There are duplicated Optimus Prime or Predaking!";
                break;
            } else if (optimusPrimeOfGroupA + predakingOfGroupA > 0 &&
                    optimusPrimeOfGroupD + predakingOfGroupD > 0) {
                msg = "001 Optimus Prime or Predaking face each other!";
                break;
            } else if (optimusPrimeOfGroupA + predakingOfGroupA > 0 ||
                    optimusPrimeOfGroupD + predakingOfGroupD > 0) {
                break;
            }

            // 1.2 check if there are enough of the layuitest to be battles
            if (groupA.size() < battles || groupD.size() < battles) {
                msg = "001 Transformer less then battles!";
                break;
            }

            // 2. For groupA and groupD, sorted by rank; and according to the number of battles,
            //    make how many Transformers will be selected. If there are 2 battles, the two layuitest of
            //    groupA and groupD should be selected.
            //------------------------------------------------------------------------------------------------

            // 2.1 sorted groupA and groupD by rank
            Collections.sort(groupA, comparing(Transformer::getRank).reversed());
            Collections.sort(groupD, comparing(Transformer::getRank).reversed());

            // 2.2 top "battles"(number) Transformers from array groupA and groupD will flight,
            //     and set the others flags' as S
            int counterOfBattlesA = 1;
            for (Transformer tf : groupA) {
                if (counterOfBattlesA > battles)
                    tf.setFlag("S");  // survivor
                counterOfBattlesA++;
            }
            int counterOfBattlesD = 1;
            for (Transformer tf : groupD) {
                if (counterOfBattlesD > battles)
                    tf.setFlag("S");  // survivor
                counterOfBattlesD++;
            }

            // 3. If (courageOfTn < courageOfTm - 4) and (strengthOfTn < strengthOfTm -3)
            //    then do not check the overallRating, Tm wins;
            //-----------------------------------------------------------------------------

            // comparing the courage and strength
            boolean hasWinner = false;
            for (int i = 0; i < groupA.size() && i < battles; i++) {
                for (int j = 0; j < groupD.size() && j < battles; j++) {
                    if (groupA.get(i).getCourage() < groupD.get(j).getCourage() - 4 &&
                            groupA.get(i).getStrength() < groupD.get(j).getStrength() - 3) {
                        groupD.get(j).setFlag("W");
                        hasWinner = true;
                        msg = "002 Group D wins! The opponent has ran away.";
                        break;
                    } else if (groupA.get(i).getCourage() - 4 > groupD.get(j).getCourage() &&
                            groupA.get(i).getStrength() - 3 > groupD.get(j).getStrength()) {
                        groupA.get(i).setFlag("W");
                        hasWinner = true;
                        msg = "002 Group A wins! The opponent has ran away.";
                        break;
                    }
                }
                if (hasWinner) break;  // for i
            }
            if (hasWinner) break;  // while

            // otherwise comparing skill
            hasWinner = false;
            for (int i = 0; i < groupA.size() && i < battles; i++) {
                for (int j = 0; j < groupD.size() && j < battles; j++) {
                    if (groupA.get(i).getSkill() < groupD.get(j).getSkill() - 3) {
                        groupD.get(j).setFlag("W");
                        hasWinner = true;
                        msg = "003 Group D wins!";
                        break;
                    } else if (groupA.get(i).getSkill() - 3 > groupD.get(j).getSkill()) {
                        groupA.get(i).setFlag("W");
                        hasWinner = true;
                        msg = "003 Group A wins!";
                        break;
                    }
                }
                if (hasWinner) break;  // for i
            }
            if (hasWinner) break;  // while

            // 4. overallRating=strength+intelligence+speed+endurance+firepower, the highest Tj is winner;
            //--------------------------------------------------------------------------------------------

            // sort the overall rating for the first battles(number) of the Transformers
            Collections.sort(groupA.subList(0, battles - 1), comparing(Transformer::getOverallRating).reversed());
            Collections.sort(groupD.subList(0, battles - 1), comparing(Transformer::getOverallRating).reversed());

            hasWinner = false;
            for (int i = 0; i < groupA.size() && i < battles; i++) {
                for (int j = 0; j < groupD.size() && j < battles; j++) {
                    if (groupA.get(i).getOverallRating() > groupD.get(j).getOverallRating()) {
                        groupA.get(i).setFlag("W");
                        hasWinner = true;
                        msg = "003 Group A wins!";
                        break;
                    } else if (groupA.get(i).getOverallRating() < groupD.get(j).getOverallRating()) {
                        groupD.get(j).setFlag("W");
                        hasWinner = true;
                        msg = "003 Group D wins!";
                        break;
                    } // if they are equal, continue to check the next flight
                }
                if (hasWinner) break;  // for i
            }
            if (hasWinner) break;  // while
            else {
                msg = "004 They are tie!";
                break; // while
            }

        } // while; ends the flight


        // give the result of battles as follows:
        //---------------------------------------

        Map<String, Object> resultOfFlight = new LinkedHashMap<>();
        String winTeam = "Winning team";
        String surTeam = "Survivors from the losing team";
        boolean firstTimeA, firstTimeD;

        // information about the battles
        if (battles > 1)
            resultOfFlight.put("NumberOfBattles", battles + " battles");
        else
            resultOfFlight.put("NumberOfBattles", battles + " battle");

        switch (msg.substring(0, 3)) {

            // for msg 000, 001
            case "000":
            case "001":
                resultOfFlight.put("SpecialResult", msg);
                break;

            // for msg 002 with W, the oppponent layuitest ran away are survivors
            case "002":
                // looking for winner from group A
                firstTimeA = true;
                for (int i = 0; i < groupA.size() && i < battles; i++) {
                    if (groupA.get(i).getFlag().equals("W")) {
                        if (firstTimeA) {
                            firstTimeA = false;
                            winTeam = winTeam + " (Autobots): " + groupA.get(i).getName();
                        } else
                            winTeam = winTeam + " " + groupA.get(i).getName().trim();
                    }
                }
                // looking for winner from group D
                firstTimeD = true;
                for (int i = 0; i < groupD.size() && i < battles; i++) {
                    if (groupD.get(i).getFlag().equals("W")) {
                        if (firstTimeD) {
                            firstTimeD = false;
                            winTeam = winTeam + " (Decepticons): " + groupD.get(i).getName();
                        } else
                            winTeam = winTeam + " " + groupD.get(i).getName().trim();
                    }
                }
                // all team are survivors
                // 1. looking for survivors from group A
                if (firstTimeA) {
                    for (int i = 0; i < groupA.size(); i++) {
                        if (firstTimeA) {
                            firstTimeA = false;
                            surTeam = surTeam + " (Autobots): " + groupA.get(i).getName();
                        } else
                            surTeam = surTeam + " " + groupA.get(i).getName().trim();
                    }
                }
                // 2. looking for survivors from group D
                if (firstTimeD) {
                    for (int i = 0; i < groupD.size(); i++) {
                        if (firstTimeD) {
                            firstTimeD = false;
                            surTeam = surTeam + " (Decepticons): " + groupD.get(i).getName();
                        } else
                            surTeam = surTeam + " " + groupD.get(i).getName().trim();
                    }
                }
                resultOfFlight.put("Winning", winTeam);
                resultOfFlight.put("Survivors", surTeam);
                break;

            // for msg 003 with W
            case "003":
                // looking for winner from group A
                firstTimeA = true;
                for (int i = 0; i < groupA.size() && i < battles; i++) {
                    if (groupA.get(i).getFlag().equals("W")) {
                        if (firstTimeA) {
                            firstTimeA = false;
                            winTeam = winTeam + " (Autobots): " + groupA.get(i).getName();
                        } else
                            winTeam = winTeam + " " + groupA.get(i).getName().trim();
                    }
                }
                // looking for winner from group D
                firstTimeD = true;
                for (int i = 0; i < groupD.size() && i < battles; i++) {
                    if (groupD.get(i).getFlag().equals("W")) {
                        if (firstTimeD) {
                            firstTimeD = false;
                            winTeam = winTeam + " (Decepticons): " + groupD.get(i).getName();
                        } else
                            winTeam = winTeam + " " + groupD.get(i).getName().trim();
                    }
                }
                // survivors are exception for flights
                // 1. looking for survivors from group A
                if (firstTimeA) {
                    for (int i = battles; i < groupA.size(); i++) {
                        if (firstTimeA) {
                            firstTimeA = false;
                            surTeam = surTeam + " (Autobots): " + groupA.get(i).getName();
                        } else
                            surTeam = surTeam + " " + groupA.get(i).getName().trim();
                    }
                }
                // 2. looking for survivors from group D
                if (firstTimeD) {
                    for (int i = battles; i < groupD.size(); i++) {
                        if (firstTimeD) {
                            firstTimeD = false;
                            surTeam = surTeam + " (Decepticons): " + groupD.get(i).getName();
                        } else
                            surTeam = surTeam + " " + groupD.get(i).getName().trim();
                    }
                }
                resultOfFlight.put("Winning", winTeam);
                resultOfFlight.put("Survivors", surTeam);
                break;

            // for msg 004 without W
            case "004":
                resultOfFlight.put("BothTie", msg);
        }
        return resultOfFlight;
    }

    // for test only
    public static AtomicLong getCounter() {
        return counter;
    }

    // for test only
    public ConcurrentMap<Long, Transformer> getTransformerMap() {
        return transformerMap;
    }
}
