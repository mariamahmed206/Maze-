import java.awt.Color;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

public class Algorithm {
	
	private final int searchTime = 100;

	public void bfs(Node start, Node end, int graphSize) {
		Queue<Node> queue = new LinkedList<>();
		Node[][] prev = new Node[graphSize][graphSize];
		queue.add(start);
		while (!queue.isEmpty()) {
			Node curNode = queue.poll();
			if (curNode.isEnd()) {
				curNode.setColor(Color.MAGENTA);
				break;
			}
			if (!curNode.isSearched()) {
				curNode.setColor(Color.pink);
				try {
					TimeUnit.MILLISECONDS.sleep(searchTime);
				} catch (Exception e) {
					e.printStackTrace();
				}
				curNode.setColor(Color.BLUE);
				for (Node adjacent : curNode.getNeighbours()) {
					queue.add(adjacent);
					prev[adjacent.getX()][adjacent.getY()] = curNode;
				}
			}
		}
		shortPath(prev, end);
	}

	private void shortPath(Node[][] prev, Node end) {
		Node pathConstructor = end;
		while(pathConstructor != null) {
			pathConstructor = prev[pathConstructor.getX()][pathConstructor.getY()];
			if(pathConstructor != null) {
				pathConstructor.setColor(Color.pink);
			}
			try {
				TimeUnit.MILLISECONDS.sleep(searchTime);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
