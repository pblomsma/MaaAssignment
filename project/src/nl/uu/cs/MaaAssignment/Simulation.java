package nl.uu.cs.MaaAssignment;

import nl.uu.cs.MaaAssignment.algorithms.Algorithm;
import nl.uu.cs.MaaAssignment.algorithms.TestAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by Peter on 9-5-2017.
 */

public class Simulation
{

    private final List<Double> _actions;
    //private final List<MALAgentPosition> _agentPositions;
    private final HashMap<Integer, MALAgentPosition> _agentPositions;

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

        _world = new TorusWorld(width, height);

        //Create actions
        double angle = 360.0 / (double) numberOfActions;

        _actions = new ArrayList<Double>();
        for(int i  = 0; i < numberOfActions; i++)
        {
            _actions.add(i * angle);
        }

        Random randomGenerator = new Random();

        _agentPositions = new HashMap<Integer, MALAgentPosition>();
        for(int i = 0; i < numberOfAgents; i++)
        {
            //TODO: init right algorithm type.
            Algorithm algorithm = new TestAlgorithm();
            algorithm.initialize(_actions, algorithmParams);
            MALAgent MALAgent = new MALAgent(algorithm, i, _collisionRadius);

            MALAgentPosition MALAgentPosition = new MALAgentPosition(MALAgent);
            double posX, posY = Double.NEGATIVE_INFINITY;

            do
            {
                posX = randomGenerator.nextDouble() * _width;
                posY = randomGenerator.nextDouble() * _height;
            }
            while (!putOnPosition(MALAgentPosition));
            MALAgentPosition.setPosition(posX, posY);
            _agentPositions.put(i, MALAgentPosition);
        }

        start();
    }

    private void start()
    {
        for(int round = 0; round < _rounds; round++)
        {
            for(MALAgentPosition agentPosition: _agentPositions.values())
            {
                MALAgent currentAgent = agentPosition.get_MAL_agent();
                int action = currentAgent.nextAction(round);

                if(simulationStep(agentPosition, action))
                {
                    currentAgent.reward(_reward1, round);
                }
                else
                {
                    currentAgent.reward(_reward2, round);
                }
            }
        }
    }


    private boolean putOnPosition(MALAgentPosition agentPosition)
    {
        //Check if agent is not colliding with other agent.
        for(MALAgentPosition otherAgent: _agentPositions.values())
        {
            if((2 * _collisionRadius) < distanceOnTorus(agentPosition.get_posX(), otherAgent.get_posX(), agentPosition.get_posY(), otherAgent.get_posY()))
            {
                return false;
            }
        }

        // Not sure what the intention was
        //_world.addAgent(agentPosition._MAL_agent.getId(), agentPosition._posX, agentPosition._posY);
        // _world.teleportAgent(agentPosition.get_MAL_agent().getId(), agentPosition.get_MAL_agent(), agentPosition.get_posX(), agentPosition.get_posY());

        // Assuming that this should just teleport the agent to proposed location
        _world.teleportAgent(agentPosition.get_MAL_agent().getId(), agentPosition.get_posX(), agentPosition.get_posY());
        return true;
    }

    private boolean simulationStep(MALAgentPosition agent, int action)
    {
        double magnitude = _speed;
        double direction = _actions.get(action);

        double xVelocity = Math.cos(direction) * magnitude;
        double yVelocity = Math.sin(direction) * magnitude;

        return _world.moveAgent(agent.get_MAL_agent().getId(), xVelocity, yVelocity);


        //double newX = agent.get_posX()  + Math.cos(direction) * magnitude;
        //double newY = agent.get_posY()  + Math.sin(direction) * magnitude;

        //TODO: add modulo and test if there's no collision from the line to all circles.
        //return true;
    }

    private double distanceOnTorus(double x1, double y1, double x2, double y2)
    {
        double dx = Math.pow(x1 - x2, 2);
        double ix = Math.pow(_width -  Math.max(x1, x2) + Math.min(x1, x2), 2);

        double dy = Math.pow(y1 - y2, 2);
        double iy = Math.pow(_width -  Math.max(y1, y2) + Math.min(y1, y2), 2);

        return Math.sqrt(Math.min(dx, ix) + Math.min(dy, iy));

    }

//    //TODO: refactor agent away.
//    class MALAgentPosition
//    {
//        MALAgent _MAL_agent;
//        double _posX;
//        double _posY;
//
//        public MALAgentPosition(MALAgent agent, double posX, double posY)
//        {
//            this._MAL_agent = agent;
//            this._posX = posX;
//            this._posY = posY;
//        }
//
//        @Override
//        public boolean equals(Object obj)
//        {
//            if(obj instanceof MALAgentPosition)
//            {
//                return ((MALAgentPosition)obj)._MAL_agent.getId() == _MAL_agent.getId();
//            }
//            return super.equals(obj);
//        }
//    }

}
