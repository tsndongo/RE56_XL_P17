package resources;

import javafx.geometry.Point2D;

import java.util.ArrayList;
import java.util.Collections;

public class Antenna implements SchedulingAlgorithms {
    public static final int RB_NUMBER = 6;
    public static final int RB_DATA_16QAM = 96;
    public Point2D position;
    public double coverage;
    private double bitPerSymbolBasedOnCQI[];

    public Antenna() {
    }

    public Antenna(double x, double y, double coverage) {
        this.position = new Point2D(x, y);
        this.coverage = coverage;

        bitPerSymbolBasedOnCQI = new double[15];
        bitPerSymbolBasedOnCQI[0] = 0.1523;
        bitPerSymbolBasedOnCQI[1] = 0.2344;
        bitPerSymbolBasedOnCQI[2] = 0.3770;
        bitPerSymbolBasedOnCQI[3] = 0.6016;
        bitPerSymbolBasedOnCQI[4] = 0.8770;
        bitPerSymbolBasedOnCQI[5] = 1.1758;
        bitPerSymbolBasedOnCQI[6] = 1.4766;
        bitPerSymbolBasedOnCQI[7] = 1.9141;
        bitPerSymbolBasedOnCQI[8] = 2.4063;
        bitPerSymbolBasedOnCQI[9] = 2.7305;
        bitPerSymbolBasedOnCQI[10] = 3.3223;
        bitPerSymbolBasedOnCQI[11] = 3.9023;
        bitPerSymbolBasedOnCQI[12] = 4.5234;
        bitPerSymbolBasedOnCQI[13] = 5.1152;
        bitPerSymbolBasedOnCQI[14] = 5.5547;
    }

    public double calculateDistance(UE ue) {
        return Math.sqrt(Math.pow(this.position.getX() - ue.position.getX(), 2) + Math.pow(this.position.getY() - ue.position.getY(), 2));
    }

    public void filterInRangeUEs(ArrayList<UE> ues) {
        ArrayList<Integer> outOfRangeUEIdx = new ArrayList<Integer>();
        for (int i = 0; i < ues.size(); i++) {
            if (this.calculateDistance(ues.get(i)) > this.coverage)
                outOfRangeUEIdx.add(i);
        }

        for (int i = outOfRangeUEIdx.size() - 1; i >= 0; i--) {
            ues.remove(outOfRangeUEIdx.get(i));
        }
    }

