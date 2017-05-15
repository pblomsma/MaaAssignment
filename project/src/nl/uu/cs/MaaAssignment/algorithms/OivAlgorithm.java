package nl.uu.cs.MaaAssignment.algorithms;

import java.util.HashMap;
import java.util.Map;

//Optimistic initial values
public class OivAlgorithm implements Algorithm {
    private int _actionCount;
    private Score[] _scores;
    private Map<Integer, Integer> _actionTrail;


    @Override
    public Algorithm initialize(int actionCount, Object[] parameters)
    {
        if ((parameters == null) || (parameters.length != 1)) {
            System.err.print("Amount of parameters specifified for optimistic initial values is incorrcet. Only specify initial value");
            System.exit(1);
        }
        double initialValue = Double.parseDouble((String) parameters[0]);

        _actionCount = actionCount;
        _actionTrail = new HashMap<>();

        _scores = new Score[_actionCount];
        for (int i = 0; i < _scores.length; i++) {
            _scores[i] = new Score();
            _scores[i].q = initialValue;
        }
        return this;
    }

    @Override
    public int nextAction(int round)
    {
        int bestAction = 0;

        for (int i = 0; i < _scores.length; i++) {
            if (_scores[i].q > _scores[bestAction].q) {
                bestAction= i;
            }
        }

        _actionTrail.put(round, bestAction);
        return bestAction;
    }

    @Override
    public void reward(double reward, int round)
    {
        int action = _actionTrail.get(round);

        _scores[action].n++;
        _scores[action].q += (1.0 / _scores[action].n) * (reward - _scores[action].q);
    }

    private static class Score {
        int n;
        double q;
    }
}