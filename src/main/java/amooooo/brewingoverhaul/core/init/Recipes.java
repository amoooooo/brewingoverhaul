package amooooo.brewingoverhaul.core.init;

import amooooo.brewingoverhaul.BrewingOverhaul;
import amooooo.brewingoverhaul.crafting.recipe.MixingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;

public class Recipes {
    public static class Types {
        public static final IRecipeType<MixingRecipe> MIXING = IRecipeType.register(BrewingOverhaul.MOD_ID + "mixing");
    }

    public static class Serializers {
        public static final RegistryObject<IRecipeSerializer<?>> MIXING = Registration.RECIPE_SERIALIZERS.register("mixing", MixingRecipe.Serializer::new);
    }
    public static void register() {
    }
}
