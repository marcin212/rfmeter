package com.utilitymeters.powermeter.registry;

import com.utilitymeters.powermeter.RfMeterMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RfMeterItems {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RfMeterMod.MODID);
    private static final DeferredRegister<Item> ITEMS_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, RfMeterMod.MODID);
    public static final RegistryObject<BlockItem> RF_METER = ITEMS_REGISTER.register("rfmeter", () -> new BlockItem(RfMeterBlocks.RF_METER.get(), new Item.Properties()));
    private static RegistryObject<CreativeModeTab> METER_TAB = CREATIVE_TABS.register("utility_meters",
            () -> CreativeModeTab.builder()
                    .displayItems((displayParams, output) -> output.accept(new ItemStack(RF_METER.get())))
                    .icon(() -> new ItemStack(RF_METER.get()))
                    .title(Component.translatable("itemGroup.utilitymeters"))
                    .build());

    public static void init(IEventBus modEventBus) {
        ITEMS_REGISTER.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
    }
}
