package nl.uu.cs.MaaAssignment.visualization;

import nl.uu.cs.MaaAssignment.IObserver;
import nl.uu.cs.MaaAssignment.Simulation;

import javax.swing.*;

/**
 * Created by duame on 12/05/2017.
 */
public class StatVisualization extends JPanel implements IObserver {

    private Simulation simulation;

    public StatVisualization(Simulation simulation){
        super();
        this.registerSimulation(simulation);
    }

    private void registerSimulation(Simulation simulation){
        this.simulation = simulation;
        simulation.attach(this);
    }

    @Override
    public void update() {

    }
}
