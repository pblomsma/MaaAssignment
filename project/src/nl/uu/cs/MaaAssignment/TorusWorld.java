package nl.uu.cs.MaaAssignment;

import com.sun.javafx.geom.Vec2d;

import java.util.HashMap;

public class TorusWorld
{

    // TODO ; - Dustin's Class desu
    // TODO : Visualization of World and Plot Mean reward of Action per Time Step
    // TODO : Requires connection to simulation for information, Observer Pattern?
    // TODO : Improve collision to consider movement over lines


    private double _width;
    private double _height;
    private double _radius;
    private final double _maxMovingDistance;

    private HashMap<Integer, Agent> MALAgents;

    public TorusWorld(double _width, double _height, double _radius)
    {
        this._width = _width;
        this._height = _height;
        this._radius = _radius;
        _maxMovingDistance = Math.min(_width, _height) / 2.0;
        this.MALAgents = new HashMap<Integer, Agent>();
    }

    public boolean addAgent(int id, Agent agent, double posX, double posY)
    {
        if(!collisionCheckPosition(id, posX, posY, this._radius)) {
            agent.setPosition(posX, posY);
            this.MALAgents.put(id, agent);
            return true;
        }
        return false;
    }

    public void teleportAgent(int id, double posX, double posY)
    {
        Agent position = this.MALAgents.get(id);
        position.setPosition(posX, posY);
    }

    /**
     * Assumptions:
     * * Distance (x1, y1) and (x2, y2) is not bigger than the half of the smallest edge of the torus.
     * @param x0 Point Location
     * @param y0 Point Location
     * @param x1 Line Point 1
     * @param y1 Line Point 1
     * @param x2 Line Point 2
     * @param y2 Line Point 2
     * @return shortestDistanceBetweenLineAndPoint
     */
    public double shortestDistanceBetweenLineAndPoint(double x0, double y0, double x1, double y1, double x2, double y2)
    {
        double movingDistance       = distanceOnTorus(x1, y1, x2, y2);
        double startToPointDistance = distanceOnTorus(x0, y0, x1, y1);
        double endToPointDistance   = distanceOnTorus(x0, y0, x2, y2);

        if(movingDistance > _maxMovingDistance)
        {
            System.err.println("The distance (" + movingDistance + ")is to big in comparison with the fieldsize(" + _width + "/"+ _height + "). Correct collision detection is not guaranteed!");
        }

        double perimeter = 0.5 * ( movingDistance + startToPointDistance + endToPointDistance);
        double doubleTriangleSize = 2 * Math.sqrt(perimeter * (perimeter - movingDistance) * (perimeter - startToPointDistance) * (perimeter - endToPointDistance));
        return doubleTriangleSize / movingDistance;
    }

    public boolean moveAgent(int id, double xVelocity, double yVelocity)
    {
        // Im choosing to do the collisionCheck here
        Agent position = this.MALAgents.get(id);
        Vec2d newPosition = getNewPosition(position.get_posX(), position.get_posY(), xVelocity, yVelocity);

        if(!this.collisionCheckLine(id, position.get_posX(), position.get_posY(), position.getRadius()*2, newPosition))
        {
            position.setPosition(newPosition.x, newPosition.y);
            System.out.println("Setting new position for agent " + id);
            return true;
        }
        return false;
    }

    public boolean collisionCheckPosition(int requestingAgent, double xPos, double yPos, double radius)
    {
        boolean collision = false;
        // Since all radii are the same now, making the checkDistance here
        double collisionDistance = radius*2;

        for (Agent position: MALAgents.values()) {
            // Check collision by seeing if radii summed is smaller than the euclidean distance
            if (position.getId() != requestingAgent && distanceOnTorus(xPos, yPos, position.get_posX(), position.get_posY()) <= collisionDistance) {
                collision = true;
                System.out.println("There is a collision!");
            }
        }
        return collision;
    }

    public boolean collisionCheckLine(int requestingAgent, double xPos, double yPos, double minDistance, Vec2d newPosition){
        boolean collision = false;

        // Get the smallest distance between the line and the location of every other agent than requestAgent
        for(Agent agent: this.MALAgents.values()){
            collision = (collision || this.shortestDistanceBetweenLineAndPoint(xPos, yPos, agent.get_posX(), agent.get_posY(), newPosition.x, newPosition.y) <= minDistance)
                    && agent.getId() != requestingAgent;
        }
        return collision;
    }

