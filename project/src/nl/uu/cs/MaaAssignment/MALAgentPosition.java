package nl.uu.cs.MaaAssignment;

import com.sun.javafx.geom.Vec2d;

/**
 * Created by Peter on 9-5-2017.
 */
public class MALAgentPosition
{
    private MALAgent _malAgent;

    private double _posX;
    private double _posY;


    public MALAgentPosition(MALAgent agent)
    {
        this(agent, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    public MALAgentPosition(MALAgent agent, double posX, double posY)
    {
        _malAgent = agent;
        setPosition(posX, posY);
    }

    public MALAgent getMALAgent()
    {
        return _malAgent;
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
}
