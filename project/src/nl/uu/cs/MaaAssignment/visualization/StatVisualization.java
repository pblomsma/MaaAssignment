package nl.uu.cs.MaaAssignment.visualization;

import nl.uu.cs.MaaAssignment.IObserver;
import nl.uu.cs.MaaAssignment.Simulation;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;

/**
 * Created by duame on 12/05/2017.
 */
public class StatVisualization extends JPanel implements IObserver {

    private Simulation simulation;
    private JFreeChart chart;
    private XYDataset dataset;
    private ChartPanel chartPanel;

    public StatVisualization(Simulation simulation){
        super();
        this.registerSimulation(simulation);

        this.dataset = this.createDataset();
        this.chart = this.createChart(dataset);
        this.chartPanel = new ChartPanel(chart);
        this.add(chartPanel);

    }

    private void registerSimulation(Simulation simulation){
        this.simulation = simulation;
        simulation.attach(this);
    }

    @Override
    public void update() {

    }

    private JFreeChart createChart( final XYDataset dataset ) {
        return ChartFactory.createTimeSeriesChart(
                "Mean reward of action per time step",
                "Time-step",
                "Reward",
                dataset,
                false,
                false,
                false);
    }

    private XYDataset createDataset( ) {
        TimeSeriesCollection toReturn = new TimeSeriesCollection();

        for (int i = 0; i < 2; i++){

            TimeSeries series = new TimeSeries( "Random Data" + i );
            Second current = new Second( );
            double value = 100.0;

            for (int j = 0; j < 4000; j++) {

                try {
                    value = value + Math.random( ) - 0.5;
                    series.add(current, new Double( value ) );
                    current = (Second) current.next( );
                } catch ( SeriesException e ) {
                    System.err.println("Error adding to series");
                }
            }
            toReturn.addSeries(series);
        }

        return toReturn;
    }
}
