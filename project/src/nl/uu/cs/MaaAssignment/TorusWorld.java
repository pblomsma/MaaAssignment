package nl.uu.cs.MaaAssignment;

import com.sun.javafx.geom.Vec2d;

import java.util.HashMap;

public class TorusWorld
{
    private double width;
    private double height;

    private HashMap<Integer, MALAgentPosition> MALAgents;

    public TorusWorld(double width, double height)
    {
        this.width = width;
        this.height = height;

        this.MALAgents = new HashMap<Integer, MALAgentPosition>();
    }

    public void addAgent(int id, MALAgent agent, double posX, double posY)
    {
        this.MALAgents.put(id, new MALAgentPosition(agent, posX, posY));
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
            if (euclideanDistance(xPos, position.get_posX(), yPos, position.get_posY()) >= collisionDistance)
                collision = true;
        }
        return collision;
    }

    public double euclideanDistance(Vec2d pos1, Vec2d pos2)
    {
        return Math.sqrt(Math.pow((pos1.x - pos2.x), 2) + Math.pow((pos1.y - pos2.y), 2 ));
    }

    public double euclideanDistance(double pos1X, double pos2X, double pos1Y, double pos2Y)
    {
        return Math.sqrt(Math.pow((pos1X - pos2X), 2) + Math.pow((pos1Y - pos2Y), 2 ));
    }

    private Vec2d getNewPosition(double xPos, double yPos, double xVelocity, double yVelocity)
    {
        double newX = xPos + xVelocity;
        double newY = yPos + yVelocity;

        Vec2d toReturn = new Vec2d();

        if(newX < 0)
            toReturn.x = newX + width;
        if(newX > width)
            toReturn.x = newX - width;

        if(newY < 0)
            toReturn.y = newX + height;
        if(newY > width)
            toReturn.y = newX - height;

        return toReturn;
    }

}
