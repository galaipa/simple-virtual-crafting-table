
package io.github.galaipa.craft;





import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static Economy econ = null;
    
    public static final Logger log = Logger.getLogger("Minecraft");    

    @Override
    public void onDisable() {
        PluginManager pluginManager = getServer().getPluginManager();
        log.info(getConfig().getString("1"));
    }

    @Override
    public void onEnable() {        
        log.info(getConfig().getString("2"));
        saveDefaultConfig();
        
        if ((getConfig().getBoolean("Economy"))){
            if (!setupEconomy()){
                log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
                
            }
        }
        
    }
    private boolean setupEconomy() {
        
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player player = (Player)sender;
        
        if(cmd.getName().equalsIgnoreCase("craftingtable")){
//ECONOMY OFF         
            if (!(getConfig().getBoolean("Economy"))){
                if (player.hasPermission("spc.craft")) {
                    player.openWorkbench(null, isEnabled());
                    sender.sendMessage(ChatColor.GREEN +(getConfig().getString("4")));
                    return true;
                }
                else {
                    sender.sendMessage(ChatColor.RED +(getConfig().getString("3")));
                }
            }
//ECONOMY ON            
            else {
                if (player.hasPermission("spc.free")) {
                    player.openWorkbench(null, isEnabled());
                    sender.sendMessage(ChatColor.GREEN +(getConfig().getString("4")));
                    return true;
                }
                else if (player.hasPermission("spc.craft")) {
                   int Prezioa = this.getConfig().getInt("Price"); 
                   EconomyResponse r = econ.withdrawPlayer(player.getName(), Prezioa);
                    if (r.transactionSuccess()){
                        player.openWorkbench(null, isEnabled());
                        sender.sendMessage(ChatColor.GREEN +(getConfig().getString("4")));
                        sender.sendMessage(ChatColor.RED + (getConfig().getString("6")) + (":") + (" ") + ("-") + (getConfig().getString("Price")) + ("$"));
                        return true;
                    }
                    else {
                        sender.sendMessage(ChatColor.RED +(getConfig().getString("5")) + (".") + (" ") + ((getConfig().getString("6")) + (":") + (" ") + ("-") + (getConfig().getString("Price")) + ("$")));
                    }
                }
                else {
                    sender.sendMessage(ChatColor.RED +(getConfig().getString("3")));
                }
                
            }
            
        }return true;


}}

    


