package nl.uu.cs.MaaAssignment;

import java.util.ArrayList;
import java.util.List;

public class StatisticsAggregator
{
    public interface Processor
    {
        void append(int round, double[] sum, double[] mean, double variance[]);
    }

    //Responsible for accumulating input stats for plot: mean reward per action per time.

    private final List<Double> _actions;
    private List<Double>[] _rewards;
    private int _round = -1;
    private final int _agentCount;
    private final List<Processor> _processors;

    public StatisticsAggregator(List<Double> actions, int agentCount)
    {
        _actions = actions;
        _agentCount = agentCount;
        _processors = new ArrayList<Processor>();
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

        _rewards = new List[_actions.size()];
    }

    public void addReward(int action, double reward)
    {
        if(_rewards[action] == null)
        {
            _rewards[action] = new ArrayList<Double>();
        }
        _rewards[action].add(reward);
    }

    private void processResults()
    {
        final double sum[] = new double[_rewards.length];
        final double mean[] = new double[_rewards.length];
        final double variance[] = new double[_rewards.length];

        for(int action = 0; action < _rewards.length ; action++)
        {
            double actionSum = 0;
            double actionMean;
            double actionVariance = 0;

            if(_rewards[action] != null)
            {
                for(Double d: _rewards[action])
                    actionSum += d;
            }

            actionMean = (actionSum > 0? actionSum / _agentCount : 0 );

            if(_rewards[action] != null)
            {
                for(Double d: _rewards[action])
                    actionVariance += Math.pow(actionMean - d, 2);
            }
            sum[action] = actionSum;
            mean[action] = actionMean;
            variance[action] = actionVariance;
        }
        updateProcessors(_round, sum,mean, variance);
    }

    private void updateProcessors(int round, double[] sum, double[] mean, double variance[])
    {
        for(Processor processor: _processors)
        {
            processor.append(round,sum,mean,variance);
        }
    }
}
