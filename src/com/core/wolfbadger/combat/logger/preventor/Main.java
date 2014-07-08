package com.core.wolfbadger.combat.logger.preventor;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: MayoDwarf
 * Date: 6/21/14
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main extends JavaPlugin implements Listener {
    logManager lM;
    Main m;
    final private HashMap<UUID, Double> combTagged = new HashMap<UUID, Double>();
    private HashMap<UUID, UUID> vTracker = new HashMap<UUID, UUID>();
    public void onEnable() {
        this.lM = new logManager(this);
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        m = this;
        this.getServer().getPluginManager().registerEvents(this, this);
        //TODO Add values back to spots.
        int i = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new BukkitRunnable() {
            public void run() {
                for(Iterator it = m.combTagged.keySet().iterator(); it.hasNext();) {     System.out.print("YEs 2");
                        UUID ids = (UUID) it.next();
                        Double d = getPlayer(ids);
                        putPlayer(ids, d- 1D);
                        if (getPlayer(ids) <= 0D) { System.out.print("YEs 3");
                            it.remove();
                    }
                }
            }
        }, 0, 20);
    }
    public void onDisable() {
        //TODO Remove values from lists and add them to config and restore on enable.
    }
    public int size() {
        return m.combTagged.keySet().size();
    }
    public double getPlayer(UUID id) {
        return m.combTagged.get(id);
    }
    public void putPlayer(UUID id, Double d) {
        m.combTagged.put(id, d);
    }
    public void removePlayer(UUID id) {
        m.combTagged.remove(id);
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if(lM.hasLogger(p.getUniqueId())) {
            playerLogger pL = lM.getLogger(p.getUniqueId());
            if(pL.getVillager() == null) {
                pL.kill();
            } else {
                pL.setHealth();
            }
        }
    }
    @EventHandler
    public void onComb(EntityDamageByEntityEvent e) {
        Entity ent = e.getEntity();
        Entity damager = e.getDamager();
        if(ent instanceof Player && damager instanceof Player) {
            final Player p = (Player) ent;
            Player damagerr = (Player) damager;
            m.combTagged.put(p.getUniqueId(), getConfig().getDouble("CombatTagExpireTime"));
            m.combTagged.put(damagerr.getUniqueId(), getConfig().getDouble("CombatTagExpireTime"));
        } else
            if(ent instanceof Villager && damager instanceof Player) {
                Villager v = (Villager) ent;
                Double d = e.getDamage();
                Player damagerr = (Player) damager;
                if(v.isCustomNameVisible()) {
                    UUID id = vTracker.get(v.getUniqueId());
                    if(lM.hasLogger(id)) {
                        playerLogger pL = lM.getLogger(id);
                        e.setCancelled(true);
                        pL.minusHealth(d, damagerr);
                }
            }
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        if(m.combTagged.containsKey(p.getUniqueId())) {
            lM.createLogger(p.getUniqueId());
            vTracker.put(lM.getLogger(p.getUniqueId()).getVillager().getUniqueId(), p.getUniqueId());
        }
    }
}
