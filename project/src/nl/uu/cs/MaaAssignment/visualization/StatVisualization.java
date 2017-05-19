package nl.uu.cs.MaaAssignment.visualization;

import nl.uu.cs.MaaAssignment.Simulation;
import nl.uu.cs.MaaAssignment.StatisticsAggregator;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisSpace;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

/**
 * Created by duame on 12/05/2017.
 */
public class StatVisualization extends JPanel implements StatisticsAggregator.Processor
{
    private Simulation _simulation;
    private JFreeChart _chart;
    private ChartPanel _chartPanel;
    //private TimeSeriesCollection _timeSeries;
    private XYSeriesCollection _xySeries;

    public StatVisualization(Simulation simulation)
    {
        super();
        _simulation = simulation;
        StatisticsAggregator.addProcessor(this);
    }

    private void initPanel()
    {
        _chart = createChart(_xySeries);
        _chartPanel = new ChartPanel(_chart);
        add(_chartPanel);
    }

    private JFreeChart createChart( final XYDataset dataset ) {
//        final JFreeChart chart =  ChartFactory.createTimeSeriesChart(
//                "Mean reward of action per time step",
//                "Time-step",
//                "Reward",
//                dataset,
//                true,
//                false,
//                false);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Mean reward of action per time step",
                "Time-step",
                "Reward",
                dataset
        );
        chart.addSubtitle(new TextTitle(_simulation.getParameters().toString()));
        chart.getXYPlot().getDomainAxis().setVisible(false);
        chart.getXYPlot().getRangeAxis().setRange(0.0, 10.0);
        return chart;
    }

    @Override
    public void append(int round, double[] sum, double[] mean, double[] variance)
    {
       if(_xySeries == null)
        {
            //_timeSeries = new TimeSeriesCollection();
            _xySeries = new XYSeriesCollection();
            for(int i = 0; i<mean.length ;i++)
            {
                //_timeSeries.addSeries(new TimeSeries( "Action" + i ));
                _xySeries.addSeries(new XYSeries("Action" + i));
            }

            initPanel();
        }

        //RegularTimePeriod time = new Millisecond();

        for(int i = 0; i < mean.length ; i++)
        {
//            TimeSeries timeSeries = _timeSeries.getSeries(i);
//            timeSeries.add(time, mean[i]);
            XYSeries xySeries = _xySeries.getSeries(i);
            xySeries.add(xySeries.getItemCount(), mean[i]);
        }
        updateChart();
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
