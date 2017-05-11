package nl.uu.cs.MaaAssignment;

import nl.uu.cs.MaaAssignment.algorithms.Algorithm;

/**
 * Representation of a blind agent that only knows that it has
 */
public class MALAgent
{
    private Algorithm _algorithm;
    private final int _id;

    public MALAgent(Algorithm algorithm, int id) {
        _algorithm = algorithm;
        _id = id;
    }

    public int nextAction(int round) {
        return _algorithm.nextAction(round);
    }

    public void reward(double reward, int round) {
        _algorithm.reward(reward, round);
    }

    public int getId()
    {
        return _id;
    }
}
