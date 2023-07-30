package com.utilitymeters.powermeter.registry;

import com.utilitymeters.powermeter.RfMeterMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RfMeterItems {
    private static final DeferredRegister<Item> ITEMS_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, RfMeterMod.MODID);

    public static final RegistryObject<BlockItem> RF_METER = ITEMS_REGISTER.register("rfmeter", ()-> new BlockItem(RfMeterBlocks.RF_METER.get(), new Item.Properties().tab(RfMeterMod.TAB)));

    public static void init(IEventBus modEventBus) {
        ITEMS_REGISTER.register(modEventBus);
    }
}
