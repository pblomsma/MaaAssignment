package nl.uu.cs.MaaAssignment;

/**
 * Created by Peter on 16-5-2017.
 */
public class CollisionDetectionTorus
{
    /**
     * Assumptions:
     * * Distannce (x1, y1) and (x2, y2) is not bigger than the half of the smallest edge of the torus.
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param torusWidth
     * @param torusHeight
     * @return
     */
    public static double distanceMovingAgent(double x0, double y0, double x1, double y1, double x2, double y2, double torusWidth, double torusHeight)
    {
        double movingDistance = shortestDistance(x1, y1, x2, y2, torusWidth, torusHeight);
        double startToPointDistance = shortestDistance(x0, y0, x1, y1, torusWidth, torusHeight);
        double endToPointDistance = shortestDistance(x0, y0, x2, y2, torusWidth, torusHeight);
        double perimeter = 0.5 * ( movingDistance + startToPointDistance + endToPointDistance);
        double doubleTriangleSize = 2 * Math.sqrt(perimeter * (perimeter - movingDistance) * (perimeter - startToPointDistance) * (perimeter - endToPointDistance));
        return doubleTriangleSize / movingDistance;
    }

    public static double shortestDistance(double x1, double y1, double x2, double y2, double torusWidth, double torusHeight)
    {
        double dx = Math.pow(x1 - x2, 2);
            double ix = Math.pow(torusWidth -  Math.max(x1, x2) + Math.min(x1, x2), 2);

            double dy = Math.pow(y1 - y2, 2);
            double iy = Math.pow(torusHeight -  Math.max(y1, y2) + Math.min(y1, y2), 2);

            return Math.sqrt(Math.min(dx, ix) + Math.min(dy, iy));
    }
}
