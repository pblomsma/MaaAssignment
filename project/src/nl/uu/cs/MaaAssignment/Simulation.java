package nl.uu.cs.MaaAssignment;

import nl.uu.cs.MaaAssignment.algorithms.Algorithm;
import nl.uu.cs.MaaAssignment.algorithms.TestAlgorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Peter on 9-5-2017.
 */
public class Simulation
{
    public static void main(String[] args)
    {
        //TODO: parse args to initialize simulation.
        // Maybe we can use a config file to setup an environment with multiple algorithms.
    }

    public Simulation(int numberOfAgents, int numberOfActions, double speed, double collisionRadius, double width, double height, double reward1, double reward2, int rounds, String algorithmType, Object[] algorithmParams)
    {
        //Create actions
        double angle = 360.0 / (double) numberOfActions;

        List<Double> actions = new ArrayList<Double>();
        for(int i  = 0; i < numberOfActions; i++)
        {
            actions.add(i * angle);
        }

        List<AgentPosition> agents = new ArrayList<AgentPosition>();
        for(int i = 0; i < numberOfAgents; i++)
        {
            //TODO: init right algorithm type.
            Algorithm algorithm = new TestAlgorithm();
            algorithm.initialize(actions, algorithmParams);
            Agent agent = new Agent(algorithm, i);

            AgentPosition agentPosition = new AgentPosition();
            agentPosition._agent = agent;

            //TODO: put agent on a start position in the map.
        }

        TorusWorld world = new TorusWorld(width, height);

        start(rounds);

    }

    private void start(int rounds)
    {
        for(int round = 0; round < rounds; round++)
        {
            //TODO
        }
    }


    class AgentPosition
    {
        Agent _agent;
        double _posX;
        double _posY;
    }

}
