package resources;
import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Point2D;

public class UE {
    public static ArrayList<UE> UEs = new ArrayList<UE>();
    Point2D position;
    public int ueRequirements;
    public int id;
    private ArrayList<Integer> maxMinRB = new ArrayList<Integer>();
    private int nbRBNeeded = 0;

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
}
