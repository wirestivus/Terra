package com.dfsek.terra.addons.biome.pipeline.api.stage.operation;

import com.dfsek.terra.addons.biome.pipeline.api.IntermediateProvider;
import com.dfsek.terra.addons.biome.pipeline.api.area.LocalArea;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.source.BiomeSource;
import com.dfsek.terra.addons.biome.pipeline.operations.SourceOperation;

import java.util.Collection;


public interface Operation extends IntermediateProvider {
    BiomeDelegate apply(LocalArea area);
    Collection<BiomeDelegate> getBiomesWith(Collection<BiomeDelegate> input);
    
    @Override
    default BiomeDelegate getBiome(int x, int z, long seed, BiomeSource source) {
        return apply(new LocalArea(new SourceOperation(source), source, x, z, seed));
    }
    
    @Override
    default Collection<BiomeDelegate> getBiomes(BiomeSource source) {
        return getBiomesWith(source.getBiomes());
    }
    
    default Operation then(Operation other) {
        return new ComposedOperation(other, this);
    }
}
