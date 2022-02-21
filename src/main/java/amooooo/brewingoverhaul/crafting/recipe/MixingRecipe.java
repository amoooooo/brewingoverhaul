package amooooo.brewingoverhaul.crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class MixingRecipe extends ShapelessRecipe {

    protected final String effect;

    public MixingRecipe(ResourceLocation recipeId, String group, ItemStack result, NonNullList<Ingredient> ingredients, String effect) {
        super(recipeId, null, result, ingredients);
        this.effect = effect;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }
    public String getEffect(){
        return this.effect;
    }

    //TODO: nbt matching logic here
    @Override
    public ItemStack assemble(CraftingInventory inv) {
        for(int i = 0; i < inv.getContainerSize() - 1; ++i){
            if (inv.getItem(i).hasTag() && inv.getItem(i).getTag().contains("mix")){
                String currentMix = inv.getItem(i).getTag().getString("mix");
                ItemStack modifiedOutput = this.getResultItem();
                modifiedOutput.getOrCreateTag().putString("mix", this.effect);
                modifiedOutput.getOrCreateTag().putString("mix_2", currentMix);
                return modifiedOutput.copy();
            }
        }
        ItemStack output = this.getResultItem();
        output.getOrCreateTag().putString("mix", this.effect);
        return output.copy();
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<MixingRecipe>{

        @Override
        public MixingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            ResourceLocation itemId = new ResourceLocation(JSONUtils.getAsString(json, "result"));
            String effect = new String(JSONUtils.getAsString(json, "effect"));
            int count = JSONUtils.getAsInt(json, "count", 1);
            NonNullList<Ingredient> nonnulllist = itemsFromJson(JSONUtils.getAsJsonArray(json, "ingredients"));
            if(nonnulllist.isEmpty()){
                throw new JsonParseException("No ingredients for mixing recipe");
            } else if (nonnulllist.size() > 12) {
                throw new JsonParseException("Too many ingredients for shapeless recipe, the max is 12");
            } else {
                ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemId), count);
                result.getOrCreateTag().putString("mix", effect);
                return new MixingRecipe(recipeId, null, result, nonnulllist, effect);
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
            int i = buffer.readVarInt();
            String effect = buffer.readUtf().toString();
            NonNullList<Ingredient> nonNullList = NonNullList.withSize(i, Ingredient.EMPTY);
            for(int j = 0; j < nonNullList.size(); ++j) {
                nonNullList.set(j, Ingredient.fromNetwork(buffer));
            }
            ItemStack result = buffer.readItem();
            return new MixingRecipe(recipeId, null, result, nonNullList, effect);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, MixingRecipe recipe) {
            buffer.writeItem(recipe.getResultItem());
            buffer.writeUtf(recipe.getEffect());
            // TODO: doesnt work, figure out a way to have this work properly
            buffer.writeVarInt(recipe.getIngredients().size());
            for(Ingredient ingredient : recipe.getIngredients()) {
                ingredient.toNetwork(buffer);
            }
        }
    }
}
