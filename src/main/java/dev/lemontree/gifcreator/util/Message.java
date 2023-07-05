package dev.lemontree.gifcreator.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Message {
    public static void sendError(Player player, String text) {
        player.sendMessage(ChatColor.RED + text);
    }
}
