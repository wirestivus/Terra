package com.dfsek.terra.addons.biome.pipeline.operations;

import com.dfsek.terra.addons.biome.pipeline.api.area.Area;
import com.dfsek.terra.addons.biome.pipeline.api.area.LocalArea;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.operation.Operation;
import com.dfsek.terra.addons.biome.pipeline.api.stage.operation.SingletonOperation;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;


public class ReplaceOperation implements SingletonOperation {
    private final String replaceableTag;
    private final ProbabilityCollection<BiomeDelegate> replace;
    private final NoiseSampler sampler;
    
    public ReplaceOperation(String replaceable, ProbabilityCollection<BiomeDelegate> replace, NoiseSampler sampler) {
        this.replaceableTag = replaceable;
        this.replace = replace;
        this.sampler = sampler;
    }
    
    
    @Override
    public BiomeDelegate apply(Area area) {
        if(area.centerBiome().getTags().contains(replaceableTag)) {
            BiomeDelegate biome = replace.get(sampler, area.centerX(), area.centerZ(), area.seed());
            return biome.isSelf() ? area.centerBiome() : biome;
        }
        return area.centerBiome();
    }
    
    @Override
    public Collection<BiomeDelegate> getBiomesWith(Collection<BiomeDelegate> biomes) {
        Set<BiomeDelegate> biomeSet = new HashSet<>();
        Set<BiomeDelegate> reject = new HashSet<>();
        biomes.forEach(biome -> {
            if(!biome.getTags().contains(replaceableTag)) {
                biomeSet.add(biome);
            } else {
                reject.add(biome);
            }
        });
        biomeSet.addAll(replace.getContents().stream().flatMap(terraBiome -> {
            if(terraBiome.isSelf()) return reject.stream();
            return Stream.of(terraBiome);
        }).toList());
        return biomeSet;
    }
}
