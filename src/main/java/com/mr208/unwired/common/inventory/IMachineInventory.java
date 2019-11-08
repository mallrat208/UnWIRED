package com.mr208.unwired.common.inventory;

public interface IMachineInventory extends IFluidInventory
{
	int getCurrentEnergy();
	void setEnergy(int energy);
	void decreaseEnergy(int energy);
}
