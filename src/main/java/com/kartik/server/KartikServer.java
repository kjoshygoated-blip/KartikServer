package com.kartik.server;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.GameMode;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import java.util.*;

public class KartikServer {
    public static Set<String> ops = new HashSet<>();
    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.init();
        InstanceManager instanceManager = MinecraftServer.getInstanceManager();
        InstanceContainer instance = instanceManager.createInstanceContainer();
        instance.setGenerator(unit -> {
            unit.modifier().fillHeight(0, 40, Block.GRASS_BLOCK);
            unit.modifier().fillHeight(0, 1, Block.BEDROCK);
        });
        GlobalEventHandler handler = MinecraftServer.getGlobalEventHandler();
        handler.addListener(AsyncPlayerConfigurationEvent.class, event -> {
            Player player = event.getPlayer();
            event.setSpawningInstance(instance);
            player.setRespawnPoint(new Pos(0, 42, 0));
            player.sendMessage("§b§lWELCOME TO KARTIK SERVER!");
            player.sendMessage("§fMade by kartikdebest! Type /help!");
        });
        Command help = new Command("help");
        help.setDefaultExecutor((s, c) -> s.sendMessage("§b§l=== ALL COMMANDS ===\n§f/kartik - Info\n§f/heal - Heal\n§f/op <player> - Make OP\n§f/gamemode <0/1> - Gamemode\n§f/give - Give sword\n§f/fly - Fly\n§f/list - List\n§f/say <msg> - Broadcast"));
        MinecraftServer.getCommandManager().register(help);
        Command kartik = new Command("kartik");
        kartik.setDefaultExecutor((s, c) -> s.sendMessage("§b§l=== KARTIK SERVER ===\n§fNo Paper, No Fabric - Just Kartik!"));
        MinecraftServer.getCommandManager().register(kartik);
        Command heal = new Command("heal");
        heal.setDefaultExecutor((s, c) -> { if (s instanceof Player p) { p.setHealth(20); p.sendMessage("§aHealed!"); } });
        MinecraftServer.getCommandManager().register(heal);
        Command op = new Command("op");
        op.addSyntax((s, c) -> { String name = c.get(ArgumentType.String("player")); ops.add(name); s.sendMessage("§aMade " + name + " OP!"); }, ArgumentType.String("player"));
        MinecraftServer.getCommandManager().register(op);
        Command gm = new Command("gamemode");
        gm.addSyntax((s, c) -> { if (s instanceof Player p) { int mode = c.get(ArgumentType.Integer("mode")); if (mode == 1) p.setGameMode(GameMode.CREATIVE); else p.setGameMode(GameMode.SURVIVAL); p.sendMessage("§aGamemode set!"); } }, ArgumentType.Integer("mode"));
        MinecraftServer.getCommandManager().register(gm);
        Command give = new Command("give");
        give.setDefaultExecutor((s, c) -> { if (s instanceof Player p) { p.getInventory().addItemStack(ItemStack.of(Material.DIAMOND_SWORD, 1)); p.sendMessage("§aGave you Diamond Sword!"); } });
        MinecraftServer.getCommandManager().register(give);
        Command fly = new Command("fly");
        fly.setDefaultExecutor((s, c) -> { if (s instanceof Player p) { p.setAllowFlying(true); p.setFlying(true); p.sendMessage("§aFly ON!"); } });
        MinecraftServer.getCommandManager().register(fly);
        Command list = new Command("list");
        list.setDefaultExecutor((s, c) -> { s.sendMessage("§bPlayers: " + MinecraftServer.getConnectionManager().getOnlinePlayers().size()); });
        MinecraftServer.getCommandManager().register(list);
        Command say = new Command("say");
        say.addSyntax((s, c) -> { String msg = c.get(ArgumentType.String("msg")); MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p -> p.sendMessage("§d[SERVER] " + msg)); }, ArgumentType.String("msg"));
        MinecraftServer.getCommandManager().register(say);
        server.start("0.0.0.0", 25565);
        System.out.println("KARTIK SERVER IS ON! Join with: localhost");
    }
}
