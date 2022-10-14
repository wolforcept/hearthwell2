package wolforce.hearthwell.blocks.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import wolforce.hearthwell.bases.BlockEntityParent;
import wolforce.hearthwell.registries.TileEntities;

public class BeSpireDevice extends BlockEntityParent {

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
		return fuelType;
	}

	public boolean tryRemoveFuel(int v) {
		if (fuel >= v) {
			fuel -= v;
			return true;
		}
		return false;
	}

	public int removeFuelUpTo(int i) {
		if (fuel <= 0)
			return 0;
		fuel -= i;
		if (fuel < 0) {
			int removed = i + fuel;
			fuel = 0;
			return removed;
		}
		return i;
	}

	public void addFuel(int v) {
		this.fuel += v;
		level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
	}

	//

	// @Override
//	public void writePacketNBT(CompoundTag nbt) {
//		nbt.put("inventory", this.inv.serializeNBT());
//		nbt.putFloat("chaos", this.chaos);
//	}
//
//	@Override
//	public void readPacketNBT(CompoundTag nbt) {
//		this.inv.deserializeNBT(nbt.getCompound("inventory"));
//		this.chaos = nbt.getFloat("chaos");
//	}

	@Override
	public void writePacketNBT(CompoundTag tag) {
		tag.putInt("fuel", fuel);
		tag.putInt("fuelType", fuelType);
		tag.put("extraInfo", extraInfo);
	}

	@Override
	public void readPacketNBT(CompoundTag tag) {
		fuel = tag.getInt("fuel");
		fuelType = tag.getInt("fuelType");
		extraInfo = tag.getCompound("extraInfo");
	}

}
