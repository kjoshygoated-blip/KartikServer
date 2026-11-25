package com.kartik.server;

import net.minestom.server.MinecraftServer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.GlobalEventHandler;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.InstanceManager;
import net.minestom.server.instance.block.Block;
import net.minestom.server.command.builder.Command;

public class KartikServer {
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
            player.sendMessage("§fThis server was made by kartikdebest!");
            player.sendMessage("§aType /kartik for commands!");
            player.sendMessage("§aType /heal to heal!");
        });
        Command kartikCmd = new Command("kartik");
        kartikCmd.setDefaultExecutor((sender, ctx) -> {
            sender.sendMessage("§b§l=== KARTIK SERVER ===");
            sender.sendMessage("§fMade by kartikdebest!");
            sender.sendMessage("§fNo Paper, No Fabric - Just Kartik!");
        });
        MinecraftServer.getCommandManager().register(kartikCmd);
        Command healCmd = new Command("heal");
        healCmd.setDefaultExecutor((sender, ctx) -> {
            if (sender instanceof Player p) {
                p.setHealth(20);
                p.sendMessage("§a§lHealed! Full HP!");
            }
        });
        MinecraftServer.getCommandManager().register(healCmd);
        server.start("0.0.0.0", 25565);
        System.out.println("KARTIK SERVER IS ON! Join with: localhost");
    }
}
