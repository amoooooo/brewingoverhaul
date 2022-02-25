package amooooo.brewingoverhaul.blocks.crucible;

import amooooo.brewingoverhaul.blocks.BaseHorizontalBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class CrucibleBlock extends BaseHorizontalBlock {

    private static final VoxelShape SHAPE = Stream.of(
            Block.box(2, 0, 2, 14, 10, 14)
    ).reduce((v1, v2) -> VoxelShapes.join(v1, v2, IBooleanFunction.OR)).get();

    public CrucibleBlock(Properties properties) {
        super(properties);
        runCalculation(SHAPE);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES.get(state.getValue(HORIZONAL_FACING));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CrucibleTileEntity();
    }

    @SuppressWarnings("deprecation")
    @Override
    public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult raytrace) {
        if(world.isClientSide) {
            return ActionResultType.SUCCESS;
        }
        this.interactWith(world, pos, player);
        return ActionResultType.CONSUME;
    }

    private void interactWith(World world, BlockPos pos, PlayerEntity player) {
        TileEntity tileEntity = world.getBlockEntity(pos);
        if (tileEntity instanceof CrucibleTileEntity && player instanceof ServerPlayerEntity) {
            CrucibleTileEntity te = (CrucibleTileEntity) tileEntity;
            ItemStack stack = player.getItemInHand(Hand.MAIN_HAND);
            if(player.getItemInHand(Hand.MAIN_HAND) == ItemStack.EMPTY) {
                te.takeItem(player);
            } else if(!te.isFull()) {
                te.addItem(new ItemStack(stack.getItem(), 1));
                player.getItemInHand(Hand.MAIN_HAND).shrink(1);
            }
        }
    }

    @SuppressWarnings("deprecated")
    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!state.is(newState.getBlock())) {
            TileEntity tileEntity = world.getBlockEntity(pos);
            if (tileEntity instanceof IInventory){
                InventoryHelper.dropContents(world, pos, (IInventory)tileEntity);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, world, pos, newState, isMoving);
        }
    }
}
