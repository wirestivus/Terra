/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra API is licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in the common/api directory.
 */

package com.dfsek.terra.api.world;

import com.dfsek.terra.api.Handle;
import com.dfsek.terra.api.block.entity.BlockEntity;
import com.dfsek.terra.api.block.state.BlockState;
import com.dfsek.terra.api.config.WorldConfig;
import com.dfsek.terra.api.entity.Entity;
import com.dfsek.terra.api.entity.EntityType;
import com.dfsek.terra.api.util.vector.Vector3;
import com.dfsek.terra.api.world.biome.generation.BiomeProvider;
import com.dfsek.terra.api.world.chunk.Chunk;
import com.dfsek.terra.api.world.chunk.generation.ChunkGenerator;


public interface World extends WorldAccess {
    Entity spawnEntity(Vector3 location, EntityType entityType);
    
    
    Chunk getChunkAt(int x, int z);
    
    default Chunk getChunkAt(Vector3 location) {
        return getChunkAt(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }
    
    ChunkGenerator getGenerator();
    
    BiomeProvider getBiomeProvider();
    
    WorldConfig getConfig();
}
