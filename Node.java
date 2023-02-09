
public class Node {
	private int id;
	private boolean isMarked;
	
	public Node(int id) {
		this.id = id;
	}
	
	public void markNode(boolean mark)  {
		this.isMarked = mark;
	}
	
	public boolean getMark() {
		return isMarked;
	}
	
	int getId() {
		return id;
	}

}
