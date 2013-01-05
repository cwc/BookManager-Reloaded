package com.LRFLEW.bukkit.book.listener;

import com.LRFLEW.bukkit.book.BMPlugin;
import com.LRFLEW.bukkit.book.BookSave;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener{
    private final BMPlugin plugin;

    public PlayerJoin(BMPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleLogin(PlayerJoinEvent event){
        if(!event.getPlayer().hasPlayedBefore()){
            for(String book : plugin.firstSpawnBooks) BookSave.loadBook(plugin.econ, event.getPlayer(), plugin.getDataFolder(), book);
        }
    }
}
