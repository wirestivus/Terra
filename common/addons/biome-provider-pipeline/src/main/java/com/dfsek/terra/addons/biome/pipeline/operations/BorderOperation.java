package com.dfsek.terra.addons.biome.pipeline.operations;

import com.dfsek.terra.addons.biome.pipeline.api.area.LocalArea;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.operation.Operation;
import com.dfsek.terra.addons.biome.pipeline.api.stage.operation.SingletonOperation;
import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;


public class BorderOperation implements Operation {
    private final String border;
    private final NoiseSampler noiseSampler;
    private final ProbabilityCollection<BiomeDelegate> replace;
    private final String replaceTag;
    
    public BorderOperation(String border, String replaceTag, NoiseSampler noiseSampler, ProbabilityCollection<BiomeDelegate> replace) {
        this.border = border;
        this.noiseSampler = noiseSampler;
        this.replace = replace;
        this.replaceTag = replaceTag;
    }
    
    @Override
    public BiomeDelegate apply(LocalArea area) {
        BiomeDelegate origin = area.centerBiome();
        if(origin.getTags().contains(replaceTag)) {
            for(int xi = -1; xi <= 1; xi++) {
                for(int zi = -1; zi <= 1; zi++) {
                    if(xi == 0 && zi == 0) continue;
                    BiomeDelegate current = area.getBiome(xi, zi);
                    if(current != null && current.getTags().contains(border)) {
                        BiomeDelegate biome = replace.get(noiseSampler, area.centerX(), area.centerZ(), area.seed());
                        return biome.isSelf() ? origin : biome;
                    }
                }
            }
        }
        return origin;
    }

    
    @Override
    public Collection<BiomeDelegate> getBiomesWith(Collection<BiomeDelegate> biomes) {
        Set<BiomeDelegate> biomeSet = new HashSet<>(biomes);
        biomeSet.addAll(
                replace
                        .getContents()
                        .stream()
                        .filter(
                                Predicate.not(BiomeDelegate::isSelf)
                               )
                        .toList()
                       );
        return biomeSet;
    }
}
