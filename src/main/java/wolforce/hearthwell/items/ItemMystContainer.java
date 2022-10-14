package wolforce.hearthwell.items;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import wolforce.hearthwell.ConfigServer;
import wolforce.hearthwell.blocks.BaseSpireDevice;
import wolforce.hearthwell.blocks.tiles.BeSpireDevice;

//public class ItemMystContainer extends BlockItem {
public class ItemMystContainer extends Item {

	private static final int MAX_FUEL_ON_STACK = 1000;

//	public ItemMystContainer(Block block, Properties props) {
//		super(block, props);
//	}

	public ItemMystContainer(Properties props) {
		super(props);
	}

	@Override
	public Component getName(ItemStack stack) {
		int fuel = getFuel(stack);
		int fuelType = getFuelType(stack);
		if (fuel > 0 && fuelType >= 0) {
			String fuelName = ConfigServer.getTokenNames().get(fuelType);
			return super.getName(stack).plainCopy().append(": " + fuelName);
		}
		return super.getName(stack);
	}

//	@Override
//	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
//
//		IItemRenderProperties t = new IItemRenderProperties() {
//			@Override
//			public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
//				return BewlrMystContainer.get();
//			}
//		};
//		consumer.accept(t);
//
//	}

	@Override
	public void appendHoverText(ItemStack stack, Level p_41422_, List<Component> list, TooltipFlag p_41424_) {
		super.appendHoverText(stack, p_41422_, list, p_41424_);

		int fuel = getFuel(stack);
		int fuelType = getFuelType(stack);
		if (fuel > 0 && fuelType >= 0) {
			String fuelName = ConfigServer.getTokenNames().get(fuelType);
			list.add(new TextComponent((100 * fuel / MAX_FUEL_ON_STACK) + "% of " + fuelName).setStyle(Style.EMPTY.withColor(0x999999)));
		} else
			list.add(new TextComponent("Right click any Spire Device to store the Mystical Energy inside.").setStyle(Style.EMPTY.withColor(0x999999)));

	}

	@Override
	public InteractionResult useOn(UseOnContext uoc) {
		Level level = uoc.getLevel();
		BlockPos blockpos = uoc.getClickedPos();
		ItemStack stack = uoc.getItemInHand();
		if (level.getBlockState(blockpos).getBlock() instanceof BaseSpireDevice device) {
			BeSpireDevice be = device.getDeviceEntity(level, blockpos);

			int typeOnDevice = be.getFuelType();
			int typeOnStack = getFuelType(stack);

			int fuelOnStack = getFuel(stack);
			int fuelOnDevice = be.getFuel();

			// device -> stack
			if (fuelOnDevice > 0 && (fuelOnStack == 0 || (typeOnDevice == typeOnStack && fuelOnStack < MAX_FUEL_ON_STACK))) {
				int removedFromDevice = be.removeFuelUpTo(MAX_FUEL_ON_STACK - fuelOnStack);
				if (removedFromDevice > 0) {
					addFuelOnStack(stack, removedFromDevice, typeOnDevice);
					return InteractionResult.SUCCESS;
				}
			}

			// stack -> device
			if (fuelOnStack > 0 && (be.getFuel() == 0 || typeOnDevice == typeOnStack)) {

				if (be.trySetFuelType(typeOnStack)) {
					int removedFromStack = removeAllFuelOnStack(stack);
					be.addFuel(removedFromStack);
				}
				return InteractionResult.SUCCESS;
			}
		}
		return super.useOn(uoc);
	}

	private int removeAllFuelOnStack(ItemStack stack) {
		int fuel = getFuel(stack);
		setFuel(stack, 0);
		return fuel;
	}

	private void addFuelOnStack(ItemStack stack, int i, int type) {
		int fuel = getFuel(stack);
		int fuelType = getFuelType(stack);
		if (fuelType == type || fuelType < 0 || fuel == 0) {

			int newFuel = Math.min(MAX_FUEL_ON_STACK, fuel + i);
			setFuel(stack, newFuel);
			setFuelType(stack, type);
		}
	}

	//

	public int getFuelType(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		if (tag.contains("fuelType"))
			return tag.getInt("fuelType");
		return -1;
	}

	private void setFuelType(ItemStack stack, int type) {
		CompoundTag tag = stack.getOrCreateTag();
		if (type < 0)
			tag.remove("fuelType");
		else
			tag.putInt("fuelType", type);
	}

	public int getFuel(ItemStack stack) {
		return stack.getOrCreateTag().getInt("fuel");
	}

	private void setFuel(ItemStack stack, int i) {
		stack.getOrCreateTag().putInt("fuel", i);
		if (i == 0)
			setFuelType(stack, -1);
	}
}
