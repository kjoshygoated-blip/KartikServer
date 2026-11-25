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
    public static Map<String, Boolean> flying = new HashMap<>();
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
            player.sendMessage("§fMade by kartikdebest! Type /help for all commands!");
        });

        // /help
        Command help = new Command("help");
        help.setDefaultExecutor((s, c) -> s.sendMessage("§b§l=== ALL COMMANDS ===\n§f/kartik - Info\n§f/heal - Heal\n§f/op <player> - Make OP\n§f/gamemode <0/1/2/3> - Change gamemode\n§f/give <item> - Give item\n§f/tp <player> - Teleport\n§f/fly - Toggle fly\n§f/kick <player> - Kick\n§f/list - List players\n§f/say <msg> - Broadcast"));
        MinecraftServer.getCommandManager().register(help);

        // /kartik
        Command kartik = new Command("kartik");
        kartik.setDefaultExecutor((s, c) -> s.sendMessage("§b§l=== KARTIK SERVER ===\n§fNo Paper, No Fabric - Just Kartik!"));
        MinecraftServer.getCommandManager().register(kartik);

        // /heal
        Command heal = new Command("heal");
        heal.setDefaultExecutor((s, c) -> { if (s instanceof Player p) { p.setHealth(20); p.sendMessage("§aHealed!"); } });
        MinecraftServer.getCommandManager().register(heal);

        // /op
        Command op = new Command("op");
        op.addSyntax((s, c) -> {
            String name = c.get(ArgumentType.String("player"));
            ops.add(name);
            s.sendMessage("§aMade " + name + " OP!");
        }, ArgumentType.String("player"));
        MinecraftServer.getCommandManager().register(op);

        // /gamemode
        Command gm = new Command("gamemode");
        gm.addSyntax((s, c) -> {
            if (s instanceof Player p) {
                int mode = c.get(ArgumentType.Integer("mode"));
                if (mode == 1) p.setGameMode(GameMode.CREATIVE);
                else if (mode == 2) p.setGameMode(GameMode.ADVENTURE);
                else if (mode == 3) p.setGameMode(GameMode.SPECTATOR);
                else p.setGameMode(GameMode.SURVIVAL);
                p.sendMessage("§aGamemode set to " + mode);
            }
        }, ArgumentType.Integer("mode"));
        MinecraftServer.getCommandManager().register(gm);

        // /give
        Command give = new Command("give");
        give.addSyntax((s, c) -> {
            if (s instanceof Player p) {
                String item = c.get(ArgumentType.String("item"));
                try {
                    Material mat = Material.fromNamespaceId("minecraft:" + item.toLowerCase());
                    if (mat == null) mat = Material.DIAMOND_SWORD;
                    p.getInventory().addItemStack(ItemStack.of(mat, 1));
                    p.sendMessage("§aGave you " + item);
                } catch (Exception e) { p.sendMessage("§cItem not found!"); }
            }
        }, ArgumentType.String("item"));
        MinecraftServer.getCommandManager().register(give);

        // /fly
        Command fly = new Command("fly");
        fly.setDefaultExecutor((s, c) -> {
            if (s instanceof Player p) {
                boolean isFlying = flying.getOrDefault(p.getUsername(), false);
                flying.put(p.getUsername(), !isFlying);
                p.setAllowFlying(!isFlying);
                p.setFlying(!isFlying);
                p.sendMessage(!isFlying ? "§aFly ON!" : "§cFly OFF!");
            }
        });
        MinecraftServer.getCommandManager().register(fly);

        // /list
        Command list = new Command("list");
        list.setDefaultExecutor((s, c) -> {
            s.sendMessage("§bPlayers: " + MinecraftServer.getConnectionManager().getOnlinePlayers().size());
            MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p -> s.sendMessage("§f- " + p.getUsername()));
        });
        MinecraftServer.getCommandManager().register(list);

        // /say
        Command say = new Command("say");
        say.addSyntax((s, c) -> {
            String msg = c.get(ArgumentType.String("msg"));
            MinecraftServer.getConnectionManager().getOnlinePlayers().forEach(p -> p.sendMessage("§d[SERVER] " + msg));
        }, ArgumentType.String("msg"));
        MinecraftServer.getCommandManager().register(say);

        server.start("0.0.0.0", 25565);
        System.out.println("KARTIK SERVER IS ON! Join with: localhost");
    }
}
