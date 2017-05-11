package nl.uu.cs.MaaAssignment;

import com.sun.javafx.geom.Vec2d;
import nl.uu.cs.MaaAssignment.algorithms.Algorithm;


/**
 * Structure to keep all agent specific info tied together.
 */
public class Agent
{
    private double _posX;
    private double _posY;
    private final Algorithm _algorithm;
    private final int _id;
    private final double _radius;


    public Agent(Algorithm algorithm, int id, double radius)
    {
        this(algorithm, id, radius, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    public Agent(Algorithm algorithm, int id, double radius, double posX, double posY)
    {
        _id = id;
        _algorithm = algorithm;
        _radius = radius;
        setPosition(posX, posY);
    }

    public double get_posX() {
        return _posX;
    }

    public double get_posY() {
        return _posY;
    }

    public Vec2d getPosition()
    {
        return new Vec2d(_posX, _posY);
    }

    public void setPosition(double posX, double posY)
    {
        this._posX = posX;
        this._posY = posY;
    }

    public int nextAction(int round) {
        return _algorithm.nextAction(round);
    }

    public void reward(double reward, int round) {
        _algorithm.reward(reward, round);
    }

    public double getRadius()
    { return _radius;   }

    public int getId()
    {
        return _id;
    }
}
