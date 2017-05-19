package nl.uu.cs.MaaAssignment;

import java.util.ArrayList;
import java.util.List;

public class StatisticsAggregator
{
    public interface Processor
    {
        void finalize(List<double[]> means);
    }

    //Responsible for accumulating input stats for plot: mean reward per action per time.

    private final List<Double> _actions;
    private double[] _rewards;
    private int _round = -1;
    private final int _agentCount;
    private final static List<Processor> _processors = new ArrayList<Processor>();

    private List<double[]> _meanListPerRound;

    public StatisticsAggregator(List<Double> actions, int agentCount)
    {
        _actions = actions;
        _agentCount = agentCount;
        _meanListPerRound = new ArrayList<>();
    }

    public void addProcessor(Processor processor)
    {
        _processors.add(processor);
    }

    public void startRound(int i)
    {
        if(_round > -1)
        {
            processResults();
        }
        _round = i;

        _rewards = new double[_actions.size()];
    }

    public void finalize()
    {
        processResults();
        finalizeProcessors();
    }

    public void addReward(int action, double reward)
    {
        _rewards[action] += reward;
    }

    //Gets executed after every round.
    private void processResults()
    {
        final double meanPerAction[] = new double[_rewards.length];

        for(int action = 0; action < _rewards.length ; action++)
        {
            meanPerAction[action] = (_rewards[action] > 0? _rewards[action] / (double)_agentCount : 0 );
        }
        _meanListPerRound.add(meanPerAction);
    }

    private void finalizeProcessors()
    {
        for(Processor processor: _processors)
        {
            processor.finalize(_meanListPerRound);
        }
    }
}
