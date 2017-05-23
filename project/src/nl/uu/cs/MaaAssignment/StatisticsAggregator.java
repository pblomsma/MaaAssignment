package nl.uu.cs.MaaAssignment;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
    private PrintWriter _printWriter;

    StatisticsAggregator(List<Double> actions, int agentCount, String file) throws IOException {
        _actions = actions;
        _agentCount = agentCount;

            File outputFile = new File(file);
            if(outputFile.createNewFile())
            {
                _printWriter = new PrintWriter( outputFile);
                _printWriter.append("round;action;mean");
            }

    }

    void startRound(int i)
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
    }

    void addReward(int action, double reward)
    {
        _rewards[action] += reward;
    }

    //Gets executed after every round.
    private void processResults()
    {
        for(int action = 0; action < _rewards.length ; action++)
        {
            //if rewards = 0, 0 else reward/agents
            _printWriter.append(System.getProperty("line.separator"));
            _printWriter.append("" + _round + ";" + "Action " + action + ";" + (_rewards[action] > 0? _rewards[action] / (double)_agentCount : 0 ));
        }
    }
}
