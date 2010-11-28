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
        private static int SMALLTHRESH = 100;
        private static int MEDTHRESH = 200;
        public enum Status{Scanning, Reading};
        public Status status;

        public ReadDetector()
        {
                this.status = Status.Scanning;
        }

        public int update(LinkedList<EyeTrackerComponent.GazePoint> points)
        {
                if(points.size() < 3)
                        return 0;
                // This just tracks how fast we are receiving events.
                int sum = 0;
                int i = java.lang.Math.max(0, points.size()-30);
                long lasttime = points.get(i).time;
                for(; i < points.size(); i++)
                {
                        sum += points.get(i).time - lasttime;
                        lasttime = points.get(i).time;
                }

                LinkedList <Integer> scores = new LinkedList<Integer>();

                // Look over the points in the last 500 ms.

                int score = 0;
                for(int j = (points.size()-1)
                        ;j >= 0 && (points.getLast().time - points.get(j).time) < 500
                        ; j--)
                {
                        // This is the last point. We need two.
                        if(j == points.size()-1) continue;

                        EyeTrackerComponent.GazePoint pt1 = points.get(j);
                        EyeTrackerComponent.GazePoint pt2 = points.get(j+1);
                        int dx = pt2.x - pt1.x;
                        int dy = pt2.y - pt1.y;
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
                        
                        scores.push(score);
                }

                if(score > 30)
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
