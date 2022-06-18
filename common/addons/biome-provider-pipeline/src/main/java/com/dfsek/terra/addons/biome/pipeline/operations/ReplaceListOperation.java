package com.dfsek.terra.addons.biome.pipeline.operations;

import com.dfsek.terra.addons.biome.pipeline.api.area.Area;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.operation.SingletonOperation;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;


public class ReplaceListOperation implements SingletonOperation {
    private final Map<BiomeDelegate, ProbabilityCollection<BiomeDelegate>> replace;
    private final NoiseSampler sampler;
    private final ProbabilityCollection<BiomeDelegate> replaceDefault;
    private final String defaultTag;
    
    public ReplaceListOperation(Map<BiomeDelegate, ProbabilityCollection<BiomeDelegate>> replace, NoiseSampler sampler,
                                ProbabilityCollection<BiomeDelegate> replaceDefault, String defaultTag) {
        this.replace = replace;
        this.sampler = sampler;
        this.replaceDefault = replaceDefault;
        this.defaultTag = defaultTag;
    }
    
    @Override
    public Collection<BiomeDelegate> getBiomesWith(Collection<BiomeDelegate> biomes) {
        Set<BiomeDelegate> biomeSet = new HashSet<>();
    
        Set<BiomeDelegate> reject = new HashSet<>();
    
        biomes.forEach(biome -> {
            if(!biome.getTags().contains(defaultTag) && !replace.containsKey(biome)) {
                biomeSet.add(biome);
            } else {
                reject.add(biome);
            }
        });
        biomeSet.addAll(replaceDefault.getContents().stream().flatMap(terraBiome -> {
            if(terraBiome.isSelf()) return reject.stream();
            return Stream.of(terraBiome);
        }).toList());
        replace.forEach((biome, collection) -> biomeSet.addAll(collection.getContents().stream().map(terraBiome -> {
            if(terraBiome.isSelf()) return biome;
            return terraBiome;
        }).toList()));
        return biomeSet;
    }
    
    @Override
    public BiomeDelegate apply(Area area) {
        BiomeDelegate center = area.centerBiome();
        if(replace.containsKey(center)) {
            BiomeDelegate biome = replace.get(center).get(sampler, area.centerX(), area.centerZ(), area.seed());
            return biome.isSelf() ? area.centerBiome() : biome;
        }
        if(area.centerBiome().getTags().contains(defaultTag)) {
            BiomeDelegate biome = replaceDefault.get(sampler, area.centerX(), area.centerZ(), area.seed());
            return biome.isSelf() ? area.centerBiome() : biome;
        }
        return center;
    }
}
