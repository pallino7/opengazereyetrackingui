import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.awt.event.*;
import javax.swing.JViewport;

public class EyeTrackerComponent extends JPanel implements Scrollable{

        private GazePoint last;
		private LinkedList<GazePoint> drawList;
		private String text;
        private JLabel status;
        //private ReadDetector detector;
		private Boolean isHighlighted;
		private Rectangle parentSize;
		private int maxUnitIncrement = 1;
		//private int readingCount = 0;
	
	public EyeTrackerComponent(String textFromFile) {
		super(new BorderLayout());
		//drawList = points;
		drawList = new LinkedList<GazePoint>();
        //detector = new ReadDetector();
        last = null;
		//parentSize = size;
		this.setOpaque(true);
		this.setBackground(Color.white);
		isHighlighted = false;
        status = new JLabel("scanning");
        this.setLayout(new BorderLayout());
        this.add(status, BorderLayout.NORTH);
		
		// Test text, this will move out of here eventually
		text = textFromFile;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int scrollOffset = (int)((JViewport)this.getParent()).getViewPosition().getY();
		// Draw the circle that follows the eyes
		g.setColor(Color.black);
                if(!drawList.isEmpty())
		g.drawOval(last.x, last.y-scrollOffset, 30, 30);
		
		// Draw the polyline that shows the path drawn by the eyes
		for(int i = 1; i < drawList.size(); i++){
			g.drawLine(drawList.get(i-1).x,
                                drawList.get(i-1).y - scrollOffset,
                                drawList.get(i).x,
                                drawList.get(i).y - scrollOffset);
		}
		
		// This draws the text into the component. 
		// We want: to get the size of each word and draw a rectangle the size of each one.
		int text_x = getWidth()/5;
		int text_y = 10;
		int size = 0;
		int liney = 0;
		String[] splitText = text.split(" "); 
		Font font = new Font("Verdana", Font.BOLD, 24);
   		g.setFont(font);
   		FontMetrics ftmet = g.getFontMetrics();

		for(int j = 0; j < splitText.length; j++){
                        if(splitText[j].length() == 0) continue;
			if(splitText[j].charAt(0) == '\n'){
				liney += ftmet.getHeight();
				size = 0;
			}else{
				//System.out.println("Width: "+getWidth());
				if(size + ftmet.stringWidth(splitText[j]) > (this.getWidth()/5)*3){
					liney += ftmet.getHeight();
					size = 0;
				}
				Rectangle2D rect = ftmet.getStringBounds(splitText[j], g);
				rect.setRect(text_x+size, text_y+liney, rect.getWidth(), rect.getHeight());
				HighlightableWord s = new HighlightableWord(splitText[j], rect);

				if(last != null && rect.contains((double)last.x, (double)last.y+scrollOffset) && isHighlighted){
					g.setColor(Color.blue);
					g.fillRect((int)rect.getX(), (int)rect.getY(), (int)rect.getWidth(), (int)rect.getHeight()+5);
				}
				g.setColor(Color.black);
				g.drawString(splitText[j]+" ", text_x+size, text_y+liney+ftmet.getHeight());
				size += ftmet.stringWidth(splitText[j]+" ");
			}
		}
		
	}
	
	public void updateNewPoint(GazePoint pt, String statusVal, int accuracy, int readingCount) {
                if(isHighlighted)
                        return;
                last = pt;
				drawList.add(pt);
                status.setText("Status: " + statusVal + ";Score: " + accuracy+" reading count: "+readingCount);
                repaint();
    }
        
	// Set the boolean for highlighting the word that corresponds to the current x, y position to true.
	public void highlightWord(){
		isHighlighted = true;
		repaint();
	}
	
	// Unhighlight the word that was previous highlighted after a second delay. This event occurs when
	// the user changes focus from the current window. In order for the user to understand where they
	// last left off, we leave the highlight for a second before disappearing.
	public void unhighlightWord(){
		javax.swing.Timer disolveTimer = new javax.swing.Timer(1000, new ActionListener() {
          public void actionPerformed(ActionEvent e) {
              	isHighlighted = false;
				repaint();
          }
        });
        disolveTimer.setInitialDelay(1000);
        disolveTimer.start();
        disolveTimer.setRepeats(false);
	}
	

	public void updateText(String newText) {
		text = newText;
		repaint();
	}
	
	public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }
	
	public boolean getScrollableTracksViewportHeight() {
        return false;
    }
    
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }
    
    public int getScrollableUnitIncrement(Rectangle visibleRect,
                                          int orientation,
                                          int direction) {
        //Get the current position.
        int currentPosition = 0;
        if (orientation == SwingConstants.HORIZONTAL) {
            currentPosition = visibleRect.x;
        } else {
            currentPosition = visibleRect.y;
        }

        //Return the number of pixels between currentPosition
        //and the nearest tick mark in the indicated direction.
        if (direction < 0) {
            int newPosition = currentPosition -
                             (currentPosition / maxUnitIncrement)
                              * maxUnitIncrement;
            return (newPosition == 0) ? maxUnitIncrement : newPosition;
        } else {
            return ((currentPosition / maxUnitIncrement) + 1)
                   * maxUnitIncrement
                   - currentPosition;
        }
    }
    
    public int getScrollableBlockIncrement(Rectangle visibleRect,
                                           int orientation,
                                           int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width - maxUnitIncrement;
        } else {
            return visibleRect.height - maxUnitIncrement;
        }
    }
}
	