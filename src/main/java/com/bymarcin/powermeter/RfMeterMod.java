package com.bymarcin.powermeter;

import com.bymarcin.powermeter.registry.RfMeterBlockEntities;
import com.bymarcin.powermeter.registry.RfMeterBlocks;
import com.bymarcin.powermeter.registry.RfMeterContainers;
import com.bymarcin.powermeter.registry.RfMeterItems;
import com.bymarcin.powermeter.tabs.RfMeterTab;
import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod("rfmeter")
public class RfMeterMod
{
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "rfmeter";
    public static final RfMeterTab TAB = new RfMeterTab(MODID);


    public RfMeterMod()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        RfMeterBlocks.init(bus);
        RfMeterItems.init(bus);
        RfMeterBlockEntities.init(bus);
        RfMeterContainers.init(bus);


        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {

    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {

    }

    private void processIMC(final InterModProcessEvent event)
    {

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }
}