    @SuppressWarnings("unchecked")
    public ArrayList<ArrayList<Integer>> roundRobin(ArrayList<UE> UEs) {

        ArrayList<ArrayList<Integer>> schedulingTable = new ArrayList<ArrayList<Integer>>();
        ArrayList<UE> ueList = new ArrayList<UE>();
        ueList = (ArrayList<UE>) UEs.clone();

        while (ueList.size() != 0) {
            ArrayList<Integer> subframeScheduling = new ArrayList<Integer>(RB_NUMBER);
            for (int i = 0; i < RB_NUMBER; i++) {

                if (ueList.size() == 0)
                    break;

                //System.out.println(ueList.get(0).id);
                subframeScheduling.add(ueList.get(0).id); //allocate the RB to the user
                ueList.get(0).consumeData(RB_DATA_16QAM); //consome the data
                //System.out.println("Req: " + ueList.get(0).getUeRequirements());
                if (ueList.get(0).getUeRequirements().getTotalRequirements() != 0) //add the user to the queue if it's requirement isn't empty
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

    public void showScheduling(ArrayList<ArrayList<Integer>> schedulingTable) {
        for (int i = 0; i < schedulingTable.size(); i++) {
            for (int j = 0; j < schedulingTable.get(i).size(); j++) {
                System.out.print(schedulingTable.get(i).get(j));
                System.out.print(" ");
            }

            System.out.println("");
        }
    }

    public ArrayList<UE> getUEInRange(ArrayList<UE> UEs) {
        ArrayList<UE> UEInRange = new ArrayList<UE>();
        //calculate the distance between the antenna and the UEs in the UEs ArrayList and add the UE in range to UEInRange
        return UEInRange;
    }

    //calculate the CQI with the distance
    public int calculateCQI(double distance) {
        return 15 - (int) distance / distanceDecreaseCQI;
    }

    // calculate the throughput of one RB, in function of the CQI
    private double calculateThroughputPerRB(int CQI) {
        return bitPerSymbolBasedOnCQI[CQI - 1] * 168;
    }

    // proportionnal fair
    public ArrayList<ArrayList<Integer>> pf(ArrayList<UE> usersList) {
        ArrayList<ArrayList<Integer>> frame = new ArrayList<>();
        ArrayList<Integer> subframe;
        //ArrayList<Double> M = new ArrayList<>(users.size());

        ArrayList<UE> users = (ArrayList<UE>) usersList.clone();
        //calculate the CQI for each user
        for (UE user : users) {
            user.setCQI(this.calculateCQI(this.calculateDistance(user)));
        }

        while (users.size() > 0) {
            for (int j = 0; j < users.size(); j++) {

                //calculation of the note
                if (users.get(j).getThroughputAverage() != 0) {
                    users.get(j).setNote(calculateThroughputPerRB(users.get(j).getCQI()) / users.get(j).getThroughputAverage());
                } else {
                    users.get(j).setNote(calculateThroughputPerRB(users.get(j).getCQI()));
                }
            }

            // the note are sorted
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
            while (i < nbRB && continuer) {
                // for (int i = 0; i < nbRB; i++) {
                if (j < users.size()) {


                    if (!users.get(j).getUeRequirements().isEmpty()) {
                        subframe.add(users.get(j).getId());
                        // problem with the different throughput
                        users.get(j).consumeData(1);
                        //users.get(j).getUeRequirements().removeRequirement(calculateThroughputPerRB(users.get(j).getCQI()));
                        users.get(j).setSendingData(true);
                        users.get(j).setThroughput(users.get(j).getThroughput() + calculateThroughputPerRB(users.get(j).getCQI()));
                    } else {
                        users.remove(j);
                        j = j - 1;
                        i = i - 1;
                    }
                } else {
                    if (users.size() > 0) {
                        j = -1;
                        i = i - 1;
                    } else {
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
    
    // MaxMin Fair Scheduler
    public ArrayList <ArrayList<Integer>> maxMin (ArrayList<UE> l) {

		ArrayList<ArrayList <Integer>> finalAllocation = new ArrayList<ArrayList <Integer>>();
		ArrayList<ArrayList <Integer>> tempAllocation;

		ArrayList<UE> UELeft = l;
				
		int N;
		
		do {
			tempAllocation = new ArrayList<ArrayList <Integer>>();

			//System.out.println("before UELeft : " + UELeft.size());
		
			ArrayList<UE> UEtoRemove = new ArrayList<UE>();
			UEtoRemove.clear();
			
			for (UE ue : UELeft)
			{
				ue.getRB().clear();
				if(ue.getUeRequirements().getTotalRequirements() <= 0)
					UEtoRemove.add(ue);
			}
			
			// Remove UE from list
			for (UE ue : UEtoRemove)
				UELeft.remove(ue);

			//System.out.println("after UELeft : " + UELeft.size());
			//System.out.println("totalRBNeeded : " + totalRBNeeded);
			
			// We set N according to number of UE
			N = UELeft.size()/2;
			
			if (N == 0)
				N = 1;
			
			//System.out.println("N = " + N);
			
			UE[][] allocateRB = new UE[RB_NUMBER][N];
			int RBLeftToAllocate = RB_NUMBER * N; // because n consecutive allocations done at once
			
			// We do the allocation as if we have N * total resource blocks
			do {
				for (UE ue : UELeft)
				{
					if (RBLeftToAllocate > 0)
					{
						--RBLeftToAllocate;
						ue.getRB().add(RBLeftToAllocate);
		                ue.consumeData(RB_DATA_16QAM); //consome the data
		                //System.out.println("RBLeftToAllocate : " + RBLeftToAllocate);
					}
				}
                //System.out.println("RBLeftToAllocate : " + RBLeftToAllocate);

				//System.out.println("size UE Left : " + UELeft.size());
			    
				/*try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				
			} while (RBLeftToAllocate > 0 && isThereStillDataToSend(UELeft));

			// We allocate the resource blocks over the n time periods
			for (UE ue : UELeft)
			{
				for (int rb : ue.getRB())
				{
					//System.out.println(rb);
					allocateRB[rb%RB_NUMBER][rb/RB_NUMBER] = ue; // sets a specific RB at a specific time for this UE
				}
			}
			
			for (int i = 0; i<N; ++i)
				tempAllocation.add(new ArrayList<Integer>());
			
			int j = -1;
			for (ArrayList<Integer> timedMultiplexing : tempAllocation)
			{
				++j;
				
				for (int i = 0; i<RB_NUMBER; ++i)
				{
					//System.out.println("i  "+ i + "j  " + j);
					if (allocateRB[i][j] != null)
						timedMultiplexing.add(allocateRB[i][j].id);
				}
			}
			
			for (ArrayList<Integer> alloc : tempAllocation) {
				finalAllocation.add(alloc);
			}
			
		} while (!UELeft.isEmpty());
		
		return finalAllocation;
	}
	
	/* Method used in MaxMin scheduling to check if the list of UE we manipulate still needs to send data */
    public boolean isThereStillDataToSend (ArrayList <UE> UELeft) {
	    for (UE ue : UELeft)
		{
			if(ue.getUeRequirements().getTotalRequirements() > 0)
				return true;
		}
	    return false;
    }
    
}

