import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;
import java.util.Iterator;
import java.util.LinkedList;


public class MyMap {
	private int scaleFactor;
	private Node startNode;
	private Node endNode;
	private int width;
	private int length;
	private int maxPrivate;
	private int maxConstruct;
	private Graph graph;

	
	/*
	 * This is the constructor for MyMap.
	 */
	public MyMap(String inputFile) throws MapException, GraphException {
		LinkedList<String> mapList = new LinkedList<String>();
		File mapFile;
		Scanner mapScanner;
		String tempMap;
		String tempRoadType;
		int mapListSize;
		int nodeCounter;
		
		//These temporary variables will hold the id's of the nodes so I can search for them later in the constructor
		int tempEndNode, tempStartNode;
		
		//Attempting to scan all the numbers in the file into variables and indices in the linked list.
		try {
			mapFile = new File(inputFile);
			mapScanner = new Scanner(mapFile);
			
			this.scaleFactor = mapScanner.nextInt();
			tempStartNode = mapScanner.nextInt();
			tempEndNode = mapScanner.nextInt();
			this.width = mapScanner.nextInt();
			this.length = mapScanner.nextInt();
			this.maxPrivate = mapScanner.nextInt();
			this.maxConstruct = mapScanner.nextInt();
			
			while (mapScanner.hasNextInt()) {
				mapList.add(mapScanner.nextLine());
			}
			mapScanner.close();
			mapListSize = mapList.size();
		}
		
		catch (FileNotFoundException e) {
			throw new MapException("Error. File DNE.");
		}
			
		//Creating a graph
		graph = new Graph(width * length);
		
		nodeCounter = -1;
		
		/*
		 * Connecting the horizontal edges first. nodeCounter is responsible for keeping track of the neighbors
		 * of each horizontal edge added to the graph.
		 * I first single out all the horizontal edges using 2 for loops and then I add them to the graph with
		 * their specified type (public, private, construction). If the edge is of type 'B', then I don't add the
		 * edge and continue to the next edge.
		 */
		for (int i = 0; i <= mapListSize; i += 2) {
			tempMap = mapList.get(i);
			nodeCounter ++;
			for (int j = 1; j < tempMap.length(); j += 2) {
				
				if (tempMap.charAt(j) == 'P') tempRoadType = "public";
				else if (tempMap.charAt(j) == 'V') tempRoadType = "private";
				else if (tempMap.charAt(j) == 'C') tempRoadType = "construction";
				else {
					nodeCounter ++;
					continue;
				}
				graph.addEdge(graph.getNode(nodeCounter), graph.getNode(nodeCounter + 1), tempRoadType);
				nodeCounter ++;
			}
		}
		nodeCounter = -1;
		
		//Connecting the vertical edges.
		for (int i = 1; i < mapListSize; i += 2) {
			tempMap = mapList.get(i);
			nodeCounter ++;
			for (int j = 0; j < tempMap.length(); j += 2) {
				if (tempMap.charAt(j) == 'P') tempRoadType = "public";
				else if (tempMap.charAt(j) == 'V') tempRoadType = "private";
				else if (tempMap.charAt(j) == 'C') tempRoadType = "construction";
				else {
					nodeCounter ++;
					continue;
				}
				graph.addEdge(graph.getNode(nodeCounter), graph.getNode(nodeCounter + width), tempRoadType);
				nodeCounter ++;
			}
		}
		
	}
	
	public Graph getGraph() {
		return graph;
	}
	
	public int getStartingNode() {
		return startNode.getId();
	}
	
	public int getDestinationNode() {
		return endNode.getId();
	}
	
	public int maxPrivateRoads() {
		return maxPrivate;
	}
	
	public int maxConstructionRoads() {
		return maxConstruct;
	}
	
	public Iterator<Node> findPath(int start, int destination, int maxPrivate, int maxConstruction) throws GraphException {
		Stack<Node> pathStack = new Stack<Node>();
		Edge currEdge;
		Node nextNode;
		
		//These variables hold the current number of construction/private roads being used for the path
		int currPrivateNum = 0, currConstructNum = 0;
		
		//Marking the start node. This is not pushed onto the stack
		graph.getNode(start).markNode(true);
		
		/*
		 * Continue looking for a path until the destination node has been reached.
		 * This algorithm first differentiates between the current node and the possible next node using
		 * an edge obtained by the iterator for the current node. It then determines if the possible next node
		 * has already been traversed, or if traversing through the edges between the 2 nodes will exceed the
		 * number of max private and/or max construction edges allowed. If true, then get the next edge in the
		 * iterator and the same process will be done. If false, then I will push the node onto the stack and
		 * the algorithm will run for the next node. Note that if the node being pushed onto the stack is traversed
		 * using a private or construction road, I will increment currPrivateNum or currConstructNum accordingly. The
		 * 2 variables keep track of the current number of private and construction edges traversed.
		 */
		while (!pathStack.peek().equals(graph.getNode(destination))) {
			Iterator<Edge> currEdges = graph.incidentEdges(pathStack.peek());
			if (currEdges.hasNext()) {
				currEdge = (Edge) currEdges.next();
				
				if (currEdge.firstNode().equals(pathStack.peek())) nextNode = currEdge.secondNode();
				else nextNode = currEdge.firstNode();
				
				if (nextNode.getMark() == true) continue;
				else if (currEdge.getType() == "private" && currPrivateNum == maxPrivate) continue;
				else if (currEdge.getType() == "construction" && currConstructNum == maxConstruct) continue;
				
				if (currEdge.getType() == "private") currPrivateNum ++;
				else if (currEdge.getType() == "construction") currConstructNum ++;
				
				pathStack.push(nextNode);

			}
			
			//If the path cannot go any further, backtrack.
			else {
				while (!pathStack.peek().equals(graph.getNode(start))) {
					nextNode = pathStack.pop();
					nextNode.markNode(false);
					
					//Checking if the edges connecting the removed node and the top node in the stack are private or construction
					//Decrementing currPrivateNum or currConstructNum if they are
					if (graph.getEdge(pathStack.peek(), nextNode).getType() == "private") currPrivateNum --;
					else if (graph.getEdge(pathStack.peek(), nextNode).getType() == "construction") currConstructNum --;
					
					//If there are incident edges for the node, break and traverse them using the above algorithm
					//Else continue to pop nodes off of the stack until a node with incident edges exists
					if (graph.incidentEdges(pathStack.peek()).hasNext() == true) break;
					else continue;
				}
				
				//Returning null if all possible routes have been traversed and no route exists to destination
				return null;
			}
		}
		return pathStack.iterator();
	}	
}
