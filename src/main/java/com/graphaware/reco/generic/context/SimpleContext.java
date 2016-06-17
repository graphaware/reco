/*
 * Copyright (c) 2013-2016 GraphAware
 *
 * This file is part of the GraphAware Framework.
 *
 * GraphAware Framework is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.graphaware.reco.generic.context;

import com.graphaware.reco.generic.config.Config;
import com.graphaware.reco.generic.stats.DefaultStatistics;
import com.graphaware.reco.generic.stats.Statistics;

import static com.graphaware.reco.generic.stats.Statistics.TOTAL_TIME;
import static com.graphaware.reco.generic.util.Assert.hasLength;
import static com.graphaware.reco.generic.util.Assert.notNull;

/**
 * The simplest possible context, allowing all recommendations.
 *
 * This class is thread-safe.
 */
public class SimpleContext<OUT, IN> implements Context<OUT, IN> {

    private final IN input;
    private final Config config;
    private final Statistics statistics;

    /**
     * Construct a new context.
     *
     * @param input  for which recommendations are being computed. Must not be <code>null</code>.
     * @param config for the computation. Must not be <code>null</code>.
     */
    public SimpleContext(IN input, Config config) {
        notNull(input);
        notNull(config);

        this.input = input;
        this.config = config;
        this.statistics = createStatistics(input);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Config config() {
        return config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IN input() {
        return input;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final <C extends Config> C config(Class<C> clazz) {
        notNull(clazz);

        if (!clazz.isAssignableFrom(config.getClass())) {
            throw new IllegalArgumentException(config.getClass() + " is not assignable from " + clazz);
        }

        return (C) config();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean timeLeft() {
        return config.maxTime() > statistics.getTime(TOTAL_TIME);
    }

    /**
     * {@inheritDoc}
     * <p/>
     * always returns true by default.
     */
    @Override
    public boolean allow(OUT recommendation, String task) {
        notNull(recommendation);
        hasLength(task);

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @throws java.lang.UnsupportedOperationException at all times, unless overridden.
     */
    @Override
    public void disallow(OUT recommendation) {
        throw new UnsupportedOperationException("SimpleContext does not support blacklisting items. Please use FilteringContext");
    }

    /**
     * Create a new statistics object. To be overridden by subclasses if needed.
     *
     * @param input for which recommendations are being computed.
     * @return {@link com.graphaware.reco.generic.stats.DefaultStatistics} by default.
     */
    protected Statistics createStatistics(IN input) {
        return new DefaultStatistics<>(input);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Statistics statistics() {
        return statistics;
    }
}

