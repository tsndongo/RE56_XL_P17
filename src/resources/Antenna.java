package resources;

import javafx.geometry.Point2D;

import java.util.ArrayList;

public class Antenna {
    public static final int RB_NUMBER = 6;
    public static final int RB_DATA_16QAM = 96;
    public Point2D position;
    public double coverage;

    public Antenna (){}

    public Antenna (double x, double y, double coverage) {
        this.position = new Point2D(x, y);
        this.coverage = coverage;
    }

    public double calculateDistance (UE ue) {
        return Math.sqrt(Math.pow(this.position.getX() - ue.position.getX() , 2) + Math.pow(this.position.getY() - ue.position.getY() , 2));
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
}

