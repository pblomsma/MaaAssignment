package nl.uu.cs.MaaAssignment.algorithms;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class EGreedyAlgorithmTest
{
    @Test
    public void testAverage() throws Exception
    {
        final EGreedyAlgorithm eGreedyAlgorithm = new EGreedyAlgorithm();

        int[] results = new int[2];

        eGreedyAlgorithm.initialize(results.length, new String[]{"0.999999"});

        for(int i = 0; i < 1000000; i++)
        {
            results[eGreedyAlgorithm.nextAction(0)]++;
        }
        assertEquals(1, (double)results[0]/(double)results[1], 0.1);
    }

    @Test
    public void testAlgorithmShouldChooseAction2() throws Exception
    {
        final EGreedyAlgorithm eGreedyAlgorithm = new EGreedyAlgorithm();

        int[] results = new int[3];

        eGreedyAlgorithm.initialize(results.length, new String[]{"0.001"});

        for(int i = 0; i < 1000000; i++)
        {
            int currentAction = eGreedyAlgorithm.nextAction(i);
            if(currentAction == 2)
            {
                eGreedyAlgorithm.reward(100.0, i);
            }
            else
            {
                eGreedyAlgorithm.reward(0.0, i);
            }
            results[currentAction]++;
        }
        assertEquals(950000, results[2], 50000);
    }

}