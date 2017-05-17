package nl.uu.cs.MaaAssignment;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;

/**
 * Created by duame on 16/05/2017.
 */
public class Bootstrapper {

    public static void main(String[] args) {

//
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
                    agents + (agentChange *i),
                    actions + (actionChange *i),
                    speed + (speedChange *i),
                    collisionRadius + (collisionRadiusChange *i),
                    width + (widthChange *i),
                    height + (heightChange *i),
                    rewardOne + (rewardOneChange *i),
                    rewardTwo + (rewardTwoChange *i),
                    roundAmount + (roundAmountChange *i),
                    algorithmID,
                    algorithmParams
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
            System.out.println(chart);
            try {
                File toSave = new File(outputFolder, timeStamp.toInstant().getEpochSecond() + ".jpg");
                if(toSave.createNewFile())
                    ChartUtilities.saveChartAsJPEG( toSave, chart, 1920, 1080);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
