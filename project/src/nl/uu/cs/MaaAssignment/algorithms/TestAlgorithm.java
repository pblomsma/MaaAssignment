package nl.uu.cs.MaaAssignment.algorithms;

import java.util.List;

/**
 * Created by Peter on 9-5-2017.
 */
public class TestAlgorithm implements Algorithm
{

    public void initialize(Object[] parameters) {

    }

    @Override
    public Algorithm initialize(List<Double> actions, Object[] parameters)
    {
        return this;
    }

    @Override
    public int nextAction(int round) {
        return 1;
    }

    @Override
    public void reward(double reward, int round) {

    }
}
