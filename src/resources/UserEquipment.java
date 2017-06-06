package resources;

import java.util.ArrayList;

/**
 * Created by Syntiche on 5/16/2017.
 */
public class UserEquipment {

    private Double powerReceived;
    private Double powerGained;

    /* Data for MaxMinScheduling */
    
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
	
	/* </ Data for MaxMinScheduling> */
}
