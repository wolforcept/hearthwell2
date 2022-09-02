package wolforce.hearthwell.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import wolforce.hearthwell.HearthWell;
import wolforce.hearthwell.data.recipes.RecipeBurstSeed;
import wolforce.hearthwell.data.recipes.RecipeCombining;
import wolforce.hearthwell.data.recipes.RecipeCoring;
import wolforce.hearthwell.data.recipes.RecipeCrushing;
import wolforce.hearthwell.data.recipes.RecipeFlare;
import wolforce.hearthwell.data.recipes.RecipeHandItem;
import wolforce.hearthwell.data.recipes.RecipeInfluence;
import wolforce.hearthwell.data.recipes.RecipeReacting;
import wolforce.hearthwell.data.recipes.RecipeTransformation;

public class MapData implements Serializable {

	public static transient final int timeSmall = 6; // TODO change to 60
	public static transient final int timeMedium = 6; // TODO change to 120
	public static transient final int timeLarge = 6; // TODO change to 300

	public transient static MapData DATA;
	public transient int minX = 0, minY = 0, maxX = 0, maxY = 0;

	private transient static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private transient static final String CONFIG_FILE = "hearthwell_mapdata.json";
	private transient static final long serialVersionUID = HearthWell.VERSION.hashCode();
	private transient HashMap<Short, MapNode> nodesByPosition;

	public final HashMap<String, MapNode> nodes = new HashMap<>();
//	private HashMap<Short, EnergyType> energyTypes;

//	private transient HashMap<RecipeType, List<RecipeHearthWell>> recipesByType;

	public final LinkedList<RecipeFlare> recipes_flare = new LinkedList<>();
	public final LinkedList<RecipeInfluence> recipes_influence = new LinkedList<>();
	public final LinkedList<RecipeTransformation> recipes_transformation = new LinkedList<>();
	public final LinkedList<RecipeHandItem> recipes_handitem = new LinkedList<>();
	public final LinkedList<RecipeBurstSeed> recipes_burst = new LinkedList<>();
	public final LinkedList<RecipeCrushing> recipes_crushing = new LinkedList<>();
	public final LinkedList<RecipeCombining> recipes_combining = new LinkedList<>();
	public final LinkedList<RecipeCoring> recipes_coring = new LinkedList<>();
	public final LinkedList<RecipeReacting> recipes_reacting = new LinkedList<>();

	public final LinkedList<LinkedList<? extends RecipeHearthWell>> allRecipes = new LinkedList<>();

	public MapData() {
		allRecipes.add(recipes_flare);
		allRecipes.add(recipes_influence);
		allRecipes.add(recipes_transformation);
		allRecipes.add(recipes_handitem);
		allRecipes.add(recipes_burst);
		allRecipes.add(recipes_crushing);
		allRecipes.add(recipes_combining);
		allRecipes.add(recipes_coring);
		allRecipes.add(recipes_reacting);
	}

	private MapData createBaseNode() {
		boolean pureFlareExists = false;
		for (RecipeFlare recipe : recipes_flare)
			pureFlareExists = pureFlareExists || recipe.recipeId.equals("pure_flare");

		String[] recipes = pureFlareExists ? array("pure_flare", "recipe_myst_ingot") : array("recipe_myst_ingot");
//		String[] recipes = recipes_flare.stream().filter(r -> r.recipeId.equals("pure_flare")).count() > 0 ? array("pure_flare") : array();

		addNode("hearthwell", 0, 0, "The Hearth Well", 0, "hearthwell:crystal_diversity", //
				"The Hearth Well is an infinite source of energy given you by the gods.", //
				"The Hearth Well is an infinite source of energy given you by the gods./n" + "Throw offerings near the Hearth Well to gain blessing and/n"
						+ "favour from the gods in the form of small flares./n" + "Flares can be made stronger by adding extra Myst Dust to the recipe./n"
						+ "You can manipulate these flares with a Flare Torch./n"
						+ "Flares may react in particular ways when touching certain blocks on the world or when nearby players with certain items on hand.", //
				recipes, array(), array());
		return this;
	}

