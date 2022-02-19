package amooooo.brewingoverhaul.core.init;

import amooooo.brewingoverhaul.BrewingOverhaul;
import amooooo.brewingoverhaul.common.blocks.CrucibleBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.swing.*;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BrewingOverhaul.MOD_ID);

    public static final RegistryObject<Block> CRUCIBLE = BLOCKS.register("crucible", () -> new Block(AbstractBlock.Properties.of(Material.HEAVY_METAL, MaterialColor.METAL)
            .strength(10f, 20f)
            .harvestTool(ToolType.PICKAXE)
            .harvestLevel(2)
            .sound(SoundType.METAL)
            .noOcclusion()
            .requiresCorrectToolForDrops()));

    public static final RegistryObject<CrucibleBlock> CRUCIBLE_BLOCK = BLOCKS.register("crucible_block", () -> new CrucibleBlock(AbstractBlock.Properties.of(Material.HEAVY_METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).sound(SoundType.METAL)));
}
