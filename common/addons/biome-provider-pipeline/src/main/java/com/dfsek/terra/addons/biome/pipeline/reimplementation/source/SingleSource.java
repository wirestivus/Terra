package com.dfsek.terra.addons.biome.pipeline.reimplementation.source;

import java.util.Collections;
import java.util.Set;

import com.dfsek.terra.addons.biome.pipeline.reimplementation.api.biome.PipelineBiome;
import com.dfsek.terra.addons.biome.pipeline.reimplementation.api.Source;


public class SingleSource implements Source {
    
    private final PipelineBiome biome;
    
    public SingleSource(PipelineBiome biome) {
        this.biome = biome;
    }
    
    @Override
    public PipelineBiome get(long seed, int x, int z) {
        return biome;
    }
    
    @Override
    public Set<PipelineBiome> getBiomes() {
        return Collections.singleton(biome);
    }
}
