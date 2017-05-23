package nl.uu.cs.MaaAssignment;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by duame on 16/05/2017.
 */
public class Bootstrapper {

    private final static String sDefaultExtension = ".csv";

    public static void main(String[] args) throws IOException {
        playingAround();
    }

    private static void playingAround() throws IOException {
        //Static:
        int agents = 10;
        //int actions = 8;
        double speed = 3;

        double width = 50;
        double height = 50;
        int roundAmount = 20000000; //20 miljoen
        int algorithmID = 0;
        double rewardOne = 2;
        double rewardTwo = 1;
        double epsilon = 0.2;


        for (double collisionRadius = 1; collisionRadius < 2; collisionRadius++) {
            for (int actions = 2; actions < 11; actions++) {
                Parameters parameters = Simulation.createParameters(
                        agents,
                        actions,
                        speed,
                        collisionRadius,
                        width,
                        height,
                        rewardOne,
                        rewardTwo,
                        roundAmount,
                        algorithmID,
                        new Object[]{epsilon}
                );

                if (isAlreadySimulated(parameters)) {
                    continue;
                }

                File file = getFileName(parameters, ".csv");
                Simulation simulation = new Simulation(parameters, file);

                long simulationStart = System.currentTimeMillis();
                System.out.println("Starting simulation!");
                simulation.start();
                System.out.println("Simulation run took : " + (System.currentTimeMillis() - simulationStart) + " ms");
            }
        }
    }


    private static boolean isAlreadySimulated(Parameters parameters) {
        return getFileName(parameters).exists();
    }

    private static File getFileName(Parameters parameters) {
        return getFileName(parameters, sDefaultExtension);
    }

    private static File getFileName(Parameters parameters, String extension) {
        Path currentRelativePath = Paths.get("");
        File outputFolder = new File(currentRelativePath.toAbsolutePath() + "/output/action_space_specific");
        if (outputFolder.mkdir() && outputFolder.exists()) {
            System.out.println("Folder for simulation output exists!");
        }

        return new File(outputFolder + "/" + getConfigurationName(parameters) + extension);
    }


    /**
     * Creates filename based on parameters, such that parametersets that are the same map to the same filename.
     */
    private static String getConfigurationName(Parameters parameters) {
        return "" + parameters.getCollisionRadius() + parameters.getNumberOfActions();
    }
}