	private MapData addDefaults() {

		addDefaultInfluenceRecipes();
		addDefaultFlareRecipes();
		addDefaultHandItemRecipes();
		addDefaultBurstRecipes();
		addDefaultTransformationRecipes();
		addDefaultCrushingRecipes();
		addDefaultCombiningRecipes();
		addDefaultCoringRecipes();
		addDefaultReactingRecipes();

		addDefaultCenterNodes();

		addNode("water", 4, 2, "Liquid Lifegiver", timeSmall, "minecraft:water_bucket", //
				"And basics of reactions", //
				"Some flares can interact with the blocks placed in the world simply by touching them./nThe best way to do this is to lead the flares to the blocks with a flare torch.", //
				array("recipe_water_from_leaves"), // recipes
				array("on_reactions"), array()); // parents / required items

		addNode("stones", 2, 3, "More Stones", timeSmall, "minecraft:tuff", //
				"", //
				"", //
				array("recipe_tuff_from_cobblestone", "recipe_diorite_from_deepslate", "recipe_andesite_from_stone", "recipe_granite_from_blackstone"), // recipes
				array("on_reactions"), array("#stone_crafting_materials")); // parents / required items

		addNode("lava", 0, 6, "Molten rock", timeSmall, "minecraft:lava_bucket", //
				"And basics of reactions", //
				"You can get lava by", //
				array("recipe_lava_from_obsidian"), // recipes
				array("on_hardening"), array("#coals")); // parents / required items

		addNode("on_hardening", 0, 4, "On Hardening", timeSmall, "flare_harden", //
				"A flare to harden the natural elements.", //
				"A flare to harden the natural elements", //
				array("flare_harden", "recipe_black_crystal", "recipe_obsidian_from_stone"), // recipes
				array("stones", "coal"), array("#coals")); // parents / required items

//		addNode("on_rarity", -2, 1, "On Rarity", timeSmall, "flare_rarity", //
//				"A flare to increase the value of worldly possessions.", //
//				"A flare to increase the value of worldly possessions.", //
//				array("flare_rarity", "recipe_blue_crystal"), // recipes
//				array("magical_plants", "crystal_formation"), array("hearthwell:crystal"));
//
//		addNode("on_growth", 0, -2, "On Growth", timeSmall, "flare_growth", //
//				"A flare to speed up life.", //
//				"A flare to speed up life.", //
//				array("flare_growth", "recipe_green_crystal"), // recipes
//				array("magical_plants", "petrification"), array("#minecraft:saplings"));

		//
		// UP FROM BURST SEEDS
		//

		addNode("burst_seed", 0, -4, "Burst seeds", timeMedium, "hearthwell:burst_seed", //
				"A surprising burst of resources!",
				"Burst Seeds are an easy albeit unstable way to multiply resources./nSimply place then down, and start adding the resources you want to multiply./nEach resource you add decreases the stability of the burst seed, and increases the chance of it blowing up./nWhen the burst seed is highly unstable you might want to let it/ncool down before adding to it again.", // descriptions
				array("recipe_burst_seed"), // recipes
				array("on_growth"), array("hearthwell:inert_seed")); // parents / required items

		//
		// LEFT FROM SPIRES
		//

		addNode("spires", -4, 0, "Spires and Devices", 0, "hearthwell:spire", //
				"Concentrated energy locations.", //
				"Spires are physical structures that help burn the mystical energies/non mysterious dust that is thrown into them./nMany blocks in the world are capable of burning mysterious energies,/nand the tokens shining all around the spire let you know/nwhich types of energies that spire can burn./n(These are related to which token names can be created/nfrom the token letter at that location.)/n/nSpire Devices utilize the mysterious energies from burning dust in a spire./nTo choose which energy the spire device will use,/n right click it with the specific token./n", //
				array(), // recipes
				array(), array("hearthwell:myst_ingot")); // parents / required items

		addNode("core_infuser", -4, 2, "Core Infuser Spire Device", timeMedium, "hearthwell:spire_device_core_turning", //
				"Some serious transformations",
				"The Core Infuser takes those energies and injects them directly/ninto the core that is placed above it. /nAfter some time, this will turn the core into some useful resources./nWhich resources it creates depends on the core, but also on the/ntype of energy that was given to the device.", // descriptions
				array("recipe_coal_from_core", "recipe_iron_from_core", "recipe_copper_from_core", "recipe_gold_from_core"), // recipes
				array("on_rarity"), array("hearthwell:myst_container")); // parents / required items

		addNode("coal", -2, 3, "Ores", timeMedium, "minecraft:coal_ore", //
				"Combining minerals", "", // descriptions
				array(), // recipes
				array("core_infuser"), array()); // parents / required items

		addNode("expert_coring", -6, 3, "Expert Coring", timeMedium, "minecraft:redstone", //
				"More mineral resources", "", // descriptions
				array("recipe_redstone_from_core", "recipe_lapis_from_core"), // recipes
				array("core_infuser"), array("minecraft:raw_iron", "minecraft:raw_gold", "minecraft:raw_copper")); // parents / required

		addNode("on_refinement", -8, 3, "On Refinement", timeMedium, "flare_refinement", //
				"Refined rocks", "A flare to embetter your minerals and make them shiny.", // descriptions
				array("flare_refinement", "recipe_cyan_crystal"), // recipes
				array("expert_coring"), array("minecraft:redstone")); // parents / required items

		addNode("core_crystal", -10, 4, "Crystal Core", timeMedium, "hearthwell:core_crystal_activated", //
				"Precious colors", "The crystal core is a more refined and precious version of the Rock Core./nYou will be able to infuse it to create much rarer and precious minerals", // descriptions
				array("recipe_activate_core_crystal", "recipe_diamond_from_core", "recipe_emerald_from_core"), // recipes
				array("on_refinement"), array("minecraft:lapis_lazuli")); // parents / required items

		//

//		addNode("deepslate", -3, 6, "", timeMedium, "minecraft:deepslate", //
//				"", "", // descriptions
//				array(), // recipes
//				array("on_hardening"), array()); // parents / required items

//		addNode("deepslate_ores", -6, 8, "", timeMedium, "minecraft:deepslate_iron_ore", //
//				"", "", // descriptions
//				array(), // recipes
//				array("deepslate"), array()); // parents / required items

//		addNode("", , , "", timeMedium, "hearthwell:", //
//				"", "", // descriptions
//				array(), // recipes
//				array(""), array()); // parents / required items
//
//		addNode("", , , "", timeMedium, "hearthwell:", //
//				"", "", // descriptions
//				array(), // recipes
//				array(""), array()); // parents / required items
//
//		addNode("", , , "", timeMedium, "hearthwell:", //
//				"", "", // descriptions
//				array(), // recipes
//				array(""), array()); // parents / required items

//		addNode("material_infusion", 2, 3, "Material Infusion", 120, "hearthwell:myst_ingot", //
//				"More mysterious stuff.", //
//				"Holding an Iron Ingot in the presence of a Pure Flare will attract it,/nand the two will fuse, creating a new, strong, mystical alloy.", //
//				array("recipe_myst_ingot"), // recipes
//				array("crystal_formation"), array("hearthwell:crystal"), false);

//		addNode("rapid_growth", 0, -4, "Rapid Growth", 120, "hearthwell:fertile_soil", //
//				"Create a new better soil for the growth of plant life.", //
//				"Make a Cyan Flare from Cyan Dye, then use it to transform/ncoal into diamonds and coal blocks into diamond blocks.", //
//				array("recipe_fertile_soil"), // recipes
//				array(), array("#minecraft:saplings|#minecraft:seeds"), false);
//
//		addNode("material_multiplication", -4, 1, "Material Multiplication", 120, "hearthwell:burst_seed", //
//				"When you need more of the same.", //
//				"Burst seeds may be fed certain materials which will grow and multiply./nBut be careful, for they are unstable.", //
//				array("recipe_burst_seed"), // recipes
//				array(), array(), false);

		//

//		addNode("material_infusion", 2, 1, "Material Infusion", 600, "hearthwell:myst_ingot", //
//				"Create a new better soil for the growth of plant life.", //
//				"Make a Cyan Flare from Cyan Dye, then use it to transform/ncoal into diamonds and coal blocks into diamond blocks.", //
//				array("flare_life", "recipe_fertile_soil"), // recipes
//				array("crystal_formation", "petrification"), array("minecraft:cobblestone", "minecraft:stone", "minecraft:blackstone"), false);

		// Layer2

//		addNode("material_infusion", 4, 1, "Material Infusion", 600, "hearthwell:mystic_ingot", //
//				"Create a new better soil for the growth of plant life.", //
//				"Make a Cyan Flare from Cyan Dye, then use it to transform/ncoal into diamonds and coal blocks into diamond blocks.", //
//				array("flare_life", "recipe_fertile_soil"), // recipes
//				array("material_infusion"), array("minecraft:cobblestone", "minecraft:stone", "minecraft:blackstone"), false);

		return this;
	}

