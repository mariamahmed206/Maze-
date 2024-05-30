import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Graphics2D;

public class Node {
	private int xPos;
	private int yPos;
	private Color nodeColor = Color.LIGHT_GRAY;
	private final int WIDTH = 35;
	private final int HEIGHT = 35;
	private Node left, right, up, down;

	public Node(int x, int y) {
		xPos = x;
		yPos = y;
	}

	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.drawRect(xPos, yPos, WIDTH, HEIGHT);
		g.setColor(nodeColor);
		g.fillRect(xPos + 1, yPos + 1, WIDTH - 1, HEIGHT - 1);
	}

	public void Clicked(int buttonCode) {
		System.out.println("called");
		switch (buttonCode) {
			case 1 -> nodeColor = Color.BLACK;
			case 4 -> clearNode();
		}
	}
	public void setColor(Color c) {
		nodeColor = c;
	}

	public List<Node> getNeighbours() {
		List<Node> neighbours = new ArrayList<>();
		if (left != null && left.isPath())
			neighbours.add(left);
		if (down != null && down.isPath())
			neighbours.add(down);
		if (right != null && right.isPath())
			neighbours.add(right);
		if (up != null && up.isPath())
			neighbours.add(up);

		return neighbours;
	}

	public void setDirections(Node l, Node r, Node u, Node d) {
		left = l;
		right = r;
		up = u;
		down = d;
	}

	public void clearNode() {
		nodeColor = Color.LIGHT_GRAY;
	}

	public int getX() {
		return (xPos - 15) / WIDTH;
	}

	public int getY() {
		return (yPos - 15) / HEIGHT;
	}

	public Node setX(int x) {
		xPos = x;
		return this;
	}

	public Node setY(int y) {
		yPos = y;
		return this;
	}

	public boolean isWall() {
		return (nodeColor == Color.BLACK);
	}

	public boolean isEnd() {
		return (nodeColor == Color.RED);
	}

	public boolean isPath() {
		return (nodeColor == Color.LIGHT_GRAY || nodeColor == Color.RED);
	}

	public boolean isSearched() {
		return (nodeColor == Color.BLUE || nodeColor == Color.ORANGE);
	}

}
