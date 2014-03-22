/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.galaipa.craft;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 *
 * @author binga_000
 */
public final class UpdateListener implements Listener {

    @EventHandler
public void onPlayerJoin(PlayerJoinEvent event)
{
  Player player = event.getPlayer();
  if(player.hasPermission("spc.update") && Main.update)
  {
    player.sendMessage(ChatColor.GREEN + "An update is available: " + ChatColor.YELLOW + Main.name + ChatColor.GREEN +  " for " + Main.version + " available at " +  ChatColor.YELLOW + "http://goo.gl/f2Hv2W");
    // Will look like - An update is available: AntiCheat v1.5.9, a release for CB 1.6.2-R0.1 available at http://media.curseforge.com/XYZ
    player.sendMessage(ChatColor.RED + "Type /svw update if you would like to automatically update.");
  }
}
}
