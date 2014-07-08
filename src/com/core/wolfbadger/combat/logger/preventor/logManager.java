package com.core.wolfbadger.combat.logger.preventor;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: MayoDwarf
 * Date: 6/21/14
 * Time: 7:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class logManager {
    Main m;
    public logManager(Main m) {
        this.m = m;
    }
    private HashMap<UUID, playerLogger> playerManager = new HashMap<UUID, playerLogger>();
    public playerLogger getLogger(UUID id) {
        if(this.playerManager.containsKey(id)) {
            return this.playerManager.get(id);
        } else {
            playerLogger pL = new playerLogger(id, m, this);
            this.playerManager.put(id, pL);
            return this.playerManager.get(id);
        }
    }
    public boolean hasLogger(UUID id) {
        if(this.playerManager.containsKey(id))
            return true;
        else
            return false;
    }
    public void createLogger(UUID id) {
        this.playerManager.put(id, new playerLogger(id, m, this));
    }
    public void removePlayer(UUID id) {
        this.playerManager.remove(id);
    }
}
