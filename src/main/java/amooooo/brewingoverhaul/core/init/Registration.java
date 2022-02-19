package amooooo.brewingoverhaul.core.init;

import amooooo.brewingoverhaul.BrewingOverhaul;
import amooooo.brewingoverhaul.common.blocks.crucible.CrucibleBlock;
import amooooo.brewingoverhaul.common.items.ConcoctionItem;
import amooooo.brewingoverhaul.core.items.SpecialItem;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class Registration {
    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, BrewingOverhaul.MOD_ID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, BrewingOverhaul.MOD_ID);
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, BrewingOverhaul.MOD_ID);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BrewingOverhaul.MOD_ID);
    public static final RegistryObject<Item> EMPTY_VIAL = ITEMS.register("empty_vial", () -> new ConcoctionItem(new Item.Properties().tab(BrewingOverhaul.BREWING_OVERHAUL_GROUP)));
    public static final RegistryObject<SpecialItem> SPECIAL_ITEM = ITEMS.register("special_item", () -> new SpecialItem(new Item.Properties().tab(BrewingOverhaul.BREWING_OVERHAUL_GROUP)));
    // Block Items
    public static final RegistryObject<BlockItem> CRUCIBLE_BLOCK_ITEM = ITEMS.register("crucible_block", () -> new BlockItem(Registration.CRUCIBLE.get(), new Item.Properties().tab(BrewingOverhaul.BREWING_OVERHAUL_GROUP)));

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BrewingOverhaul.MOD_ID);
    public static final RegistryObject<CrucibleBlock> CRUCIBLE = BLOCKS.register("crucible_block", () -> new CrucibleBlock(AbstractBlock.Properties.of(Material.METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2).sound(SoundType.METAL).strength(4,20)));

    public static void register() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        Registration.ITEMS.register(bus);
        Registration.BLOCKS.register(bus);
        Registration.CONTAINERS.register(bus);
        Registration.TILE_ENTITIES.register(bus);
        Registration.RECIPE_SERIALIZERS.register(bus);

        TileEntityTypes.register();
        Recipes.register();
        ContainerTypes.register();
    }


}
