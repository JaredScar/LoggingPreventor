package com.core.wolfbadger.combat.logger.preventor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: MayoDwarf
 * Date: 6/21/14
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class playerLogger {
    private Player p;
    private String id;
    private String name;
    Main m;
    logManager lM;
    private ItemStack[] contents;
    private ItemStack[] equipment;
    private Villager v;
    private Double vHealth;
    public playerLogger(UUID id, Main m, logManager lM) {
        this.p = Bukkit.getPlayer(id);
        this.id = p.getUniqueId().toString();
        this.name = p.getName();
        this.m = m;
        this.lM = lM;
        this.contents = p.getInventory().getContents();
        this.equipment = p.getInventory().getArmorContents();
        this.v = (Villager) p.getLocation().getWorld().spawnEntity(p.getLocation(), EntityType.VILLAGER);
        this.v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 127));
        Double health = p.getHealth();
        this.v.setMaxHealth(health);
        this.v.setHealth(health);
        this.vHealth = health;
        this.v.setCustomName(""+p.getDisplayName());
        this.v.setCustomNameVisible(true);
    }
    public Villager getVillager() {
        return this.v;
    }
    public void delete() {
        this.v.remove();
    }
    public void minusHealth(Double d, Player damager) {
        if(this.vHealth - d <= 0) {
            for(ItemStack itemStack : this.contents) {
                if(itemStack !=null) {
                    if(!itemStack.getType().equals(null) && !itemStack.getType().equals(Material.AIR)) {
                this.v.getWorld().dropItemNaturally(this.v.getLocation(), itemStack);
                    }
                }
            }
            for(ItemStack itemStack : this.equipment) {
                if(itemStack !=null) {
                    if(!itemStack.getType().equals(null) && !itemStack.getType().equals(Material.AIR)) {
                        this.v.getWorld().dropItemNaturally(this.v.getLocation(), itemStack);
                    }
                }
            }
            this.delete();
            this.v = null;
            String msg = this.m.getConfig().getString("Messages.LoggerWasKilled");
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg).replace("%killed%", this.name).replace("%killer%", damager.getName()));
        } else {
            this.vHealth = this.vHealth - d;
            this.v.setHealth(this.vHealth);
        }
    }
    public void kill() {
        Bukkit.getPlayer(UUID.fromString(this.id)).getInventory().clear();
        Bukkit.getPlayer(UUID.fromString(this.id)).getInventory().setArmorContents(null);
        Bukkit.getPlayer(UUID.fromString(this.id)).setHealth(0D);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.m.getConfig().getString("Messages.PlayerKilledOnLogin")).replace("%killed%", this.name));
        this.lM.removePlayer(UUID.fromString(this.id));
        this.m.removePlayer(UUID.fromString(this.id));
    }
    public void setHealth() {
        Bukkit.getPlayer(UUID.fromString(this.id)).setHealth(this.vHealth);  System.out.print("On Login: "+this.vHealth);
        this.delete();
        this.lM.removePlayer(UUID.fromString(this.id));
    }
}
