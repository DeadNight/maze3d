package view;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import algorithms.mazeGenerators.Position;

public class Maze2dDisplayer extends MazeDisplayer {
	public Maze2dDisplayer(Composite parent, int style) {
		super(parent, style);
		
		Display display = getDisplay();
		Color black = new Color(display, 0, 0, 0);
		Color white = new Color(display, 255, 255, 255);
		Color red = new Color(display, 200, 50, 50);
		Color green = new Color(display, 50, 200, 50);
		Color gray = new Color(display, 200, 200, 200);
		
		setBackground(white);
		
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if(maze == null)
					return;
				
				e.gc.setBackground(black);
				e.gc.setForeground(black);
				
				double height = (double)getSize().y / maze.getDepth();
				
				int y = characterPosition.getY();
				for(int z = 0; z < maze.getDepth(); ++z) {
					for(int x = 0; x < maze.getWidth(); ++x) {
						double backPerspective = (1 - ((double)z/(maze.getDepth())));
						double frontPerspective = (1 - ((double)(z+1)/(maze.getDepth())));
						
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
						} else if(new Position(x, y, z).equals(maze.getGoalPosition())) {
							e.gc.setBackground(green);
							e.gc.fillPolygon(new int[] { (int)backX, (int)backY, (int)frontX, (int)frontY
									, (int)(frontX + frontWidth), (int)frontY, (int)(backX + backWidth), (int)backY });
						}
						
						// character
						if(x == characterPosition.getX() && z == characterPosition.getZ()) {
							e.gc.setBackground(red);
							e.gc.fillOval((int)(frontX + frontWidth*.1), (int)(backY - height/2 + height*.1)
									, (int)(frontWidth*.8), (int)(height*1.5 - height*.2));
						}
						
						if(maze.isPath(x, y, z)) {
							// arrow line
							if((y < maze.getHeight() - 1 && maze.isPath(x, y+1, z))
									|| (y > 0 && maze.isPath(x, y-1, z))) {
								e.gc.drawLine((int)(backX + backWidth/2), (int)(backY - height/4),
										(int)(frontX + frontWidth/2), (int)(frontY - height/2));
							}
							
							// up arrowhead
							if(y < maze.getHeight() - 1 && maze.isPath(x, y+1, z)) {
								e.gc.drawLine((int)(backX + backWidth/2), (int)(backY - height/4),
										(int)(backX + backWidth/4 + (frontX-backX)/3), (int)(backY - height/4 + height/3));
								e.gc.drawLine((int)(backX + backWidth/2), (int)(backY - height/4),
										(int)(backX + 3*backWidth/4 + (frontX-backX)/3), (int)(backY - height/4 + height/3));
							}
							
							// down arrowhead
							if(y > 0 && maze.isPath(x, y-1, z)) {
								e.gc.drawLine((int)(frontX + frontWidth/2), (int)(frontY - height/2),
										(int)(frontX + frontWidth/4 + (backX-frontX)/3), (int)(frontY - height/2 - height/3));
								e.gc.drawLine((int)(frontX + frontWidth/2), (int)(frontY - height/2),
										(int)(frontX + 3*frontWidth/4 + (backX-frontX)/3), (int)(frontY - height/2 - height/3));
							}
						}
					}
				}
			}
		});
	}
}
