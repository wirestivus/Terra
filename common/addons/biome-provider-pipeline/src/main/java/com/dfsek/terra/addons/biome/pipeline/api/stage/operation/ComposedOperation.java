package com.dfsek.terra.addons.biome.pipeline.api.stage.operation;

import java.util.Collection;
import java.util.HashSet;

import com.dfsek.terra.addons.biome.pipeline.SeededSourceVector;
import com.dfsek.terra.addons.biome.pipeline.api.area.LocalArea;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;

import com.dfsek.terra.addons.biome.pipeline.api.source.BiomeSource;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;


public class ComposedOperation implements Operation {
    private final Operation outer;
    private final Operation inner;
    private final LoadingCache<SeededSourceVector, BiomeDelegate> cache = Caffeine.newBuilder()
                                                                                  .maximumSize(1024)
                                                                                  .build(vec -> Operation.super.getBiome(vec.x(), vec.z(), vec.seed(), vec.source()));
    
    protected ComposedOperation(Operation outer, Operation inner) {
        this.outer = outer;
        this.inner = inner;
    }
    
    @Override
    public BiomeDelegate apply(LocalArea area) {
        return outer.apply(new LocalArea(inner, area.getSource(), area.centerX(), area.centerZ(), area.seed()));
    }
    
    @Override
    public Collection<BiomeDelegate> getBiomesWith(Collection<BiomeDelegate> input) {
        return new HashSet<>(outer.getBiomesWith(inner.getBiomesWith(input)));
    }
    
    @Override
    public BiomeDelegate getBiome(int x, int z, long seed, BiomeSource source) {
        return cache.get(new SeededSourceVector(x, z, seed, source));
    }
}
