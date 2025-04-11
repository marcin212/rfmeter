package com.utilitymeters.powermeter.registry;

import com.utilitymeters.powermeter.RfMeterMod;
import com.utilitymeters.powermeter.blockentity.FluidMeterBlockEntity;
import com.utilitymeters.powermeter.blockentity.ItemMeterBlockEntity;
import com.utilitymeters.powermeter.blockentity.RfMeterBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RfMeterBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, RfMeterMod.MODID);
    public static final RegistryObject<BlockEntityType<RfMeterBlockEntity>> RF_METER = BLOCK_ENTITY_TYPE_REGISTER.register("rfmeter", () -> BlockEntityType.Builder.of(RfMeterBlockEntity::new, RfMeterBlocks.RF_METER.get()).build(null));
    public static final RegistryObject<BlockEntityType<ItemMeterBlockEntity>> ITEM_METER = BLOCK_ENTITY_TYPE_REGISTER.register("item_meter", () -> BlockEntityType.Builder.of(ItemMeterBlockEntity::new, RfMeterBlocks.ITEM_METER.get()).build(null));
    public static final RegistryObject<BlockEntityType<FluidMeterBlockEntity>> FLUID_METER = BLOCK_ENTITY_TYPE_REGISTER.register("fluid_meter", () -> BlockEntityType.Builder.of(FluidMeterBlockEntity::new, RfMeterBlocks.FLUID_METER.get()).build(null));

    public static void init(IEventBus modEventBus) {
        BLOCK_ENTITY_TYPE_REGISTER.register(modEventBus);
    }

}
