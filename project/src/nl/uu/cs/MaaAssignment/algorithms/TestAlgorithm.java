package nl.uu.cs.MaaAssignment.algorithms;

import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created by Peter on 9-5-2017.
 */
public class TestAlgorithm implements Algorithm
{
    private int _actionCount;

    public void initialize(Object[] parameters) {

    }

    @Override
    public Algorithm initialize(int actionCount, Object[] parameters)
    {
        _actionCount = actionCount;
        return this;
    }

    @Override
    public int nextAction(int round) {
        //random choice
        Random random = new Random();
        return random.nextInt(_actionCount - 1);
    }

    @Override
    public void reward(double reward, int round) {

    }
}
