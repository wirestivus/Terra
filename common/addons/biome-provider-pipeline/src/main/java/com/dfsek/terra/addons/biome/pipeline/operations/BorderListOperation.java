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
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;


public class BorderListOperation implements Operation {
    private final String border;
    private final NoiseSampler noiseSampler;
    private final ProbabilityCollection<BiomeDelegate> replaceDefault;
    private final String defaultReplace;
    private final Map<BiomeDelegate, ProbabilityCollection<BiomeDelegate>> replace;
    
    public BorderListOperation(String border, NoiseSampler noiseSampler, ProbabilityCollection<BiomeDelegate> replaceDefault,
                               String defaultReplace, Map<BiomeDelegate, ProbabilityCollection<BiomeDelegate>> replace) {
        this.border = border;
        this.noiseSampler = noiseSampler;
        this.replaceDefault = replaceDefault;
        this.defaultReplace = defaultReplace;
        this.replace = replace;
    }
    
    @Override
    public Collection<BiomeDelegate> getBiomesWith(Collection<BiomeDelegate> biomes) {
        Set<BiomeDelegate> biomeSet = new HashSet<>(biomes);
        biomeSet.addAll(replaceDefault.getContents().stream().filter(Predicate.not(BiomeDelegate::isSelf)).toList());
        replace.forEach((biome, collection) -> biomeSet.addAll(collection.getContents()));
        return biomeSet;
    }
    
    @Override
    public BiomeDelegate apply(LocalArea area) {
        BiomeDelegate origin = area.centerBiome();
        if(origin.getTags().contains(defaultReplace)) {
            for(int xi = -1; xi <= 1; xi++) {
                for(int zi = -1; zi <= 1; zi++) {
                    if(xi == 0 && zi == 0) continue;
                    BiomeDelegate current = area.getBiome(xi, zi);
                    if(current != null && current.getTags().contains(border)) {
                        if(replace.containsKey(origin)) {
                            BiomeDelegate biome = replace.get(origin).get(noiseSampler, area.centerX(), area.centerZ(), area.seed());
                            return biome.isSelf() ? origin : biome;
                        }
                        BiomeDelegate biome = replaceDefault.get(noiseSampler, area.centerX(), area.centerZ(), area.seed());
                        return biome.isSelf() ? origin : biome;
                    }
                }
            }
        }
        return origin;
    }
}
