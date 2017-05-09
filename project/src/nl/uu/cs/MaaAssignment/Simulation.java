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
        //Check if agent is not colliding with other agent.
        for(AgentPosition otherAgent: _agents)
        {
            if((2 * _collisionRadius) < distanceOnTorus(agent._posX, otherAgent._posX, agent._posY, otherAgent._posY))
            {
                return false;
            }
        }
        _world.addAgent(agent._agent.getId(), agent._posX, agent._posY);
        return true;
    }

    private boolean makeMove(AgentPosition agent, int action)
    {
        double magnitude = _speed;
        double direction = _actions.get(action);

        double newX = agent._posX  + Math.cos(direction) * magnitude;
        double newY = agent._posY  + Math.sin(direction) * magnitude;

        //TODO: add modulo and test if there's no collision from the line to all circles.
        return true;
    }

    private double distanceOnTorus(double x1, double y1, double x2, double y2)
    {
        double dx = Math.pow(x1 - x2, 2);
        double ix = Math.pow(_width -  Math.max(x1, x2) + Math.min(x1, x2), 2);

        double dy = Math.pow(y1 - y2, 2);
        double iy = Math.pow(_width -  Math.max(y1, y2) + Math.min(y1, y2), 2);

        return Math.sqrt(Math.min(dx, ix) + Math.min(dy, iy));

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