	private void addDefaultInfluenceRecipes() {
		recipes_influence.add(new RecipeInfluence("recipe_myst_grass", "minecraft:dirt|minecraft:grass_block", "hearthwell:myst_grass"));
		recipes_influence.add(new RecipeInfluence("recipe_petrified_wood", "#minecraft:logs", "hearthwell:petrified_wood"));
		recipes_influence.add(new RecipeInfluence("recipe_crystal_ore", "#minecraft:base_stone_overworld", "hearthwell:crystal_ore"));
		recipes_influence.add(new RecipeInfluence("recipe_crystal_ore_black", "minecraft:blackstone", "hearthwell:crystal_ore_black"));
	}

	private void addDefaultFlareRecipes() {
		recipes_flare.add(new RecipeFlare("pure_flare", "Pure Flare", "FFFFFF", "hearthwell:myst_dust_block,hearthwell:crystal,hearthwell:petrified_wood_chunk"));
		recipes_flare.add(new RecipeFlare("flare_growth", "Flare of Growth", "55FF55", //
				"hearthwell:myst_dust_block,#minecraft:saplings,hearthwell:token_0"));
		recipes_flare.add(new RecipeFlare("flare_rarity", "Flare of Rarity", "0011FF", //
				"hearthwell:myst_dust_block,hearthwell:crystal,hearthwell:token_1"));
		recipes_flare.add(new RecipeFlare("flare_reaction", "Flare of Reaction", "FF3300", //
				"hearthwell:myst_dust_block,minecraft:torch,hearthwell:token_2"));
		recipes_flare.add(new RecipeFlare("flare_harden", "Flare of Hardening", "111111", //
				"hearthwell:myst_dust_block,minecraft:tuff,hearthwell:token_3"));
		recipes_flare.add(new RecipeFlare("flare_refinement", "Flare of Refinement", "00AAAA", //
				"hearthwell:myst_dust_block,minecraft:redstone_torch,minecraft:clock,hearthwell:token_8"));
	}

