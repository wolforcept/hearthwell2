package wolforce.hearthwell;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import wolforce.hearthwell.bases.BaseFallingBlock;
import wolforce.hearthwell.bases.BlockItemProperties;
import wolforce.hearthwell.blocks.BaseSpireDevice;
import wolforce.hearthwell.blocks.BlockBurstSeed;
import wolforce.hearthwell.blocks.BlockCore;
import wolforce.hearthwell.blocks.BlockCrushingBlock;
import wolforce.hearthwell.blocks.BlockCrystalOre;
import wolforce.hearthwell.blocks.BlockFertileSoil;
import wolforce.hearthwell.blocks.BlockMystBush;
import wolforce.hearthwell.blocks.BlockMystContainer;
import wolforce.hearthwell.blocks.BlockMystGrass;
import wolforce.hearthwell.blocks.BlockPetrifiedWood;
import wolforce.hearthwell.blocks.BlockSpire;
import wolforce.hearthwell.blocks.BlockSpireDeviceCombiner;
import wolforce.hearthwell.blocks.BlockSpireDeviceCoreInfuser;
import wolforce.hearthwell.blocks.BlockSpireDeviceReactor;
import wolforce.hearthwell.items.ItemPetrifiedWoodDust;
import wolforce.hearthwell.items.ItemPrayerLetter;
import wolforce.hearthwell.items.ItemTokenBase;
import wolforce.hearthwell.items.ItemTokenOf;
import wolforce.hearthwell.registries.Entities;
import wolforce.hearthwell.registries.TileEntities;

@Mod(HearthWell.MODID)
public class HearthWell {

	public static final String MODID = "hearthwell";
	public static final String VERSION = "0.1";

	public HearthWell() {
		ConfigServer.init();
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigServer.CONFIG_SPEC, MODID + "_server.toml");
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
//		modBus.addListener(Client::setupCompleteClient);
//		modBus.addListener(this::setupCompleteServer);
		MinecraftForge.EVENT_BUS.register(this);
		TileEntities.REGISTRY.register(modBus);
		Entities.REGISTRY.register(modBus);
	}

//	private void setupCompleteServer(final FMLDedicatedServerSetupEvent event) {
//		MapData.loadData();
//	}
//
//	private void setupCompleteClient(final FMLClientSetupEvent event) {
//		MapData.loadData();
//	}

	public static HashMap<String, Block> blocks = new HashMap<>();
	public static HashMap<String, Item> items = new HashMap<>();

	public static CreativeModeTab group = new CreativeModeTab(0, "Hearth Well") {

		@Override
		public ItemStack makeIcon() {
			return new ItemStack(myst_dust);
		}
	};

	@Target(ElementType.FIELD)
	@interface Mineable {
		String value();
	}

	@Mineable("shovel")
	public static Block crystal_ore, crystal_ore_black, petrified_wood;
	@Mineable("shovel")
	public static Block myst_grass, fertile_soil, myst_dust_block, inert_dust_block;
	public static Block myst_bush, myst_bush_small, burst_seed;
	public static Block crushing_block;
	public static Block spire;
	public static BlockMystContainer myst_container;
	public static BaseSpireDevice spire_device_core_turning, spire_device_combiner, spire_device_reactor;

	public static Set<Block> getSpireDevices() {
		return Set.of(spire_device_core_turning, spire_device_combiner, spire_device_reactor);
	}

	public static final HashMap<String, Block> devices = new HashMap<>();

//	public static BlockColor spireColor = new BlockColor() {
//		@Override
//		public int getColor(BlockState bs, BlockAndTintGetter bat, BlockPos pos, int index) {
//			if (TokenNames.getNamesOfPos(pos).equals(""))
//				return 0x111111;
//			return getColorOfPos(pos);
//		}
//	};

	public static void setupBlocks() {
		Block.Properties organic = Block.Properties.of(Material.GRASS, MaterialColor.COLOR_PURPLE)//
				.strength(1).sound(SoundType.GRASS);
//		Block.Properties plants = Block.Properties.create(Material.PLANTS, MaterialColor.PURPLE);
		Block.Properties sand = Block.Properties.of(Material.SAND, MaterialColor.COLOR_PURPLE)//
				.strength(.7f).sound(SoundType.SAND);
		Block.Properties rock = Block.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY)//
				.strength(1);
