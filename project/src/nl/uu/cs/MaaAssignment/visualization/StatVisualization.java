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
public class StatVisualization extends JPanel implements StatisticsAggregator.Processor
{
    private Simulation _simulation;
    private JFreeChart _chart;
    private ChartPanel _chartPanel;
    private XYSeriesCollection _xySeries;

    public StatVisualization(Simulation simulation)
    {
        super();
        _simulation = simulation;
        _simulation.getStatisticsAggregator().addProcessor(this);
    }

    private void initPanel()
    {
        _chart = createChart(_xySeries);
        _chartPanel = new ChartPanel(_chart);
        add(_chartPanel);
    }

    private JFreeChart createChart( final XYDataset dataset ) {
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
    public void append(int round, double[] sum, double[] mean, double[] variance)
    {
       if(_xySeries == null)
        {
            _xySeries = new XYSeriesCollection();
            for(int i = 0; i<mean.length ;i++)
            {
                _xySeries.addSeries(new XYSeries("Action" + i));
            }
        }
        try {
            for(int i = 0; i < mean.length ; i++)
            {
                XYSeries xySeries = _xySeries.getSeries(i);
                xySeries.add(xySeries.getItemCount(), mean[i]);
            }
            updateChart();
       }
       catch (SeriesException exception)
       {
           //We're to fast. Try again.
           append(round, sum, mean, variance);
       }
    }

    @Override
    public void finalize(List<double[]> sums, List<double[]> means, List<double[]> variances)
    {
        if(_xySeries == null)
        {
            _xySeries = new XYSeriesCollection();
            for(int i = 0; i < means.get(0).length ;i++)
            {
                _xySeries.addSeries(new XYSeries("Action" + i));
            }
        }
        try {
            for(int i = 0; i < means.get(0).length ; i++)
                for(int j = 0; j < means.size(); j++)
                {
                    XYSeries xySeries = _xySeries.getSeries(i);
                    xySeries.add(xySeries.getItemCount(), means.get(j)[i]);
                }
        }
        catch (SeriesException exception)
        {
            //We're too fast. Try again.
            //append(round, sum, mean, variance);
            System.err.println("ERROR FINALIZING STATVISUALIZATION!");
            System.err.println(exception.getMessage());
        }
        initPanel();
    }

    public JFreeChart get_chart() {
        return _chart;
    }

    private void updateChart()
    {
//        _chart = createChart(_timeSeries);
//        _chartPanel = new ChartPanel(_chart);
//        add(_chartPanel);
    }
}
