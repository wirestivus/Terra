/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.config.stage.mutator;

import com.dfsek.tectonic.api.config.template.annotations.Value;

import com.dfsek.terra.addons.biome.pipeline.api.delegate.BiomeDelegate;
import com.dfsek.terra.addons.biome.pipeline.api.stage.operation.Operation;
import com.dfsek.terra.addons.biome.pipeline.config.stage.OperationTemplate;
import com.dfsek.terra.addons.biome.pipeline.operations.ReplaceOperation;
import com.dfsek.terra.api.config.meta.Meta;
import com.dfsek.terra.api.util.collection.ProbabilityCollection;


@SuppressWarnings("unused")
public class ReplaceMutatorTemplate extends OperationTemplate {
    @Value("from")
    private @Meta String from;
    
    @Value("to")
    private @Meta ProbabilityCollection<@Meta BiomeDelegate> to;
    
    @Override
    public Operation get() {
        return new ReplaceOperation(from, to, noise);
    }
}
