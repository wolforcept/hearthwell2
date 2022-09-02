package wolforce.hearthwell.registries;

import java.util.Set;
import java.util.function.Supplier;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.blocks.tiles.BeBurstSeed;
import wolforce.hearthwell.blocks.tiles.BeFertileSoil;
import wolforce.hearthwell.blocks.tiles.BeSpireDevice;

public class TileEntities {

	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, HearthWell.MODID);

	private static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerType( //
			BlockEntityType.BlockEntitySupplier<T> create, Supplier<Block> valid) {
		return () -> new BlockEntityType<>(create, Set.of(valid.get()), null);
	}

	private static <T extends BlockEntity> Supplier<BlockEntityType<T>> registerTypeBlocks( //
			BlockEntityType.BlockEntitySupplier<T> create, Supplier<Set<Block>> set) {

		return () -> new BlockEntityType<>(create, set.get(), null);
	}

	// TILE REGISTRY

	public static final RegistryObject<BlockEntityType<BeBurstSeed>> burst_seed = //
			REGISTRY.register("burst_seed_tile_entity", registerType(BeBurstSeed::new, () -> HearthWell.burst_seed));

	public static final RegistryObject<BlockEntityType<BeFertileSoil>> fertile_soil = //
			REGISTRY.register("fertile_soil_tile_entity", registerType(BeFertileSoil::new, () -> HearthWell.fertile_soil));

	public static final RegistryObject<BlockEntityType<BeSpireDevice>> spire_device = //
			REGISTRY.register("spire_device_tile_entity", registerTypeBlocks(BeSpireDevice::new, () -> HearthWell.getSpireDevices()));

}
