package io.github.portlek.nospawner.util;

import io.github.portlek.nospawner.NoSpawner;
import io.github.portlek.nospawner.NoSpawnerAPI;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.cactoos.Scalar;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public final class BlocksOfChunk implements Scalar<List<Block>> {

    @NotNull
    private final Chunk chunk;

    @NotNull
    private final Material material;

    public BlocksOfChunk(@NotNull Chunk chunk, @NotNull Material material) {
        this.chunk = chunk;
        this.material = material;
    }

    @NotNull
    @Override
    public List<Block> value() {
        final List<Block> blocks = new ArrayList<>();
        final World world = chunk.getWorld();
        final int minX = chunk.getX() << 4;
        final int minZ = chunk.getZ() << 4;
        final int maxX = minX | 15;
        final int maxY = chunk.getWorld().getMaxHeight();
        final int maxZ = minZ | 15;

        for (int x = minX; x <= maxX; ++x) {
            for (int y = 0; y <= maxY; ++y) {
                for (int z = minZ; z <= maxZ; ++z) {
                    final Block block;

                    try {
                        block = world.getBlockAt(x, y, z);
                    } catch (Exception exception) {
                        continue;
                    }

                    if (block.getType() != material || NoSpawner.getAPI().thereIsRegion(block.getLocation())) {
                        continue;
                    }

                    blocks.add(block);
                }
            }
        }

        return blocks;
    }

}
