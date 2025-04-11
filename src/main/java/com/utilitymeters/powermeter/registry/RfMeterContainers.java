package com.utilitymeters.powermeter.registry;

import com.utilitymeters.powermeter.RfMeterMod;
import com.utilitymeters.powermeter.containers.BaseMeterContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RfMeterContainers {
    private static final DeferredRegister<MenuType<?>> CONTAINERS_REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, RfMeterMod.MODID);

    public static final RegistryObject<MenuType<BaseMeterContainer>> BASE_METER = CONTAINERS_REGISTRY.register("base_meter",
            () -> IForgeMenuType.create((containerId, inventory, data) -> new BaseMeterContainer(containerId, data.readBlockPos(), inventory, inventory.player)));

    public static void init(IEventBus modEventBus) {
        CONTAINERS_REGISTRY.register(modEventBus);
    }
}
