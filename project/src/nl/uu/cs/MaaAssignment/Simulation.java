package nl.uu.cs.MaaAssignment;

import nl.uu.cs.MaaAssignment.algorithms.Algorithm;
import nl.uu.cs.MaaAssignment.algorithms.TestAlgorithm;

import java.util.*;

/**
 * Created by Peter on 9-5-2017.
 */

public class Simulation
{

    //TODO : ADD REINFORCEMENT LEARNING ALGORITHMS!!!!

    //TODO : Peter's class desu

    //TODO : Main loop -> Ask every agent what they plan to do, in random order try to execute those movements and give
    //TODO : rewards accordingly.

    //TODO : Track mean rewards per action per time step

    private final List<Double> _actions;
    private final Map<Integer, MALAgentPosition> _agentPositions;

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

        _world = new TorusWorld(width, height, collisionRadius);

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
            MALAgent malAgent = new MALAgent(algorithm, i);

            MALAgentPosition agentPosition = new MALAgentPosition(malAgent);
            double posX, posY;

            do
            {
                posX = randomGenerator.nextDouble() * _width;
                posY = randomGenerator.nextDouble() * _height;
            }
            while (!putOnPosition(agentPosition));

            agentPosition.setPosition(posX, posY);

             _agentPositions.put(i, agentPosition);
        }

        start();
    }

    private void start()
    {
        for(int round = 0; round < _rounds; round++) {
            Map<Integer, Integer> decisions = new HashMap<Integer, Integer>();

            //Yield decisions
            for (MALAgentPosition agentPosition : _agentPositions.values()) {
                MALAgent currentAgent = agentPosition.getMALAgent();

                decisions.put(currentAgent.getId(), currentAgent.nextAction(round));
            }

            //Play in random order
            List<Integer> agentIds = new ArrayList<Integer>();
            agentIds.addAll(decisions.keySet());
            Collections.shuffle(agentIds);

            for (int i : agentIds) {
                MALAgentPosition agentPosition = _agentPositions.get(i);

                if (simulationStep(agentPosition, decisions.get(i))) {
                    agentPosition.getMALAgent().reward(_reward1, round);
                } else {
                    agentPosition.getMALAgent().reward(_reward2, round);
                }
            }
        }
    }


    private boolean putOnPosition(MALAgentPosition agentPosition)
    {
        return _world.addAgent(agentPosition.getMALAgent().getId(), agentPosition.getMALAgent(), agentPosition.get_posX(), agentPosition.get_posY());
    }

    private boolean simulationStep(MALAgentPosition agent, int action)
    {
        double magnitude = _speed;
        double direction = _actions.get(action);

        double xVelocity = Math.cos(direction) * magnitude;
        double yVelocity = Math.sin(direction) * magnitude;

        return _world.moveAgent(agent.getMALAgent().getId(), xVelocity, yVelocity);

        //double newX = agent.get_posX()  + Math.cos(direction) * magnitude;
        //double newY = agent.get_posY()  + Math.sin(direction) * magnitude;

        //TODO: add modulo and test if there's no collision from the line to all circles.
        //return true;
    }
}
