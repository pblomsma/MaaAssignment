package nl.uu.cs.MaaAssignment;

import nl.uu.cs.MaaAssignment.algorithms.Algorithm;

public class MALAgent
{
    private Algorithm _algorithm;
    private int _id;

    private double _radius;

    public MALAgent(Algorithm algorithm, int id, double radius)
    {
        _algorithm = algorithm;
        _id = id;
        _radius = radius;
    }

    public int nextAction(int round)
    {
        return _algorithm.nextAction(round);
    }

    public void reward(double reward, int round)
    {
        _algorithm.reward(reward, round);
    }

    public int getId()
    {
        return _id;
    }

    public double getRadius() {
        return _radius;
    }
}
