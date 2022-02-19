package amooooo.brewingoverhaul.core.init;

import amooooo.brewingoverhaul.common.blocks.crucible.CrucibleTileEntity;
import amooooo.brewingoverhaul.core.init.Registration;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import java.util.function.Supplier;

public class TileEntityTypes {
    public static final RegistryObject<TileEntityType<CrucibleTileEntity>> CRUCIBLE = register(
            "crucible",
            CrucibleTileEntity::new,
            Registration.CRUCIBLE
    );
    public static void register() {}

    private static <T extends TileEntity> RegistryObject<TileEntityType<T>> register(String name, Supplier<T> factory, RegistryObject<? extends Block> block) {
        return Registration.TILE_ENTITIES.register(name, () -> {
            //noinspection ConstantConditions - null in build
           return TileEntityType.Builder.of(factory, block.get()).build(null);
        });
    }

}
