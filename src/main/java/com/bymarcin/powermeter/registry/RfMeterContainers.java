package com.bymarcin.powermeter.registry;

import com.bymarcin.powermeter.RfMeterMod;
import com.bymarcin.powermeter.containers.RfMeterContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RfMeterContainers {
    private static final DeferredRegister<MenuType<?>> CONTAINERS_REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, RfMeterMod.MODID);

    public static final RegistryObject<MenuType<RfMeterContainer>> RF_METER = CONTAINERS_REGISTRY.register("rfmeter",
            () -> IForgeMenuType.create((containerId, inventory, data) -> new RfMeterContainer(containerId, data.readBlockPos(), inventory, inventory.player)));

    public static void init(IEventBus modEventBus) {
        CONTAINERS_REGISTRY.register(modEventBus);
    }
}
