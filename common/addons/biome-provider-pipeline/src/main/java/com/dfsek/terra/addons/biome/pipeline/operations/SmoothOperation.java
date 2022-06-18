package com.dfsek.terra.addons.biome.pipeline.operations;

import com.dfsek.terra.addons.biome.pipeline.api.area.LocalArea;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.operation.Operation;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.MathUtil;

import java.util.Collection;
import java.util.Objects;


public class SmoothOperation implements Operation {
    private final NoiseSampler sampler;
    
    public SmoothOperation(NoiseSampler sampler) {
        this.sampler = sampler;
    }
    
    @Override
    public BiomeDelegate apply(LocalArea area) {
        BiomeDelegate top = area.getBiome(1, 0);
        BiomeDelegate bottom = area.getBiome(-1, 0);
        BiomeDelegate left = area.getBiome(0, 1);
        BiomeDelegate right = area.getBiome(0, -1);
        
        
        boolean vert = Objects.equals(top, bottom) && top != null;
        boolean horiz = Objects.equals(left, right) && left != null;
        
        if(vert && horiz) {
            return MathUtil.normalizeIndex(sampler.noise(area.seed(), area.centerX(), area.centerZ()), 2) == 0 ? left : top;
        }
        
        if(vert) return top;
        if(horiz) return left;
        
        return area.centerBiome();
    }
    
    @Override
    public Collection<BiomeDelegate> getBiomesWith(Collection<BiomeDelegate> input) {
        return input;
    }
}
