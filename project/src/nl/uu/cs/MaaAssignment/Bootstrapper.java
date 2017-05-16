package nl.uu.cs.MaaAssignment;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by duame on 16/05/2017.
 */
public class Bootstrapper {

    public static void main(String[] args) {
        Simulation simulation = new Simulation(
                Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Double.parseDouble(args[2]),
                Double.parseDouble(args[3]),
                Double.parseDouble(args[4]),
                Double.parseDouble(args[5]),
                Double.parseDouble(args[6]),
                Double.parseDouble(args[7]),
                Integer.parseInt(args[8]),
                Integer.parseInt(args[9]),
                Arrays.copyOfRange(args, 10, args.length)
        );

        Path currentRelativePath = Paths.get("");
        File outputFolder = new File(currentRelativePath + "/simOutput");
        if(outputFolder.mkdir() && outputFolder.exists())
            System.out.println("Folder for simulation output exists!");


        Date timeStamp = Date.from(Instant.now());
        System.out.println("Current TimeStamp : " + timeStamp);

        System.out.println("Starting simulation!");
        simulation.start();

        JFreeChart chart = simulation.getWindowFrame().getStatPanel().get_chart();
        try {
            File toSave = new File(outputFolder, timeStamp.toInstant().getEpochSecond() + ".jpg");
            if(toSave.createNewFile())
                ChartUtilities.saveChartAsJPEG( toSave, chart, 1920, 1080);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
