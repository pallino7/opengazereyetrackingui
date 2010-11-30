import java.awt.geom.Rectangle2D;
import java.awt.*;
import java.util.*;

public class HighlightableWord {

	public String word;
	public Rectangle2D rect;
	public Color color;
	
	public HighlightableWord(String txt, Rectangle2D rectangle){
		word = txt;
		rect = rectangle;
		color = Color.black;
	}
}