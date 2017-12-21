/*
 * Copyright (c) 2008-2017, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.jet.impl.processor;

import com.hazelcast.jet.aggregate.AggregateOperation;
import com.hazelcast.jet.core.AbstractProcessor;
import com.hazelcast.jet.aggregate.AggregateOperation1;
import com.hazelcast.jet.function.DistributedBiConsumer;
import com.hazelcast.jet.function.DistributedFunction;

import javax.annotation.Nonnull;

/**
 * Batch processor that computes the supplied aggregate operation on all
 * the received items. The number of inbound edges must not exceed the
 * number of {@code accumulate} primitives in the aggregate operation.
 */
public class AggregateP<A, R> extends AbstractProcessor {
    @Nonnull private final AggregateOperation<A, R> aggrOp;
    @Nonnull private final A acc;
    private R result;

    public AggregateP(@Nonnull AggregateOperation<A, R> aggrOp) {
        this.acc = aggrOp.createFn().get();
        this.aggrOp = aggrOp;
    }

    @Override
    protected boolean tryProcess(int ordinal, @Nonnull Object item) {
        aggrOp.accumulateFn(ordinal).accept(acc, item);
        return true;
    }

    @Override
    public boolean complete() {
        if (result == null) {
            result = aggrOp.finishFn().apply(acc);
        }
        return tryEmit(result);
    }
}
