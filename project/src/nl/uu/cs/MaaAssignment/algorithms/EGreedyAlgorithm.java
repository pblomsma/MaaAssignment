package nl.uu.cs.MaaAssignment.algorithms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EGreedyAlgorithm implements Algorithm {

    private double _epsilon;
    private int _actionCount;
    private Score[] _scores;
    private Map<Integer, Integer> _actionTrail;

    @Override
    public Algorithm initialize(int actionCount, Object[] parameters)
    {
        //TODO: exception throwing is more elegant, but maybe overkill for this simulation.
        if((parameters == null) || (parameters.length != 1))
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

        _actionCount = actionCount;
        _actionTrail = new HashMap<>();

        _scores = new Score[_actionCount];
        for(int i = 0 ; i < _scores.length;i++)
        {
            _scores[i] = new Score();
        }

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

        _scores[action].n++;
        _scores[action].q += (1.0 / _scores[action].n) * (reward - _scores[action].q);
    }

    //Exploration (learning)
    private int getRandomAction()
    {
       return new Random().nextInt(_actionCount);
    }

    //Exploitation (use what is learned)
    private int getOptimalAction()
    {
        int highestScore = 0;

        for(int i = 0; i < _scores.length; i++)
        {
            if(_scores[i].q > _scores[highestScore].q)
            {
                highestScore = i;
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
