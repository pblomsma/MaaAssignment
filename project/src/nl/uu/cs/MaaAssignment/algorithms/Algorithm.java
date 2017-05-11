package nl.uu.cs.MaaAssignment.algorithms;

import java.util.List;

/**
 * Created by Peter on 9-5-2017.
 */
public interface Algorithm
{
    public Algorithm initialize(List<Double> actions, Object[] parameters);

    public int nextAction(int round);

    public void reward(double reward, int round);

}
