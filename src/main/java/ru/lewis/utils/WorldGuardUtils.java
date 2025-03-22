package ru.lewis.utils;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.Location;

public class WorldGuardUtils {
    public static boolean regionIsCreated(int radius, Location center) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(center.getWorld()));
        if (regionManager == null) {
            return false;
        }

        for (ProtectedRegion region : regionManager.getRegions().values()) {
            if (region.contains(BlockVector3.at(center.getX(), center.getY(), center.getZ()))) {
                return true;
            }
        }
        return false;
    }

    public static void regionCreate(int radius, Location center) {
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = container.get(BukkitAdapter.adapt(center.getWorld()));
        if (regionManager == null) {
            return;
        }

        String regionName = "region_" + center.getBlockX() + "_" + center.getBlockY() + "_" + center.getBlockZ();
        BlockVector3 min = BlockVector3.at(center.getX() - radius, center.getY() - radius, center.getZ() - radius);
        BlockVector3 max = BlockVector3.at(center.getX() + radius, center.getY() + radius, center.getZ() + radius);

        ProtectedCuboidRegion region = new ProtectedCuboidRegion(regionName, min, max);
        regionManager.addRegion(region);
    }
}
