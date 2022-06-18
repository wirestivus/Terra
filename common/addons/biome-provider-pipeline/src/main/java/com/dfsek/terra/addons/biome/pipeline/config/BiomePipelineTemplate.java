/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.config;

import com.dfsek.tectonic.api.config.template.annotations.Description;
import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.List;

import com.dfsek.terra.addons.biome.pipeline.BiomePipelineProvider;
import com.dfsek.terra.addons.biome.pipeline.api.source.BiomeSource;
import com.dfsek.terra.addons.biome.pipeline.api.stage.operation.Operation;
import com.dfsek.terra.addons.biome.pipeline.operations.SourceOperation;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;


@SuppressWarnings({ "FieldMayBeFinal", "unused" })
public class BiomePipelineTemplate extends BiomeProviderTemplate {
    @Value("pipeline.source")
    @Description("The Biome Source to use for initial population of biomes.")
    private @Meta BiomeSource source;
    
    @Value("pipeline.stages")
    @Description("A list of pipeline stages to apply to the result of #source")
    private @Meta List<@Meta Operation> stages;
    
    @Override
    public BiomeProvider get() {
        return new BiomePipelineProvider(stages.stream().reduce(Operation::then).orElseGet(() -> new SourceOperation(source)), source, resolution, blend, blendAmp);
    }
}
