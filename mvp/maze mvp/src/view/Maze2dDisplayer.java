package view;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;


public class Maze2dDisplayer extends MazeDisplayer {
	public Maze2dDisplayer(Composite parent, int style) {
		super(parent, style);
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				if(maze == null)
					return;
				
				e.gc.setForeground(new Color(getDisplay(), 0, 0, 0));
				
				int width = getSize().x / maze.getWidth();
				int height = getSize().y / maze.getDepth();
				
				int y = characterPosition.getY();
				for(int z = maze.getDepth() - 1; z >= 0; --z) {
					for(int x = 0; x < maze.getWidth(); ++x) {
						Color color;
						if(maze.isWall(x, y, z))
							color = new Color(getDisplay(), 0, 0, 0);
						else
							color = new Color(getDisplay(), 255, 255, 255);
						e.gc.setBackground(color);
						e.gc.fillRectangle(x * width, z * height, width, height);
						
						if(x == characterPosition.getX() && z == characterPosition.getZ()) {
							e.gc.setBackground(new Color(getDisplay(), 200, 50, 50));
							e.gc.fillOval(x * width, z * height, width, height);
						}
						
						if(maze.isPath(x, y, z))
							if((y < maze.getHeight() - 1 && maze.isPath(x, y+1, z))
									|| (y > 0 && maze.isPath(x, y-1, z))) {
								e.gc.drawLine(x*width + width/2, z*height + (int)(height*.1),
										x*width + width/2, z*height + (int)(height*.9));
							}
							
							if(y < maze.getHeight() - 1 && maze.isPath(x, y+1, z)) {
								e.gc.drawLine(x*width + width/2, z*height + (int)(height*.1),
										x*width + width/4, z*height + (int)(height*.4));
								e.gc.drawLine(x*width + width/2, z*height + (int)(height*.1),
										x*width + 3*width/4, z*height + (int)(height*.4));
							}
							
							if(y > 0 && maze.isPath(x, y-1, z)) {
								e.gc.drawLine(x*width + width/2, z*height + (int)(height*.9),
										x*width + width/4, z*height + (int)(height*.6));
								e.gc.drawLine(x*width + width/2, z*height + (int)(height*.9),
										x*width + 3*width/4, z*height + (int)(height*.6));
							}
					}
				}
			}
		});
	}
}
