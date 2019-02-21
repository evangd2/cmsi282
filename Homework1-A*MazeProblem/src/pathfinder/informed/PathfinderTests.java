package pathfinder.informed;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

/**
 * Unit tests for Maze Pathfinder. Tests include completeness and
 * optimality.
 */
public class PathfinderTests {
    
    @Test
    public void testPathfinder_t0() {
        String[] maze = {
            "XXXXXXX",
            "XI...KX",
            "X.....X",
            "X.X.XGX",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);

        // result will be a 2-tuple (isSolution, cost) where
        // - isSolution = 0 if it is not, 1 if it is
        // - cost = numerical cost of proposed solution
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]); // Test that result is a solution
        assertEquals(6, result[1]); // Ensure that the solution is optimal
    }
	
    @Test
    public void testPathfinder_t1() {
        String[] maze = {
            "XXXXXXX",
            "XI....X",
            "X.MMM.X",
            "X.XKXGX",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]);  // Test that result is a solution
        assertEquals(14, result[1]); // Ensure that the solution is optimal
    }
    
    @Test
    public void testPathfinder_t2() {
        String[] maze = {
            "XXXXXXX",
            "XI.G..X",
            "X.MMMGX",
            "X.XKX.X",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]);  // Test that result is a solution
        assertEquals(10, result[1]); // Ensure that the solution is optimal
    }
    
    @Test
    public void testPathfinder_t3() {
        String[] maze = {
            "XXXXXXX",
            "XI.G..X",
            "X.MXMGX",
            "X.XKX.X",
            "XXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        assertNull(solution); // Ensure that Pathfinder knows when there's no solution
    }
    
    @Test
    public void testPathfinder_t4() {
        String[] maze = {
            "XXXXXXXX",
            "XIXGX..X",
            "X..X...X",
            "XK....GX",
            "XXXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        
        int[] result = prob.testSolution(solution);
        assertEquals(1, result[0]);  // Test that result is a solution
        assertEquals(7, result[1]); // Ensure that the solution is optimal
    }
    
    @Test
    public void testPathfinder_t5() { //THE LABYRINTH (just for fun)
        String[] maze = {
    		"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
    		"XI.X........X....................X",
    		"X..X...X....X..XXXXXXXXXXXXXXXXX.X",
    		"X..X...XXXXXX..XXXXXX.G..X..MMMX.X",
    		"XMMXMMMX..X..........XXXXX.XMMMX.X",
    		"X.........X..XXXXXXX.....X.XMMMX.X",
    		"X.........XXXX.....XXXXX.X.X.G.X.X",
    		"XXXXXXXXMMMMMX.MMM.X...X.X.XXXXX.X",
    		"X....X.......X.MKM.X.G.X.X.......X",
    		"X.G..X..M..M.X.MMM.X...X.XXXXXXXXX",
    		"X.MM.X..M..M.X.....XXXXX.X...X...X",
    		"X.MM.X..M..M.XMM..MMMMMX.X.X.X.X.X",
    		"X.MM.X...MM..XMM...MMMMX.X.X.X.X.X",
    		"X............XMMM..M.......X...X.X",
    		"XXXXXXXXXX...XXXXXXXXXXXXXXXXXXX.X",
    		"X.G.MM..M....M.X...XM..X...X...X.X",
    		"X...MM..X....X...X...X...XMMMX..MX",
    		"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
        };
        MazeProblem prob = new MazeProblem(maze);
        ArrayList<String> solution = Pathfinder.solve(prob);
        System.out.println(solution);
        int[] result = prob.testSolution(solution);
    }
    
    
}