	private void addDefaultHandItemRecipes() {
		recipes_handitem.add(new RecipeHandItem("recipe_myst_ingot", "pure_flare", "hearthwell:myst_ingot_raw", "hearthwell:myst_ingot"));
		recipes_handitem.add(new RecipeHandItem("recipe_red_crystal", "flare_reaction", "hearthwell:crystal", "hearthwell:crystal_red"));
		recipes_handitem.add(new RecipeHandItem("recipe_green_crystal", "flare_growth", "hearthwell:crystal", "hearthwell:crystal_green"));
		recipes_handitem.add(new RecipeHandItem("recipe_blue_crystal", "flare_rarity", "hearthwell:crystal", "hearthwell:crystal_blue"));
		recipes_handitem.add(new RecipeHandItem("recipe_black_crystal", "flare_harden", "hearthwell:crystal", "hearthwell:crystal_black"));
		recipes_handitem.add(new RecipeHandItem("recipe_cyan_crystal", "flare_refinement", "hearthwell:crystal", "hearthwell:crystal_cyan"));
		recipes_handitem.add(new RecipeHandItem("recipe_burst_seed", "flare_growth", "hearthwell:inert_seed", "hearthwell:burst_seed"));

	}

	private void addDefaultBurstRecipes() {
		recipes_burst.add(new RecipeBurstSeed("burstseed_dirt", "minecraft:dirt"));
		recipes_burst.add(new RecipeBurstSeed("burstseed_gravel", "minecraft:gravel"));
		recipes_burst.add(new RecipeBurstSeed("burstseed_sand", "minecraft:sand"));
		recipes_burst.add(new RecipeBurstSeed("burstseed_redsand", "minecraft:red_sand"));
		recipes_burst.add(new RecipeBurstSeed("burstseed_clay", "minecraft:clay"));

		recipes_burst.add(new RecipeBurstSeed("burstseed_stone", "minecraft:stone"));
		recipes_burst.add(new RecipeBurstSeed("burstseed_cobblestone", "minecraft:cobblestone"));
		recipes_burst.add(new RecipeBurstSeed("burstseed_andesite", "minecraft:andesite"));
		recipes_burst.add(new RecipeBurstSeed("burstseed_diorite", "minecraft:diorite"));
		recipes_burst.add(new RecipeBurstSeed("burstseed_granite", "minecraft:granite"));
		recipes_burst.add(new RecipeBurstSeed("burstseed_calcite", "minecraft:calcite"));
		recipes_burst.add(new RecipeBurstSeed("burstseed_basalt", "minecraft:basalt"));
		recipes_burst.add(new RecipeBurstSeed("burstseed_blackstone", "minecraft:blackstone"));
		recipes_burst.add(new RecipeBurstSeed("burstseed_mossy_cobblestone", "minecraft:mossy_cobblestone"));
		recipes_burst.add(new RecipeBurstSeed("burstseed_tuff", "minecraft:tuff"));
		recipes_burst.add(new RecipeBurstSeed("burstseed_deepslate", "minecraft:deepslate"));

		recipes_burst.add(new RecipeBurstSeed("burstseed_quartz", "minecraft:quartz_block"));

	}

