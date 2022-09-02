package wolforce.hearthwell.blocks;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import wolforce.hearthwell.bases.BaseBlock;
import wolforce.hearthwell.particles.ParticleEnergyData;

public class BlockMystLight extends BaseBlock {

	public BlockMystLight(Properties properties) {
		super(properties);
	}

	@Override
	public void animateTick(BlockState stateIn, Level world, BlockPos pos, Random rand) {

		double d0 = (double) pos.getX() + 0.5D;
		double d1 = (double) pos.getY() + 0.7D;
		double d2 = (double) pos.getZ() + 0.5D;
		world.addParticle(new ParticleEnergyData(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
	}

	protected static final VoxelShape SHAPE = Block.box(6.0D, 6.0D, 6.0D, 10.0D, 10.0D, 10.0D);

	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

}
