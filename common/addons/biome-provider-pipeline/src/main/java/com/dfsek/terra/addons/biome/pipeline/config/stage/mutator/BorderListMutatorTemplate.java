/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import java.util.Map;

import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.operation.Operation;
import com.dfsek.terra.addons.biome.pipeline.config.stage.OperationTemplate;
import com.dfsek.terra.addons.biome.pipeline.operations.BorderListOperation;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


@SuppressWarnings("unused")
public class BorderListMutatorTemplate extends OperationTemplate {
    @Value("from")
    private @Meta String from;
    
    @Value("default-replace")
    private @Meta String defaultReplace;
    
    @Value("default-to")
    private @Meta ProbabilityCollection<@Meta BiomeDelegate> defaultTo;
    
    @Value("replace")
    private @Meta Map<@Meta BiomeDelegate, @Meta ProbabilityCollection<@Meta BiomeDelegate>> replace;
    
    
    @Override
    public Operation get() {
        return new BorderListOperation(from, noise, defaultTo, defaultReplace, replace);
    }
}
