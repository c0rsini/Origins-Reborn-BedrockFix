package com.starshootercity.abilities;

import com.destroystokyo.paper.event.player.PlayerPostRespawnEvent;
import com.starshootercity.OriginsReborn;
import com.starshootercity.abilities.Phantomize;
import com.starshootercity.abilities.types.DependantAbility;
import net.kyori.adventure.key.Key;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PhantomizeOverlay
implements DependantAbility,
Listener {
    @NotNull
    public Key getKey() {
        return Key.key((String)"origins:phantomize_overlay");
    }

    @NotNull
    public Key getDependencyKey() {
        return Key.key((String)"origins:phantomize");
    }

    @EventHandler
    public void onPhantomizeToggle(Phantomize.PhantomizeToggleEvent event) {
        this.updatePhantomizeOverlay(event.getPlayer());
    }

    @EventHandler
    public void onPlayerPostRespawn(PlayerPostRespawnEvent event) {
        this.updatePhantomizeOverlay(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.updatePhantomizeOverlay(event.getPlayer());
    }

    private void updatePhantomizeOverlay(Player player) {
        // Do not send world border overlay (ClientboundSetBorderSizePacket) to Bedrock
        // players via Geyser+Floodgate - it causes permanent Nether fog on their client.
        if (Bukkit.getPluginManager().getPlugin("floodgate") != null) {
            try {
                Class<?> apiClass = Class.forName("org.geysermc.floodgate.api.FloodgateApi");
                Object api = apiClass.getMethod("getInstance").invoke(null);
                Boolean isFloodgate = (Boolean) apiClass.getMethod("isFloodgatePlayer", java.util.UUID.class).invoke(api, player.getUniqueId());
                if (Boolean.TRUE.equals(isFloodgate)) {
                    return;
                }
            } catch (Throwable ignored) {
                // Floodgate API unavailable or error; proceed with normal behavior
            }
        }
        OriginsReborn.getMVE().setWorldBorderOverlay(player, this.getDependency().isEnabled(player));
    }
}
