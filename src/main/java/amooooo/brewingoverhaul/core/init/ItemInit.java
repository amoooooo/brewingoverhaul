package amooooo.brewingoverhaul.core.init;

import amooooo.brewingoverhaul.BrewingOverhaul;
import amooooo.brewingoverhaul.core.items.SpecialItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BrewingOverhaul.MOD_ID);

    public static final RegistryObject<Item> EMPTY_VIAL = ITEMS.register("empty_vial", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_BREWING)));

    public static final RegistryObject<SpecialItem> SPECIAL_ITEM = ITEMS.register("special_item", () -> new SpecialItem(new Item.Properties().tab(ItemGroup.TAB_MISC)));

    // Block Items
    public static final RegistryObject<BlockItem> CRUCIBLE = ITEMS.register("crucible", () -> new BlockItem(BlockInit.CRUCIBLE.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<BlockItem> CRUCIBLE_BLOCK = ITEMS.register("crucible_block", () -> new BlockItem(BlockInit.CRUCIBLE_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_MISC)));
}
