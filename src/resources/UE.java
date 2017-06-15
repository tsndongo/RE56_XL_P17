package resources;
import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Point2D;

public class UE implements Comparable<UE>{
    private static final double VIDEO_PROPORTION = 1/2;
    private static final double VOICE_PROPORTION = 1/3;
    private static final double DATA_PROPORTION = 1/6;

    public static ArrayList<UE> UEs = new ArrayList<UE>();
    Point2D position;
    public UserRequirement ueRequirements;
    public int id;
    private ArrayList<Integer> maxMinRB = new ArrayList<Integer>();
    private int nbRBNeeded = 0;
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
    public UserRequirement generateRandomRequirements(int min, int range){
        Random rand = new Random();
        int data = rand.nextInt(range) + min;
        int voice = rand.nextInt(range) + min;
        int video = rand.nextInt(range) + min;
        UserRequirement ueReq = new UserRequirement(data, voice, video);
        return ueReq;
    }

    //TODO To change function to take into consideration QoS
    public void consumeData(int rbData) {
        int videoPartition = (int) (rbData * VIDEO_PROPORTION);
        int voicePartition = (int) (rbData * VOICE_PROPORTION);
        int dataPartition = rbData - videoPartition - voicePartition;

        ueRequirements.setData(ueRequirements.getData() - dataPartition);
        ueRequirements.setVoice(ueRequirements.getVoice() - voicePartition);
        ueRequirements.setVoice(ueRequirements.getVoice() - voicePartition);

        while (ueRequirements.containsNegativeValues()) {
            if (ueRequirements.getVideo() < 0){
                ueRequirements.setVoice(ueRequirements.getVideo() + ueRequirements.getVoice());
                ueRequirements.setVideo(0);
            }
            else if (ueRequirements.getVoice() < 0){
                if (ueRequirements.getVideo() < 0){
                    ueRequirements.setVideo(ueRequirements.getVideo() + ueRequirements.getVoice());
                }

                else {
                    ueRequirements.setData(ueRequirements.getData() + ueRequirements.getVoice());
                }

                ueRequirements.setVoice(0);
            }
            else if (ueRequirements.getData() < 0) {
                if (ueRequirements.getVideo() > 0)
                    ueRequirements.setVideo(ueRequirements.getVideo() + ueRequirements.getData());
                else if (ueRequirements.getVoice() > 0)
                    ueRequirements.setVoice(ueRequirements.getVoice() + ueRequirements.getData());
                ueRequirements.setData(0);
            }
        }
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

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
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

    public UserRequirement getUeRequirements() {
        return ueRequirements;
    }

    public int getId() {
        return id;
    }
}
