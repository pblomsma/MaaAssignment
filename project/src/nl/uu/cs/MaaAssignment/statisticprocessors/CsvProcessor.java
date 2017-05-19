package nl.uu.cs.MaaAssignment.statisticprocessors;

import nl.uu.cs.MaaAssignment.StatisticsAggregator;

import java.util.List;

/**
 * Created by Peter on 13-5-2017.
 */
public class CsvProcessor implements StatisticsAggregator.Processor
{
    @Override
    public void append(int round, double[] sum, double[] mean, double[] variance)
    {
        for(int i = 0; i < sum.length; i++)
        {
            System.out.println(round + ";" + i + ";" + sum[i] + ";" + mean[i] + ";" + variance[i]);
        }
    }

    @Override
    public void finalize(List<double[]> sums, List<double[]> means, List<double[]> variances) {

    }
}
