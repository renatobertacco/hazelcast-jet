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

package com.hazelcast.jet.impl.connector;

import com.hazelcast.jet.impl.util.ArrayDequeInbox;
import com.hazelcast.test.annotation.QuickTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Category(QuickTest.class)
public class WriteBufferedPTest {

    private static final int ENTRY_COUNT = 5;

    @Test
    public void test() {
        WriteBufferedP<List<Integer>, Integer> p = new WriteBufferedP<>(
                ArrayList::new,
                List::add,
                list -> assertEquals(ENTRY_COUNT, list.size()),
                map -> {}
        );

        ArrayDequeInbox inbox = new ArrayDequeInbox();
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < ENTRY_COUNT; i++) {
            map.put(i, i);
        }
        inbox.addAll(map.keySet());
        p.process(0, inbox);
    }
}
