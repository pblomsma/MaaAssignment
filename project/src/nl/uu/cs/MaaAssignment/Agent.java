package nl.uu.cs.MaaAssignment;

import nl.uu.cs.MaaAssignment.algorithms.Algorithm;

public class Agent
{
    private Algorithm _algorithm;
    private int _id;


    public Agent(Algorithm algorithm, int id)
    {
        _algorithm = algorithm;
        _id = id;
    }

    public int nextAction(int round)
    {
        return -1;
    }

    public void reward(double reward, int round)
    {

    }

}
