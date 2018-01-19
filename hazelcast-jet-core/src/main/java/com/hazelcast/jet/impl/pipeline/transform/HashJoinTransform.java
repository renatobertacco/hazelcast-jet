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

import com.hazelcast.jet.datamodel.Tag;
import com.hazelcast.jet.pipeline.JoinClause;

import javax.annotation.Nonnull;
import java.util.List;

public class HashJoinTransform<T0, R> extends AbstractTransform implements Transform {
    @Nonnull
    public final List<JoinClause<?, ? super T0, ?, ?>> clauses;
    @Nonnull
    public final List<Tag> tags;

    public HashJoinTransform(
            @Nonnull List<Transform> upstream,
            @Nonnull List<JoinClause<?, ? super T0, ?, ?>> clauses,
            @Nonnull List<Tag> tags
    ) {
        super(upstream);
        this.clauses = clauses;
        this.tags = tags;
    }

    @Override
    public String name() {
        return tags.size() + "-way hash-join";
    }

    @Override
    public String toString() {
        return name();
    }
}