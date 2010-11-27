import java.awt.geom.Rectangle2D;
import java.awt.*;
import java.util.*;

public class HighlightableWord {

	private String word;
	private Rectangle2D rect;
	private Color color;
	
	public HighlightableWord(String txt, Rectangle2D rectangle){
		word = txt;
		rect = rectangle;
		color = Color.black;
	}
	
	

}