	private void addDefaultTransformationRecipes() {

		recipes_transformation.add(new RecipeTransformation("recipe_fertile_soil", "flare_life", "minecraft:dirt", "hearthwell:fertile_soil"));
		recipes_transformation.add(new RecipeTransformation("recipe_myst_grass_2", "pure_flare", "minecraft:dirt|minecraft:grass_block", "hearthwell:myst_grass"));
		recipes_transformation.add(new RecipeTransformation("recipe_crystal_ore_black_2", "pure_flare", "minecraft:blackstone", "hearthwell:crystal_ore_black"));
		recipes_transformation.add(new RecipeTransformation("recipe_crystal_ore_2", "pure_flare", "#minecraft:base_stone_overworld", "hearthwell:crystal_ore"));
		recipes_transformation.add(new RecipeTransformation("recipe_petrified_wood_2", "pure_flare", "#minecraft:logs", "hearthwell:petrified_wood"));
		recipes_transformation.add(new RecipeTransformation("recipe_water_from_leaves", "flare_reaction", "#minecraft:leaves", "minecraft:water"));
		recipes_transformation.add(new RecipeTransformation("recipe_tuff_from_cobblestone", "flare_reaction", "minecraft:cobblestone", "minecraft:tuff"));
		recipes_transformation.add(new RecipeTransformation("recipe_andesite_from_stone", "flare_reaction", "minecraft:stone", "minecraft:andesite"));
		recipes_transformation.add(new RecipeTransformation("recipe_granite_from_blackstone", "flare_reaction", "minecraft:blackstone", "minecraft:granite"));
		recipes_transformation.add(new RecipeTransformation("recipe_diorite_from_deepslate", "flare_reaction", "minecraft:cobbled_deepslate", "minecraft:diorite"));
		recipes_transformation.add(new RecipeTransformation("recipe_obsidian_from_stone", "flare_harden", "#stone_crafting_materials", "minecraft:obsidian"));

		recipes_transformation.add(new RecipeTransformation("recipe_activate_core_anima", "pure_flare", "hearthwell:core_anima", "hearthwell:core_anima_activated"));
		recipes_transformation.add(new RecipeTransformation("recipe_activate_core_crystal", "pure_flare", "hearthwell:core_crystal", "hearthwell:core_crystal_activated"));
		recipes_transformation.add(new RecipeTransformation("recipe_activate_core_heat", "pure_flare", "hearthwell:core_heat", "hearthwell:core_heat_activated"));
		recipes_transformation.add(new RecipeTransformation("recipe_activate_core_rotten", "pure_flare", "hearthwell:core_rotten", "hearthwell:core_rotten_activated"));
		recipes_transformation.add(new RecipeTransformation("recipe_activate_core_soft", "pure_flare", "hearthwell:core_soft", "hearthwell:core_soft_activated"));
		recipes_transformation.add(new RecipeTransformation("recipe_activate_core_verdant", "pure_flare", "hearthwell:core_verdant", "hearthwell:core_verdant_activated"));
		recipes_transformation.add(new RecipeTransformation("recipe_activate_core_rock", "pure_flare", "hearthwell:core_rock", "hearthwell:core_rock_activated"));

	}

