package wolforce.hearthwell.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import wolforce.hearthwell.blocks.tiles.BeSpireDevice;

public class BlockSpireDeviceMutator extends BaseSpireDevice {

	private static final VoxelShape shape = Shapes.or(box(2, 0, 2, 14, 9, 14), Shapes.or(box(5, 9, 5, 11, 15, 11), box(4, 15, 4, 12, 16, 12)));

	public BlockSpireDeviceMutator(Properties props) {
		super(props, shape);
	}

	@Override
	public Vec3 getParticleCenterPos() {
		return new Vec3(.5, .65, .5);
	}

	@Override
	boolean tickDeviceClient(Level level, BlockPos blockpos, BlockState bs, BeSpireDevice be) {
		return false;
	}

	@Override
	boolean tickDeviceServer(ServerLevel level, BlockPos blockpos, BlockState bs, BeSpireDevice be) {
		return false;
	}

	@Override
	protected InteractionResult onRightClick(BlockState bs, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result, ItemStack heldItem, BeSpireDevice entity) {
		return InteractionResult.FAIL;
	}

}
