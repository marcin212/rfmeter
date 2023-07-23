package com.bymarcin.powermeter.registry;

import com.bymarcin.powermeter.RfMeterMod;
import com.bymarcin.powermeter.blockentity.RfMeterBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RfMeterBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RfMeterMod.MODID);
    public static final RegistryObject<BlockEntityType<RfMeterBlockEntity>> RF_METER = BLOCK_ENTITY_TYPE_REGISTER.register("rfmeter", ()-> BlockEntityType.Builder.of(RfMeterBlockEntity::new, RfMeterBlocks.RF_METER.get()).build(null));

    public static void init(IEventBus modEventBus) {
        BLOCK_ENTITY_TYPE_REGISTER.register(modEventBus);
    }

}
