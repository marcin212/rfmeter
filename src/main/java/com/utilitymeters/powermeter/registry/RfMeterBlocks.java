package com.utilitymeters.powermeter.registry;

import com.utilitymeters.powermeter.RfMeterMod;
import com.utilitymeters.powermeter.blocks.FluidMeterBlock;
import com.utilitymeters.powermeter.blocks.ItemMeterBlock;
import com.utilitymeters.powermeter.blocks.RfMeterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RfMeterBlocks {
    private static final DeferredRegister<Block> BLOCK_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, RfMeterMod.MODID);

    public static final RegistryObject<RfMeterBlock> RF_METER = BLOCK_REGISTER.register("rfmeter", RfMeterBlock::new);
    public static final RegistryObject<ItemMeterBlock> ITEM_METER = BLOCK_REGISTER.register("item_meter", ItemMeterBlock::new);
    public static final RegistryObject<FluidMeterBlock> FLUID_METER = BLOCK_REGISTER.register("fluid_meter", FluidMeterBlock::new);

    public static void init(IEventBus modEventBus) {
        BLOCK_REGISTER.register(modEventBus);
    }
}
