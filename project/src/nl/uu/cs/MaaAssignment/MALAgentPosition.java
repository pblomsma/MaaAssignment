package nl.uu.cs.MaaAssignment;

import com.sun.javafx.geom.Vec2d;

/**
 * Created by Peter on 9-5-2017.
 */
public class MALAgentPosition
{
    private MALAgent _MAL_agent;
    private double _posX;
    private double _posY;


    public MALAgentPosition(MALAgent agent)
    {
        this(agent, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    public MALAgentPosition(MALAgent agent, double posX, double posY)
    {
        this._MAL_agent = agent;
        this.setPosition(posX, posY);
    }

    public MALAgent get_MAL_agent()
    {
        return _MAL_agent;
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

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof MALAgentPosition)
        {
            return ((MALAgentPosition)obj)._MAL_agent.getId() == _MAL_agent.getId();
        }
        return super.equals(obj);
    }
}
