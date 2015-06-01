package kr.loveholy.exastris;

import java.io.File;

import kr.loveholy.exastris.gui.GuiHandler;
import kr.loveholy.exastris.proxy.CommonProxy;
import kr.loveholy.exastris.registry.RegistryRecipe;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

@Mod(modid = ExAstrisData.MODID, version = ExAstrisData.VERSION, dependencies = ExAstrisData.DEPENDENCIES)
public class ExAstris {

	@Instance(ExAstrisData.MODID)
	public static ExAstris instance;
	
	@SidedProxy(clientSide = "kr.loveholy.exastris.proxy.ClientProxy", serverSide = "kr.loveholy.exastris.proxy.CommonProxy")
	public static CommonProxy proxy;

	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		
		ExAstrisBlock.init();
		ExAstrisItem.init();
		
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		proxy.registerRenderer();
		RegistryRecipe.register();
		if (Loader.isModLoaded("NotEnoughItems"))
		{
			proxy.initNEI();
		}
	}
	
}
