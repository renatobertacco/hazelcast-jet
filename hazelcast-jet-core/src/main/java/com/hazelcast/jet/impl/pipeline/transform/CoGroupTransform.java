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

package com.hazelcast.jet.impl.pipeline.transform;

import com.hazelcast.jet.aggregate.AggregateOperation;
import com.hazelcast.jet.function.DistributedBiFunction;
import com.hazelcast.jet.function.DistributedFunction;
import com.hazelcast.jet.function.KeyedWindowResultFunction;
import com.hazelcast.jet.pipeline.WindowDefinition;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class CoGroupTransform<K, A, R, OUT> extends AbstractTransform implements Transform {
    @Nonnull
    private List<DistributedFunction<?, ? extends K>> groupKeyFns;
    @Nonnull
    private AggregateOperation<A, R> aggrOp;
    @Nonnull
    private final DistributedBiFunction<? super K, ? super R, OUT> mapToOutputFn;

    public CoGroupTransform(
            @Nonnull List<Transform> upstream,
            @Nonnull List<DistributedFunction<?, ? extends K>> groupKeyFns,
            @Nonnull AggregateOperation<A, R> aggrOp,
            @Nonnull DistributedBiFunction<? super K, ? super R, OUT> mapToOutputFn
    ) {
        super(upstream.size() + "-way cogroup-and-aggregate", upstream);
        this.groupKeyFns = groupKeyFns;
        this.aggrOp = aggrOp;
        this.mapToOutputFn = mapToOutputFn;
    }

    @Nonnull
    public List<DistributedFunction<?, ? extends K>> groupKeyFns() {
        return groupKeyFns;
    }

    @Nonnull
    public AggregateOperation<A, R> aggrOp() {
        return aggrOp;
    }

    @Nonnull
    public DistributedBiFunction<? super K, ? super R, OUT> mapToOutputFn() {
        return mapToOutputFn;
    }
}
