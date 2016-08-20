package me.homes;

import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class Home
  implements CommandExecutor
{
  int high = Math.addExact(con.getInt("Config.long"), 1);
  public int countdown;
  public static File conf = new File("plugins/Homes/config.yml");
  public static FileConfiguration con = YamlConfiguration.loadConfiguration(conf);

  
  
  public Home(Homes homes) {
    homes.getCommand("home").setExecutor(this);
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    File file = new File("plugins/" + ((Homes)Homes.getPlugin(Homes.class)).getDescription().getName() + "/homes.yml");
    final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
    if ((sender instanceof Player)) {
      Player p = (Player)sender;

      if (command.getName().equalsIgnoreCase("home")) {
        if (p.hasPermission("homes.home")) {
          if (args.length == 1)
          {
            if (Math.addExact(con.getInt("Config.long"), 1) <= -2) {
              System.err.println("[ERROR] HOME PLUGIN!");
              System.err.println("[ERROR] CONFIG.YML LONG IS SMALLER THEN -1");
              Bukkit.getScheduler().cancelTask(this.countdown);
            }
            else if (cfg.getString("Homes." + p.getName() + "." + args[0]) == null) {
              p.sendMessage(Homes.prefix + "Das Home existiert nicht");
              Bukkit.getScheduler().cancelTask(this.countdown);
            }
            else {
              this.countdown = Bukkit.getScheduler().scheduleSyncRepeatingTask(Homes.getPlugin(Homes.class), new Runnable()
              {
                public void run()
                {
                  Player p = (Player)sender;
                  if (Home.this.high != 0) {
                    Home.this.high -= 1;
                    p.playSound(p.getLocation(), Sound.valueOf(con.getString("Config.Sound")), 1.0F, 1.0F);
                  }
                  else {
                    Location home = new Location(Bukkit.getWorld(cfg.getString(new StringBuilder("Homes.").append(p.getName()).append(".").append(args[0]).append(".").append("world").toString())), cfg.getDouble("Homes." + p.getName() + "." + args[0] + "." + "x"), cfg.getDouble("Homes." + p.getName() + "." + args[0] + "." + "y"), cfg.getDouble("Homes." + p.getName() + "." + args[0] + "." + "z"));
                    p.teleport(home);
                    Bukkit.getScheduler().cancelTask(Home.this.countdown);
                    Home.this.high = Math.addExact(Home.con.getInt("Config.long"), 1);
                  }
                }
              }
              , 0L, 20L);
            }

          }
          else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("delete"))
            {
              cfg.set("Homes." + p.getName() + "." + args[1], null);
              p.sendMessage(Homes.prefix + "Home " + args[1] + " was deleted.");

              if (cfg.getInt("Homes." + p.getName() + ".homes") <= 0) {
                cfg.set("Homes." + p.getName() + ".homes", Integer.valueOf(Math.addExact(cfg.getInt("Homes." + p.getName() + ".homes"), 1)));
                try {
                  cfg.save(file);
                } catch (IOException e) {
                  e.printStackTrace();
                }
              }

              cfg.set("Homes." + p.getName() + ".homes", Integer.valueOf(Math.subtractExact(cfg.getInt("Homes." + p.getName() + ".homes"), 1)));
            }
            try {
              cfg.save(file);
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
        else {
          p.sendMessage(Homes.prefix + "/home <Home>");
          p.sendMessage(Homes.prefix + "/home delete <Home>");
        }
      }
    }
    else
    {
      sender.sendMessage(Homes.prefix + "Du musst ein Spieler sein!");
    }
    return true;
  }
}