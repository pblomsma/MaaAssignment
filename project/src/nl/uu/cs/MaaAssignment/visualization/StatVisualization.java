package nl.uu.cs.MaaAssignment.visualization;

import nl.uu.cs.MaaAssignment.Simulation;
import nl.uu.cs.MaaAssignment.StatisticsAggregator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.SeriesException;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.List;

/**
 * Created by duame on 12/05/2017.
 */
public class StatVisualization extends JPanel implements StatisticsAggregator.Processor {
    private Simulation _simulation;
    private JFreeChart _chart;
    private ChartPanel _chartPanel;

    public StatVisualization(Simulation simulation) {
        super();
        _simulation = simulation;
        _simulation.getStatisticsAggregator().addProcessor(this);
    }

    private void initPanel(XYSeriesCollection dataCollection) {
        _chart = createChart(dataCollection);
        _chartPanel = new ChartPanel(_chart);
        add(_chartPanel);
    }

    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Mean reward of action per time step",
                "Time-step",
                "Reward",
                dataset
        );
        chart.addSubtitle(new TextTitle(_simulation.getParameters().toString()));
        chart.getXYPlot().getDomainAxis().setVisible(false);
        chart.getXYPlot().getRangeAxis().setRange(0.0, 10.0);
        chart.getXYPlot().getRangeAxis().setAutoRange(true);
        return chart;
    }

    @Override
    public void finalize(List<Double>[] meansPerRoundPerAction)
    {
        XYSeriesCollection dataCollection = new XYSeriesCollection();

        for (int action = 0; action < meansPerRoundPerAction.length; action++) {
            XYSeries seriesForAction = new XYSeries("Action" + action);

            for (int round = 0; round < meansPerRoundPerAction[action].size(); round++) {
                seriesForAction.add(round, meansPerRoundPerAction[action].get(round));
            }
            dataCollection.addSeries(seriesForAction);
        }
        initPanel(dataCollection);
    }

    public JFreeChart get_chart() {
        return _chart;
    }

}
