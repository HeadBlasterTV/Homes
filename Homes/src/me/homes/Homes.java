package me.homes;

import java.io.File;
import java.io.IOException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Homes extends JavaPlugin
{
  public static File conf = new File("plugins/Homes/config.yml");
  public static FileConfiguration con = YamlConfiguration.loadConfiguration(conf);

  public static String prefix = ChatColor.translateAlternateColorCodes('&', "&e[&6Home&e]&6");

  public void onEnable()
  {
    con.addDefault("Config.long", Integer.valueOf(3));
    con.addDefault("Config.homes-per-player", Integer.valueOf(5));
    con.addDefault("Config.prefix", "&e[&6Home&e]&6");
    con.addDefault("Config.Sound", "BLOCK_NOTE_PLING");
    conf.setWritable(true);
    con.options().copyDefaults(true);
    try
    {
      con.save(conf);
    } catch (IOException e) {
      e.printStackTrace();
    }

    getServer().getPluginCommand("sethome").setExecutor(new setHome(this));
    getServer().getPluginCommand("home").setExecutor(new Home(this));
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    if ((sender instanceof Player)) {
      Player p = (Player)sender;
      if ((command.getName().equalsIgnoreCase("homes")) && 
        (p.hasPermission("homes.prefix")) && 
        (args.length == 0)) {
        setPrefix(args[0]);
        sender.sendMessage(prefix + "Der Prefix wurde geändert!");
      }

    }
    else if ((command.getName().equalsIgnoreCase("homes")) && 
      (sender.hasPermission("homes.prefix"))) {
      setPrefix(args[0]);
      sender.sendMessage(prefix + "Der Prefix wurde geändert!");
    }

    return true;
  }
  public static void setPrefix(String prefix) {
    prefix = ChatColor.translateAlternateColorCodes('&', prefix);
  }
}