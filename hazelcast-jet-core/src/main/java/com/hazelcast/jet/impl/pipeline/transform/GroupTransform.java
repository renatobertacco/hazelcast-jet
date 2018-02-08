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

import com.hazelcast.jet.aggregate.AggregateOperation1;
import com.hazelcast.jet.function.DistributedBiFunction;
import com.hazelcast.jet.function.DistributedFunction;

import javax.annotation.Nonnull;

public class GroupTransform<T, K, A, R, OUT> extends AbstractTransform implements Transform {
    @Nonnull
    private DistributedFunction<? super T, ? extends K> keyFn;
    @Nonnull
    private AggregateOperation1<? super T, A, R> aggrOp;
    @Nonnull
    private final DistributedBiFunction<? super K, ? super R, OUT> mapToOutputFn;

    public GroupTransform(
            @Nonnull Transform upstream,
            @Nonnull DistributedFunction<? super T, ? extends K> keyFn,
            @Nonnull AggregateOperation1<? super T, A, R> aggrOp,
            @Nonnull DistributedBiFunction<? super K, ? super R, OUT> mapToOutputFn
    ) {
        super("group-and-aggregate", upstream);
        this.keyFn = keyFn;
        this.aggrOp = aggrOp;
        this.mapToOutputFn = mapToOutputFn;
    }

    @Nonnull
    public DistributedFunction<? super T, ? extends K> keyFn() {
        return keyFn;
    }

    @Nonnull
    public AggregateOperation1<? super T, A, R> aggrOp() {
        return aggrOp;
    }

    @Nonnull
    public DistributedBiFunction<? super K, ? super R, OUT> mapToOutputFn() {
        return mapToOutputFn;
    }
}
