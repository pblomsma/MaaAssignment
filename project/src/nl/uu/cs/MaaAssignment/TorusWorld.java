package nl.uu.cs.MaaAssignment;

import com.sun.javafx.geom.Vec2d;

import java.util.HashMap;

public class TorusWorld
{
    private double _width;
    private double _height;
    private double _radius;

    private HashMap<Integer, MALAgentPosition> MALAgents;

    public TorusWorld(double _width, double _height, double _radius)
    {
        this._width = _width;
        this._height = _height;
        this._radius = _radius;

        this.MALAgents = new HashMap<Integer, MALAgentPosition>();
    }

    public boolean addAgent(int id, MALAgent agent, double posX, double posY)
    {
        if(!collisionCheckPosition(posX, posY, this._radius)) {
            this.MALAgents.put(id, new MALAgentPosition(agent, posX, posY));
            return true;
        }
        return false;
    }

    public void teleportAgent(int id, double posX, double posY)
    {
        MALAgentPosition position = this.MALAgents.get(id);
        position.setPosition(posX, posY);
    }

    public boolean moveAgent(int id, double xVelocity, double yVelocity)
    {
        // Im choosing to do the collisionCheck here
        MALAgentPosition position = this.MALAgents.get(id);
        Vec2d newPosition = getNewPosition(position.get_posX(), position.get_posY(), xVelocity, yVelocity);

        if(!this.collisionCheckPosition(newPosition.x, newPosition.y, position.get_MAL_agent().getRadius()))
        {
            position.setPosition(newPosition.x, newPosition.y);
            return true;
        }
        return false;
    }

    public boolean collisionCheckPosition(double xPos, double yPos, double radius)
    {
        boolean collision = false;
        // Since all radii are the same now, making the checkDistance here
        double collisionDistance = radius*2;

        for (MALAgentPosition position: MALAgents.values()) {
            // Check collision by seeing if radii summed is smaller than the euclidean distance
            if (distanceOnTorus(xPos, yPos, position.get_posX(), position.get_posY()) >= collisionDistance)
                collision = true;
        }
        return collision;
    }

    private double distanceOnTorus(double x1, double y1, double x2, double y2)
    {
        double dx = Math.pow(x1 - x2, 2);
        double ix = Math.pow(_width -  Math.max(x1, x2) + Math.min(x1, x2), 2);

        double dy = Math.pow(y1 - y2, 2);
        double iy = Math.pow(_width -  Math.max(y1, y2) + Math.min(y1, y2), 2);

        return Math.sqrt(Math.min(dx, ix) + Math.min(dy, iy));

    }

    private Vec2d getNewPosition(double xPos, double yPos, double xVelocity, double yVelocity)
    {
        double newX = xPos + xVelocity;
        double newY = yPos + yVelocity;

        Vec2d toReturn = new Vec2d();

        if(newX < 0)
            toReturn.x = newX + _width;
        if(newX > _width)
            toReturn.x = newX - _width;

        if(newY < 0)
            toReturn.y = newX + _height;
        if(newY > _width)
            toReturn.y = newX - _height;

        return toReturn;
    }

//    public double euclideanDistance(Vec2d pos1, Vec2d pos2)
//    {
//        return Math.sqrt(Math.pow((pos1.x - pos2.x), 2) + Math.pow((pos1.y - pos2.y), 2 ));
//    }
//
//    public double euclideanDistance(double pos1X, double pos2X, double pos1Y, double pos2Y)
//    {
//        return Math.sqrt(Math.pow((pos1X - pos2X), 2) + Math.pow((pos1Y - pos2Y), 2 ));
//    }

}
