package com.dfsek.terra.addons.biome.pipeline.api.source;

import com.dfsek.terra.addons.biome.pipeline.api.IntermediateProvider;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;

import java.util.Collection;


public interface BiomeSource {
    BiomeDelegate getBiome(int x, int z, long seed);
    Collection<BiomeDelegate> getBiomes();
}
