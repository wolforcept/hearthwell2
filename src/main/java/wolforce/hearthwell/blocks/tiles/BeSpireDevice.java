package wolforce.hearthwell.blocks.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import wolforce.hearthwell.registries.TileEntities;

public class BeSpireDevice extends BlockEntity {

	private int fuel = 0;
	private int fuelType = -1;
	public CompoundTag extraInfo = new CompoundTag();

	public BeSpireDevice(BlockPos worldPosition, BlockState blockState) {
		super(TileEntities.spire_device.get(), worldPosition, blockState);
	}

	public int getFuel() {
		return fuel;
	}

	public boolean trySetFuelType(int newType) {
		if (fuel <= 0) {
			fuelType = newType;
			return true;
		}
		return newType == fuelType;
	}

	public int getFuelType() {
		if (fuel <= 0)
			return -1;
		return fuelType;
	}

	public boolean tryAddFuel(int v) {
		int fuel = getFuel();
		// v may be negative
		// in which case we need to check if has enough fuel to extract
		if (v > 0 || (fuel > 1 && v < 0 && fuel >= v)) {
			this.fuel += v;
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
			return true;
		}
		return false;
	}

	//

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("fuel", fuel);
		tag.putInt("fuelType", fuelType);
		tag.put("extraInfo", extraInfo);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		fuel = tag.getInt("fuel");
		fuelType = tag.getInt("fuelType");
		extraInfo = tag.getCompound("extraInfo");
	}

	public CompoundTag getUpdateTag() {
		CompoundTag tag = super.getUpdateTag();
		saveAdditional(tag);
		return tag;
	}

	@Override
	public void handleUpdateTag(CompoundTag tag) {
		super.handleUpdateTag(tag);
		load(tag);
	}

	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	// TODO is this necessary?
	// @Override
	// public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket
	// pkt) {
	// super.onDataPacket(net, pkt);
	// }

}
