import java.util.Map;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * Group Members: Jenna Berlinberg, Donovan Evangelio
 *
 * Artificial Intelligence responsible for playing the game of Nim!
 * Implements the alpha-beta-pruning mini-max search algorithm
 */
public class NimPlayer {

    private final int MAX_REMOVAL;
    /**
     * Maps visited game states to the best action available to
     * the AI at each state. Used to avoid searching through
     * memoized GameTreeNodes to find repeated game states.
     */
    private HashMap<Integer, Integer> actionTable;

    NimPlayer (int MAX_REMOVAL) {
        this.MAX_REMOVAL = MAX_REMOVAL;
        this.actionTable = new HashMap<Integer, Integer>();
    }

    /**
     * Given an input game state, returns the best action to take against an optimal
     * opponent.
     * @param   remaining   Integer representing the amount of stones left in the pile
     * @return  An int action representing the number of stones to remove in the range
     *          of [1, MAX_REMOVAL]
     */
    public int choose (int remaining) {
    	// Check if input game state has been computed before
    	/* Note: Since alphaBetaMinimax() expands child game states in
    	 * ascending order of stone removal (beginning with taking 1 stone),
    	 * the HashMap used for game graph memoization will contain a node
    	 * for EVERY possible game state. By the way actionTable is filled
    	 * later on, it will either contain the best action for any remaining
    	 * value of stones or be empty.
    	 */
    	if (actionTable.containsKey(remaining)) {
    		return actionTable.get(remaining);
    	}
    	// Otherwise, conduct Minimax search and fill game state table
    	else {
    		GameTreeNode root = new GameTreeNode(remaining, 0, true);
            HashMap<GameTreeNode, Integer> visited = new HashMap<GameTreeNode, Integer>();
    		alphaBetaMinimax(root, 0, 1, true, visited);

    		int bestAction = root.children.peek().action;
			for (GameTreeNode node: visited.keySet()) {
    			if (node.isMax &&
					node.remaining < remaining - bestAction &&
					!node.children.isEmpty()) {
    				actionTable.put(node.remaining, node.children.peek().action);
    			}
    		}
    		return bestAction;
    	}
    }

    /**
     * Constructs the minimax game tree by the tenets of alpha-beta pruning with
     * memoization for repeated states.
     * @param   node    The root of the current game sub-tree
     * @param   alpha   Smallest minimax score possible
     * @param   beta    Largest minimax score possible
     * @param   isMax   Boolean representing whether the given node is a max (true) or min (false) node
     * @param   visited Map of GameTreeNodes to their minimax scores to avoid repeating large subtrees
     * @return  Minimax score of the given node + [Side effect] constructs the game tree originating
     *          from the given node
     */
    private int alphaBetaMinimax (GameTreeNode node, int alpha, int beta, boolean isMax, Map<GameTreeNode, Integer> visited) {
    	// If node has been visited before, return its score
    	if (visited.containsKey(node)) {
    		node.score = visited.get(node);
    	}
    	// If node is a terminal node, return its utility score
    	else if (node.remaining == 0) {
    		node.score = (isMax) ? 0 : 1;
    		visited.put(node, node.score);
    	}
    	// Otherwise, expand with pruning
    	else {
    		int v;
        	int largestRemoval = node.remaining > MAX_REMOVAL ? MAX_REMOVAL : node.remaining;
        	if (isMax) {
        		v = 0;
        		for (int k = 1; k <= largestRemoval; k++) {
        			if (beta <= alpha) {break;}
        			else {
        				GameTreeNode newChild = new GameTreeNode(node.remaining - k, k, false);
        				v = Math.max(v, alphaBetaMinimax(newChild, alpha, beta, false, visited));
        				alpha = Math.max(alpha, v);
        				node.children.add(newChild);
        			}
        		}
        	}
        	else {
        		v = 1;
        		for (int k = 1; k <= largestRemoval; k++) {
        			if (beta <= alpha) {break;}
        			else {
        				GameTreeNode newChild = new GameTreeNode(node.remaining - k, k, true);
        				v = Math.min(v, alphaBetaMinimax(newChild, alpha, beta, true, visited));
        				beta = Math.min(beta, v);
        				node.children.add(newChild);
        			}
        		}
        	}
            node.score = v;
            visited.put(node, v);
    	}
    	return node.score;
    }

}

/**
 * GameTreeNode to manage the Nim game tree.
 */
class GameTreeNode {

    int remaining, action, score;
    boolean isMax;
    // MODIFICATION: Changed children from ArrayList to PriorityQueue
    // to allow for quick access of the node with the highest/lowest
    // Minimax score.
    PriorityQueue<GameTreeNode> children;

    /**
     * Constructs a new GameTreeNode with the given number of stones
     * remaining in the pile, and the action that led to it. We also
     * initialize an empty ArrayList of children that can be added-to
     * during search, and a placeholder score of -1 to be updated during
     * search.
     *
     * @param   remaining   The Nim game state represented by this node: the #
     *          of stones remaining in the pile
     * @param   action  The action (# of stones removed) that led to this node
     * @param   isMax   Boolean as to whether or not this is a maxnode
     */
    GameTreeNode (int remaining, int action, boolean isMax) {
        this.remaining = remaining;
        this.action = action;
        this.isMax = isMax;
        children = new PriorityQueue<GameTreeNode>(1,
        		(node1, node2) -> (isMax) ? (node2.score - node1.score) : (node1.score - node2.score)
        		);
        score = -1;
    }

    @Override
    public boolean equals (Object other) {
        return other instanceof GameTreeNode
            ? remaining == ((GameTreeNode) other).remaining &&
              isMax == ((GameTreeNode) other).isMax &&
              action == ((GameTreeNode) other).action
            : false;
    }

    @Override
    public int hashCode () {
        return remaining + ((isMax) ? 1 : 0);
    }


}
