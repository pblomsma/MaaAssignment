package nl.uu.cs.MaaAssignment;

import org.junit.Test;

import static org.junit.Assert.*;

public class CollisionDetectionTorusTest {
    @Test
    public void distanceMovingAgentPlaneHorizontal() throws Exception
    {
        //Below
        assertEquals(1, CollisionDetectionTorus.distanceMovingAgent(2,1,1,2,3,2,5,5), 0.01);

        //Above
        assertEquals(1, CollisionDetectionTorus.distanceMovingAgent(2,3,1,2,3,2,5,5), 0.01);
    }
    @Test
    public void distanceMovingAgentTorusHorizontal() throws Exception
    {
        //Below
        assertEquals(2, CollisionDetectionTorus.distanceMovingAgent(2,4,1,1,3,1,5,5), 0.01);

        //Above
        assertEquals(2, CollisionDetectionTorus.distanceMovingAgent(2,1,1,4,3,4,5,5), 0.01);
    }

    @Test
    public void distanceMovingAgentTorusHorizontalVar1() throws Exception
    {
        //Advanced one
        assertEquals(Math.sqrt(2), CollisionDetectionTorus.distanceMovingAgent(18   ,18,17,1,1,17,20,20), 0.01);
        assertEquals(Math.sqrt(2), CollisionDetectionTorus.distanceMovingAgent(19   ,19,19,1,1,19,20,20), 0.01);
    }

}