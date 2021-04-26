package com.dfsek.terra.config.loaders.config.biome.templates.source;

import com.dfsek.tectonic.annotations.Value;
import com.dfsek.terra.api.util.collections.ProbabilityCollection;
import com.dfsek.terra.api.util.seeded.NoiseSeeded;
import com.dfsek.terra.api.world.biome.pipeline.source.BiomeSource;
import com.dfsek.terra.api.world.biome.pipeline.source.RandomSource;
import com.dfsek.terra.config.builder.BiomeBuilder;

/**
 * Configures a noise-based biome source.
 */
public class NoiseSourceTemplate extends SourceTemplate {
    /**
     * Noise function to use for selecting biomes.
     */
    @Value("noise")
    private NoiseSeeded noise;

    /**
     * ProbabilityCollection of biomes to use.
     */
    @Value("biomes")
    private ProbabilityCollection<BiomeBuilder> biomes;

    @Override
    public BiomeSource apply(Long seed) {
        return new RandomSource(biomes.map((biome) -> biome.apply(seed), false), noise.apply(seed));
    }
}
