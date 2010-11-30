import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.awt.event.*;
import javax.swing.JViewport;

public class EyeTrackerComponent extends JPanel implements Scrollable{

        private GazePoint last;
        public LinkedList<GazePoint> drawList;

        private LinkedList<HighlightableWord> words;

        private String text;
        private JLabel status;
        //private ReadDetector detector;
        private HighlightableWord hword;
        private boolean isHighlighted;
        private boolean initialized;
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

                initialized = false;
	}

        // Precomputes text layout and word rectangles to speed up response time.
        private void initializePaint(Graphics g)
        {
                initialized = true;
                words = new LinkedList<HighlightableWord>();
		// This draws the text into the component.
		// We want: to get the size of each word and draw a rectangle the size of each one.
		int text_x = getWidth()/5;
		int text_y = 30;
		int size = 0;
		int liney = 0;
		String[] splitText = text.split(" ");
		Font font = new Font("Verdana", Font.BOLD, 24);
   		g.setFont(font);
   		FontMetrics ftmet = g.getFontMetrics();
                double space = ftmet.getStringBounds(" ", g).getWidth();

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
				words.add(new HighlightableWord(splitText[j], rect));
                                size += rect.getWidth() + space;
			}
		}
        }
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;
		// Draw the circle that follows the eyes
		g2.setColor(Color.black);
                if(last != null)
                        g2.drawOval(last.x, last.y, 30, 30);

		// Draw the polyline that shows the path drawn by the eyes
		for(int i = 1; i < drawList.size(); i++){
			g2.drawLine(drawList.get(i-1).x,
                                drawList.get(i-1).y,
                                drawList.get(i).x,
                                drawList.get(i).y);
		}

                if(!initialized) initializePaint(g2);

                Font font = new Font("Verdana", Font.BOLD, 24);
   		g2.setFont(font);

				if(hword != null && isHighlighted){
                        g.setColor(Color.blue);
                        g.fillRect((int)hword.rect.getX(), (int)hword.rect.getY()-font.getSize(), (int)hword.rect.getWidth(), (int)hword.rect.getHeight());
                }
                g.setColor(Color.black);
                
                for(int i = 0; i < words.size(); i++)
                {
                        Rectangle2D rect = words.get(i).rect;
                        String word = words.get(i).word;
                        g2.drawString(word, (int) rect.getX(), (int) rect.getY());
                }

                
	}
	
	public void updateNewPoint(GazePoint pt, String statusVal, int accuracy, int readingCount) {
                if(isHighlighted)
                        return;
                last = pt;
                drawList.add(pt);

                // Update highlighted word
                if(statusVal == "Reading" && initialized)
                {
                        double mindistance = Double.MAX_VALUE;
                        HighlightableWord w = null;
                        for(int i = 0; i < words.size(); i++)
                        {
                                Rectangle2D rect = words.get(i).rect;
                                GazePoint rectpt = new GazePoint((int)rect.getCenterX(), (int)rect.getCenterY(), 0);
                                if(pt.distance(rectpt) < mindistance){
                                        mindistance = pt.distance(rectpt);
                                	w = words.get(i);
                                }
                        }
                        hword = w;
                }

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
        hword = null;
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
	