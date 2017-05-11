package nl.uu.cs.MaaAssignment;

import nl.uu.cs.MaaAssignment.algorithms.Algorithm;
import nl.uu.cs.MaaAssignment.algorithms.TestAlgorithm;

import java.util.*;

/**
 * Created by Peter on 9-5-2017.
 */

public class Simulation {

    //TODO : ADD REINFORCEMENT LEARNING ALGORITHMS!!!!
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

    public static void main(String[] args) {
        new Simulation(Integer.parseInt(args[0]),
                Integer.parseInt(args[1]), Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]), Double.parseDouble(args[6]), Double.parseDouble(args[7]), Integer.parseInt(args[8]), Integer.parseInt(args[9]), Arrays.copyOfRange(args, 10, args.length));
    }

    public Simulation(int numberOfAgents, int numberOfActions, double speed, double collisionRadius, double width, double height, double reward1, double reward2, int rounds, int algorithmId, Object[] algorithmParams) {

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
        for (int i = 0; i < numberOfActions; i++) {
            _actions.add(i * angle);
        }

        Random randomGenerator = new Random();

        _agents = new HashMap<Integer, Agent>();

        for (int i = 0; i < numberOfAgents; i++) {
            Agent agent = new Agent(getAlgorithm(algorithmId).initialize(_actions, algorithmParams), i, _collisionRadius);

            double posX, posY;

            do {
                posX = randomGenerator.nextDouble() * _width;
                posY = randomGenerator.nextDouble() * _height;
            }
            while (!putOnPosition(agent));

            agent.setPosition(posX, posY);

            _agents.put(i, agent);
        }

        start();
    }

    private void start() {
        Statistics statistics = new Statistics(_actions, _agents.size());

        for (int round = 0; round < _rounds; round++) {
            statistics.startRound(round);

            Map<Integer, Integer> decisions = new HashMap<Integer, Integer>();

            //Yield decisions
            for (Agent agent : _agents.values()) {
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


    private boolean putOnPosition(Agent agent) {
        return _world.addAgent(agent.getId(), agent, agent.get_posX(), agent.get_posY());
    }

    private boolean simulationStep(Agent agent, int action) {
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

    private Algorithm getAlgorithm(int identifier) {
        switch (identifier) {
            default:
                return new TestAlgorithm();
        }
    }
}
