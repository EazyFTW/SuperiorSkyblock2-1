package com.bgsoftware.superiorskyblock.api.island;

import com.bgsoftware.superiorskyblock.api.key.Key;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public interface Island extends Comparable<Island> {

    SuperiorPlayer getOwner();

    List<UUID> getMembers();

    boolean isSpawn();

    void inviteMember(SuperiorPlayer superiorPlayer);

    void revokeInvite(SuperiorPlayer superiorPlayer);

    boolean isInvited(SuperiorPlayer superiorPlayer);

    void addMember(SuperiorPlayer superiorPlayer, IslandRole islandRole);

    void kickMember(SuperiorPlayer superiorPlayer);

    void banMember(SuperiorPlayer superiorPlayer);

    boolean isBanned(SuperiorPlayer superiorPlayer);

    List<UUID> getAllMembers();

    List<UUID> getVisitors();

    List<UUID> allPlayersInside();

    boolean isMember(SuperiorPlayer superiorPlayer);

    Location getCenter();

    Location getMinimum();

    Location getMaximum();

    boolean hasPermission(CommandSender sender, IslandPermission islandPermission);

    boolean hasPermission(SuperiorPlayer superiorPlayer, IslandPermission islandPermission);

    void setPermission(IslandRole islandRole, IslandPermission islandPermission, boolean value);

    PermissionNode getPermisisonNode(IslandRole islandRole);

    IslandRole getRequiredRole(IslandPermission islandPermission);

    void disbandIsland();

    List<Chunk> getAllChunks();

    double getMoneyInBank();

    void depositMoney(double amount);

    void withdrawMoney(double amount);

    void calcIslandWorth(SuperiorPlayer asker);

    void handleBlockPlace(Block block);

    void handleBlockPlace(Block block, int amount);

    void handleBlockPlace(Key key, int amount);

    void handleBlockBreak(Block block);

    void handleBlockBreak(Block block, int amount);

    void handleBlockBreak(Key key, int amount);

    int getHoppersAmount();

    int getBlockCount(Key key);

    double getWorth();

    double getRawWorth();

    int getIslandLevel();

    boolean isInside(Location location);

    boolean isInsideRange(Location location);

    int getUpgradeLevel(String upgradeName);

    void setUpgradeLevel(String upgradeName, int level);

    int getIslandSize();

    int getHoppersLimit();

    int getTeamLimit();

    double getCropGrowthMultiplier();

    double getSpawnerRatesMultiplier();

    double getMobDropsMultiplier();

    void setIslandSize(int islandSize);

    void setHoppersLimit(int hoppersLimit);

    void setTeamLimit(int teamLimit);

    void setCropGrowthMultiplier(double cropGrowth);

    void setSpawnerRatesMultiplier(double spawnerRates);

    void setMobDropsMultiplier(double mobDrops);

    String getDiscord();

    void setDiscord(String discord);

    String getPaypal();

    void setPaypal(String paypal);

    void setBiome(Biome biome);

    void sendMessage(String message, UUID... ignoredMembers);

    Location getWarpLocation(String name);

    void setWarpLocation(String name, Location location);

    void warpPlayer(SuperiorPlayer superiorPlayer, String warp);

    void deleteWarp(SuperiorPlayer superiorPlayer, Location location);

    void deleteWarp(String name);

    List<String> getAllWarps();
}