    private double distanceOnTorus(double x1, double y1, double x2, double y2)
    {
        double dx = Math.pow(x1 - x2, 2);
        double ix = Math.pow(_width -  Math.max(x1, x2) + Math.min(x1, x2), 2);
        double dy = Math.pow(y1 - y2, 2);
        double iy = Math.pow(_height -  Math.max(y1, y2) + Math.min(y1, y2), 2);

        return Math.sqrt(Math.min(dx, ix) + Math.min(dy, iy));
    }

    private Vec2d getNewPosition(double xPos, double yPos, double xVelocity, double yVelocity)
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


//    public boolean collisionCheckLine(int requestingAgent, double xPos, double yPos, double xVelocity, double yVelocity, double minDistance){
//        boolean collision = false;
//        // Get the smallest distance between the line and the location of every other agent than requestAgent
//        for(Agent agent: this.MALAgents.values()){
//            collision = (collision || this.distanceLinePointOnTorus(xPos, yPos, agent.get_posX(), agent.get_posY(), xVelocity, yVelocity) <= minDistance)
//                    && agent.getId() != requestingAgent;
//        }
//        return collision;
//    }
//    private double distanceLinePointOnTorus(double x1, double y1, double x2, double y2, double xVelocity, double yVelocity){
//        double distance = Double.POSITIVE_INFINITY;
//        double xIsPositive = xVelocity > 0? 1.0: -1.0;
//        double yIsPositive = yVelocity > 0? 1.0: -1.0;
//        // TODO: Fix this code, does not work at all! WIP
//        // Split the line up into line segments and check the distance for every segment, take the smallest one.
//        double xStart = x1;
//        for (double xEnd = xVelocity + x1; xIsPositive * xEnd >= x1; xEnd -= xIsPositive * _width) {
//            double yStart = y1;
//            for (double yEnd = yVelocity + y1; yIsPositive * yEnd >= y1; yEnd -= yIsPositive * _height) {
//                distance = Double.min(distance, this.distanceLineSegmentPoint(xStart, yStart, xEnd, yEnd, x2, y2));
//                yStart = yEnd;
//            }
//            xStart = xEnd;
//        }
//
//        return distance;
//    }
//
//    private double distanceLineSegmentPoint(double p1x, double p1y, double p2x, double p2y, double x, double y){
//        double distance = Double.POSITIVE_INFINITY;
//            // Check the distance of the line with the other agent for the plane and each of the 8 neighboring planes (projections), take the smallest distance.
//            for(int i = -1; i < 2; i++){
//            for(int j = -1; j<2; j++){
//                 distance = Double.min(distance, this.distanceLinePoint(p1x, p1y, p2x, p2y, x + i*_width,y+ j*_height));
//            }
//        }
//        return distance;
//    }
//
//    private double distanceLinePoint(double p1x, double p1y, double p2x, double p2y, double x, double y){
//        // Check the distance in Euclidean space
//        // The creation of line segments caused the line to be within the boundaries of a single projection of the torus
//        // The creation of the agent projections caused the point to be within those boundaries as well.
//        // Thus we can use Euclidean math.
//        double slope = -(p2y-p1y)/(p2x-p1x);
//        double b1 = p1y-slope*p1x;
//        // Find line (y=-slope*x+b2) perpendicular to the line defined by (px1, py1) and (px2, py2)
//        double b2 = y+slope*x;
//        double xIntersect = (b2 - b1)/(slope + slope);
//        double yIntersect = slope*xIntersect + b1;
//
//        // If the intersection point is not between p1 and p2, take the smallest distance to either point
//        // Else, return the distance between the point to the intersection point
//        double minx = Double.min(p1x, p2x);
//        double maxx = Double.max(p1x, p2x);
//        double miny = Double.min(p1y, p2y);
//        double maxy = Double.max(p1y, p2y);
//        if(xIntersect < maxx && xIntersect > minx && yIntersect < maxy && yIntersect > miny){
//            double distance = Math.pow(x-xIntersect, 2) + Math.pow(y-yIntersect, 2);
//            return Math.sqrt(distance);
//
//        } else {
//            double distanceP1 = Math.pow(p1x-xIntersect, 2) + Math.pow(p1y-yIntersect, 2);
//            double distanceP2 = Math.pow(p2x-xIntersect, 2) + Math.pow(p2y-yIntersect, 2);
//            return Math.sqrt(Double.min(distanceP1, distanceP2));
//        }
//    }
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
