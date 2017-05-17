package nl.uu.cs.MaaAssignment.visualization;

import nl.uu.cs.MaaAssignment.Agent;
import nl.uu.cs.MaaAssignment.IObserver;
import nl.uu.cs.MaaAssignment.Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

/**
 * Created by duame on 12/05/2017.
 */
public class SimulationVisualization extends JPanel implements IObserver {

    private Simulation simulation;

    public SimulationVisualization(Simulation simulation){
        super();
        this.registerSimulation(simulation);
        this.setSize(simulation.getWorldSize());
        this.setPreferredSize(simulation.getWorldSize());
    }

    private void registerSimulation(Simulation simulation){
        this.simulation = simulation;
        simulation.attach(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Collection<Agent> agents = simulation.getAgents();
        for(Agent agent: agents) {
            g.setColor(Color.getHSBColor(agent.getId()*25, 5, 5));
            g.fillOval((int) agent.get_posX(), (int) agent.get_posY(), (int) agent.getRadius(), (int) agent.getRadius());
//            System.out.println("Agent " + agent.getId() + " location : x=" + agent.get_posX() + " y=" + agent.get_posY());
        }
    }

    @Override
    public void update() {
        repaint();
    }
}