	private void addDefaultCrushingRecipes() {
		recipes_crushing.add(new RecipeCrushing("recipe_petrified_wood_dust", "hearthwell:petrified_wood_chunk", "4*hearthwell:petrified_wood_dust"));

	}

	private void addDefaultCombiningRecipes() {
		recipes_combining.add(new RecipeCombining("recipe_test", "hearthwell:myst_dust,minecraft:blackstone", "hearthwell:crystal"));
	}

	private void addDefaultCoringRecipes() {
		recipes_coring.add(new RecipeCoring("recipe_iron_from_core", "hearthwell:core_rock_activated", 3, 250, "4*minecraft:raw_iron"));
		recipes_coring.add(new RecipeCoring("recipe_copper_from_core", "hearthwell:core_rock_activated", 4, 250, "6*minecraft:raw_copper"));
		recipes_coring.add(new RecipeCoring("recipe_gold_from_core", "hearthwell:core_rock_activated", 7, 600, "4*minecraft:raw_gold"));
		recipes_coring.add(new RecipeCoring("recipe_redstone_from_core", "hearthwell:core_rock_activated", 2, 75, "4*minecraft:redstone"));
		recipes_coring.add(new RecipeCoring("recipe_lapis_from_core", "hearthwell:core_rock_activated", 1, 75, "3*minecraft:lapis_lazuli"));
		recipes_coring.add(new RecipeCoring("recipe_coal_from_core", "hearthwell:core_rock_activated", 6, 100, "2*minecraft:coal"));
		recipes_coring.add(new RecipeCoring("recipe_diamond_from_core", "hearthwell:core_crystal_activated", 6, 1000, "3*minecraft:diamond"));
		recipes_coring.add(new RecipeCoring("recipe_emerald_from_core", "hearthwell:core_crystal_activated", 8, 1000, "3*minecraft:emerald"));
		recipes_coring.add(new RecipeCoring("recipe_amethist_from_core", "hearthwell:core_crystal_activated", 10, 1000, "8*minecraft:amethyst_shard"));
	}

	private void addDefaultReactingRecipes() {
		recipes_reacting.add(new RecipeReacting("recipe_lava_from_obsidian", "minecraft:obsidian", 11, 500, "minecraft:lava"));
	}

