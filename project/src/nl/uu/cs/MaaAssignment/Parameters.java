package nl.uu.cs.MaaAssignment;

import java.util.Arrays;

public class Parameters {
    private final int _numberOfAgents;
    private final int _numberOfActions;
    private final double _speed;
    private final double _collisionRadius;
    private final double _width;
    private final double _height;
    private final double _reward1;
    private final double _reward2;
    private final int _rounds;
    private final int _algorithmId;
    private final Object[] _algorithmParams;

    Parameters(String[] args) {
        this(Integer.parseInt(args[0]),
                Integer.parseInt(args[1]),
                Double.parseDouble(args[2]),
                Double.parseDouble(args[3]),
                Double.parseDouble(args[4]),
                Double.parseDouble(args[5]),
                Double.parseDouble(args[6]),
                Double.parseDouble(args[7]),
                Integer.parseInt(args[8]),
                Integer.parseInt(args[9]),
                Arrays.copyOfRange(args, 10, args.length));
    }

    Parameters(int numberOfAgents, int numberOfActions, double speed, double collisionRadius, double width, double height, double reward1, double reward2, int rounds, int algorithmId, Object[] algorithmParams) {
        _numberOfAgents = numberOfAgents;
        _numberOfActions = numberOfActions;
        _speed = speed;
        _collisionRadius = collisionRadius;
        _width = width;
        _height = height;
        _reward1 = reward1;
        _reward2 = reward2;
        _rounds = rounds;
        _algorithmId = algorithmId;
        _algorithmParams = algorithmParams;
    }

    public int getNumberOfAgents() {
        return _numberOfAgents;
    }

    public int getNumberOfActions() {
        return _numberOfActions;
    }

    public double getSpeed() {
        return _speed;
    }

    public double getCollisionRadius() {
        return _collisionRadius;
    }

    public double getWidth() {
        return _width;
    }

    public double getHeight() {
        return _height;
    }

    public double getReward1() {
        return _reward1;
    }

    public double getReward2() {
        return _reward2;
    }

    public int getRounds() {
        return _rounds;
    }

    public int getAlgorithmId() {
        return _algorithmId;
    }

    public Object[] getAlgorithmParams() {
        return _algorithmParams;
    }

    public String toString() {
        //TODO:
        return "tostring()";
    }
}
