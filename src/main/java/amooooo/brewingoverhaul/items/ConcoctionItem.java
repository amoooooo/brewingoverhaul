package amooooo.brewingoverhaul.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class ConcoctionItem extends PotionItem {

    public ConcoctionItem(Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        if (stack.hasTag() && stack.getTag().contains("discovered")) {
            PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World world, LivingEntity livingEntity) {
        System.out.println(stack.getTag().getString("mix"));
        System.out.println(stack.getTag().getString("mix_2"));
        System.out.println(stack.getTag().getString("mix_3"));
        if (stack.hasTag() && stack.getTag().contains("mix")){
            ForgeRegistries.POTIONS.forEach(effect -> {
                if (effect.getRegistryName().toString().equals(stack.getTag().getString("mix"))) {
                    System.out.println("mix worked");
                    livingEntity.addEffect(new EffectInstance(effect, stack.getTag().getInt("length"), stack.getTag().getInt("potency")));
                }
            });
        }
        if (stack.hasTag() && stack.getTag().contains("mix_2")) {
            ForgeRegistries.POTIONS.forEach(effect -> {
                if (effect.getRegistryName().toString().equals(stack.getTag().getString("mix_2"))) {
                    System.out.println("mix 2 worked");
                    livingEntity.addEffect(new EffectInstance(effect, stack.getTag().getInt("length"), stack.getTag().getInt("potency")));
                }
            });
        }
        if (stack.hasTag() && stack.getTag().contains("mix_3")) {
            ForgeRegistries.POTIONS.forEach(effect -> {
                if (effect.getRegistryName().toString().equals(stack.getTag().getString("mix_3"))) {
                    System.out.println("mix 3 worked");
                    livingEntity.addEffect(new EffectInstance(effect, stack.getTag().getInt("length"), stack.getTag().getInt("potency")));
                }
            });
        }
        return super.finishUsingItem(stack, world, livingEntity);
    }
}
