
public class Edge {
	private Node endPointU;
	private Node endPointV;
	private String type;

	public Edge(Node u, Node v, String type) {
		this.endPointU = u;
		this.endPointV = v;
		this.type = type;
	}
	
	public Node firstNode() {
		return endPointU;
	}
	
	public Node secondNode() {
		return endPointV;
	}
	
	public String getType() {
		return type;
	}
}
