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

    private final static String sDefaultExtension = ".jpg";

    public static void main(String[] args) {
        playingAround();
    }


    private static void firstImplementation(String[] args)
    {
//        Simulation simulation = new Simulation(
//                Integer.parseInt(args[0]),                      // number of agents
//                Integer.parseInt(args[1]),                      // number of actions
//                Double.parseDouble(args[2]),                    // speed
//                Double.parseDouble(args[3]),                    // collision radius
//                Double.parseDouble(args[4]),                    // width
//                Double.parseDouble(args[5]),                    // height
//                Double.parseDouble(args[6]),                    // reward 1
//                Double.parseDouble(args[7]),                    // reward 2
//                Integer.parseInt(args[8]),                      // rounds
//                Integer.parseInt(args[9]),                      // algorithm ID
//                Arrays.copyOfRange(args, 10, args.length)       // algorithm params
//        );


        int amountOfRuns = 10;

        int agents = 2;
        int actions = 3;
        double speed = 1;
        double collisionRadius = 1;
        double width = 1000;
        double height = 1000;
        double rewardOne = 1;
        double rewardTwo = -1;
        int roundAmount = 1;
        int algorithmID = 0;

        int agentChange = 1;
        int actionChange = 1;
        double speedChange = 1;
        double collisionRadiusChange = 1;
        double widthChange = 0;
        double heightChange = 0;
        double rewardOneChange = 1;
        double rewardTwoChange = 1;
        int roundAmountChange = 1;

        Object[] algorithmParams = {"0.4"};

        for (int i = 0; i < amountOfRuns; i++) {
            Simulation simulation = new Simulation(
                    agents + (agentChange * i),
                    actions + (actionChange * i),
                    speed + (speedChange * i),
                    collisionRadius + (collisionRadiusChange * i),
                    width + (widthChange * i),
                    height + (heightChange * i),
                    rewardOne + (rewardOneChange * i),
                    rewardTwo + (rewardTwoChange * i),
                    roundAmount + (roundAmountChange * i),
                    algorithmID,
                    algorithmParams
            );

            simulation.start();
            saveSimulation(simulation);
        }

    }

    private static void playingAround()
    {
        //Static:
        int agents = 10;
        int actions = 10;
        double speed = 3;
        double collisionRadius = 10;
        double width = 1000;
        double height = 1000;
        int roundAmount = 1000000;
        int algorithmID = 0;


        for(double rewardOne = 1; rewardOne < 10; rewardOne++)
        {
            for(double rewardTwo = 1; rewardTwo < 10; rewardTwo++)
            {
                for(double epsilon = 0.1; epsilon < 0.9; epsilon += 0.1)
                {
                    Simulation simulation = new Simulation(
                            agents ,
                            actions ,
                            speed ,
                            collisionRadius ,
                            width,
                            height,
                            rewardOne ,
                            rewardTwo ,
                            roundAmount,
                            algorithmID,
                            new Object[]{epsilon}
                    );

                    if(isAlreadySimulated(simulation.getParameters()))
                    {
                        continue;
                    }

                    long simulationStart = System.currentTimeMillis();
                    System.out.println("Starting simulation!");
                    simulation.start();
                    saveSimulation(simulation);
                    simulation.shutDown();
                    System.out.println("Simulation run took : " + (System.currentTimeMillis() - simulationStart) + " ms");
                }
            }
        }
    }

    public static void saveSimulation(Simulation simulation)
    {
        //Chart
        JFreeChart chart = simulation.getWindowFrame().getStatPanel().get_chart();

        try {
            File toSave = getFileName(simulation.getParameters(), ".jpg");
            if(toSave.createNewFile())
                ChartUtilities.saveChartAsJPEG( toSave, chart, 1920, 1080);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Csv
//        try {
//            File toSave = getFileName(simulation.getParameters(), ".csv");
//            if(toSave.createNewFile())
//                try(  PrintWriter out = new PrintWriter( toSave )  ){
//                    out.println( simulation.getCsv() );
//                }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }

    private static boolean isAlreadySimulated(Parameters parameters)
    {
        return getFileName(parameters).exists();
    }

    private static File getFileName(Parameters parameters)
    {
        return getFileName(parameters, sDefaultExtension);
    }

    private static File getFileName(Parameters parameters, String extension)
    {
        Path currentRelativePath = Paths.get("");
        File outputFolder = new File(currentRelativePath.toAbsolutePath() +  "/output");
        if(outputFolder.mkdir() && outputFolder.exists())
        {
            System.out.println("Folder for simulation output exists!");
        }

        return new File(outputFolder + "/" + getConfigurationName(parameters) + extension);
    }


    /**
     * Creates filename based on parameters, such that parametersets that are the same map to the same filename.
     */
    private static String getConfigurationName(Parameters parameters)
    {
        return "" + parameters.getAlgorithmId() + parameters.getReward1() + parameters.getReward2() + parameters.getRounds() +
                parameters.getCollisionRadius() + parameters.getSpeed() + parameters.getHeight() + parameters.getWidth() +
                parameters.getRounds() + parameters.getNumberOfActions() + parameters.getNumberOfAgents() +  parameters.getAlgorithmParams()[0];
    }
}
