/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.biome.pipeline.config.stage.expander;

import com.dfsek.terra.addons.biome.pipeline.api.stage.operation.Operation;
import com.dfsek.terra.addons.biome.pipeline.config.stage.OperationTemplate;
import com.dfsek.terra.addons.biome.pipeline.operations.ExpandOperation;


public class ExpanderOperationTemplate extends OperationTemplate {
    @Override
    public Operation get() {
        return new ExpandOperation(noise);
    }
}
