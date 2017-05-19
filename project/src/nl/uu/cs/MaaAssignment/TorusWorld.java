package nl.uu.cs.MaaAssignment;

import com.sun.javafx.geom.Vec2d;

import java.util.*;

public class TorusWorld
{

    // TODO ; - Dustin's Class desu
    // TODO : Visualization of World and Plot Mean reward of Action per Time Step
    // TODO : Requires connection to simulation for information, Observer Pattern?
    // TODO : Improve collision to consider movement over lines


    private final double _width;
    private final double _height;
    private final double _radius;
    private final double _maxMovingDistance;
    private final double _collisionDistance;

    private Set<Agent> _agents;

    public TorusWorld(double _width, double _height, double _radius)
    {
        this._width = _width;
        this._height = _height;
        this._radius = _radius;
        _maxMovingDistance = Math.min(_width, _height) / 2.0;
        _agents = new HashSet<Agent>();
        _collisionDistance = _radius * 2.0;
    }

    public boolean addAgent(Agent agent, double posX, double posY)
    {
        if(!collisionCheckPosition(agent, posX, posY, this._radius)) {
            agent.setPosition(posX, posY);
            _agents.add(agent);
            return true;
        }
        return false;
    }

    /**
     * Assumptions:
     * * Distance (x1, y1) and (x2, y2) is not bigger than the half of the smallest edge of the torus.
     * @param pointX Point Location
     * @param pointY Point Location
     * @param lineStartX Line Point 1
     * @param lineStartY Line Point 1
     * @param lineEndX Line Point 2
     * @param lineEndY Line Point 2
     * @return shortestDistanceBetweenLineAndPoint
     */
    public double shortestDistanceBetweenLineAndPoint(double pointX, double pointY, double lineStartX, double lineStartY, double lineEndX, double lineEndY)
    {
        double lineLength       = distanceOnTorus(lineStartX, lineStartY, lineEndX, lineEndY);
        double startToPointDistance = distanceOnTorus(pointX, pointY, lineStartX, lineStartY);
        double endToPointDistance   = distanceOnTorus(pointX, pointY, lineEndX, lineEndY);

        if(lineLength > _maxMovingDistance)
        {
            //Moving distance should be equal to speed.
            System.err.println("The distance (" + lineLength + ")is to big in comparison with the fieldsize(" + _width + "/"+ _height + "). Correct collision detection is not guaranteed!");
        }

        double perimeter = 0.5 * ( lineLength + startToPointDistance + endToPointDistance);
        double doubleTriangleSize = 2 * Math.sqrt(perimeter * (perimeter - lineLength) * (perimeter - startToPointDistance) * (perimeter - endToPointDistance));
        return doubleTriangleSize / lineLength;
    }

    public boolean moveAgent(Agent agent, double xVelocity, double yVelocity)
    {
        Vec2d newPosition = getNewPosition(agent.get_posX(), agent.get_posY(), xVelocity, yVelocity);

        if(!this.isColliding(agent, newPosition,agent.getRadius()*2))
        {
            agent.setPosition(newPosition.x, newPosition.y);
//            System.out.println("Setting new position for agent " + id);
            return true;
        }
        return false;
    }

    public boolean collisionCheckPosition(Agent currentAgent, double xPos, double yPos, double radius)
    {
        for (Agent otherAgent: _agents)
        {
            if(otherAgent.equals(currentAgent))
            {
                continue;
            }
            // Check collision by seeing if radii summed is smaller than the euclidean distance
            if (distanceOnTorus(xPos, yPos, otherAgent.get_posX(), otherAgent.get_posY()) <= _collisionDistance)
            {
                return true;
            }
        }
        return false;
    }

    public boolean isColliding(Agent currentAgent, Vec2d newPosition, double minDistance)
    {
        // Get the smallest distance between the line and the location of every other agent than requestAgent
        for(Agent otherAgent: _agents)
        {
            if(otherAgent.equals(currentAgent))
            {
                //ignore
                continue;
            }

            if(shortestDistanceBetweenLineAndPoint(otherAgent.get_posX(), otherAgent.get_posY(), currentAgent.get_posX(), currentAgent.get_posY(), newPosition.x, newPosition.y) <= minDistance)
            {
                return true;
            }
        }
        return false;
    }

    private double distanceOnTorus(double x1, double y1, double x2, double y2)
    {
        double dx = Math.pow(x1 - x2, 2);
        double ix = Math.pow(_width -  Math.max(x1, x2) + Math.min(x1, x2), 2);
        double dy = Math.pow(y1 - y2, 2);
        double iy = Math.pow(_height -  Math.max(y1, y2) + Math.min(y1, y2), 2);

        return Math.sqrt(Math.min(dx, ix) + Math.min(dy, iy));
    }

    public Vec2d getNewPosition(double xPos, double yPos, double xVelocity, double yVelocity)
    {
        double newX = xPos + xVelocity;
        double newY = yPos + yVelocity;

        Vec2d toReturn = new Vec2d();

        if(newX < 0)
            toReturn.x = newX + _width;
        else if(newX > _width)
            toReturn.x = newX - _width;
        else
            toReturn.x = newX;

        if(newY < 0)
            toReturn.y = newX + _height;
        if(newY > _width)
            toReturn.y = newX - _height;
        else
            toReturn.y = newY;

        return toReturn;
    }

}
