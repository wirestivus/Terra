package com.dfsek.terra.addons.biome.pipeline.operations;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.dfsek.terra.addons.biome.pipeline.api.area.LocalArea;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.source.BiomeSource;
import com.dfsek.terra.addons.biome.pipeline.api.stage.operation.Operation;


public class SourceOperation implements Operation {
    private final BiomeSource source;
    
    public SourceOperation(BiomeSource source) {
        this.source = source;
    }
    
    @Override
    public BiomeDelegate apply(LocalArea area) {
        return source.getBiome(area.centerX(), area.centerZ(), area.seed());
    }
    
    @Override
    public Collection<BiomeDelegate> getBiomesWith(Collection<BiomeDelegate> input) {
        Set<BiomeDelegate> biomes = new HashSet<>(input);
        input.addAll(source.getBiomes());
        return biomes;
    }
}
