import javax.swing.*;
import java.awt.*;

public class EyeTrackerComponent extends JComponent {

	private int x;
	private int y;
	
	public EyeTrackerComponent() {
		
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
		g.drawOval(x, y, 30, 30);
	}
	
	public void setPosition(int setX, int setY) {
		x = setX;
		y = setY;
		repaint();
	}
}
	