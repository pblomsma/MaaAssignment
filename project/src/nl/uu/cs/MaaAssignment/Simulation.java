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

    private final List<Double> _actions;
    private final Map<Integer, Agent> _agents;

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

        _agents = new HashMap<Integer, Agent>();

        for(int i = 0; i < numberOfAgents; i++)
        {
            //TODO: init right algorithm type.
            Algorithm algorithm = new TestAlgorithm();
            algorithm.initialize(_actions, algorithmParams);
            Agent agent = new Agent(algorithm, i,_collisionRadius);

            double posX, posY;

            do
            {
                posX = randomGenerator.nextDouble() * _width;
                posY = randomGenerator.nextDouble() * _height;
            }
            while (!putOnPosition(agent));

            agent.setPosition(posX, posY);

             _agents.put(i, agent);
        }

        start();
    }

    private void start()
    {
        Statistics statistics = new Statistics(_actions, _agents.size());

        for(int round = 0; round < _rounds; round++)
        {
            statistics.startRound(round);

            Map<Integer, Integer> decisions = new HashMap<Integer, Integer>();

            //Yield decisions
            for (Agent agent : _agents.values())
            {
                decisions.put(agent.getId(), agent.nextAction(round));
            }

            //Play in random order
            List<Integer> agentIds = new ArrayList<Integer>();
            agentIds.addAll(decisions.keySet());
            Collections.shuffle(agentIds);

            for (int i : agentIds) {
                Agent agent = _agents.get(i);
                double reward;
                if (simulationStep(agent, decisions.get(i))) {
                    reward = _reward1;
                } else {
                    reward = _reward2;
                }

                agent.reward(reward, round);
                statistics.addReward(decisions.get(i), reward);
            }
        }
    }


    private boolean putOnPosition(Agent agent)
    {
        return _world.addAgent(agent.getId(), agent, agent.get_posX(), agent.get_posY());
    }

    private boolean simulationStep(Agent agent, int action)
    {
        double magnitude = _speed;
        double direction = _actions.get(action);

        double xVelocity = Math.cos(direction) * magnitude;
        double yVelocity = Math.sin(direction) * magnitude;

        return _world.moveAgent(agent.getId(), xVelocity, yVelocity);

        //double newX = agent.get_posX()  + Math.cos(direction) * magnitude;
        //double newY = agent.get_posY()  + Math.sin(direction) * magnitude;

        //TODO: add modulo and test if there's no collision from the line to all circles.
        //return true;
    }
}