	private void addDefaultCenterNodes() {
		{
			addNode("magical_plants", -2, -1, "Magical Plants", timeSmall, "hearthwell:myst_grass", //
					"The Hearth Well can give life and magic.", //
					"Nearby grass absorbs the mysterious energies and changes colour.", //
					array("recipe_myst_grass", "recipe_myst_grass_2"), // recipes
					array("hearthwell"), array());

			addNode("petrification", 2, -1, "Petrification", timeSmall, "hearthwell:petrified_wood", //
					"The Hearth Well can also take away life and magic.", //
					"Nearby wood petrifies and becomes harder and brittle.", //
					array("recipe_petrified_wood", "recipe_petrified_wood_2"), // recipes
					array("hearthwell"), array());

			addNode("crystal_formation", 0, 2, "Crystal Formation", timeSmall, "hearthwell:crystal", //
					"Nearby stone begins to transform into some sort of Mysterious Crystal.", //
					"Stone near the Hearth Well is influenced by it./nIt absorbs the mysterious energies and has a chance/nto transform into crystal ore.", //
					array("recipe_crystal_ore", "recipe_crystal_ore_black", "recipe_crystal_ore_2", "recipe_crystal_ore_black_2"), // recipes
					array("hearthwell"), array());

			addNode("on_reactions", 2, 1, "On Reactions", timeSmall, "flare_reaction", //
					"A flare to alter the natural interactions.", //
					"A flare to alter the natural interactions.", //
					array("flare_reaction", "recipe_red_crystal"), // recipes
					array("crystal_formation", "petrification"), array("#coals"));

			addNode("on_rarity", -2, 1, "On Rarity", timeSmall, "flare_rarity", //
					"A flare to increase the value of worldly possessions.", //
					"A flare to increase the value of worldly possessions.", //
					array("flare_rarity", "recipe_blue_crystal"), // recipes
					array("magical_plants", "crystal_formation"), array("hearthwell:crystal"));

			addNode("on_growth", 0, -2, "On Growth", timeSmall, "flare_growth", //
					"A flare to speed up life.", //
					"A flare to speed up life.", //
					array("flare_growth", "recipe_green_crystal"), // recipes
					array("magical_plants", "petrification"), array("#minecraft:saplings"));
		}
	}

	public MapData init() {

		mapNodesByPosition();

		for (MapNode node : nodes.values()) {
			node.init();
			for (String recipeId : node.recipes_ids) {
				RecipeHearthWell recipe = getRecipeById(recipeId);
				if (recipe == null) {
					System.out.println("HEARTHWELL ERROR: " + recipeId + " is missing!");
					new HearthWellException(recipeId + " is missing!").printStackTrace();
				} else
					recipe.init(this, node);
			}
		}

		MapNode hwNode = getHwNode();

		for (LinkedList<? extends RecipeHearthWell> recipes : allRecipes)
			for (RecipeHearthWell recipe : recipes)
				if (recipe.mapNode == null)
					recipe.init(this, hwNode);

		minX = 0;
		minY = 0;
		maxX = 0;
		maxY = 0;

		for (MapNode node : nodes.values()) {
			if (node.x < minX)
				minX = node.x;
			if (node.y < minY)
				minY = node.y;

			if (node.x > maxX)
				maxX = node.x;
			if (node.y > maxY)
				maxY = node.y;
		}
		minX = Math.abs(minX);
		minY = Math.abs(minY);

		return this;
	}

	public RecipeHearthWell getRecipeById(String recipeId) {
		for (RecipeInfluence recipe : recipes_influence)
			if (recipe.recipeId.equals(recipeId))
				return recipe;
		for (RecipeFlare recipe : recipes_flare)
			if (recipe.recipeId.equals(recipeId))
				return recipe;
		for (RecipeTransformation recipe : recipes_transformation)
			if (recipe.recipeId.equals(recipeId))
				return recipe;
		for (RecipeHandItem recipe : recipes_handitem)
			if (recipe.recipeId.equals(recipeId))
				return recipe;
		for (RecipeBurstSeed recipe : recipes_burst)
			if (recipe.recipeId.equals(recipeId))
				return recipe;
		for (RecipeCrushing recipe : recipes_crushing)
			if (recipe.recipeId.equals(recipeId))
				return recipe;
		for (RecipeCombining recipe : recipes_combining)
			if (recipe.recipeId.equals(recipeId))
				return recipe;
		for (RecipeCoring recipe : recipes_coring)
			if (recipe.recipeId.equals(recipeId))
				return recipe;
		for (RecipeReacting recipe : recipes_reacting)
			if (recipe.recipeId.equals(recipeId))
				return recipe;
		return null;
	}

