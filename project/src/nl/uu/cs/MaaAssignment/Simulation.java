package nl.uu.cs.MaaAssignment;


import nl.uu.cs.MaaAssignment.algorithms.Algorithm;
import nl.uu.cs.MaaAssignment.algorithms.EGreedyAlgorithm;
import nl.uu.cs.MaaAssignment.algorithms.OivAlgorithm;
import nl.uu.cs.MaaAssignment.algorithms.TestAlgorithm;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class Simulation {

    private final List<Double> _actions;
    private final Map<Integer, Agent> _agents;

    private final TorusWorld _world;
    private final Parameters _parameters;
    private final StatisticsAggregator _statisticsAggregator;


    public static void main(String[] args) throws IOException {
        Simulation simulation = new Simulation(
                Integer.parseInt(args[0]),                      // number of agents
                Integer.parseInt(args[1]),                      // number of actions
                Double.parseDouble(args[2]),                    // speed
                Double.parseDouble(args[3]),                    // collision radius
                Double.parseDouble(args[4]),                    // width
                Double.parseDouble(args[5]),                    // height
                Double.parseDouble(args[6]),                    // reward 1
                Double.parseDouble(args[7]),                    // reward 2
                Integer.parseInt(args[8]),                      // rounds
                Integer.parseInt(args[9]),                      // algorithm ID
                Arrays.copyOfRange(args, 10, args.length), null       // algorithm params
        );
        simulation.start();
    }

    public static Parameters createParameters(int numberOfAgents, int numberOfActions, double speed, double collisionRadius, double width, double height, double reward1, double reward2, int rounds, int algorithmId, Object[] algorithmParams)
    {
        return new Parameters(numberOfAgents, numberOfActions, speed, collisionRadius, width, height, reward1, reward2, rounds, algorithmId, algorithmParams);
    }

    public Simulation(int numberOfAgents, int numberOfActions, double speed, double collisionRadius, double width, double height, double reward1, double reward2, int rounds, int algorithmId, Object[] algorithmParams, File  file)throws IOException {
        this(new Parameters(numberOfAgents, numberOfActions, speed, collisionRadius, width, height, reward1, reward2, rounds, algorithmId, algorithmParams), file);
    }

    public Simulation(Parameters parameters, File file) throws IOException {
        _parameters = parameters;


        _world = new TorusWorld(parameters.getWidth(), parameters.getHeight(), parameters.getCollisionRadius());

        //Create actions
        double angle = 360.0 / (double) parameters.getNumberOfActions();

        _actions = new ArrayList<>();
        for (int i = 0; i < parameters.getNumberOfActions(); i++) {
            _actions.add(i * angle);
        }

        Random randomGenerator = new Random();

        _agents = new HashMap<>();

        for (int i = 0; i < parameters.getNumberOfAgents(); i++) {
            Agent agent = new Agent(getAlgorithm(parameters.getAlgorithmId()).initialize(_actions.size(), parameters.getAlgorithmParams()), i, parameters.getCollisionRadius());

            double posX, posY;

            do {
                posX = randomGenerator.nextDouble() * parameters.getWidth();
                posY = randomGenerator.nextDouble() * parameters.getHeight();
            }
            while (!putOnPosition(agent));

            agent.setPosition(posX, posY);

            _agents.put(i, agent);
        }

        _statisticsAggregator = new StatisticsAggregator(_actions, _agents.size(), file);
    }

    public Collection<Agent> getAgents() {
        return _agents.values();
    }

    public Dimension getWorldSize() {
        return new Dimension((int) _parameters.getWidth(), (int) _parameters.getHeight());
    }

    public void start()
    {
        for (int round = 0; round < _parameters.getRounds(); round++) {
            _statisticsAggregator.startRound(round);

            if((round % 1000000) == 0 ) {
                System.out.println("Round: " + round + " Time = " + System.currentTimeMillis());
            }

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
                    reward = _parameters.getReward1();
                } else {
                    reward = _parameters.getReward2();
                }

                agent.reward(reward, round);
                _statisticsAggregator.addReward(decisions.get(i), reward);
            }
        }
        _statisticsAggregator.finalize();
    }


    private boolean putOnPosition(Agent agent) {
        return _world.addAgent(agent, agent.get_posX(), agent.get_posY());
    }

    private boolean simulationStep(Agent agent, int action) {
        double magnitude = _parameters.getSpeed();
        double direction = _actions.get(action);

        double xVelocity = Math.cos(direction) * magnitude;
        double yVelocity = Math.sin(direction) * magnitude;

        return _world.moveAgent(agent, xVelocity, yVelocity);
    }

    private Algorithm getAlgorithm(int identifier) {
        switch (identifier) {
            case 0:
                return new EGreedyAlgorithm();
            case 1:
                return new OivAlgorithm();
            default:
                return new TestAlgorithm();
        }
    }

    public Parameters getParameters() {
        return _parameters;
    }

}
