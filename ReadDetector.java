import java.util.*;
import java.lang.Math.*;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This class will detect when reading is done, and change its internal status
 * accordingly.
 * @author Denis
 */
public class ReadDetector {
        private static int NOISETHRESH = 30;
        private static int SMALLTHRESH = 100;
        private static int MEDTHRESH = 200;
        public enum Status{Scanning, Reading};
        public Status status;

        public ReadDetector()
        {
                this.status = Status.Scanning;
        }

        public int update(LinkedList<GazePoint> points)
        {
                if(points.size() < 5)
                        return 0;

                // Look over the points in the last 500 ms.

                int score = 0;
                for(int j = (points.size()-1)
                        ;j >= points.size()-5
                        ; j--)
                {
                        // This is the last point. We need two.
                        if(j == points.size()-1) continue;

                        GazePoint pt1 = points.get(j);
                        GazePoint pt2 = points.get(j+1);
                        int dx = pt2.x - pt1.x;
                        int dy = pt2.y - pt1.y;

                        if(java.lang.Math.abs(dx) < NOISETHRESH) dx = 0;
                        if(java.lang.Math.abs(dy) < NOISETHRESH) dy = 0;

                        if(dx < 0)
                                if(dx<SMALLTHRESH)
                                        if(dx<MEDTHRESH)
                                                score -= 99;
                                        else
                                                score -= 5;
                                else
                                        score -= 10;

                        if(dx > 0)
                                if(dx > SMALLTHRESH)
                                        if(dx>MEDTHRESH)
                                                score -= 99;
                                        else
                                                score += 5;
                                else
                                        score += 10;

                        if(dy < 0)
                                if(dy<SMALLTHRESH)
                                        if(dy<MEDTHRESH)
                                                score -= 99;
                                        else
                                                score -= 5;
                                else
                                        score -= 0;

                        if(dy > 0)
                                if(dy > SMALLTHRESH)
                                        if(dy>MEDTHRESH)
                                                score -= 99;
                                        else
                                                score -= 99;
                                else
                                        score -= 5;
                }

                if(score >= 30)
                        status = Status.Reading;
                else
                        status = Status.Scanning;

                // extract the relevant sublist of points
                // identify eye events
                // score the list
                // threshold for reading/scanning
                // set status
                return(score);
        }
}
