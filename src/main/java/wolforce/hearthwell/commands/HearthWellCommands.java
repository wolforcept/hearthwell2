package wolforce.hearthwell.commands;

import static net.minecraft.commands.Commands.literal;

import java.util.function.Predicate;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import wolforce.hearthwell.client.screen.ScreenHearthWellMap;
import wolforce.hearthwell.data.MapData;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HearthWellCommands {

	private static Predicate<CommandSourceStack> OP = (source) -> {
		return source.hasPermission(2);
	};

	@SubscribeEvent
	public static void init(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> d = event.getDispatcher();

		if (FMLEnvironment.dist == Dist.CLIENT) {
			d.register(literal("hearthwell")//
					.then(literal("reload") //
							.requires(OP) //
							.executes(HearthWellCommands.reloadData)//
					)//
					.then(literal("edit") //
							.requires(OP) //
							.executes(HearthWellCommands.toggleEditMode)//
					)//
			); //
		}
	}

	public static final Command<CommandSourceStack> reloadData = (CommandContext<CommandSourceStack> context) -> {
		CommandSourceStack source = context.getSource();
		try {
			ServerLevel level = source.getLevel();
			Player player = source.getPlayerOrException();
			if (level.getServer().isSingleplayer() && !level.getServer().isPublished()) {
				player.sendMessage(new TextComponent("Will reload Hearth Well Map Data."), Util.NIL_UUID);
				MapData.loadData();
			} else {
				TextComponent tc = new TextComponent("You may only reload the Hearth Well Map in Single Player.");
				tc.setStyle(Style.EMPTY.withColor(0xFF5555));
				player.sendMessage(tc, Util.NIL_UUID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	};

	public static final Command<CommandSourceStack> toggleEditMode = (CommandContext<CommandSourceStack> context) -> {

		CommandSourceStack source = context.getSource();
		try {
			ServerLevel level = source.getLevel();
			Player player = source.getPlayerOrException();
			if (level.getServer().isSingleplayer() && !level.getServer().isPublished()) {
				ScreenHearthWellMap.EDIT_MODE = !ScreenHearthWellMap.EDIT_MODE;
				player.sendMessage(new TextComponent("Hearth Well Edit Mode is now: " + (ScreenHearthWellMap.EDIT_MODE ? "ON" : "OFF")), Util.NIL_UUID);
			} else {
				TextComponent tc = new TextComponent("You may only edit the Hearth Well Map in Single Player.");
				tc.setStyle(Style.EMPTY.withColor(0xFF5555));
				player.sendMessage(tc, Util.NIL_UUID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 1;
	};
}
