package nl.uu.cs.MaaAssignment.visualization;

import nl.uu.cs.MaaAssignment.Simulation;

import javax.swing.*;
import java.awt.*;

/**
 * Created by duame on 12/05/2017.
 */
public class MaaAssignmentFrame extends JFrame {

    private JTabbedPane tabContainer;
    private SimulationVisualization simulationPanel;
    private StatVisualization statPanel;

    public MaaAssignmentFrame(nl.uu.cs.MaaAssignment.Simulation simulation){
        this.setTitle("MaaAssignment");

        this.setSize(new Dimension(1000, 1000));
        this.setPreferredSize(new Dimension(1000, 1000));
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        this.tabContainer = new JTabbedPane();
        this.getContentPane().add(tabContainer);

        this.simulationPanel = new SimulationVisualization(simulation);
        //simulationPanel.setSize(new Dimension((int) _width, (int) _height));

        tabContainer.addTab("Simulation Visualization", simulationPanel);

        this.statPanel = new StatVisualization(simulation);
        tabContainer.addTab("Statistics", statPanel);

        this.pack();
        this.setVisible(true);
    }

    public StatVisualization getStatPanel() {
        return statPanel;
    }
}
