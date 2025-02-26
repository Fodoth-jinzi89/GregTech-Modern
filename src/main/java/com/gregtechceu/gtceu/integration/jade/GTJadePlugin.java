package com.gregtechceu.gtceu.integration.jade;

import com.gregtechceu.gtceu.common.blockentity.FluidPipeBlockEntity;
import com.gregtechceu.gtceu.common.data.GTItems;
import com.gregtechceu.gtceu.integration.jade.provider.*;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.tterrag.registrate.util.entry.ItemProviderEntry;
import snownee.jade.addon.harvest.HarvestToolProvider;
import snownee.jade.addon.harvest.SimpleToolHandler;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

import java.util.Objects;

@WailaPlugin
public class GTJadePlugin implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(new ElectricContainerBlockProvider(), BlockEntity.class);
        registration.registerBlockDataProvider(new WorkableBlockProvider(), BlockEntity.class);
        registration.registerBlockDataProvider(new ControllableBlockProvider(), BlockEntity.class);
        registration.registerBlockDataProvider(new RecipeLogicProvider(), BlockEntity.class);

        registration.registerFluidStorage(FluidPipeStorageProvider.INSTANCE, FluidPipeBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(new ElectricContainerBlockProvider(), Block.class);
        registration.registerBlockComponent(new WorkableBlockProvider(), Block.class);
        registration.registerBlockComponent(new ControllableBlockProvider(), Block.class);
        registration.registerBlockComponent(new RecipeLogicProvider(), Block.class);

        registration.registerFluidStorageClient(FluidPipeStorageProvider.INSTANCE);
    }

    static {
        GTItems.TOOL_ITEMS.columnMap().forEach((type, map) -> {
            if (type.harvestTags.isEmpty() || type.harvestTags.get(0).location().getNamespace().equals("minecraft"))
                return;
            HarvestToolProvider.registerHandler(new SimpleToolHandler(type.name, type.harvestTags.get(0),
                    map.values().stream().filter(Objects::nonNull).filter(ItemProviderEntry::isPresent)
                            .map(ItemProviderEntry::asItem).toArray(Item[]::new)));
        });
    }
}
