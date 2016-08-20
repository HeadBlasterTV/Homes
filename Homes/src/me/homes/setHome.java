package me.homes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class setHome
  implements CommandExecutor
{
  public static File conf = new File("plugins/Homes/config.yml");
  public static FileConfiguration con = YamlConfiguration.loadConfiguration(conf);

  public static List<String> homes = new ArrayList<String>();

  public setHome(Homes homes) {
    homes.getCommand("sethome").setExecutor(this);
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    File file = new File("plugins/" + ((Homes)Homes.getPlugin(Homes.class)).getDescription().getName() + "/homes.yml");
    FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    if ((sender instanceof Player)) {
      Player p = (Player)sender;

      if (command.getName().equalsIgnoreCase("sethome")) {
        if (cfg.getInt("Homes." + p.getName() + ".homes") >= con.getInt("Config.homes-per-player")) {
          p.sendMessage(Homes.prefix + "Du hast schon mehr als " + con.getInt("Config.homes-per-player") + " Homes!");
          return false;
        }

        if (p.hasPermission("homes.sethome")) {
          cfg.set("Homes." + p.getName() + "." + args[0] + ".world", p.getWorld().getName());
          cfg.set("Homes." + p.getName() + "." + args[0] + ".x", Integer.valueOf(p.getLocation().getBlockX()));
          cfg.set("Homes." + p.getName() + "." + args[0] + ".y", Integer.valueOf(p.getLocation().getBlockY()));
          cfg.set("Homes." + p.getName() + "." + args[0] + ".z", Integer.valueOf(p.getLocation().getBlockZ()));

          cfg.set("Homes." + p.getName() + ".homes", Integer.valueOf(Math.addExact(cfg.getInt("Homes." + p.getName() + ".homes"), 1)));

          homes.add(args[0]);
          try
          {
            con.save(conf);
            cfg.save(file);
            p.sendMessage(Homes.prefix + "Das Home '" + args[0] + "' wurde gesetzt!");
          } catch (IOException e) {
            e.printStackTrace();
          }
        } else {
          p.sendMessage(Homes.prefix + "§4Du brauchst die Permission: homes.sethome");
        }
      }

    }

    return true;
  }
}