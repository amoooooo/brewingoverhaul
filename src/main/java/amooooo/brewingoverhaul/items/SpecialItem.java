package amooooo.brewingoverhaul.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.List;

public class SpecialItem extends Item {

    public SpecialItem(Properties properties) {
        super(properties);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (InputMappings.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT)) {
            tooltip.add(new StringTextComponent("Advanced Tooltip"));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.special_item.test"));
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack p_77654_1_, World p_77654_2_, LivingEntity p_77654_3_) {
        return super.finishUsingItem(p_77654_1_, p_77654_2_, p_77654_3_);
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(!playerIn.getCooldowns().isOnCooldown(this)){
            playerIn.addEffect(new EffectInstance(Effects.GLOWING, 200, 5));
            ZombieEntity entity = new ZombieEntity(worldIn);
            entity.setPos(playerIn.getX(), playerIn.getY(), playerIn.getZ());
            worldIn.addFreshEntity(entity);
            playerIn.getCooldowns().addCooldown(this, 100);
        }
        return ActionResult.success(playerIn.getItemInHand(handIn));
    }
}
