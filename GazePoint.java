
import java.awt.Point;

public class GazePoint{
     public int x;
     public int y;
     public long time;

     public GazePoint(int a, int b, long t)
     {
          x = a;
          y = b;
          time = t;
     }

     public double distance(GazePoint pt2)
     {
            Point pt1 = new Point(x, y);
            Point ptt2 = new Point(pt2.x, pt2.y);

            return pt1.distance(ptt2);
     }
}