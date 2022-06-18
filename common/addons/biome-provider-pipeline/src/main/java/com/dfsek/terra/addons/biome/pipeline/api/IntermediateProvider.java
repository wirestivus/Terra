package com.dfsek.terra.addons.biome.pipeline.api;

import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.source.BiomeSource;

import java.util.Collection;


public interface IntermediateProvider {
    BiomeDelegate getBiome(int x, int z, long seed, BiomeSource source);
    
    Collection<BiomeDelegate> getBiomes(BiomeSource source);
}
