package nl.uu.cs.MaaAssignment.visualization;

import nl.uu.cs.MaaAssignment.Simulation;

import javax.swing.*;
import java.awt.*;

/**
 * Created by duame on 12/05/2017.
 */
public class MaaAssignmentFrame extends JFrame {

    public MaaAssignmentFrame(nl.uu.cs.MaaAssignment.Simulation simulation){
        this.setTitle("MaaAssignment");

        this.setSize(new Dimension(1000, 1000));
        this.setPreferredSize(new Dimension(1000, 1000));
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


        JTabbedPane tabContainer = new JTabbedPane();
        this.getContentPane().add(tabContainer);

        SimulationVisualization simulationPanel = new SimulationVisualization(simulation);
        //simulationPanel.setSize(new Dimension((int) _width, (int) _height));

        tabContainer.addTab("Simulation Visualization", simulationPanel);

        StatVisualization statPanel = new StatVisualization(simulation);
        tabContainer.addTab("Statistics", statPanel);

        this.pack();
        this.setVisible(true);
    }
}
