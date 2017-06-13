package resources;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collections;

public class Antenna implements SchedulingAlgorithms{
    public static final int RB_NUMBER = 6;
    public static final int RB_DATA_16QAM = 96;
    public Point2D position;
    public double coverage;
    private double bitPerSymbolBasedOnCQI [];

    public Antenna (){
    bitPerSymbolBasedOnCQI = new double[15];
    bitPerSymbolBasedOnCQI [0]= 0.1523;
    bitPerSymbolBasedOnCQI [1] = 0.2344;
    bitPerSymbolBasedOnCQI [2] = 0.3770;
    bitPerSymbolBasedOnCQI [3] = 0.6016;
    bitPerSymbolBasedOnCQI [4] = 0.8770;
    bitPerSymbolBasedOnCQI [5] = 1.1758;
    bitPerSymbolBasedOnCQI [6] = 1.4766;
    bitPerSymbolBasedOnCQI [7] = 1.9141;
    bitPerSymbolBasedOnCQI [8] = 2.4063;
    bitPerSymbolBasedOnCQI [9] = 2.7305;
    bitPerSymbolBasedOnCQI [10] = 3.3223;
    bitPerSymbolBasedOnCQI [11] = 3.9023;
    bitPerSymbolBasedOnCQI [12] = 4.5234;
    bitPerSymbolBasedOnCQI [13] = 5.1152;
    bitPerSymbolBasedOnCQI [14] = 5.5547;
    }

    public Antenna (double x, double y, double coverage) {
        this.position = new Point2D(x, y);
        this.coverage = coverage;
    }

    public double calculateDistance (UE ue) {
        return Math.sqrt(Math.pow(this.position.getX() - ue.position.getX() , 2) + Math.pow(this.position.getY() - ue.position.getY() , 2));
    }

    public int calculateCQI (double distance){
        return 15 - (int) distance/distanceDecreaseCQI;

    }

    public void filterInRangeUEs (ArrayList<UE> ues) {
        ArrayList<Integer> outOfRangeUEIdx = new ArrayList<Integer>();
        for (int i=0; i<ues.size(); i++){
            if (this.calculateDistance(ues.get(i)) > this.coverage)
                outOfRangeUEIdx.add(i);
        }

        for (int i=outOfRangeUEIdx.size() - 1; i>=0; i--){
            ues.remove(outOfRangeUEIdx.get(i));
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<ArrayList<Integer>> roundRobin (ArrayList <UE> UEs){

        ArrayList<ArrayList<Integer>> schedulingTable = new ArrayList<ArrayList<Integer>>();
        ArrayList <UE> ueList = new ArrayList<UE>();
        ueList = (ArrayList<UE>) UEs.clone();

        while (ueList.size() != 0){
            ArrayList<Integer> subframeScheduling = new ArrayList<Integer>(RB_NUMBER);
            for (int i=0; i<RB_NUMBER; i++){

                if (ueList.size() == 0)
                    break;

                //System.out.println(ueList.get(0).id);
                subframeScheduling.add(ueList.get(0).id); //allocate the RB to the user
                ueList.get(0).consumeData(RB_DATA_16QAM); //consome the data
                //System.out.println("Req: " + ueList.get(0).ueRequirements);
                if (ueList.get(0).ueRequirements != 0) //add the user to the queue if it's requirement isn't empty
                {
                    ueList.add(ueList.get(0));
                    //System.out.println("Here");
                }
                ueList.remove(0); //Remove the user from the beginning
            }
            schedulingTable.add(subframeScheduling);
        }

        return schedulingTable;
    }

    public void showScheduling (ArrayList<ArrayList<Integer>> schedulingTable){
        for (int i=0; i<schedulingTable.size(); i++){
            for (int j=0; j<schedulingTable.get(i).size(); j++) {
                System.out.print(schedulingTable.get(i).get(j));
                System.out.print(" ");
            }

            System.out.println("");
        }
    }

    public ArrayList<UE> getUEInRange(ArrayList<UE> UEs) {
        ArrayList <UE> UEInRange = new ArrayList<UE> ();
        //calculate the distance between the antenna and the UEs in the UEs ArrayList and add the UE in range to UEInRange
        return UEInRange;
    }

    private double calculateThroughputPerRB(int CQI){
        return bitPerSymbolBasedOnCQI[CQI - 1] * 168;
    }

    public ArrayList<ArrayList<Integer>> PF (ArrayList<UE> users) {
        ArrayList<ArrayList<Integer>> frame = new ArrayList<>();
        ArrayList<Integer> subframe;
        //ArrayList<Double> M = new ArrayList<>(users.size());

        while (users.size() > 0) {
            for (int j = 0; j < users.size(); j++) {

                //calculation of the note
                if (users.get(j).getThroughputAverage() != 0) {
                    users.get(j).setNote(calculateThroughputPerRB(users.get(j).getCQI()) / users.get(j).getThroughputAverage());
                    //M.add(j, calculateThroughputPerSubRb(users.get(j).getCQI()) / users.get(j).getThroughputAverage());
                } else {
                    users.get(j).setNote(calculateThroughputPerRB(users.get(j).getCQI()));
                    // M.add(j,calculateThroughputPerSubframe(users.get(j).getCQI()));
                }

                //users.get(j).setSendingData(false);

            }

            //tri de M  (il faudrait que M soit un user avec une note, en gros rajouter directement la note sur le user)

            Collections.sort(users);
            //we change sendingData to false and set throughput at 0
            for (UE user : users) {
                user.setSendingData(false);
                user.setThroughput(0);
            }


            subframe = new ArrayList<>();

            //choose the user in each RB
            int j = 0;
            int i = 0;
            boolean continuer = true;
            while (i<nbRB && continuer){
                // for (int i = 0; i < nbRB; i++) {
                if (j < users.size()) {
                    if (! users.get(j).getRequirement().isEmpty()) {             // if the user didn't send data in this rb at time n-1
                        subframe.add(users.get(j).getId());
                        users.get(j).getRequirement().removeRequirement(calculateThroughputPerRB(users.get(j).getCQI()));
                        //System.out.println(users.get(j).getRequirement().getData() + "   " + users.get(j).getRequirement().getVoice() + "    " + users.get(j).getRequirement().getVideo());
                        users.get(j).setSendingData(true);
                        users.get(j).setThroughput(users.get(j).getThroughput() + calculateThroughputPerRB(users.get(j).getCQI()));
                    }
                    else {
                        users.remove(j);
                        j = j-1;
                        i = i-1;
                    }
                } else {
                    if (users.size()>0) {
                        j = -1;
                        i = i - 1;
                    }
                    else
                    {
                        continuer = false;
                    }
                }
                j++;
                i++;
            }

            //recalculation of the average throughput
            for (int l = 0; l < users.size(); l++) {
                //System.out.println("entry throughput : " + users.get(j).getThroughputAverage());
                users.get(l).setThroughputAverage((1 - (1 / k)) * users.get(l).getThroughputAverage());
                // System.out.println("middle throughput : " + users.get(j).getThroughputAverage());
                if (users.get(l).isSendingData()) {
                    users.get(l).setThroughputAverage(users.get(l).getThroughputAverage() + users.get(l).getThroughput() / k);
                }
                // System.out.println(" sortie throughput : " + users.get(j).getThroughputAverage());
            }

            frame.add(subframe);
        }

        return frame;

    }

}

