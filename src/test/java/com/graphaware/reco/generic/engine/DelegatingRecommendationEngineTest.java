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

package com.graphaware.reco.generic.engine;

import com.graphaware.reco.generic.config.SimpleConfig;
import com.graphaware.reco.generic.context.Context;
import com.graphaware.reco.generic.context.SimpleContext;
import com.graphaware.reco.generic.post.PostProcessor;
import com.graphaware.reco.generic.result.PartialScore;
import com.graphaware.reco.generic.result.Recommendations;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test for {@link com.graphaware.reco.generic.engine.DelegatingRecommendationEngine}, especially the optimisation
 * part that takes place before post processing.
 */
public class DelegatingRecommendationEngineTest {

    private DelegatingRecommendationEngine<String, String> engine;
    private String testInput;
    private PostProcessor<String, String> mockPP1, mockPP2;
    private Context<String, String> testContext;

    private Map<String, Integer> peopleWithPoints;

    @Before
    public void populateDatabase() {
        peopleWithPoints = new HashMap<>();

        peopleWithPoints.put("Michal", 100);
        peopleWithPoints.put("Daniela", 80);
        peopleWithPoints.put("Vince", 60);
        peopleWithPoints.put("Adam", 40);
        peopleWithPoints.put("Luanne", 20);
        peopleWithPoints.put("Christophe", 0);
        peopleWithPoints.put("Jim", -20);

    }

    @Before
    public void setUp() throws Exception {
        engine = new DelegatingRecommendationEngine<>();
        engine.addEngine(new TestEngine());

        testInput = "TestInput";

        mockPP1 = mock(PostProcessor.class);
        mockPP2 = mock(PostProcessor.class);

        testContext = new SimpleContext<>(testInput, new SimpleConfig(4));
    }

    @Test
    public void verifyCorrectOptimisation1() {
        when(mockPP1.maxPositiveScore(testInput, testContext)).thenReturn(Float.POSITIVE_INFINITY);
        when(mockPP2.maxPositiveScore(testInput, testContext)).thenReturn(Float.POSITIVE_INFINITY);
        when(mockPP1.maxNegativeScore(testInput, testContext)).thenReturn(Float.NEGATIVE_INFINITY);
        when(mockPP2.maxNegativeScore(testInput, testContext)).thenReturn(Float.NEGATIVE_INFINITY);

        engine.addPostProcessors(Arrays.asList(mockPP1, mockPP2));

        Recommendations<String> result = engine.recommend(testInput, testContext);

        assertEquals(7, result.size());
        verify(mockPP1).postProcess(result, testInput, testContext);
        verify(mockPP2).postProcess(result, testInput, testContext);
    }

    @Test
    public void verifyCorrectOptimisation2() {
        when(mockPP1.maxPositiveScore(testInput, testContext)).thenReturn(0f);
        when(mockPP2.maxPositiveScore(testInput, testContext)).thenReturn(Float.POSITIVE_INFINITY);
        when(mockPP1.maxNegativeScore(testInput, testContext)).thenReturn(0f);
        when(mockPP2.maxNegativeScore(testInput, testContext)).thenReturn(Float.NEGATIVE_INFINITY);

        engine.addPostProcessors(Arrays.asList(mockPP1, mockPP2));

        Recommendations<String> result = engine.recommend(testInput, testContext);

        assertEquals(7, result.size());
        verify(mockPP1).postProcess(result, testInput, testContext);
        verify(mockPP2).postProcess(result, testInput, testContext);
    }

    @Test
    public void verifyCorrectOptimisation3() {
        when(mockPP1.maxPositiveScore(testInput, testContext)).thenReturn(0f);
        when(mockPP2.maxPositiveScore(testInput, testContext)).thenReturn(0f);
        when(mockPP1.maxNegativeScore(testInput, testContext)).thenReturn(0f);
        when(mockPP2.maxNegativeScore(testInput, testContext)).thenReturn(0f);

        engine.addPostProcessors(Arrays.asList(mockPP1, mockPP2));

        Recommendations<String> result = engine.recommend(testInput, testContext);

        assertEquals(4, result.size());
        verify(mockPP1).postProcess(result, testInput, testContext);
        verify(mockPP2).postProcess(result, testInput, testContext);
    }

    @Test
    public void verifyCorrectOptimisation4() {
        when(mockPP1.maxPositiveScore(testInput, testContext)).thenReturn(5f);
        when(mockPP2.maxPositiveScore(testInput, testContext)).thenReturn(5f);
        when(mockPP1.maxNegativeScore(testInput, testContext)).thenReturn(-5f);
        when(mockPP2.maxNegativeScore(testInput, testContext)).thenReturn(-5f);

        engine.addPostProcessors(Arrays.asList(mockPP1, mockPP2));

        Recommendations<String> result = engine.recommend(testInput, testContext);

        assertEquals(5, result.size());
        verify(mockPP1).postProcess(result, testInput, testContext);
        verify(mockPP2).postProcess(result, testInput, testContext);
    }

    @Test
    public void verifyCorrectOptimisation5() {
        when(mockPP1.maxPositiveScore(testInput, testContext)).thenReturn(4f);
        when(mockPP2.maxPositiveScore(testInput, testContext)).thenReturn(5f);
        when(mockPP1.maxNegativeScore(testInput, testContext)).thenReturn(-5f);
        when(mockPP2.maxNegativeScore(testInput, testContext)).thenReturn(-5f);

        engine.addPostProcessors(Arrays.asList(mockPP1, mockPP2));

        Recommendations<String> result = engine.recommend(testInput, testContext);

        assertEquals(4, result.size());
        verify(mockPP1).postProcess(result, testInput, testContext);
        verify(mockPP2).postProcess(result, testInput, testContext);
    }

    @Test(expected = IllegalStateException.class)
    public void verifyIncorrectParams1() {
        when(mockPP1.maxPositiveScore(testInput, testContext)).thenReturn(-1f);
        when(mockPP2.maxPositiveScore(testInput, testContext)).thenReturn(0f);
        when(mockPP1.maxNegativeScore(testInput, testContext)).thenReturn(0f);
        when(mockPP2.maxNegativeScore(testInput, testContext)).thenReturn(0f);

        engine.addPostProcessors(Arrays.asList(mockPP1, mockPP2));

        engine.recommend(testInput, testContext);
    }

    @Test(expected = IllegalStateException.class)
    public void verifyIncorrectParams2() {
        when(mockPP1.maxPositiveScore(testInput, testContext)).thenReturn(0f);
        when(mockPP2.maxPositiveScore(testInput, testContext)).thenReturn(0f);
        when(mockPP1.maxNegativeScore(testInput, testContext)).thenReturn(1f);
        when(mockPP2.maxNegativeScore(testInput, testContext)).thenReturn(0f);

        engine.addPostProcessors(Arrays.asList(mockPP1, mockPP2));

        engine.recommend(testInput, testContext);
    }

    private class TestEngine extends SingleScoreRecommendationEngine<String, String> {

        @Override
        protected Map<String, PartialScore> doRecommendSingle(String input, Context<String, String> context) {
            Map<String, PartialScore> result = new HashMap<>();
            for (String s : peopleWithPoints.keySet()) {
                result.put(s, new PartialScore(Float.valueOf(peopleWithPoints.get(s))));
            }
            return result;
        }

        @Override
        public String name() {
            return "test";
        }
    }
}
