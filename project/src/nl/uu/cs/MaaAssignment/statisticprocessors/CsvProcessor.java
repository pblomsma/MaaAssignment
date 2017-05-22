package nl.uu.cs.MaaAssignment.statisticprocessors;

import nl.uu.cs.MaaAssignment.StatisticsAggregator;
import java.util.List;



public class CsvProcessor implements StatisticsAggregator.Processor
{
    private final StringBuilder _csvBuilder;

    public CsvProcessor()
    {
        _csvBuilder = new StringBuilder();
    }


    @Override
    public void finalize(List<Double>[] meansPerRoundPerAction)
    {
        //Print headers
        _csvBuilder.append("round;");
        for (int action = 0; action < meansPerRoundPerAction.length; action++) {
            {
                _csvBuilder.append("Mean Action " + action + ";");
            }
        }

        //Print data
        for (int round = 0; round < meansPerRoundPerAction[0].size(); round++)
        {
            _csvBuilder.append(System.getProperty("line.separator"));
            _csvBuilder.append(round + ";");

            for (int action = 0; action < meansPerRoundPerAction.length; action++) {
                    {
                        _csvBuilder.append(meansPerRoundPerAction[action].get(round) + ";");
                    }
            }
        }
    }

    public String getCsvString()
    {
        return _csvBuilder.toString();
    }
}