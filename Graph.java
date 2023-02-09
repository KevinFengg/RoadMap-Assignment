import java.util.Iterator;
import java.util.Stack;

public class Graph implements GraphADT{
	private Edge[][] edgeMatrix;
	private Node[] nodeMatrix;

	public Graph(int n) {
		this.edgeMatrix = new Edge[n][n];
		this.nodeMatrix = new Node[n];

		//Creating new node objects and assigning them each a unique id. The id's also correspond to their indices.
		for (int i = 0; i < n; i ++) {
			this.nodeMatrix[i] = new Node(i);
		}
	}

	public void addEdge(Node u, Node v, String edgeType) throws GraphException {
		
		//Checking if both nodes exist in the graph
		if (u.getId() > nodeMatrix.length - 1 || u.getId() < 0 || v.getId() > nodeMatrix.length - 1 || v.getId() < 0) throw new GraphException("Error. Node u or v DNE.");
		
		//Checking if the edge already exists in the graph
		else if (edgeMatrix[u.getId()][v.getId()] != null) throw new GraphException("Error. Edge already exists.");
		
		//Creating new edges. 2 are made because the adjacency matrix is undirected.
		else {
			edgeMatrix[u.getId()][v.getId()] = new Edge(u, v, edgeType);
			edgeMatrix[v.getId()][u.getId()] = new Edge(u, v, edgeType);
		}
		
	}

	public Node getNode(int id) throws GraphException {
		
		if (id > nodeMatrix.length - 1 || id < 0) throw new GraphException("Error. Node with that id DNE.");
		else return nodeMatrix[id];
	}

	public Iterator<Edge> incidentEdges(Node u) throws GraphException {
		
		//This stack will hold all the edges incident on the parameter node u
		Stack<Edge> edgeStack = new Stack<Edge>();
		
		//Checking if u is a node and throwing GraphException if it isn't
		if (u.getId() > nodeMatrix.length - 1 || u.getId() < 0) throw new GraphException("Error. Node u DNE");
		
		//Looking for all existing edges incident to u and returning an iterator for them.
		//If the node does not have any edges incident on it, I return null;
		for (int i = 0; i < nodeMatrix.length; i ++) {
			if (edgeMatrix[u.getId()][i] != null) {
				edgeStack.push(edgeMatrix[u.getId()][i]);
			}
		}
		if (edgeStack.isEmpty() == true) return null;
		else return edgeStack.iterator();
	}

	public Edge getEdge(Node u, Node v) throws GraphException {
		
		//Trying to return the edge at edgeMatrix[u.getid()][v.getid()]. If an exception occurs, a GraphException is thrown.
		try {
			return edgeMatrix[u.getId()][v.getId()];
		}
		catch (Exception e) {
			throw new GraphException("Error. u and/or v DNE or the edge connecting them DNE.");
		}
	}

	public boolean areAdjacent(Node u, Node v) throws GraphException {
		
		//Checking if both nodes exist in the adjacency matrix and also if edgeMatrix at that index is null (Meaning that the edge connecting u and v DNE).
		if (u.getId() > nodeMatrix.length - 1 || u.getId() < 0 || v.getId() > nodeMatrix.length - 1 || v.getId() < 0) throw new GraphException("Error. Node u or v DNE.");
		else if (edgeMatrix[u.getId()][v.getId()] == null) return false;
		else return true;
	}
}
