package nl.uu.cs.MaaAssignment;

import java.util.ArrayList;
import java.util.List;

public class StatisticsAggregator
{
    public interface Processor
    {
        void finalize(List<Double>[] meansPerActionPerRound);
    }

    //Responsible for accumulating input stats for plot: mean reward per action per time.

    private final List<Double> _actions;
    private double[] _rewards;
    private int _round = -1;
    private final int _agentCount;
    private final List<Processor> _processors = new ArrayList<Processor>();

    private List<Double>[] _meanListPerRound;

    public StatisticsAggregator(List<Double> actions, int agentCount)
    {
        _actions = actions;
        _agentCount = agentCount;
        _meanListPerRound = (ArrayList<Double>[])new ArrayList[_actions.size()];

        //init
        for(int i = 0; i < _actions.size(); i++)
        {
            _meanListPerRound[i] = new ArrayList<Double>();
        }
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
        for(int action = 0; action < _rewards.length ; action++)
        {
            //if rewards = 0, 0 else reward/agents
            _meanListPerRound[action].add((_rewards[action] > 0? _rewards[action] / (double)_agentCount : 0 ));
        }
    }

    private void finalizeProcessors()
    {
        for(Processor processor: _processors)
        {
            processor.finalize(_meanListPerRound);
        }
    }
}