//		Block.Properties glass = Block.Properties.of(Material.GLASS, MaterialColor.COLOR_GRAY)//
//				.strength(.3f);
		Block.Properties amethyst = Block.Properties.of(Material.AMETHYST, MaterialColor.COLOR_GRAY)//
				.strength(.3f);
		Block.Properties clay = Block.Properties.of(Material.CLAY, MaterialColor.COLOR_GRAY)//
				.strength(.6f);
		Block.Properties grass = Block.Properties.of(Material.GRASS, MaterialColor.COLOR_GRAY)//
				.strength(.5f);
		Block.Properties egg = Block.Properties.of(Material.EGG, MaterialColor.COLOR_GRAY)//
				.strength(.5f);
		Block.Properties sculk = Block.Properties.of(Material.SCULK, MaterialColor.COLOR_GRAY)//
				.strength(.5f);
		Block.Properties cactus = Block.Properties.of(Material.CACTUS, MaterialColor.COLOR_GRAY)//
				.strength(.5f);
		Block.Properties rockNoToolNeeded = Block.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY)//
				.strength(.8f);
//		Block.Properties torch = AbstractBlock.Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().zeroHardnessAndResistance()
//				.setLightLevel((state) -> 14).sound(SoundType.WOOD);
		Block.Properties plantNoDrops = Properties.of(Material.PLANT, MaterialColor.COLOR_PURPLE).noCollission().instabreak().sound(SoundType.GRASS).noDrops();
		Block.Properties miscellaneous = Properties.of(Material.DECORATION, MaterialColor.NONE).strength(.3f).sound(SoundType.BONE_BLOCK);

		myst_grass = addBlock("myst_grass", new BlockMystGrass(organic));
		petrified_wood = addBlock("petrified_wood", new BlockPetrifiedWood(rockNoToolNeeded));
		myst_dust_block = addBlock("myst_dust_block", new BaseFallingBlock(sand));
		inert_dust_block = addBlock("inert_dust_block", new BaseFallingBlock(sand));
		myst_bush = addBlock("myst_bush", new BlockMystBush(true, plantNoDrops), "");
		myst_bush_small = addBlock("myst_bush_small", new BlockMystBush(false, plantNoDrops), "");
		crystal_ore = addBlock("crystal_ore", new BlockCrystalOre(rock, false));
		crystal_ore_black = addBlock("crystal_ore_black", new BlockCrystalOre(rock, true));
		crushing_block = addBlock("crushing_block", new BlockCrushingBlock(rockNoToolNeeded));

		addBlock("core_anima_activated", new BlockCore(egg, addBlock("core_anima", new BlockCore(egg))));
		addBlock("core_crystal_activated", new BlockCore(amethyst, addBlock("core_crystal", new BlockCore(amethyst))));
		addBlock("core_heat_activated", new BlockCore(cactus, addBlock("core_heat", new BlockCore(cactus))));
		addBlock("core_rock_activated", new BlockCore(rock, addBlock("core_rock", new BlockCore(rock))));
		addBlock("core_rotten_activated", new BlockCore(sculk, addBlock("core_rotten", new BlockCore(sculk))));
		addBlock("core_soft_activated", new BlockCore(clay, addBlock("core_soft", new BlockCore(clay))));
		addBlock("core_verdant_activated", new BlockCore(grass, addBlock("core_verdant", new BlockCore(grass))));

		spire = addBlock("spire", new BlockSpire(rockNoToolNeeded));
		myst_container = addBlock("myst_container", new BlockMystContainer(rockNoToolNeeded));

		spire_device_core_turning = addBlock("spire_device_core_turning", new BlockSpireDeviceCoreInfuser(rockNoToolNeeded));
		spire_device_combiner = addBlock("spire_device_combiner", new BlockSpireDeviceCombiner(rockNoToolNeeded));
		spire_device_reactor = addBlock("spire_device_reactor", new BlockSpireDeviceReactor(rockNoToolNeeded));

