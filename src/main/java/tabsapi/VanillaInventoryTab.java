package tabsapi;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;

public class VanillaInventoryTab extends AbstractTab
{
	
	public VanillaInventoryTab()
	{
		super(new ItemStack(Blocks.CRAFTING_TABLE));
		this.setMessage("Inventory");
	}
	
	@Override
	public void onTabClicked()
	{
		TabRegistry.openInventorScreen();
	}
}
