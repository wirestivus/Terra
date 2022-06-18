package com.dfsek.terra.addons.biome.pipeline.operations;

import com.dfsek.terra.addons.biome.pipeline.api.area.LocalArea;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.operation.Operation;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.MathUtil;

import java.util.Collection;


public class ExpandOperation implements Operation {
    private final NoiseSampler sampler;
    
    public ExpandOperation(NoiseSampler sampler) {
        this.sampler = sampler;
    }
    
    @Override
    public BiomeDelegate apply(LocalArea area) {
        int index = MathUtil.normalizeIndex(sampler.noise(area.seed(), area.centerX(), area.centerZ()), 4);
        return switch(index) {
            case 0 -> area.getBiome(0, 0);
            case 1 -> area.getBiome(1, 1);
            case 2 -> area.getBiome(0, 1);
            case 3 -> area.getBiome(1, 0);
            default -> throw new IllegalStateException("Invalid index");
        };
    }
    
    @Override
    public Collection<BiomeDelegate> getBiomesWith(Collection<BiomeDelegate> input) {
        return input;
    }
}
