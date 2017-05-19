package nl.uu.cs.MaaAssignment;


import nl.uu.cs.MaaAssignment.algorithms.Algorithm;
import nl.uu.cs.MaaAssignment.algorithms.EGreedyAlgorithm;
import nl.uu.cs.MaaAssignment.algorithms.OivAlgorithm;
import nl.uu.cs.MaaAssignment.algorithms.TestAlgorithm;
import nl.uu.cs.MaaAssignment.visualization.MaaAssignmentFrame;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class Simulation extends ASubject{

    private final List<Double> _actions;
    private final Map<Integer, Agent> _agents;

    private final TorusWorld _world;
    private final MaaAssignmentFrame _windowFrame;
    private final Parameters _parameters;
    private final StatisticsAggregator _statisticsAggregator;


    public static void main(String[] args){
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
            Arrays.copyOfRange(args, 10, args.length)       // algorithm params
        );
        simulation.start();
    }

    public Simulation(int numberOfAgents, int numberOfActions, double speed, double collisionRadius, double width, double height, double reward1, double reward2, int rounds, int algorithmId, Object[] algorithmParams)
    {
            this(new Parameters(numberOfAgents, numberOfActions, speed, collisionRadius,width, height, reward1, reward2, rounds, algorithmId, algorithmParams ));
    }

    public Simulation(Parameters parameters)
        {
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

//        System.out.println("Making Agents!");
        for (int i = 0; i < parameters.getNumberOfAgents(); i++) {
            Agent agent = new Agent(getAlgorithm(parameters.getAlgorithmId()).initialize(_actions.size(), parameters.getAlgorithmParams()), i, parameters.getCollisionRadius());

            double posX, posY;

            do {
                posX = randomGenerator.nextDouble() * parameters.getWidth();
                posY = randomGenerator.nextDouble() * parameters.getHeight();
            }
            while (!putOnPosition(agent));

//            System.out.println("Placing Agent " + i + " !");
            agent.setPosition(posX, posY);

            _agents.put(i, agent);
        }

//        System.out.println("Making Window!");
        _statisticsAggregator =  new StatisticsAggregator(_actions, _agents.size());
        _windowFrame = new MaaAssignmentFrame(this);

    }

    public Collection<Agent> getAgents(){
        return _agents.values();
    }

    public Dimension getWorldSize(){
        return new Dimension((int)_parameters.getWidth(), (int)_parameters.getHeight());
    }

    public MaaAssignmentFrame getWindowFrame() {
        return _windowFrame;
    }

    public void start() {
        StatisticsAggregator statistics = new StatisticsAggregator(_actions, _agents.size());

        for (int round = 0; round < _parameters.getRounds(); round++) {
            statistics.startRound(round);
            System.out.println("Round: " + round);

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
                statistics.addReward(decisions.get(i), reward);
            }
            super.notifyAllObservers();
        }
        statistics.finalize();
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

    public Parameters getParameters() {
        return _parameters;
    }

    public void shutDown()
    {
        _windowFrame.dispose();
    }

    public StatisticsAggregator getStatisticsAggregator()
    {
        return _statisticsAggregator;
    }
}
