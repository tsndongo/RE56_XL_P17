package scheduler;

import java.util.ArrayList;

import resources.UserEquipment;

public class MaxMinScheduler extends Scheduler {

	final static int TOTAL_NB_RB = 10;
	final static int N = 10; // nb of allocations we do at once
	
	ArrayList <UserEquipment> Scheduler (ArrayList<UserEquipment> l) {
		
		UserEquipment[][] allocateRB = null;
		int totalRBNeeded = 0;
		int RBLeftToAllocate = TOTAL_NB_RB * N; // because n consecutive allocations done at once
		
		for (UserEquipment ue : l)
		{
			ue.updateNbOfRBNeeded();
			totalRBNeeded += ue.getNbRBNeeded();
		}
		
		// We do the allocation as if we have N * total resource blocks
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
				allocateRB[rb%TOTAL_NB_RB][rb] = ue; // sets a specific RB at a specific time for this UE
			}
		}

		finalResourceAllocation = null;
		
		for (int i = 0; i<N; ++i)
			finalResourceAllocation.add(new ArrayList<UserEquipment>());
		
		int j = -1;
		for (ArrayList<UserEquipment> timedMultiplexing : finalResourceAllocation)
		{
			++j;
			
			for (int i = 0; i<TOTAL_NB_RB; ++i)
				if (allocateRB[j][i] != null)
					timedMultiplexing.add(allocateRB[j][i]);
		}
		
		return l;
	}
}
