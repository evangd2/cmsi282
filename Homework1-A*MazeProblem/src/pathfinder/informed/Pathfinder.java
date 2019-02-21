package pathfinder.informed;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Map;
import java.util.HashSet;

//(Group Members: Jenna Berlinberg, Donovan Evangelio)

/**
 * A Maze Pathfinding algorithm that implements A* graph search.
 */
public class Pathfinder {

	/**
	 * Given a MazeProblem, which specifies the actions and transitions available in
	 * the search, returns a solution to the problem as a sequence of actions that
	 * leads from the initial to a goal state.
	 * 
	 * @param problem A MazeProblem that specifies the maze, actions, transitions.
	 * @return An ArrayList of Strings representing actions that lead from the
	 *         initial to the goal state, of the format: ["R", "R", "L", ...]
	 */
	public static ArrayList<String> solve(MazeProblem problem) {
		
		MazeState target = problem.KEY_STATE;
		NodeComparator nodeComp = new NodeComparator(target);
		PriorityQueue<SearchTreeNode> frontier = new PriorityQueue<SearchTreeNode>(1, nodeComp);
		HashSet<MazeState> graveyard = new HashSet<MazeState>();

		frontier.add(new SearchTreeNode(problem.INITIAL_STATE, null, null, 0));
		SearchTreeNode keyNode = findPath(target, problem, frontier, graveyard);
		
		if (keyNode != null) {
			PriorityQueue<MazeState> goalNodes = new PriorityQueue<MazeState>(
					problem.GOAL_STATES.size(),
					(state1, state2) -> state1.getDistance(keyNode.state) - state2.getDistance(keyNode.state)
				);
			
			for (MazeState goal: problem.GOAL_STATES) {
				goalNodes.add(goal);
			}
			
			while (!goalNodes.isEmpty()) {
				frontier.clear();
				graveyard.clear();
				target = goalNodes.remove();
				nodeComp.target = target;
				frontier.add(keyNode);
				SearchTreeNode goalNode = findPath(target, problem, frontier, graveyard);
				if (goalNode != null) {
					return getSolution(goalNode);
				}
			}
		}
		return null;
	}
	
	/**
	 * Helper method to solve() that finds the optimal path from the top node in the
	 * input frontier (either the initial state node or the key node) to the input
	 * target in the given MazeProblem. Returns a SearchTreeNode containing the
	 * target MazeState if the path was found.
	 * 
	 * @param target    A MazeState containing the target coordinates
	 * @param problem   A MazeProblem that specifies the maze, actions, transitions.
	 * @param frontier  A PriorityQueue&lt;SearchTreeNode&gt; containing the unexpanded
	 *                  nodes at the current level.
	 * @param graveyard A HashSet&lt;MazeState&gt; used to memoize previously visited
	 *                  states in the problem.
	 * @return A SearchTreeNode containing the target MazeState, its generating
	 *         action and parent node, and the total historical cost to get to the
	 *         target.
	 */
	private static SearchTreeNode findPath(MazeState target, MazeProblem problem, PriorityQueue<SearchTreeNode> frontier, HashSet<MazeState> graveyard) {
		while (!frontier.isEmpty()) {
			SearchTreeNode node = frontier.remove();
			if (node.state.equals(target)) {
				return node;
			}
			graveyard.add(node.state);
			Map<String, MazeState> actions = problem.getTransitions(node.state);
			for (Map.Entry<String, MazeState> action : actions.entrySet()) {
				if (!graveyard.contains(action.getValue())) {
					frontier.add(new SearchTreeNode(action.getValue(), action.getKey(), node, node.pastCost + problem.getCost(action.getValue())));
				}
			}
		}
		return null;
	}

	/**
	 * Helper method to solve() that returns an ArrayList containing the optimal
	 * sequence of actions to get from the initial state in the MazeProblem to the
	 * input goal state.
	 * 
	 * @param goalNode A SearchTreeNode containing a goal state.
	 * @return An ArrayList of Strings representing actions that lead from the
	 *         initial to the goal state, of the format: ["R", "R", "L", ...]
	 */
	private static ArrayList<String> getSolution(SearchTreeNode goalNode) {
		ArrayList<String> sequence = new ArrayList<String>();
		SearchTreeNode current = goalNode;
		while (current.action != null) {
			// Prepending to the sequence gives the correct order at the end
			// since we begin traversal from the goal state.
			sequence.add(0, current.action);
			current = current.parent;
		}
		return sequence;
	}

}

/**
 * A Comparator used for ordering SearchTreeNodes by their evaluation function
 * ratings in a collection.
 */
class NodeComparator implements Comparator<SearchTreeNode> {
	
	MazeState target;
	
	/**
	 * Constructs a new NodeComparator with the given target state.
	 * @param target A MazeState containing the desired target state.
	 */
	NodeComparator(MazeState target) {
		this.target = target;
	}
	
	/**
	 * Comparator interface method that compares two SearchTreeNodes
	 * according to their evaluation function ratings.
	 * @param node1 A SearchTreeNode in a collection using this Comparator.
	 * @param node2 Another SearchTreeNode in a collection using this Comparator.
	 * @return An Integer reflecting the sort order of node1 and node2:
	 * 			-1 if node1's rating is less than node2,
	 * 			0 if the two nodes have equal ratings,
	 * 			1 if node1's rating is greater than node2.
	 */
	@Override
	public int compare(SearchTreeNode node1, SearchTreeNode node2) {
		return node1.getNodeRating(target) - node2.getNodeRating(target);
	}
}


/**
 * SearchTreeNode that is used in the Search algorithm to construct the Search
 * tree.
 */
class SearchTreeNode {

	MazeState state;
	String action;
	SearchTreeNode parent;
	int pastCost;

	/**
	 * Constructs a new SearchTreeNode to be used in the Search Tree.
	 * 
	 * @param state  The MazeState (col, row) that this node represents.
	 * @param action The action that *led to* this state / node.
	 * @param parent Reference to parent SearchTreeNode in the Search Tree.
	 * @param pastCost The total past cost of exploration, including the
	 * 					cost of entering this node's state.
	 */
	SearchTreeNode(MazeState state, String action, SearchTreeNode parent, int pastCost) {
		this.state = state;
		this.action = action;
		this.parent = parent;
		this.pastCost = pastCost;
	}
	
	/**
	 * Evaluation function used by the PriorityQueue&lt;SearchTreeNode&gt; frontier in
	 * solve() to rate potential nodes for expansion using the Manhattan distance
	 * heuristic.
	 * 
	 * @param target A MazeState containing the desired target state.
	 * @return An Integer rating for this SearchTreeNode to be used
 * 				in determining expansion order in solve(); 
	 */
	public int getNodeRating(MazeState target) {
    	//return pastCost + Manhattan distance to target
    	return this.pastCost + this.state.getDistance(target);
    }

}