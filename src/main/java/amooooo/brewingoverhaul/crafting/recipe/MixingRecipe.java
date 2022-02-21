package amooooo.brewingoverhaul.crafting.recipe;

import amooooo.brewingoverhaul.core.init.Recipes;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.ints.IntList;
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

public class MixingRecipe implements IRecipe<IInventory> {

    private final ResourceLocation id;
    private final String group;
    private final ItemStack result;
    private final NonNullList<Ingredient> ingredients;
    private final boolean isSimple;
    protected final String effect;

    public MixingRecipe(ResourceLocation recipeId, String group, ItemStack result, NonNullList<Ingredient> ingredients, String effect) {
        this.id = recipeId;
        this.group = group;
        this.result = result;
        this.ingredients = ingredients;
        this.isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
        this.effect = effect;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public IRecipeSerializer<?> getSerializer() {
        return Recipes.Serializers.MIXING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return Recipes.Types.MIXING;
    }

    public String getGroup() {
        return this.group;
    }

    public ItemStack getResultItem() {
        return this.result;
    }

    public NonNullList<Ingredient> getIngredients() {
        return this.ingredients;
    }
    public String getEffect(){
        return this.effect;
    }

    @Override
    public boolean matches(IInventory p_77569_1_, World p_77569_2_) {
        RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for(int j = 0; j < p_77569_1_.getContainerSize(); ++j) {
            ItemStack itemstack = p_77569_1_.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                if (isSimple)
                    recipeitemhelper.accountStack(itemstack, 1);
                else inputs.add(itemstack);
            }
        }

        return i == this.ingredients.size() && (isSimple ? recipeitemhelper.canCraft(this, (IntList)null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.ingredients) != null);
    }

    @Override
    public ItemStack assemble(IInventory inv) {
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

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return true;
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