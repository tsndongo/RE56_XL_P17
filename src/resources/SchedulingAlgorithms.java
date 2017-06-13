package resources;

import java.util.ArrayList;

/**
 * Created by micka on 29/05/2017.
 */

public interface SchedulingAlgorithms {

    int k = 3; //number of point use to calculate ethe moving average
    int distanceDecreaseCQI = 21; // The CQI decrease every 21 pixels
    int nbRB = 6 ; //6 RB for a 1.4MHz bandwidth

    ArrayList<ArrayList<Integer>> RoundRobin(ArrayList<UserEquipment> users);
    ArrayList<ArrayList<Integer>> BestCQI (ArrayList<UserEquipment> users);
    ArrayList<ArrayList<Integer>> MinMax(ArrayList<UserEquipment> users);
    ArrayList<ArrayList<Integer>> PF(ArrayList<UserEquipment> users);
}
