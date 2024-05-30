import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class RatInMaze extends Canvas implements Runnable, MouseListener {
	private static Node start = null;
	private static Node target = null;
	private static JFrame frame;
	private static JFrame openFrame;
	private Node[][] nodeList;
	private static RatInMaze runTimeMain;
	private final static int WIDTH = 320;
	private final static int HEIGHT = 420;
	private static int GRID_SIZE;

	public static void main(String[] args) {
		frame = new JFrame("Maze Solver");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT);
		frame.setResizable(false);
		frame.setLayout(null);
		RatInMaze m = new RatInMaze();
		m.setBounds(0, 0, 300, 300);
		runTimeMain = m;
		frame.add(m);
		JButton runButton = new JButton();
		JButton clearButton = new JButton();
		clearButton.setText("CLEAR");
		clearButton.setBounds(160,  330, 100, 30);
		clearButton.setVisible(true);
		clearButton.addActionListener(arg0 -> runTimeMain.clearSearchResults());
		runButton.setText("RUN");
		runButton.setBounds(40,  330, 100, 30);
		runButton.setVisible(true);
		runButton.addActionListener(arg0 -> {
			Algorithm algorithm =  new Algorithm();
			algorithm.bfs(start, target, GRID_SIZE);
			if(target.isEnd()){
				JOptionPane.showMessageDialog(null, "No Solution Path...!", "Error", JOptionPane.ERROR_MESSAGE);
			}else{
				JOptionPane.showMessageDialog(null, "Path Found", "Congratulation", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		frame.add(runButton);
		frame.add(clearButton);

		openFrame = new JFrame("Start");
		openFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		openFrame.setSize(WIDTH, HEIGHT);
		openFrame.setResizable(false);
		openFrame.setLayout(null);
		JTextField textField = new  JTextField();
		textField.setBounds(50, 140, 200, 30);
		textField.setVisible(true);
		JButton startButton = new JButton();
		startButton.setText("Start Play");
		startButton.setBounds(100, 190, 100, 30);
		startButton.setVisible(true);
		startButton.addActionListener(arg0 -> {
			GRID_SIZE = Integer.parseInt(textField.getText());
			if(GRID_SIZE >= 2 && GRID_SIZE < 9){
				openFrame.dispose();
				frame.setVisible(true);
				m.startThread();
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				runTimeMain.drawMaze();
			}else{
				JOptionPane.showMessageDialog(null, "invalid Grid Size...!", "Error", JOptionPane.ERROR_MESSAGE);
			}

		});
		openFrame.add(textField);
		openFrame.add(startButton);
		openFrame.setVisible(true);
	}

	@Override
	public void run() {
		init();
		while (Thread.currentThread().isAlive()) {
			BufferStrategy bs = getBufferStrategy();
			if (bs == null) {
				createBufferStrategy(2);
				continue;
			}
			Graphics2D graph = (Graphics2D) bs.getDrawGraphics();
			renderMaze(graph);
			bs.show();
		}
	}

	public void init() {
		addMouseListener(this);
		nodeList = new Node[GRID_SIZE][GRID_SIZE];
		createNodes();
		setMazeDirections();
	}

	public void setMazeDirections() {
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				Node up = null,down = null,left = null,right = null;
				int u = j - 1;
				int d = j + 1;
				int l = i - 1;
				int r = i + 1;

				if(u >= 0) up = nodeList[i][u];
				if(d < GRID_SIZE) down =  nodeList[i][d];
				if(l >= 0) left = nodeList[l][j];
				if(r < GRID_SIZE) right =  nodeList[r][j];
				nodeList[i][j].setDirections(left, right, up, down);
			}
		}
	}

	public void createNodes() {
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				nodeList[i][j] = new Node(i, j).setX(15 + i * 35).setY(15 + j * 35);
				nodeList[i][j].clearNode();
			}
		}
	}

	public void drawMaze(){
		int[][] maze = new int[GRID_SIZE][GRID_SIZE];
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				if(i==0 && j == 0)
					maze[0][0] = 2;
				else if (i == GRID_SIZE-1 && j == GRID_SIZE-1)
					maze[GRID_SIZE-1][GRID_SIZE-1]=3;
				int nodeType = maze[i][j];
				switch (nodeType) {
					case 2 -> {
						nodeList[i][j].setColor(Color.GREEN);
						start = nodeList[i][j];
					}
					case 3 -> {
						nodeList[i][j].setColor(Color.RED);
						target = nodeList[i][j];
					}
				}
			}
		}
	}

	public void clearSearchResults() {
		for (int i = 0; i < nodeList.length; i++) {
			for (int j = 0; j < nodeList[i].length; j++) {
				if((i==0 && j == 0) || (i == GRID_SIZE-1 && j == GRID_SIZE-1))
					continue;
				if (nodeList[i][j].isSearched()) {
					nodeList[i][j].clearNode();
				}
			}
		}
		target.setColor(Color.RED);
		start.setColor(Color.GREEN);

	}

	public void renderMaze(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		for (Node[] nodes : nodeList) {
			for (Node node : nodes) {
				node.render(g);
			}
		}
	}

	public void startThread() {
		new Thread(this).start();
	}

	public void mousePressed(MouseEvent e) {
		Node clickedNode = getNodeAt(e.getX(), e.getY());
		if (clickedNode == null)
			return;
		if ((clickedNode.getX() == 0 && clickedNode.getY() == 0) || (clickedNode.getX() == GRID_SIZE - 1 && clickedNode.getY() == GRID_SIZE - 1))
			return;
		if (clickedNode.isWall()) {
			clickedNode.clearNode();
			return;
		}
		clickedNode.Clicked(e.getButton());
	}


	public Node getNodeAt(int x, int y) {
		x -= 15;
		x /= 35;
		y -= 15;
		y /= 35;
		System.out.println(x + ":" + y);
		if (x >= 0 && y >= 0 && x < nodeList.length && y < nodeList[x].length) {
			return nodeList[x][y];
		}
		return null;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}
}
