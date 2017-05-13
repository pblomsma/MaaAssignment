package nl.uu.cs.MaaAssignment.algorithms;

import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Created by Peter on 9-5-2017.
 */
public class TestAlgorithm implements Algorithm
{
    List<Double> _actions;

    public void initialize(Object[] parameters) {

    }

    @Override
    public Algorithm initialize(List<Double> actions, Object[] parameters)
    {
        _actions = actions;
        return this;
    }

    @Override
    public int nextAction(int round) {
        //random choice
        Random random = new Random();
        return random.nextInt(_actions.size());
    }

    @Override
    public void reward(double reward, int round) {

    }
}
