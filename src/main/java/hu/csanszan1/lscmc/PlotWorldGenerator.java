package hu.csanszan1.lscmc;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;
import java.util.Random;

public class PlotWorldGenerator  extends ChunkGenerator {
    int plotSize;
    int roadWidth;
    int groundHeight;
    PlotConfig plotCfg;

    public PlotWorldGenerator(File dataFolder) {
        this.plotCfg = loadPlotConfig(dataFolder);
        this.plotSize = plotCfg.getPlotSize();
        this.roadWidth = plotCfg.getRoadWith();
        this.groundHeight = plotCfg.getGroundHeight();
    }

    private PlotConfig loadPlotConfig(File dataFolder) {
        File file = new File(dataFolder, "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);


        int plotSize = config.getInt("plots.plotSize");
        int roadWidth = config.getInt("plots.roadWidth");
        int groundHeight = config.getInt("plots.groundHeight");
        PlotConfig plotConfig = new PlotConfig(plotSize, roadWidth, groundHeight);
        System.out.println(plotConfig.getPlotSize());
        return plotConfig;
    }

    @Override
    public void generateSurface(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, ChunkData chunkData) {
        int plotSize = 32;  // 32x32 grass plots
        int roadWidth = 5;   // 5-block wide stone roads
        int groundHeight = 64; // Base ground level

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = (chunkX << 4) + x;
                int worldZ = (chunkZ << 4) + z;

                // Properly handle negative coordinates
                int plotArea = plotSize + roadWidth;
                int localX = Math.floorMod(worldX, plotArea);
                int localZ = Math.floorMod(worldZ, plotArea);

                boolean isRoad = localX < roadWidth || localZ < roadWidth;
                boolean isRoadEdge =
                        (localX == roadWidth - 1 && localZ >= roadWidth) ||  // East edge
                                (localZ == roadWidth - 1 && localX >= roadWidth) ||   // South edge
                                (localX == 0 && localZ >= roadWidth) ||              // West edge
                                (localZ == 0 && localX >= roadWidth);                // North edge
                // Bedrock at bottom
                chunkData.setBlock(x, 0, z, Material.BEDROCK);

                if (isRoad) {
                    // Road section
                    for (int y = 1; y < groundHeight - 1; y++) {
                        chunkData.setBlock(x, y, z, Material.STONE);
                    }
                    chunkData.setBlock(x, groundHeight - 1, z, Material.STONE_BRICKS);
                    chunkData.setBlock(x, groundHeight, z, Material.STONE_BRICKS);
                    if (isRoadEdge) {
                        chunkData.setBlock(x, groundHeight + 1, z, Material.OAK_SLAB);
                    }

                } else {
                    // Plot section
                    for (int y = 1; y < groundHeight - 1; y++) {
                        chunkData.setBlock(x, y, z, Material.DIRT);
                    }
                    chunkData.setBlock(x, groundHeight - 1, z, Material.DIRT);
                    chunkData.setBlock(x, groundHeight, z, Material.GRASS_BLOCK);
                }
            }
        }
    }
    @Override
    public boolean shouldGenerateStructures() {
        return false;
    }

    @Override
    public boolean isParallelCapable() {
        return true;
    }
}
