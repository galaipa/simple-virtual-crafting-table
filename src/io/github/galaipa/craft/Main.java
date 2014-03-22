
package io.github.galaipa.craft;





import io.github.galaipa.craft.Updater.ReleaseType;
import java.io.IOException;
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
    public static boolean update = false;
    public static String name = "";
    public static ReleaseType type = null;
    public static String version = "";
    public static String link = "";

    @Override
    public void onDisable() {
        PluginManager pluginManager = getServer().getPluginManager();
        log.info(getConfig().getString("1"));
    }

    @Override
    public void onEnable() {        
        log.info(getConfig().getString("2"));
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new UpdateListener(), this);
        if ((getConfig().getBoolean("Economy"))){
            if (!setupEconomy()){
                log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return; 
            }
        }
        if ((getConfig().getBoolean("Updater"))){
        Updater updater = new Updater(this, 76220, this.getFile(), Updater.UpdateType.NO_DOWNLOAD, false); // Start Updater but just do a version check
        update = updater.getResult() == Updater.UpdateResult.UPDATE_AVAILABLE; // Determine if there is an update ready for us
        name = updater.getLatestName(); // Get the latest name
        version = updater.getLatestGameVersion(); // Get the latest game version
        type = updater.getLatestType(); // Get the latest file's type
        link = updater.getLatestFileLink(); // Get the latest link

                }
        //Metrics
        if ((getConfig().getBoolean("Metrics"))){
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            } catch (IOException e) {
                // Failed to submit the stats :-(
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
        if (cmd.getName().equalsIgnoreCase("svw")) {
            if (args[0].equalsIgnoreCase("update")) {
            Updater updater = new Updater(this, 76220, this.getFile(), Updater.UpdateType.NO_VERSION_CHECK, true); // Go straight to downloading, and announce progress to console.
            sender.sendMessage(ChatColor.GREEN + "Update progress in the console");
            }
            }
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


}

}





