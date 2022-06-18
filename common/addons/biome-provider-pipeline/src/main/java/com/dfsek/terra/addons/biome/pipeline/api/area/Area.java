package com.dfsek.terra.addons.biome.pipeline.api.area;

import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;


public interface Area {
    int centerX();
    int centerZ();
    long seed();
    BiomeDelegate centerBiome();
}
