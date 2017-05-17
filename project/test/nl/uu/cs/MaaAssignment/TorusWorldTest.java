package nl.uu.cs.MaaAssignment;

import com.sun.javafx.geom.Vec2d;
import org.junit.Test;

import static org.junit.Assert.*;

public class TorusWorldTest
{

    @Test
    public void testNewPosition() throws Exception {
        TorusWorld world = new TorusWorld(20, 20, 0);

        //Horizontal
        Vec2d newPosition = world.getNewPosition(0,0,5,0);
        assertEquals(5, newPosition.x, 0.0);
        assertEquals(0, newPosition.y, 0.0);

        //Vertical
        newPosition = world.getNewPosition(0,0,0,5);
        assertEquals(0, newPosition.x, 0.0);
        assertEquals(5, newPosition.y, 0.0);

        //Diagonaal
        newPosition = world.getNewPosition(0,0,5,5);
        assertEquals(5, newPosition.x, 0.0);
        assertEquals(5, newPosition.y, 0.0);
    }
}