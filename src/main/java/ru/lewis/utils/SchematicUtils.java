package ru.lewis.utils;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.Plugin;

import java.nio.file.Path;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class SchematicUtils {
    public static void saveSchematic(Path path, Location one, Location two, Location center) {
        BinIO.write(path, out -> {
            int xMin = Math.min(one.getBlockX(), two.getBlockX());
            int xMax = Math.max(one.getBlockX(), two.getBlockX());
            int yMin = Math.min(one.getBlockY(), two.getBlockY());
            int yMax = Math.max(one.getBlockY(), two.getBlockY());
            int zMin = Math.min(one.getBlockZ(), two.getBlockZ());
            int zMax = Math.max(one.getBlockZ(), two.getBlockZ());

            for (int x = xMin; x < xMax; x++) {
                for (int y = yMin; y < yMax; y++) {
                    for (int z = zMin; z < zMax; z++) {
                        Location location = new Location(center.getWorld(), x, y, z);
                        String blockDataString = location.getBlock().getBlockData().getAsString();

                        int schematicX = (x - center.getBlockX()) & 0xFF;
                        int schematicY = (y - center.getBlockY()) & 0xFF;
                        int schematicZ = (z - center.getBlockZ()) & 0xFF;
                        int packedCoords = (schematicX << 16) | (schematicY << 8) | schematicZ;

                        out.writeUTF(blockDataString);
                        out.writeInt(packedCoords);
                    }
                }
            }
        });
    }

    public static Int2ObjectArrayMap<BlockData> loadSchematic(Path path) {
        Int2ObjectArrayMap<BlockData> blocks = new Int2ObjectArrayMap<>();
        BinIO.read(path, input -> {
            while (input.available() > 0) {
                String blockDataString = input.readUTF();
                BlockData blockData = Bukkit.createBlockData(blockDataString);
                int packedCoords = input.readInt();

                blocks.put(packedCoords, blockData);
            }
        });
        return blocks;
    }

    public static ObjectArrayList<BlockState> pasteSchematic(Int2ObjectArrayMap<BlockData> blocks, Location center) {
        ObjectArrayList<BlockState> backUp = new ObjectArrayList<>();
        blocks.int2ObjectEntrySet().fastForEach(entry -> {
            int offset = entry.getIntKey();
            BlockData blockData = entry.getValue();
            int x = (byte) (offset >>> 16) + center.getBlockX();
            int y = (byte) (offset >>> 8) + center.getBlockY();
            int z = (byte) offset + center.getBlockZ();
            Location location = new Location(center.getWorld(), x, y, z);
            backUp.add(location.getBlock().getState());

            location.getBlock().setBlockData(blockData, false);
        });
        return backUp;
    }

    public static void pasteSchematicInterval(Plugin plugin, Int2ObjectArrayMap<BlockData> blocks, Location center, Duration interval, Consumer<ObjectArrayList<BlockState>> pastedHandler) {
        ObjectArrayList<BlockState> backUp = new ObjectArrayList<>();
        AtomicInteger remainingTasks = new AtomicInteger(blocks.size()); // Количество задач

        blocks.int2ObjectEntrySet().fastForEach(entry -> {
            int offset = entry.getIntKey();
            BlockData blockData = entry.getValue();
            int x = (byte) (offset >>> 16) + center.getBlockX();
            int y = (byte) (offset >>> 8) + center.getBlockY();
            int z = (byte) offset + center.getBlockZ();
            Location location = new Location(center.getWorld(), x, y, z);
            backUp.add(location.getBlock().getState());

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                location.getBlock().setBlockData(blockData, false);

                if (remainingTasks.decrementAndGet() == 0) {
                    pastedHandler.accept(backUp);
                }
            }, interval.toSeconds() * 20L);
        });
    }
}
