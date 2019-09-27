package tabsapi;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.network.play.client.CCloseWindowPacket;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(Dist.CLIENT)
public class TabRegistry
{
	protected static Logger LOGGER = LogManager.getLogger("TabsAPI");
	
	private static Minecraft mc = Minecraft.getInstance();
	private static boolean registeredVanillaTab = false;
	private static boolean initWithPotion;
	public static int recipeBookOffset;
	
	private static ArrayList<AbstractTab> tabList = new ArrayList<>();
	private static Class<?> classJEIConfig = null;
	private static Class<?> classREIConfig = null;
	
	static
	{
		try
		{
			classJEIConfig = Class.forName("mezz.jei.config.Config");
		}
		catch(Exception ignore)
		{
			LOGGER.info("Unable to find JEI. Cannot load option offset");
		}
	}
	
	public static void registerTab(AbstractTab tab)
	{
		if(!registeredVanillaTab)
		{
			TabRegistry.LOGGER.info("Initializing Tab Registry - Vanilla Inventory");
			TabRegistry.tabList.add(new VanillaInventoryTab());
			registeredVanillaTab = true;
		}
		
		TabRegistry.LOGGER.info("Adding new Tab - {}",tab.getClass().getName());
		
		TabRegistry.tabList.add(tab);
	}
	
	public static ArrayList<AbstractTab> getTabList ()
	{
		return TabRegistry.tabList;
	}
	
	@SubscribeEvent
	public static void guiPostInit(GuiScreenEvent.InitGuiEvent.Post event)
	{
		if(event.getGui() instanceof InventoryScreen)
		{
			int guiLeft = (event.getGui().width - 176) / 2;
			int guiTop = (event.getGui().height - 166) / 2;
			recipeBookOffset = getRecipeBookOffset((InventoryScreen) event.getGui());
			guiLeft += getPotionOffset() + recipeBookOffset;
			
			TabRegistry.updateTabValues(guiLeft, guiTop, VanillaInventoryTab.class);
			
			for(AbstractTab tab: TabRegistry.tabList)
			{
				if(tab.shouldAddToList())
				{
					event.addWidget(tab);
				}
			}
		}
	}
	
	public static void openInventorScreen()
	{
		TabRegistry.mc.player.connection.sendPacket(new CCloseWindowPacket());
		InventoryScreen screen = new InventoryScreen(TabRegistry.mc.player);
		TabRegistry.mc.displayGuiScreen(screen);
	}
	
	public static void updateTabValues(int cornerX, int cornerY, Class<?> button)
	{
		int count = 2;
		for(int i = 0; i < TabRegistry.tabList.size(); i++)
		{
			AbstractTab tab = TabRegistry.getTabList().get(i);
			
			if(tab.shouldAddToList())
			{
				tab.id = count;
				tab.x = cornerX + (count - 2) * 28;
				tab.y = cornerY - 28;
				tab.visible = true;
				tab.potionOffsetLast = getPotionOffsetNEI();
				count++;
			}
		}
	}
	
	public static void addTabsToList(List<Widget> widgetList)
	{
		for(AbstractTab tab: TabRegistry.tabList)
		{
			if(tab!=null && tab.shouldAddToList())
			{
				widgetList.add(tab);
			}
		}
	}
	
	public static int getPotionOffset()
	{
		initWithPotion = false;
		return 0;
	}
	
	public static int getPotionOffsetNEI()
	{
		return 0;
	}
	
	public static int getRecipeBookOffset(InventoryScreen screen)
	{
		boolean tooNarrow = screen.width < 379;
		screen.func_194310_f().func_201520_a(screen.width, screen.height, mc, tooNarrow, screen.getContainer());
		return screen.func_194310_f().updateScreenPosition(tooNarrow, screen.width, screen.getXSize()) - (screen.width - 176) / 2;
	}
	
	
	
	
	
}
