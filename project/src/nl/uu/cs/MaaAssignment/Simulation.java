package nl.uu.cs.MaaAssignment;

import nl.uu.cs.MaaAssignment.algorithms.Algorithm;
import nl.uu.cs.MaaAssignment.algorithms.EGreedyAlgorithm;
import nl.uu.cs.MaaAssignment.algorithms.OivAlgorithm;
import nl.uu.cs.MaaAssignment.algorithms.TestAlgorithm;
import nl.uu.cs.MaaAssignment.visualization.MaaAssignmentFrame;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Simulation extends ASubject{

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
    private final MaaAssignmentFrame _windowFrame;

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

        _actions = new ArrayList<>();
        for (int i = 0; i < numberOfActions; i++) {
            _actions.add(i * angle);
        }

        Random randomGenerator = new Random();

        _agents = new HashMap<>();

        System.out.println("Making Agents!");
        for (int i = 0; i < numberOfAgents; i++) {
            Agent agent = new Agent(getAlgorithm(algorithmId).initialize(_actions.size(), algorithmParams), i, _collisionRadius);

            double posX, posY;

            do {
                posX = randomGenerator.nextDouble() * _width;
                posY = randomGenerator.nextDouble() * _height;
            }
            while (!putOnPosition(agent));

            System.out.println("Placing Agent " + i + " !");
            agent.setPosition(posX, posY);

            _agents.put(i, agent);
        }

        System.out.println("Making Window!");
        this._windowFrame = new MaaAssignmentFrame(this);
    }

    public Collection<Agent> getAgents(){
        return _agents.values();
    }

    public Dimension getWorldSize(){
        return new Dimension((int)_width, (int)_height);
    }

    public MaaAssignmentFrame getWindowFrame() {
        return _windowFrame;
    }

    public void start() {
        StatisticsAggregator statistics = new StatisticsAggregator(_actions, _agents.size());

        for (int round = 0; round < _rounds; round++) {
            long roundStartTime = System.currentTimeMillis();
            statistics.startRound(round);

            Map<Integer, Integer> decisions = new HashMap<>();

            //Yield decisions
            for (Agent agent : _agents.values()) {
                decisions.put(agent.getId(), agent.nextAction(round));
            }

            //Play in random order
            List<Integer> agentIds = new ArrayList<>();
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
            super.notifyAllObservers();
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
     }

    private Algorithm getAlgorithm(int identifier) {
        switch (identifier)
        {
            case 0:
                return new EGreedyAlgorithm();
            case 1:
                return new OivAlgorithm();
            default:
                return new TestAlgorithm();
        }
    }
}
