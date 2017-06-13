package resources;
import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Point2D;

public class UE implements Comparable<UE>{
    public static ArrayList<UE> UEs = new ArrayList<UE>();
    Point2D position;
    public int ueRequirements;
    public int id;
    private ArrayList<Integer> maxMinRB = new ArrayList<Integer>();
    private int nbRBNeeded = 0;

    private int id;
    private double distance;
    private UserRequirement requirement;
    private double throughputAverage;
    private double throughput;
    private boolean sendingData;
    private double throughputPerRb;
    private double note;
    private int CQI;


    public void updateNbOfRBNeeded() {
        // calculate nb of RB needed based only on the data to receive
        nbRBNeeded = 0;
    }

    public int getNbRBNeeded() {
        return nbRBNeeded;
    }

    public ArrayList<Integer> getRB() {
        return maxMinRB;
    }

    public UE(Point2D position, int id){
        this.position = position;
        this.id = id;
    }

    public UE (double x, double y, int id){
        this.position = new Point2D(x,y);
        this.ueRequirements = generateRandomRequirements(800, 1000);
        this.id = id;
    }

    //TODO To change function to take into consideration QoS
    public int generateRandomRequirements(int min, int range){
        Random rand = new Random();
        int  n = rand.nextInt(range) + min;
        return n;
    }

    //TODO To change function to take into consideration QoS
    public void consumeData(int rbData) {
        if (rbData <= ueRequirements)
            this.ueRequirements -= rbData;
        else
            this.ueRequirements = 0;
    }

    public void showUE (){
        System.out.println("UE: " + this.id + " Req: " + this.ueRequirements);
    }

    public static void showUEs() {
        for (int i=0; i<UEs.size(); i++){
            UEs.get(i).showUE();
        }
    }

    public int getCQI() {
        return CQI;
    }

    public void setCQI(int CQI) {
        this.CQI = CQI;
    }

    public double getThroughputAverage() {
        return throughputAverage;
    }

    public double getThroughput() {
        return throughput;
    }

    public void setThroughputAverage(double throughputAverage) {
        this.throughputAverage = throughputAverage;
    }

    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }

    public boolean isSendingData() {
        return sendingData;
    }

    public void setSendingData( boolean sendingData) {
        this.sendingData = sendingData;
    }

    public double getThroughputPerRb() {
        return throughputPerRb;
    }

    public void setThroughputPerRb(double throughputPerRb) {
        this.throughputPerRb = throughputPerRb;
    }


    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserRequirement getRequirement() {
        return requirement;
    }

    @Override
    public int compareTo(UE UserEquipment) {
        if (this.getNote() > UserEquipment.getNote()){
            return -1;
        }
        else
        {
            return 1;
        }
    }

}
}
