package view;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import algorithms.mazeGenerators.Position;

/**
 * @author Nir Leibovitch
 * <h1>Widget for displaying a maze, one 2D view plane at a time, in 2.5D</h1>
 */
public class Maze2dDisplayer extends MazeDisplayer {
	private final Color black;
	private final Color white;
	private final Color red;
	private final Color green;
	private final Color gray;
	private final Image winImage;
	
	/**
	 * Initialize the 2D maze displayer
	 * @param parent Parent of the widget
	 * @param style Style for the widget
	 */
	public Maze2dDisplayer(Composite parent, int style) {
		super(parent, style);
		
		Display display = getDisplay();
		black = new Color(display, 0, 0, 0);
		white = new Color(display, 255, 255, 255);
		red = new Color(display, 200, 50, 50);
		green = new Color(display, 50, 200, 50);
		gray = new Color(display, 200, 200, 200);
		winImage = new Image(display, "./resources/win.jpg");
		
		setBackground(white);
		
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if(maze == null)
					return;
				
				switch(viewPlane) {
				case "XZ":
					drawYCrossSection(e);
					break;
				case "XY":
					drawZCrossSection(e);
					break;
				case "ZY":
					drawXCrossSection(e);
					break;
				}
				
				double imageRatio = (double)winImage.getBounds().width / winImage.getBounds().height;
				double targetRatio = (double)getSize().x / getSize().y;
				int targetWidth;
				int targetHeight;
				if(imageRatio > targetRatio) {
					targetWidth = (int)(getSize().x*.5);
					targetHeight = (int)(targetWidth * imageRatio);
				} else {
					targetHeight = (int)(getSize().y*.5);
					targetWidth = (int)(targetHeight / imageRatio);
				}
				
				if(solved)
					e.gc.drawImage(winImage, 0, 0, winImage.getBounds().width, winImage.getBounds().height
							, (getSize().x - targetWidth)/2, (getSize().y - targetHeight)/2, targetWidth, targetHeight);
			}
		});
	}
	
	private void drawYCrossSection(PaintEvent e) {
		e.gc.setForeground(black);
		
		double height = (double)getSize().y / maze.getDepth();
		
		int y = characterPosition.getY();
		for(int z = 0; z < maze.getDepth(); ++z) {
			for(int x = 0; x < maze.getWidth(); ++x) {
				double backPerspective = 1 - (double)z/maze.getDepth();
				double frontPerspective = 1 - (double)(z+1)/maze.getDepth();
				
				double backWidth = (getSize().x - (getSize().x * backPerspective*.3)) / maze.getWidth();
				double frontWidth = (getSize().x - (getSize().x * frontPerspective*.3)) / maze.getWidth();
				
				double backX = getSize().x * backPerspective*.15 + x*backWidth;
				double backY = z * height;
				
				double frontX = getSize().x * frontPerspective*.15 + x*frontWidth;
				double frontY = backY + height;
				
				if(maze.isWall(x, y, z)) {
					e.gc.setBackground(black);
					// wall bottom
					e.gc.drawPolygon(new int[] { (int)backX, (int)backY, (int)frontX, (int)frontY
							, (int)(frontX + frontWidth), (int)frontY, (int)(backX + backWidth), (int)backY });
					
					e.gc.setBackground(gray);
					// wall left
					if(x-1 > 0 && maze.isPath(x-1, y, z))
						e.gc.fillPolygon(new int[] { (int)backX, (int)backY, (int)backX, (int)(backY - height/2)
								, (int)frontX, (int)(frontY - height/2), (int)frontX, (int)frontY });
					// wall right
					if(x+1 < maze.getWidth() && maze.isPath(x+1, y, z))
						e.gc.fillPolygon(new int[] { (int)(backX + backWidth), (int)backY, (int)(backX + backWidth), (int)(backY - height/2)
								, (int)(frontX + frontWidth), (int)(frontY - height/2), (int)(frontX + frontWidth), (int)frontY });
					// wall front
					e.gc.fillPolygon(new int[] { (int)frontX, (int)frontY, (int)frontX, (int)(frontY - height/2)
							, (int)(frontX + frontWidth), (int)(frontY - height/2), (int)(frontX + frontWidth), (int)frontY });
					
					e.gc.setBackground(black);
					// wall top
					e.gc.fillPolygon(new int[] { (int)backX, (int)(backY - height/2), (int)frontX, (int)(frontY - height/2)
							, (int)(frontX + frontWidth), (int)(frontY - height/2), (int)(backX + backWidth), (int)(backY - height/2) });
				} else {
					if(new Position(x, y, z).equals(maze.getGoalPosition())) {
						// goal
						e.gc.setBackground(green);
						e.gc.fillPolygon(new int[] { (int)backX, (int)backY, (int)frontX, (int)frontY
								, (int)(frontX + frontWidth), (int)frontY, (int)(backX + backWidth), (int)backY });
					}
					
					if(x == characterPosition.getX() && z == characterPosition.getZ()) {
						// character
						e.gc.setBackground(red);
						e.gc.fillOval((int)(frontX + frontWidth*.1), (int)(backY - height/2 + height*.1)
								, (int)(frontWidth*.8), (int)(height*1.5 - height*.2));
					}
					
					
					if((y < maze.getHeight() - 1 && maze.isPath(x, y+1, z))
							|| (y > 0 && maze.isPath(x, y-1, z))) {
						// arrow line
						e.gc.drawLine((int)(backX + backWidth/2), (int)(backY - height/4)
								, (int)(frontX + frontWidth/2), (int)(frontY - height/2));
						
						if(y < maze.getHeight() - 1 && maze.isPath(x, y+1, z)) {
							// up arrowhead
							e.gc.drawLine((int)(backX + backWidth/2), (int)(backY - height/4)
									, (int)(backX + backWidth/4 + (frontX-backX)/3), (int)(backY - height/4 + height/3));
							e.gc.drawLine((int)(backX + backWidth/2), (int)(backY - height/4)
									, (int)(backX + 3*backWidth/4 + (frontX-backX)/3), (int)(backY - height/4 + height/3));
						}
						
						if(y > 0 && maze.isPath(x, y-1, z)) {
							// down arrowhead
							e.gc.drawLine((int)(frontX + frontWidth/2), (int)(frontY - height/2)
									, (int)(frontX + frontWidth/4 + (backX-frontX)/3), (int)(frontY - height/2 - height/3));
							e.gc.drawLine((int)(frontX + frontWidth/2), (int)(frontY - height/2)
									, (int)(frontX + 3*frontWidth/4 + (backX-frontX)/3), (int)(frontY - height/2 - height/3));
						}
					}
				}
			}
		}
	}
	
	private void drawZCrossSection(PaintEvent e) {
		e.gc.setForeground(black);
		
		double width = (double)getSize().x / maze.getWidth();
		
		int z = characterPosition.getZ();
		for(int x = 0; x < maze.getWidth(); ++x) {
			double leftPerspective = 1 - (double)x/maze.getWidth();
			double rightPerspective = 1 - (double)(x+1)/maze.getWidth();
			
			double leftHeight = (getSize().y - (getSize().y * leftPerspective*.3)) / maze.getHeight();
			double rightHeight = (getSize().y - (getSize().y * rightPerspective*.3)) / maze.getHeight();
			
			for(int y = 0; y < maze.getHeight() ; ++y) {
				double leftX = x * width;
				double leftY = getSize().y - (getSize().y * leftPerspective*.15 + y*leftHeight);
				
				double rightX = leftX + width;
				double rightY = getSize().y - (getSize().y * rightPerspective*.15 + y*rightHeight);
			
				if(maze.isWall(x, y, z)) {
					e.gc.setBackground(gray);
					// wall front
					e.gc.drawPolygon(new int[] { (int)leftX, (int)leftY, (int)rightX, (int)rightY
							, (int)rightX, (int)(rightY - rightHeight), (int)leftX, (int)(leftY - leftHeight) });
					
					// wall top
					if(y < maze.getHeight() - 1 && maze.isPath(x, y+1, z))
						e.gc.fillPolygon(new int[] { (int)leftX, (int)(leftY - leftHeight), (int)rightX, (int)(rightY - rightHeight)
								, (int)(rightX - width/2), (int)(rightY - rightHeight), (int)(leftX - width/2), (int)(leftY - leftHeight) });
					
					// wall bottom
					if(y > 0 && maze.isPath(x, y-1, z))
						e.gc.fillPolygon(new int[] { (int)leftX, (int)leftY, (int)rightX, (int)rightY
								, (int)(rightX - width/2), (int)rightY, (int)(leftX - width/2), (int)leftY });
					
					// wall right
					e.gc.fillPolygon(new int[] { (int)rightX, (int)rightY, (int)rightX, (int)(rightY - rightHeight)
							, (int)(rightX - width/2), (int)(rightY - rightHeight), (int)(rightX - width/2), (int)rightY });
					
					e.gc.setBackground(black);
					// wall back
					e.gc.fillPolygon(new int[] { (int)(leftX - width/2), (int)leftY, (int)(rightX - width/2), (int)rightY
							, (int)(rightX - width/2), (int)(rightY - rightHeight), (int)(leftX - width/2), (int)(leftY - leftHeight) });
				} else {
					if(new Position(x, y, z).equals(maze.getGoalPosition())) {
						// goal
						e.gc.setBackground(green);
						e.gc.fillPolygon(new int[] { (int)leftX, (int)leftY, (int)rightX, (int)rightY
								, (int)rightX, (int)(rightY - rightHeight), (int)leftX, (int)(leftY - leftHeight) });
					}
					
					if(x == characterPosition.getX() && y == characterPosition.getY()) {
						// character
						e.gc.setBackground(red);
						e.gc.fillOval((int)(leftX - width/2 + width*.1), (int)(rightY - rightHeight*.9)
								, (int)(width*1.5 - width*.2), (int)(rightHeight*.8));
					}
					
					if((z < maze.getDepth() - 1 && maze.isPath(x, y, z+1))
							|| (z > 0 && maze.isPath(x, y, z-1))) {
						// arrow line
						e.gc.drawLine((int)(leftX - width/4), (int)(leftY - leftHeight/2)
								, (int)(rightX - width/2), (int)(rightY - rightHeight/2));
						
						if(z < maze.getDepth() - 1 && maze.isPath(x, y, z+1)) {
							// left arrowhead
							e.gc.drawLine((int)(leftX - width/4), (int)(leftY - leftHeight/2)
									, (int)(leftX - width/4 + width/3), (int)(leftY - leftHeight/4 - (leftY - rightY)/3));
							e.gc.drawLine((int)(leftX - width/4), (int)(leftY - leftHeight/2)
									, (int)(leftX - width/4 + width/3), (int)(leftY - 3*leftHeight/4 - (leftY - rightY)/3));
						}
						
						if(z > 0 && maze.isPath(x, y, z-1)) {
							// right arrowhead
							e.gc.drawLine((int)(rightX - width/2), (int)(rightY - rightHeight/2)
									, (int)(rightX - width/2 - width/3), (int)(leftY - leftHeight/4 - (leftY - rightY)/3));
							e.gc.drawLine((int)(rightX - width/2), (int)(rightY - rightHeight/2)
									, (int)(rightX - width/2 - width/3), (int)(leftY - 3*leftHeight/4 - (leftY - rightY)/3));
						}
					}
				}
			}
		}
	}
	
	private void drawXCrossSection(PaintEvent e) {
		e.gc.setForeground(black);
		
		double width = (double)getSize().x / maze.getDepth();
		
		int x = characterPosition.getX();
		for(int z = 0; z < maze.getDepth() ; ++z) {
			double rightPerspective = 1 - (double)z/maze.getDepth();
			double leftPerspective = 1 - (double)(z+1)/maze.getDepth();
			
			double leftHeight = (getSize().y - (getSize().y * leftPerspective*.3)) / maze.getHeight();
			double rightHeight = (getSize().y - (getSize().y * rightPerspective*.3)) / maze.getHeight();
			
			for(int y = 0; y < maze.getHeight() ; ++y) {
				double rightX = getSize().x - z * width;
				double rightY = getSize().y - (getSize().y * rightPerspective*.15 + y*rightHeight);
				
				double leftX = rightX - width;
				double leftY = getSize().y - (getSize().y * leftPerspective*.15 + y*leftHeight);
			
				if(maze.isWall(x, y, z)) {
					e.gc.setBackground(gray);
					// wall front
					e.gc.drawPolygon(new int[] { (int)leftX, (int)leftY, (int)rightX, (int)rightY
							, (int)rightX, (int)(rightY - rightHeight), (int)leftX, (int)(leftY - leftHeight) });
					
					// wall top
					if(y < maze.getHeight() - 1 && maze.isPath(x, y+1, z))
					e.gc.fillPolygon(new int[] { (int)leftX, (int)(leftY - leftHeight), (int)rightX, (int)(rightY - rightHeight)
							, (int)(rightX + width/2), (int)(rightY - rightHeight), (int)(leftX + width/2), (int)(leftY - leftHeight) });
					
					// wall bottom
					if(y > 0 && maze.isPath(x, y-1, z))
					e.gc.fillPolygon(new int[] { (int)leftX, (int)leftY, (int)rightX, (int)rightY
							, (int)(rightX + width/2), (int)rightY, (int)(leftX + width/2), (int)leftY });
					
					// wall left
					e.gc.fillPolygon(new int[] { (int)leftX, (int)leftY, (int)leftX, (int)(leftY - leftHeight)
							, (int)(leftX + width/2), (int)(leftY - leftHeight), (int)(leftX + width/2), (int)leftY });
					
					e.gc.setBackground(black);
					// wall back
					e.gc.fillPolygon(new int[] { (int)(leftX + width/2), (int)leftY, (int)(rightX + width/2), (int)rightY
							, (int)(rightX + width/2), (int)(rightY - rightHeight), (int)(leftX + width/2), (int)(leftY - leftHeight) });
				} else {
					if(new Position(x, y, z).equals(maze.getGoalPosition())) {
						// goal
						e.gc.setBackground(green);
						e.gc.fillPolygon(new int[] { (int)leftX, (int)leftY, (int)rightX, (int)rightY
								, (int)rightX, (int)(rightY - rightHeight), (int)leftX, (int)(leftY - leftHeight) });
					}
					
					if(new Position(x, y, z).equals(characterPosition)) {
						// character
						e.gc.setBackground(red);
						e.gc.fillOval((int)(leftX + width*.1), (int)(rightY - rightHeight*.9)
								, (int)(width*1.5 - width*.2), (int)(rightHeight*.8));
					}
					
					if((x < maze.getWidth() - 1 && maze.isPath(x+1, y, z))
							|| (x > 0 && maze.isPath(x-1, y, z))) {
						// arrow line
						e.gc.drawLine((int)(leftX + width/2), (int)(leftY - leftHeight/2)
								, (int)(rightX + width/4), (int)(rightY - rightHeight/2));
						
						if(x < maze.getWidth() - 1 && maze.isPath(x+1, y, z)) {
							// right arrowhead
							e.gc.drawLine((int)(rightX + width/4), (int)(rightY - rightHeight/2)
									, (int)(rightX + width/4 - width/3), (int)(leftY - leftHeight/4 - (leftY - rightY)/3));
							e.gc.drawLine((int)(rightX + width/4), (int)(rightY - rightHeight/2)
									, (int)(rightX + width/4 - width/3), (int)(leftY - 3*leftHeight/4 - (leftY - rightY)/3));
						}
						
						if(x > 0 && maze.isPath(x-1, y, z)) {
							// left arrowhead
							e.gc.drawLine((int)(leftX + width/2), (int)(leftY - leftHeight/2)
									, (int)(leftX + width/2 + width/3), (int)(leftY - leftHeight/4 - (leftY - rightY)/3));
							e.gc.drawLine((int)(leftX + width/2), (int)(leftY - leftHeight/2)
									, (int)(leftX + width/2 + width/3), (int)(leftY - 3*leftHeight/4 - (leftY - rightY)/3));
						}
					}
				}
			}
		}
	}
}
