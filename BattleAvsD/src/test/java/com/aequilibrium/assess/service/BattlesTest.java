package com.aequilibrium.assess.service;

import com.aequilibrium.assess.entity.Transformer;
import com.google.common.collect.Ordering;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.Comparator.comparing;

@RunWith(SpringRunner.class)
public class BattlesTest {
    private static final Logger log = LoggerFactory.getLogger(BattlesTest.class);

    // simulate repository, and cached the data in the memory based on ConcurrentMap
    private final ConcurrentMap<Long, Transformer> transformerMap = new ConcurrentHashMap<>();

    // count, increase for each new created Transformer; thread safe
    private static AtomicLong counter = new AtomicLong(1);

    @Test
    //--------------------------
    // test battles (main entry)
    //--------------------------
    public void checkWinner() {

        // Process orders:
        // 1. split according to the type A/D; type A as groupA, type D as groupD;
        // 2. For groupA and groupD, sorted by rank; and according to the number of battles,
        //    make how many Transformers will be selected. If there are 2 battles, the two transformers of
        //    groupA and groupD should be selected.
        // 3. If (courageOfTn <= courageOfTm - 4) and (strengthOfTn <= strengthOfTm -3)
        //    then do not check the overallRating, Tm wins;
        // 4. overallRating=strength+intelligence+speed+endurance+firepower, the highest Tj is winner;
        // 5. if Tj and Tk overallRating are equal, both destroyed;
        // 6. Unselected T in groupA and groupD, are skipped (outSkippedTransformers);
        // 7. The team who eliminated the largest number of the opposing team is the winner
        //
        // notes：
        //    flag: W-Winner; L-Loser; S-Survivor; V-Optimus Prime/Predaking;
        //          E-Special rules the game immediately ends; T-tie

        int battles = 1;
        String msg = null;
        List<Transformer> groupA = new ArrayList<>();
        List<Transformer> groupD = new ArrayList<>();

        while (true) {   // flight start hear

            // prepare the transformers' data
            //-------------------------------
            DataOf1vs1Battle();
//            DataOf2vs2Battle();

//        // duplicated names check (the name could be same)
//        //------------------------------------------------
//        List<Transformer> temp1 = new ArrayList<>();
//        for (Long key : transformerMap.keySet()) {
//            temp1.add(transformerMap.get(key));
//        }
//        Collections.sort(temp1, comparing(Transformer::getName));
//        for (int i = 0; i < temp1.size() - 1; i++) {
//            if (temp1.get(i).getName().toUpperCase()
//                    .equals(temp1.get(i + 1).getName().toUpperCase())) {
//                log.info("There are duplicated Transformers' name.");
//                System.exit(1);
//            }
//        }

            // 1. split according to the type A/D; type A as groupA, type D as groupD;
            //    1.1 check if there are enough of the transformers to be battles
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
                log.error("There are duplicated Optimus Prime or Predaking!");
                msg = "001 There are duplicated Optimus Prime or Predaking!";
                break;
            } else if (optimusPrimeOfGroupA + predakingOfGroupA > 0 &&
                    optimusPrimeOfGroupD + predakingOfGroupD > 0) {
                log.error("Optimus Prime or Predaking face each other!");
                msg = "001 Optimus Prime or Predaking face each other!";
                break;
            } else if (optimusPrimeOfGroupA + predakingOfGroupA > 0 ||
                    optimusPrimeOfGroupD + predakingOfGroupD > 0) {
                break;
            }

            // 1.2 check if there are enough of the transformers to be battles
            if (groupA.size() < battles || groupD.size() < battles) {
                log.error("Transformer less then battles!");
                msg = "001 Transformer less then battles!";
                break;
            }

            // 2. For groupA and groupD, sorted by rank; and according to the number of battles,
            //    make how many Transformers will be selected. If there are 2 battles, the two transformers of
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
                        log.info("Group D wins! The opponent has ran away.");
                        msg = "002 Group D wins! The opponent has ran away.";
                        break;
                    } else if (groupA.get(i).getCourage() - 4 > groupD.get(j).getCourage() &&
                            groupA.get(i).getStrength() - 3 > groupD.get(j).getStrength()) {
                        groupA.get(i).setFlag("W");
                        hasWinner = true;
                        log.info("Group A wins! The opponent has ran away.");
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
                        log.info("Group D wins!");
                        msg = "003 Group D wins!";
                        break;
                    } else if (groupA.get(i).getSkill() - 3 > groupD.get(j).getSkill()) {
                        groupA.get(i).setFlag("W");
                        hasWinner = true;
                        log.info("Group A wins!");
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
                        log.info("Group A wins!");
                        msg = "003 Group A wins!";
                        break;
                    } else if (groupA.get(i).getOverallRating() < groupD.get(j).getOverallRating()) {
                        groupD.get(j).setFlag("W");
                        hasWinner = true;
                        log.info("Group D wins!");
                        msg = "003 Group D wins!";
                        break;
                    } // if they are equal, continue to check the next flight
                }
                if (hasWinner) break;  // for i
            }
            if (hasWinner) break;  // while
            else {
                log.info("They are tie!");
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

            // for msg 002 with W, the oppponent transformers ran away are survivors
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

    }


    @Test
    //---------------------------------
    // sort method 2, based on Java 1.8
    //---------------------------------
    public void sortMethod2() {
        List<Transformer> list = new ArrayList<>();
        for (Long key : transformerMap.keySet()) {
            list.add(transformerMap.get(key));
        }
//        Collections.sort(list, comparing(Transformer::getRank));
//        Collections.sort(list, comparing(Transformer::getType)
//                .thenComparing(Transformer::getRank));
        Collections.sort(list, comparing(Transformer::getType)
                .thenComparing(Transformer::getRank).reversed());
        for (Transformer l : list) {
            System.out.println(l.getId() + "\t" + l.getName() + "\t" + l.getRank());
        }
    }

    @Test
    //----------------------------------------
    // sort method 1, based on Google Ordering
    //----------------------------------------
    public void sortMethod1() {
        Ordering<Transformer> ordering = Ordering.natural().reverse().nullsLast().onResultOf((Transformer) -> {
            if (Transformer == null) {
                return null;
            }
//            return Transformer.getName();
            return Transformer.getRank();
        });
        List<Transformer> list = new ArrayList<>();
        for (Long key : transformerMap.keySet()) {
            list.add(transformerMap.get(key));
        }
        List<Transformer> orderedList = ordering.sortedCopy(list);
        for (Transformer transf : orderedList) {
            System.out.println(transf.getId() + "\t" + transf.getName() + "\t" + transf.getRank());
        }
    }

    @Test
    //-------------------------------------
    // prepare the transformers' data
    //-------------------------------------
    public void DataOf1vs1Battle() {
        Transformer tf1 = new Transformer();
        Long lg = counter.getAndIncrement();
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
        this.transformerMap.put(lg, tf1);

        Transformer tf2 = new Transformer();
        lg = counter.getAndIncrement();
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
        this.transformerMap.put(lg, tf2);

        Transformer tf3 = new Transformer();
        lg = counter.getAndIncrement();
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
        this.transformerMap.put(lg, tf3);

//        for (Long key : transformerMap.keySet()) {
//            System.out.println(transformerMap.get(key));
//        }
    }

    public void DataOf2vs2Battle() {
        Transformer tf1 = new Transformer();
        Long lg = counter.getAndIncrement();
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
        this.transformerMap.put(lg, tf1);

        Transformer tf2 = new Transformer();
        lg = counter.getAndIncrement();
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
        this.transformerMap.put(lg, tf2);

        Transformer tf3 = new Transformer();
        lg = counter.getAndIncrement();
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
        this.transformerMap.put(lg, tf3);

        Transformer tf4 = new Transformer();
        lg = counter.getAndIncrement();
        tf4.setId(lg);
        tf4.setName("Predaking1");
        tf4.setType("D");
        tf4.setStrength(8);
        tf4.setIntelligence(9);
        tf4.setSpeed(2);
        tf4.setEndurance(6);
        tf4.setRank(7);
        tf4.setCourage(5);
        tf4.setFirepower(6);
        tf4.setSkill(10);
        this.transformerMap.put(lg, tf4);

    }

}
