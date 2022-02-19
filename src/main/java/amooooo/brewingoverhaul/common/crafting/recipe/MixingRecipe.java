package amooooo.brewingoverhaul.common.crafting.recipe;

import amooooo.brewingoverhaul.core.init.Recipes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class MixingRecipe extends ShapelessRecipe {
/*
public MixingRecipe(
                        ResourceLocation recipeId,
                        NonNullList<Ingredient> ingredient,
                        ItemStack result) {
        super(Recipes.Types.MIXING, Recipes.Serializers.MIXING.get(), recipeId, "", Ingredient.merge(ingredient), result);
    }
* */

    public MixingRecipe(ResourceLocation recipeId, String group, ItemStack result, NonNullList<Ingredient> ingredients) {
        super(recipeId, null, result, ingredients);
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    //TODO: nbt matching logic here
    @Override
    public ItemStack assemble(CraftingInventory p_77572_1_) {
        return super.assemble(p_77572_1_);
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MixingRecipe>{

        @Override
        public MixingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ResourceLocation itemId = new ResourceLocation(JSONUtils.getAsString(json, "result"));
            int count = JSONUtils.getAsInt(json, "count", 1);
            NonNullList<Ingredient> nonnulllist = itemsFromJson(JSONUtils.getAsJsonArray(json, "ingredients"));
            if(nonnulllist.isEmpty()){
                throw new JsonParseException("No ingredients for mixing recipe");
            } else if (nonnulllist.size() > 12) {
                throw new JsonParseException("Too many ingredients for shapeless recipe, the max is 12");
            } else {
                ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), count);
                return new MixingRecipe(recipeId, null, result, nonnulllist);
            }
        }
        private static NonNullList<Ingredient> itemsFromJson(JsonArray p_199568_0_) {
            NonNullList<Ingredient> nonnulllist = NonNullList.create();

            for(int i = 0; i < p_199568_0_.size(); ++i) {
                Ingredient ingredient = Ingredient.fromJson(p_199568_0_.get(i));
                if (!ingredient.isEmpty()) {
                    nonnulllist.add(ingredient);
                }
            }

            return nonnulllist;
        }

        @Nullable
        @Override
        public MixingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int i = buffer.readVarInt();
            NonNullList<Ingredient> nonNullList = NonNullList.withSize(i, Ingredient.EMPTY);
            for(int j = 0; j < nonNullList.size(); ++j) {
                nonNullList.set(j, Ingredient.fromNetwork(buffer));
            }
            ItemStack result = buffer.readItem();
            return new MixingRecipe(recipeId, null, result, nonNullList);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, MixingRecipe recipe) {
            buffer.writeItem(recipe.getResultItem());
            // TODO: doesnt work, figure out a way to have this work properly
            buffer.writeVarInt(recipe.getIngredients().size());
            for(Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }
            /* use this to read all ingredients
            buffer.writeByte(numberOfIngredients);
            for (int i = 0; i < numberOfIngredients; ++i){
                buffer.write(...);
            }*/
        }
    }
}
