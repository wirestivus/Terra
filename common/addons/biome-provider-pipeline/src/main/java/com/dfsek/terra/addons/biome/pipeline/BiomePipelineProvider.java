/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline;

import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.source.BiomeSource;
import com.dfsek.terra.addons.biome.pipeline.api.stage.operation.Operation;
import com.dfsek.terra.api.util.Column;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.dfsek.terra.api.noise.NoiseSampler;
import com.dfsek.terra.api.registry.key.StringIdentifiable;
import com.dfsek.terra.api.world.biome.Biome;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


public class BiomePipelineProvider implements BiomeProvider {
    private final Operation operation;
    private final BiomeSource source;
    private final int resolution;
    private final NoiseSampler mutator;
    private final double noiseAmp;
    
    private final Set<Biome> biomes;
    
    private final LoadingCache<SeededVector, Biome> cache;
    
    public BiomePipelineProvider(Operation operation, BiomeSource source, int resolution, NoiseSampler mutator, double noiseAmp) {
        this.operation = operation;
        this.source = source;
        this.resolution = resolution;
        this.mutator = mutator;
        this.noiseAmp = noiseAmp;
    
        this.biomes = new HashSet<>();
    
        Collection<BiomeDelegate> result = operation.getBiomes(source);
        result.forEach(biomeDelegate -> {
            if(biomeDelegate.isEphemeral()) {
                
                StringBuilder biomeList = new StringBuilder("\n");
                result.stream()
                             .sorted(Comparator.comparing(StringIdentifiable::getID))
                             .forEach(delegate -> biomeList
                                     .append("    - ")
                                     .append(delegate.getID())
                                     .append(':')
                                     .append(delegate.getClass().getCanonicalName())
                                     .append('\n'));
                throw new IllegalArgumentException("Biome Pipeline leaks ephemeral biome \"" + biomeDelegate.getID() +
                                                   "\". Ensure there is a stage to guarantee replacement of the ephemeral biome. Biomes: " +
                                                   biomeList);
            }
            this.biomes.add(biomeDelegate.getBiome());
        });
        
        this.cache = Caffeine
                .newBuilder()
                .maximumSize(2048)
                .build(vec -> operation.getBiome(vec.x, vec.z, vec.seed, source).getBiome());
    }
    
    @Override
    public Biome getBiome(int x, int y, int z, long seed) {
        return getBiome(x, z, seed);
    }
    
    public Biome getBiome(int x, int z, long seed) {
        x += mutator.noise(seed + 1, x, z) * noiseAmp;
        z += mutator.noise(seed + 2, x, z) * noiseAmp;
        
        return cache.get(new SeededVector(x / resolution, z / resolution, seed));
    }
    
    @Override
    public Optional<Biome> getBaseBiome(int x, int z, long seed) {
        return Optional.of(getBiome(x, z, seed));
    }
    
    @Override
    public Iterable<Biome> getBiomes() {
        return biomes;
    }
    
    @Override
    public Column<Biome> getColumn(int x, int z, long seed, int min, int max) {
        return new BiomePipelineColumn(this, min, max, x, z, seed);
    }
    
    @Override
    public int resolution() {
        return resolution;
    }
    
    public record SeededVector(int x, int z, long seed) {
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof SeededVector that) {
                return this.z == that.z && this.x == that.x && this.seed == that.seed;
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            int code = x;
            code = 31 * code + z;
            return 31 * code + ((int) (seed ^ (seed >>> 32)));
        }
    }
}
