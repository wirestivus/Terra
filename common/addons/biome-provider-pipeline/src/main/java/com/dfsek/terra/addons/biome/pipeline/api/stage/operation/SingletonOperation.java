package com.dfsek.terra.addons.biome.pipeline.api.stage.operation;


import com.dfsek.terra.addons.biome.pipeline.api.area.Area;
import com.dfsek.terra.addons.biome.pipeline.api.area.LocalArea;
import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;


public interface SingletonOperation extends Operation {
    BiomeDelegate apply(Area area);
    
    @Override
    default BiomeDelegate apply(LocalArea area) {
        return apply((Area) area);
    }
}
