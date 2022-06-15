/*
 * Copyright (c) 2020-2021 Polyhedral Development
 *
 * The Terra Core Addons are licensed under the terms of the MIT License. For more details,
 * reference the LICENSE file in this module's root directory.
 */

package com.dfsek.terra.addons.terrascript.parser.lang.functions;

import com.dfsek.terra.addons.terrascript.parser.lang.ImplementationArguments;
import com.dfsek.terra.addons.terrascript.parser.lang.Returnable;
import com.dfsek.terra.addons.terrascript.parser.lang.Scope;
import com.dfsek.terra.addons.terrascript.tokenizer.Position;


public interface Function<T> extends Returnable<T> {
    @Override
    default double applyDouble(ImplementationArguments implementationArguments, Scope scope) {
        return (Double) apply(implementationArguments, scope);
    }
    
    @Override
    default boolean applyBoolean(ImplementationArguments implementationArguments, Scope scope) {
        return (Boolean) apply(implementationArguments, scope);
    }
    
    Function<?> NULL = new Function<>() {
        @Override
        public ReturnType returnType() {
            return null;
        }
        
        @Override
        public Object apply(ImplementationArguments implementationArguments, Scope scope) {
            return null;
        }
        
        @Override
        public Position getPosition() {
            return null;
        }
    };
}