//		myst_light = addBlock("myst_light", new BlockMystLight(torch));

		// HAVE TILE REGISTRY
		burst_seed = addBlock("burst_seed", new BlockBurstSeed(miscellaneous));
		fertile_soil = addBlock("fertile_soil", new BlockFertileSoil(organic));
	}

	public static Item myst_dust, petrified_wood_chunk, petrified_wood_dust, crystal, flare_torch, prayer_letter, token_base;
	public static final HashMap<Item, String> descriptions = new HashMap<>();

	private static final ArrayList<ItemTokenOf> tokenItems = new ArrayList<>(TokenNames.NUMBER_OF_TOKENS);

	public static void setupItems() {

		Item.Properties props = new Item.Properties().tab(group);
		Item.Properties propsNoStack = new Item.Properties().tab(group).stacksTo(1);
		myst_dust = addItem("myst_dust", props, "");
		addItem("inert_dust", props);
		petrified_wood_chunk = addItem("petrified_wood_chunk", props, "");
		petrified_wood_dust = addItem("petrified_wood_dust", new ItemPetrifiedWoodDust(props));
		crystal = addItem("crystal", props, "");
		addItem("myst_ingot", props);
		addItem("myst_ingot_raw", props);
		addItem("inert_seed", props);

		addItem("crystal_black", props);
		addItem("crystal_blue", props);
		addItem("crystal_brown", props);
		addItem("crystal_cyan", props);
		addItem("crystal_green", props);
		addItem("crystal_orange", props);
		addItem("crystal_pink", props);
		addItem("crystal_purple", props);
		addItem("crystal_red", props);
		addItem("crystal_white", props);
		addItem("crystal_yellow", props);

		token_base = addItem("token_base", new ItemTokenBase(propsNoStack), "token_base");
		for (int i = 0; i < 12; i++)
			tokenItems.add(i, addItem("token_" + i, new ItemTokenOf(propsNoStack, i), "token_base"));

		flare_torch = addItem("flare_torch", props, "");

		prayer_letter = addItem("prayer_letter", new ItemPrayerLetter(propsNoStack), "");
	}

	//
	//
	//

	private static Item addItem(String string, Item.Properties props) {
		return addItem(string, new Item(props), null);
	}

	private static <T extends Item> T addItem(String id, T item) {
		return addItem(id, item, null);
	}

	private static Item addItem(String string, Item.Properties props, String jeiDescription) {
		return addItem(string, new Item(props), jeiDescription);
	}

	private static <T extends Item> T addItem(String id, T item, String jeiDescription) {
		item.setRegistryName(new ResourceLocation(MODID, id));
		items.put(id, item);
		if (jeiDescription != null)
			descriptions.put(item, jeiDescription.equals("") ? id : jeiDescription);
		return item;
	}

	private static <T extends Block> T addBlock(String regId, T block) {
		return addBlock(regId, block, null);
	}

	private static <T extends Block> T addBlock(String regId, T block, String jeiDescription) {
		block.setRegistryName(new ResourceLocation(MODID, regId));
		blocks.put(regId, block);
		if (block instanceof BlockItemProperties)
			addItem(regId, new BlockItem(block, ((BlockItemProperties) block).getItemProperties()), jeiDescription);
		return block;
	}

	public static Item getTokenItem(int i) {
		return tokenItems.get(i);
	}

	public static List<ItemTokenOf> getTokenItems() {
		return tokenItems.stream().toList();
	}

//	public static int getColorOfPos(BlockPos blockpos) {
//		return getColorOfLetter(blockpos != null ? TokenNames.getLetterOfPos(blockpos) : '.');
//	}

	public static int getRandomColorOfHearthwell() {
		Random random = new Random();
		int f = random.nextInt(50, 125);
		int r = f + 50;
		int g = f;
		int b = random.nextInt(128, 256);
		return (0xFF << 24) | (r << 16) | (g << 8) | b;
	}

//	public static int getColorOfLetter(char c) {
//		Random random = new Random(c);
//		if (c == '.') {
//			int f = random.nextInt(50, 125);
//			int r = f + 50;
//			int g = f;
//			int b = random.nextInt(128, 256);
//			return (0xFF << 24) | (r << 16) | (g << 8) | b;
//		}
////		return (0xFF << 24) | Color.HSBtoRGB(random.nextFloat(), 1, .5f);
//		Random rand = new Random(("" + c).hashCode());
////		Random rand = new Random((blockpos.getX() + "" + blockpos.getY() + "" + blockpos.getZ()).hashCode());
//		int r = rand.nextInt(10) * 0xFF / 10;
//		int g = rand.nextInt(10) * 0xFF / 10;
//		int b = rand.nextInt(10) * 0xFF / 10;
//		return (0xFF << 24) | (r << 16) | (g << 8) | b;
//	}

	public static List<Block> getSpireDevicesForBlockEntity() {
		return null;
	}
}
