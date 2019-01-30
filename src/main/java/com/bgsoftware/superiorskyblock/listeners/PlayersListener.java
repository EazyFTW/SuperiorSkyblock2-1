package com.bgsoftware.superiorskyblock.listeners;

import com.bgsoftware.superiorskyblock.Locale;
import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.events.IslandEnterEvent;
import com.bgsoftware.superiorskyblock.api.events.IslandLeaveEvent;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPermission;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import com.bgsoftware.superiorskyblock.utils.legacy.Materials;
import com.bgsoftware.superiorskyblock.wrappers.SSuperiorPlayer;
import com.bgsoftware.superiorskyblock.wrappers.SBlockPosition;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("unused")
public final class PlayersListener implements Listener {

    private SuperiorSkyblockPlugin plugin;

    public PlayersListener(SuperiorSkyblockPlugin plugin){
        this.plugin = plugin;
        new PlayerArrowPickup();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        SuperiorPlayer superiorPlayer = SSuperiorPlayer.of(e.getPlayer());
        if(!superiorPlayer.getName().equals(e.getPlayer().getName())){
            superiorPlayer.updateName();
        }
        plugin.getNMSAdapter().setSkinTexture(superiorPlayer);

        Island island = superiorPlayer.getIsland();

        if(island != null)
            island.sendMessage(Locale.PLAYER_JOIN_ANNOUNCEMENT.getMessage(superiorPlayer.getName()), superiorPlayer.getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        SuperiorPlayer superiorPlayer = SSuperiorPlayer.of(e.getPlayer());
        Island island = superiorPlayer.getIsland();

        if(island != null)
            island.sendMessage(Locale.PLAYER_QUIT_ANNOUNCEMENT.getMessage(superiorPlayer.getName()), superiorPlayer.getUniqueId());
    }

    @EventHandler
    public void onIslandJoin(IslandEnterEvent e){
        plugin.getNMSAdapter().setWorldBorder(e.getPlayer(), e.getIsland());
    }

    @EventHandler
    public void onIslandJoin(IslandLeaveEvent e){
        plugin.getNMSAdapter().setWorldBorder(e.getPlayer(), null);
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player))
            return;

        SuperiorPlayer targetPlayer = SSuperiorPlayer.of((Player) e.getEntity());
        SuperiorPlayer damagerPlayer = SSuperiorPlayer.of((Player) e.getDamager());

        if(targetPlayer.getIsland() != null && targetPlayer.getIsland().equals(damagerPlayer.getIsland())){
            e.setCancelled(true);
            Locale.HIT_TEAM_MEMBER.send(targetPlayer);
        }

    }

    @EventHandler
    public void onEntityAttack(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player || !(e.getDamager() instanceof Player))
            return;

        SuperiorPlayer damagerPlayer = SSuperiorPlayer.of((Player) e.getDamager());
        Island island = plugin.getGrid().getIslandAt(e.getEntity().getLocation());

        if(island != null && !island.hasPermission(damagerPlayer, IslandPermission.ANIMAL_DAMAGE)){
            e.setCancelled(true);
            Locale.sendProtectionMessage(damagerPlayer);
        }
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractAtEntityEvent e){
        SuperiorPlayer superiorPlayer = SSuperiorPlayer.of(e.getPlayer());
        Island island = plugin.getGrid().getIslandAt(e.getRightClicked().getLocation());

        if(island != null && !island.hasPermission(superiorPlayer, IslandPermission.ANIMAL_BREED)){
            e.setCancelled(true);
            Locale.sendProtectionMessage(superiorPlayer);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerAsyncChat(AsyncPlayerChatEvent e){
        SuperiorPlayer superiorPlayer = SSuperiorPlayer.of(e.getPlayer());
        Island island = superiorPlayer.getIsland();

        if(superiorPlayer.hasTeamChatEnabled()){
            e.setCancelled(true);
            island.sendMessage(Locale.TEAM_CHAT_FORMAT.getMessage(superiorPlayer.getIslandRole(), superiorPlayer.getName(), e.getMessage()));
        }

        else {
            e.setFormat(e.getFormat()
                    .replace("{island-level}", String.valueOf(island == null ? 0 : island.getIslandLevel()))
                    .replace("{island-worth}", String.valueOf(island == null ? 0 : (int) island.getWorth())));
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        if(e.getItem() == null || e.getItem().getType() != Materials.GOLDEN_AXE.toBukkitType() ||
                !(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_BLOCK))
            return;

        SuperiorPlayer superiorPlayer = SSuperiorPlayer.of(e.getPlayer());

        if(!superiorPlayer.hasSchematicModeEnabled())
            return;

        e.setCancelled(true);

        if(e.getAction().name().contains("RIGHT")){
            Locale.SCHEMATIC_RIGHT_SELECT.send(superiorPlayer, SBlockPosition.of(e.getClickedBlock().getLocation()));
            superiorPlayer.setSchematicPos1(e.getClickedBlock());
        }
        else{
            Locale.SCHEMATIC_LEFT_SELECT.send(superiorPlayer, SBlockPosition.of(e.getClickedBlock().getLocation()));
            superiorPlayer.setSchematicPos2(e.getClickedBlock());
        }

        if(superiorPlayer.getSchematicPos1() != null && superiorPlayer.getSchematicPos2() != null)
            Locale.SCHEMATIC_READY_TO_CREATE.send(superiorPlayer);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e){
        SuperiorPlayer superiorPlayer = SSuperiorPlayer.of(e.getPlayer());
        Island island = plugin.getGrid().getIslandAt(superiorPlayer.getLocation());

        if(island != null && !island.hasPermission(superiorPlayer, IslandPermission.DROP_ITEMS)){
            e.setCancelled(true);
            Locale.sendProtectionMessage(superiorPlayer);
        }
    }

    @EventHandler
    public void onPlayerItemPickup(PlayerPickupItemEvent e){
        SuperiorPlayer superiorPlayer = SSuperiorPlayer.of(e.getPlayer());
        Island island = plugin.getGrid().getIslandAt(superiorPlayer.getLocation());

        if(island != null && !island.hasPermission(superiorPlayer, IslandPermission.PICKUP_DROPS)){
            e.setCancelled(true);
            Locale.sendProtectionMessage(superiorPlayer);
        }
    }

    class PlayerArrowPickup implements Listener{

        PlayerArrowPickup(){
            if(load())
                plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }

        boolean load(){
            try{
                Class.forName("org.bukkit.event.player.PlayerPickupArrowEvent");
                return true;
            }catch(ClassNotFoundException ex){
                return false;
            }
        }

        @EventHandler
        public void onPlayerArrowPickup(PlayerPickupArrowEvent e){
            SuperiorPlayer superiorPlayer = SSuperiorPlayer.of(e.getPlayer());
            Island island = plugin.getGrid().getIslandAt(superiorPlayer.getLocation());

            if(island != null && !island.hasPermission(superiorPlayer, IslandPermission.PICKUP_DROPS)){
                e.setCancelled(true);
                Locale.sendProtectionMessage(superiorPlayer);
            }
        }

    }

}