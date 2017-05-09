package nl.uu.cs.MaaAssignment;

import nl.uu.cs.MaaAssignment.algorithms.Algorithm;
import nl.uu.cs.MaaAssignment.algorithms.TestAlgorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Peter on 9-5-2017.
 */

public class Simulation
{

    private final List<Double> _actions;
    private final List<AgentPosition> _agents;

    private final double _speed;
    private final double _collisionRadius;
    private final double _width;
    private final double _height;


    private final double _reward1;
    private final double _reward2;

    private final int _rounds;

    private final TorusWorld _world;

    public static void main(String[] args)
    {
        //TODO: parse args to initialize simulation.
        // Maybe we can use a config file to setup an environment with multiple algorithms.

        new Simulation(10,10,4,100,100,1,10,100,100, "Default", null);
    }

    public Simulation(int numberOfAgents, int numberOfActions, double speed, double collisionRadius, double width, double height, double reward1, double reward2, int rounds, String algorithmType, Object[] algorithmParams)
    {

        _reward1 = reward1;
        _reward2 = reward2;

        _speed = speed;
        _collisionRadius = collisionRadius;
        _rounds = rounds;

        _width = width;
        _height = height;

        //Create actions
        double angle = 360.0 / (double) numberOfActions;

        _actions = new ArrayList<Double>();
        for(int i  = 0; i < numberOfActions; i++)
        {
            _actions.add(i * angle);
        }

        Random randomGenerator = new Random();


        _agents = new ArrayList<AgentPosition>();
        for(int i = 0; i < numberOfAgents; i++)
        {
            //TODO: init right algorithm type.
            Algorithm algorithm = new TestAlgorithm();
            algorithm.initialize(_actions, algorithmParams);
            Agent agent = new Agent(algorithm, i);

            AgentPosition agentPosition = new AgentPosition();
            agentPosition._agent = agent;

            do
            {
                agentPosition._posX = randomGenerator.nextDouble() * _width;
                agentPosition._posY = randomGenerator.nextDouble() * _height;
            }
            while (!putItPosition(agentPosition));
            _agents.add(agentPosition);
        }

        _world = new TorusWorld(width, height);

        start();

    }

    private void start()
    {
        for(int round = 0; round < _rounds; round++)
        {
            for(AgentPosition agent: _agents)
            {
                int action = agent._agent.nextAction(round);

                if(makeMove(agent, action))
                {
                    agent._agent.reward(_reward1, round);
                }
                else
                {
                    agent._agent.reward(_reward2, round);
                }
            }
        }
    }


    private boolean putItPosition(AgentPosition agent)
    {
        //check if agent is not colliding with border.
        if((agent._posX < _collisionRadius)|(agent._posY < _collisionRadius)|(_height - agent._posY < _collisionRadius)|(_width - agent._posX < _collisionRadius))
        {
            return false;
        }

        //Check if agent is not colliding with other agent.
        for(AgentPosition otherAgent: _agents)
        {
            if((2 * _collisionRadius) < (Math.sqrt(Math.pow(agent._posX-otherAgent._posX, 2)+ Math.pow(agent._posY-otherAgent._posY, 2))))
            {
                return false;
            }
        }
        _world.addAgent(agent._agent.getId(), agent._posX, agent._posY);
        return true;
    }

    private boolean makeMove(AgentPosition agent, int action)
    {

        return true;
    }

    //TODO: refactor agent away.
    class AgentPosition
    {
        Agent _agent;
        double _posX;
        double _posY;

        @Override
        public boolean equals(Object obj)
        {
            if(obj instanceof  AgentPosition)
            {
                return ((AgentPosition)obj)._agent.getId() == _agent.getId();
            }
            return super.equals(obj);
        }
    }

}
