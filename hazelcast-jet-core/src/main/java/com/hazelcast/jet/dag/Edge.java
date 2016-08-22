/*
 * Copyright (c) 2008-2016, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.jet.dag;

import com.hazelcast.core.PartitioningStrategy;
import com.hazelcast.jet.impl.strategy.DefaultHashingStrategy;
import com.hazelcast.jet.strategy.DataTransferringStrategy;
import com.hazelcast.jet.strategy.HashingStrategy;
import com.hazelcast.jet.strategy.ProcessingStrategy;
import com.hazelcast.jet.strategy.ShufflingStrategy;
import com.hazelcast.partition.strategy.StringPartitioningStrategy;
import java.io.Serializable;

/**
 * Represents an edge between two vertices in a DAG
 */
public class Edge implements Serializable {
    private Vertex to;
    private String name;
    private Vertex from;
    private boolean shuffled;

    private HashingStrategy hashingStrategy;
    private ShufflingStrategy shufflingStrategy;
    private ProcessingStrategy processingStrategy;
    private PartitioningStrategy partitioningStrategy;

    /**
     * Creates an edge between two vertices.
     *
     * @param name name of the edge
     * @param from the origin vertex
     * @param to   the destination vertex
     */
    public Edge(String name,
                Vertex from,
                Vertex to) {
        this(name, from, to, false);
    }

    /**
     * Creates an edge between two vertices.
     *
     * @param name     name of the edge
     * @param from     the origin vertex
     * @param to       the destination vertex
     * @param shuffled shuffling property
     */
    public Edge(String name,
                Vertex from,
                Vertex to,
                boolean shuffled) {
        this(name, from, to, shuffled, null, null, null, null, null);
    }

    /**
     * Creates an edge between two vertices.
     *
     * @param name                     name of the edge
     * @param from                     the origin vertex
     * @param to                       the destination vertex
     * @param shuffled                 shuffling property
     * @param shufflingStrategy        shuffling strategy
     * @param processingStrategy       processing strategy
     * @param partitioningStrategy     partitioning strategy
     * @param hashingStrategy          hashing strategy
     * @param dataTransferringStrategy data transfer strategy
     */
    public Edge(String name,
                Vertex from,
                Vertex to,
                boolean shuffled,
                ShufflingStrategy shufflingStrategy,
                ProcessingStrategy processingStrategy,
                PartitioningStrategy partitioningStrategy,
                HashingStrategy hashingStrategy,
                DataTransferringStrategy dataTransferringStrategy) {
        this.to = to;
        this.name = name;
        this.from = from;
        this.shuffled = shuffled;
        this.shufflingStrategy = shufflingStrategy;
        this.hashingStrategy = nvl(hashingStrategy, DefaultHashingStrategy.INSTANCE);
        this.partitioningStrategy = nvl(partitioningStrategy, StringPartitioningStrategy.INSTANCE);
        this.processingStrategy = nvl(processingStrategy, ProcessingStrategy.ROUND_ROBIN);
    }

