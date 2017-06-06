package scheduler;

import java.util.ArrayList;

import resources.UserEquipment;

public class MaxMinScheduler {

	final static int TOTAL_NB_RB = 10;
	
	ArrayList <UserEquipment> Scheduler (ArrayList<UserEquipment> l) {
		
		int totalRBNeeded = 0;
		int RBLeftToAllocate = TOTAL_NB_RB * 10; // because n consecutive allocations done at once
		
		for (UserEquipment ue : l)
		{
			ue.updateNbOfRBNeeded();
			totalRBNeeded += ue.getNbRBNeeded();
		}
		
		// We do the allocation as if we have 10 * total resource blocks
		do {
			for (UserEquipment ue : l)
			{
				if (RBLeftToAllocate > 0 && ue.getRB().size() < ue.getNbRBNeeded())
				{
					--RBLeftToAllocate;
					ue.getRB().add(RBLeftToAllocate);
				}
			}
		} while (RBLeftToAllocate != 0 || RBLeftToAllocate == totalRBNeeded);
		
		// We allocate the resource blocks over the n time periods
		for (UserEquipment ue : l)
		{
			for (int rb : ue.getRB())
			{
				ue.allocateRB[rb%TOTAL_NB_RB][rb] = true; // sets a specific RB at a specific time for this UE
			}
		}
		
		return l;
	}
}
