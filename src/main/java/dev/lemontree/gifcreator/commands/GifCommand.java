package dev.lemontree.gifcreator.commands;

import dev.lemontree.gifcreator.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;

public class GifCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        if (args.length == 0) {
            sender.sendMessage("Invalid number of args!");
            return false;
        }

        String argString = Arrays.stream(args).reduce((s, o) -> s + " " + o).get();

        MapRenderer gifRenderer;

        try {
            gifRenderer = new GifMapRenderer(argString);
        } catch (RuntimeException e) {
            Message.sendError((Player) sender, e.getMessage());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            Message.sendError((Player) sender, "Error creating gif");
            return true;
        }


        MapView map = Bukkit.createMap(Bukkit.getWorlds().get(0));  // Replace with your desired world

        for (MapRenderer renderer : map.getRenderers()) {
            map.removeRenderer(renderer);
        }

        map.addRenderer(gifRenderer);

        ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
        MapMeta meta = (MapMeta) mapItem.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + argString);
        meta.setMapView(map);
        mapItem.setItemMeta(meta);

        ((Player) sender).getInventory().addItem(mapItem);
        return true;
    }
}