    private <T> T nvl(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    /**
     * @return output vertex of edge
     */
    public Vertex getOutputVertex() {
        return this.to;
    }

    /**
     * @return true if edge will take into account cluster
     * false if data through edge's channel will be passed only
     * locally without shuffling
     */
    public boolean isShuffled() {
        return this.shuffled;
    }

    /**
     * @return name of the edge
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return input vertex of edge
     */
    public Vertex getInputVertex() {
        return this.from;
    }

    /**
     * @return Edge's shuffling strategy
     */
    public ShufflingStrategy getShufflingStrategy() {
        return this.shufflingStrategy;
    }

    /**
     * @return Edge's processing strategy
     */
    public ProcessingStrategy getProcessingStrategy() {
        return this.processingStrategy;
    }

    /**
     * @return Edge's partitioning strategy
     */
    public PartitioningStrategy getPartitioningStrategy() {
        return this.partitioningStrategy;
    }

    /**
     * @return Edge's hashing strategy
     */
    public HashingStrategy getHashingStrategy() {
        return this.hashingStrategy;
    }


    /**
     * Set if the edge is shuffled
     *
     * @return the builder
     */
    public Edge shuffled() {
        this.shuffled = true;
        return this;
    }

    /**
     * Set the shuffling strategy on the edge
     *
     * @param shufflingStrategy the shuffling strategy to set
     * @return the builder
     */
    public Edge shuffled(ShufflingStrategy shufflingStrategy) {
        this.shuffled = true;
        this.shufflingStrategy = shufflingStrategy;
        return this;
    }

    /**
     * Set the processing strategy for the edge to broadcast
     *
     * @return the builder
     */
    public Edge broadcast() {
        this.processingStrategy = ProcessingStrategy.BROADCAST;
        return this;
    }

    /**
     * Set the processing strategy for the edge to partitioned
     *
     * @return the builder
     */
    public Edge partitioned() {
        this.processingStrategy = ProcessingStrategy.PARTITIONING;
        return this;
    }

    /**
     * Set the processing strategy for the edge to partitioned
     *
     * @param partitioningStrategy the partitioning strategy to set
     * @return the builder
     */
    public Edge partitioned(PartitioningStrategy partitioningStrategy) {
        this.processingStrategy = ProcessingStrategy.PARTITIONING;
        this.partitioningStrategy = partitioningStrategy;
        return this;
    }

    /**
     * Set the processing strategy for the edge to partitioned
     *
     * @param hashingStrategy the hashing strategy to set
     * @return the builder
     */
    public Edge partitioned(HashingStrategy hashingStrategy) {
        this.processingStrategy = ProcessingStrategy.PARTITIONING;
        this.hashingStrategy = hashingStrategy;
        return this;
    }

    /**
     * Set the processing strategy for the edge to partitioned
     *
     * @param partitioningStrategy the partitioning strategy to set
     * @param hashingStrategy      the hashing strategy to set
     * @return the builder
     */
    public Edge partitioned(PartitioningStrategy partitioningStrategy, HashingStrategy hashingStrategy) {
        this.processingStrategy = ProcessingStrategy.PARTITIONING;
        this.partitioningStrategy = partitioningStrategy;
        this.hashingStrategy = hashingStrategy;
        return this;
    }


    @Override
    @SuppressWarnings({
            "checkstyle:npathcomplexity",
            "checkstyle:cyclomaticcomplexity"
    })
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Edge edge = (Edge) o;

        if (shuffled != edge.shuffled) {
            return false;
        }

        if (to != null
                ? !to.equals(edge.to)
                : edge.to != null) {
            return false;
        }

        if (name != null
                ? !name.equals(edge.name)
                : edge.name != null) {
            return false;
        }

        if (from != null
                ? !from.equals(edge.from)
                : edge.from != null) {
            return false;
        }

        if (hashingStrategy != null
                ?
                !hashingStrategy.getClass().equals(edge.hashingStrategy.getClass())
                :
                edge.hashingStrategy != null) {
            return false;
        }

        if (shufflingStrategy != null
                ?
                !shufflingStrategy.equals(edge.shufflingStrategy)
                :
                edge.shufflingStrategy != null) {
            return false;
        }

        if (processingStrategy != edge.processingStrategy) {
            return false;
        }

        return partitioningStrategy != null
                ?
                partitioningStrategy.getClass().equals(edge.partitioningStrategy.getClass())
                :
                edge.partitioningStrategy == null;

    }

    @Override
    @SuppressWarnings({
            "checkstyle:npathcomplexity"
    })
    public int hashCode() {
        int result = to != null ? to.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (shuffled ? 1 : 0);
        result = 31 * result + (hashingStrategy != null ? hashingStrategy.getClass().hashCode() : 0);
        result = 31 * result + (shufflingStrategy != null ? shufflingStrategy.hashCode() : 0);
        result = 31 * result + (processingStrategy != null ? processingStrategy.getClass().hashCode() : 0);
        result = 31 * result + (partitioningStrategy != null ? partitioningStrategy.getClass().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Edge{"
                + "name='" + name + '\''
                + ", from=" + from
                + ", to=" + to
                + '}';
    }
}
