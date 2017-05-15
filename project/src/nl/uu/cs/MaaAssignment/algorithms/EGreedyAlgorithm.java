package nl.uu.cs.MaaAssignment.algorithms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EGreedyAlgorithm implements Algorithm {

    private double _epsilon;
    private List<Double> _actions;
    private Score[] _scores;
    private Map<Integer, Integer> _actionTrail;

    @Override
    public Algorithm initialize(List<Double> actions, Object[] parameters)
    {
        //TODO: exception throwing is more elegant, but maybe overkill for this simulation.
        if(parameters.length != 1)
        {
            System.err.print("Amount of parameters specifified for e-greedy is incorrect. Only specify the epsilon");
            System.exit(1);
        }
        _epsilon = Double.parseDouble((String)parameters[0]);

        if(!(_epsilon > 0 && _epsilon < 1))
        {
            System.err.print("Epsilon parameter for e-greedy algorithm must be between 0 and 1. epsilon of " + _epsilon + " is incorrect.");
            System.exit(1);
        }

        _actions = actions;
        _actionTrail = new HashMap<>();

        _scores = new Score[_actions.size()];

        return this;
    }

    @Override
    public int nextAction(int round)
    {
        int action;
        if (Math.random() < _epsilon)
        {
           action = getRandomAction();
        }
        else
        {
            action = getOptimalAction();
        }
        _actionTrail.put(round, action);

        return action;
    }

    @Override
    public void reward(double reward, int round)
    {
        int action = _actionTrail.get(round);

        if(_scores[action] == null)
        {
            _scores[action] = new Score();
        }

        _scores[action].n++;
        _scores[action].q += (1.0 / _scores[action].n) * (reward - _scores[action].q);
    }

    //Exploration (learning)
    private int getRandomAction()
    {
       return new Random().nextInt(_actions.size());
    }

    //Exploitation (use what is learned)
    private int getOptimalAction()
    {
        int highestScore = 0;

        for(int i = 0; i < _scores.length; i++)
        {
            if(_scores[i].q > _scores[highestScore].q)
            {
                highestScore =i;
            }
        }
        return highestScore;
    }

    private static class Score
    {
        int n;
        double q;
    }
}
