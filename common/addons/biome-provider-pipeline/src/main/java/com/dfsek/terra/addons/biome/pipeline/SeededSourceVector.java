package com.dfsek.terra.addons.biome.pipeline;

import com.dfsek.terra.addons.biome.pipeline.api.source.BiomeSource;


public record SeededSourceVector(int x, int z, long seed, BiomeSource source) {
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SeededSourceVector that) {
            return this.z == that.z && this.x == that.x && this.seed == that.seed && this.source.equals(that.source);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int code = x;
        code = 31 * code + z;
        code = 31 * code + ((int) (seed ^ (seed >>> 32)));
        return 31 * code + source.hashCode();
    }
}
