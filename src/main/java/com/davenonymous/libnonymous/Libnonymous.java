package com.davenonymous.libnonymous;

import com.davenonymous.libnonymous.compat.top.TOPPlugin;
import com.davenonymous.libnonymous.registry.SoilCompatibility;
import com.davenonymous.libnonymous.setup.ClientSetup;
import com.davenonymous.libnonymous.setup.ForgeEventHandlers;
import com.davenonymous.libnonymous.setup.ModSetup;
import com.davenonymous.libnonymous.setup.Registration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("libnonymous")
public class Libnonymous {
	// Directly reference a log4j logger.
	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MODID = "libnonymous";

	public Libnonymous() {
		Registration.init();

		MinecraftForge.EVENT_BUS.register(new ForgeEventHandlers());

		IEventBus modbus = FMLJavaModLoadingContext.get().getModEventBus();
		modbus.addListener(ModSetup::init);
		modbus.addListener(this::enqueueIMC);
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modbus.addListener(ClientSetup::init));
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		if(ModList.get().isLoaded("theoneprobe")) {
			InterModComms.sendTo("theoneprobe", "getTheOneProbe", TOPPlugin::new);
		}
	}
}