	private void mapNodesByPosition() {
		nodesByPosition = new HashMap<>();
		for (MapNode node : nodes.values()) {
			short nodeXY = MapNode.hash(node.x, node.y);
			if (nodesByPosition.containsKey(nodeXY))
				new HearthWellException("Location collision for nodes: " + node.name + " and " + nodesByPosition.get(nodeXY).name).printStackTrace();
			nodesByPosition.put(nodeXY, node);
			for (String parentId : node.parent_ids) {
				MapNode parent = getNode(parentId);
				if (parent == null)
					new HearthWellException("Invalid parent of node " + node.name + ": " + parentId).printStackTrace();
			}
		}
	}

	public void addNode(String id, int x, int y, String name, int time, String stack, String description, String fullDescription, String[] recipes, String[] connections, String[] required_items) {
		addNode(id, x, y, name, time, stack, description, fullDescription, recipes, connections, required_items, false);
	}

	public void addNode(String id, int x, int y, String name, int time, String stack, String description, String fullDescription, String[] recipes, String[] connections, String[] required_items,
			boolean write) {
		nodes.put(id, new MapNode((byte) x, (byte) y, name, time, stack, description, fullDescription, recipes, connections, required_items));
		if (write) {
			init();
			writeData(this);
		}
	}

	public void removeNode(byte x, byte y, boolean write) {

		for (Iterator<String> iterator = nodes.keySet().iterator(); iterator.hasNext();) {
			String nodeId = (String) iterator.next();
			MapNode node = nodes.get(nodeId);
			if (node.x == x && node.y == y && (node.x != 0 || node.y != 0))
				iterator.remove();
		}

		if (write) {
			init();
			writeData(this);
		}
	}

	public void changeNodeId(MapNode node, String id) {
		removeNode(node.x, node.y, false);
		nodes.put(id, node);
		init();
		writeData(this);
	}

	public MapNode getHwNode() {
		return getNode("hearthwell");
	}

	public MapNode getNode(String id) {
		return nodes.get(id);
	}

	public MapNode getNode(byte x, byte y) {
		return nodesByPosition.get(MapNode.hash(x, y));
	}

	public MapNode getNode(byte[] rp) {
		return getNode(rp[0], rp[1]);
	}

	public String getNodeId(MapNode node) {
		for (String nodeId : nodes.keySet()) {
			if (nodes.get(nodeId) == node)
				return nodeId;
		}
		return null;
	}

	public Collection<MapNode> getAll() {
		return nodes.values();
	}

	public boolean nodeExists(short nodeXY) {
		for (MapNode node : nodes.values()) {
			if (nodeXY == MapNode.hash(node.x, node.y))
				return true;
		}
		return false;
	}

	@SafeVarargs
	public final static <T> T[] array(T... recipes) {
		return recipes;
	}

	//
	//
	//

	public static void loadData() {
		DATA = readData();
		DATA.createBaseNode();
		DATA.init();
		if (DATA == null)
			throw new RuntimeException("Could not load Hearth Well data successfully from config " + CONFIG_FILE);
	}

	public static MapData readData() {
		BufferedReader reader = null;
		File file = new File("config", CONFIG_FILE);
		if (!file.exists()) {
			return writeDefaultData();
		}
		try {
			reader = new BufferedReader(new FileReader(file));
			return ((MapData) gson.fromJson(reader, MapData.class)) //
					.createBaseNode() //
					.init(); //
			// @f--
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (Exception e) {
				}
		} // @f++
		return null;
	}

	public static MapData writeDefaultData() {
		return writeData( //
				new MapData() //
						.createBaseNode() //
						.addDefaults() //
						.init() //
		);
	}

	public static MapData writeData(MapData data) {
		BufferedWriter writer = null;
		try {
			// TODO
//			writer = new BufferedWriter(new FileWriter(new File("config", CONFIG_FILE)));
//			Gson gson = new GsonBuilder().setPrettyPrinting().create();
//			writer.write(gson.toJson(data));
			return data;
			// @f--
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null)
				try {
					writer.close();
				} catch (Exception e) {
				}
		} // @f++
		return null;
	}

	public static class HearthWellException extends Exception {
		private static final long serialVersionUID = HearthWell.VERSION.hashCode();

		public HearthWellException(String message) {
			super(message);
		}
	}

}
