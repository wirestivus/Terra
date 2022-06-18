package com.dfsek.terra.addons.biome.pipeline.api.area;

import com.dfsek.terra.addons.biome.pipeline.api.IntermediateProvider;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.source.BiomeSource;


public class LocalArea implements Area {
    private final IntermediateProvider provider;
    private final BiomeSource source;
    private final BiomeDelegate[] biomes = new BiomeDelegate[9];
    private final int x, z;
    private final long seed;
    
    public LocalArea(IntermediateProvider provider, BiomeSource source, int x, int z, long seed) {
        this.provider = provider;
        this.source = source;
        this.x = x;
        this.z = z;
        this.seed = seed;
    }
    
    public BiomeDelegate getBiome(int x, int z) {
        int index = (x+1) + (z+1) * 3;
        BiomeDelegate biome = biomes[index];
        if(biome == null) {
            biome = provider.getBiome(this.x + x, this.z + z, seed, source);
            biomes[index] = biome;
        }
        return biome;
    }
    
    @Override
    public int centerX() {
        return x;
    }
    
    @Override
    public int centerZ() {
        return z;
    }
    
    @Override
    public long seed() {
        return seed;
    }
    
    @Override
    public BiomeDelegate centerBiome() {
        return getBiome(0, 0);
    }
    
    public BiomeSource getSource() {
        return source;
    }
}
