import java.util.Scanner;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.*;
import java.awt.Rectangle;
import java.util.*;
import java.awt.*;

public class EyeTracker
{
        // If true, use mouse input instead of opengazer.
        boolean debug = true;
        private EyeTrackerComponent comp = null;
        private JFrame frame;
        private JScrollPane scrollArea;
        private JPanel navigation;
        private ReadDetector detector;
        private GazePoint last;
        private LinkedList<GazePoint> drawList;
        private int readingCount = 0;
        private Boolean gazedComboBox = false;
		

	public EyeTracker(){
	
		detector = new ReadDetector();
		drawList = new LinkedList<GazePoint>();
	
		// Initialize the overall JFrame window
		frame = new JFrame("EyeTracker Demo");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	 	frame.setSize(1400,800);
	 	frame.setLayout(new BorderLayout());
	 	frame.addWindowFocusListener( new WindowFocusListener(){
	 		public void windowLostFocus(WindowEvent e) {
	 			if(comp != null){
	 				comp.highlightWord();
	 			}
    		}
    		public void windowGainedFocus(WindowEvent e) {
        		if(comp != null){
	 				comp.unhighlightWord();
	 			}
    		}
    	});
    	
    	navigation = new JPanel();
    	JButton calibration = new JButton("Calibration Ended");
    	calibration.addActionListener(new ActionListener(){
    		public void actionPerformed(ActionEvent event){
    		 drawList = new LinkedList<GazePoint>();
    		 readingCount = 0;
    		 gazedComboBox = false;
    		 navigation.setBackground(Color.gray);
    		 }
   		});
    	
	 	String[] files = {"BenjaminButton.txt", "Emma.txt"};
	 	JComboBox selectText = new JComboBox(files);
	 	navigation.add(calibration, BorderLayout.NORTH);
	 	navigation.add(selectText, BorderLayout.NORTH);
	 	navigation.setBackground(Color.gray);
	 	frame.add(navigation, BorderLayout.NORTH);
	 	
	 	selectText.addActionListener(new ActionListener(){
	 		public void actionPerformed(ActionEvent e) {
        		JComboBox cb = (JComboBox)e.getSource();
        		String petName = (String)cb.getSelectedItem();
        	}
	 	});
	 	
	 	String testText = "";
	 	try{
	 		FileReader fro = new FileReader( "ExampleTexts/BenjaminButton.txt" );
	 		BufferedReader in = new BufferedReader(fro);
			String stringRead = in.readLine();
	 		while(stringRead != null){
	 			testText += stringRead;
	 			stringRead = in.readLine();
	 		}
	 	}catch(Exception e){
	 		System.out.println(e);
	 	}
	 	
	 	// Create the new EyeTrackerComponent
	 	comp = new EyeTrackerComponent(testText);
	 	comp.setPreferredSize(new Dimension(frame.getWidth()-20,1400));
	 	
	 	// Add the eyeTrackerComponent to a scrollable area
	 	scrollArea = new JScrollPane(comp, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );	 	
	 	scrollArea.setPreferredSize(new Dimension(frame.getWidth(),frame.getHeight()));

		// Add the scroll area to the main frame
	 	frame.add(scrollArea, BorderLayout.CENTER);
	 	frame.setVisible(true);
	 
	 	// This part gets the output from stdout from the opengazer output and parses it
	 	// in order to use it as input for this program.
                if(debug)
                {
                    comp.addMouseMotionListener(new MouseMotionAdapter(){
                        public void mouseMoved(MouseEvent e)
                        {
                            try
                            {
                               eyeEventAt(e.getX(), e.getY());
                            }catch (NumberFormatException nfe){
                                System.out.println("NumberFormatException: " + nfe.getMessage());
                            }
                        }
                    });
                }
                else
                {
		Scanner bar = new Scanner(System.in);
		String text;
		while( bar.hasNext() )
                {
		   text = bar.nextLine();
		   String[] vals =  text.split(" ");
		   System.out.println(vals[0]+" "+vals[1]);
		   try{
		   		eyeEventAt(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]));
		   		
		   }catch (NumberFormatException nfe){
      			System.out.println("NumberFormatException: " + nfe.getMessage());
                   }
                 }
                }
    	
	}
	
	public void eyeEventAt(int x, int y){

                int scrollValue = scrollArea.getVerticalScrollBar().getValue();

                // Scroll the scroll pane if the eye position is at the very bottom of the screen.
                if(y > (frame.getHeight()/7)*6
                        && detector.status == ReadDetector.Status.Reading){
                        scrollArea.getVerticalScrollBar().setValue(scrollValue+20);
                }
                // Scroll the scroll pane if the eye position is at the very top of the screen.
                if(y < scrollValue+((frame.getHeight()/7)*2)
                        && detector.status == ReadDetector.Status.Reading){
                        scrollArea.getVerticalScrollBar().setValue(scrollValue-20);
                }

                GazePoint pt = new GazePoint(x, y+scrollValue, System.currentTimeMillis());
                last = pt;
                drawList.add(pt);
                if(detector.status.toString() == "Reading"){
                        readingCount++;
                }
                comp.updateNewPoint(pt, detector.status+"", detector.update(drawList), readingCount);

                // Navigation bar alert feature

                // If the user looks at the top part of the screen, then they have glanced at the choices
                // for texts and we don't need to alert them (set gazedComboBox to true). Also, if the
                // alert has been activated, at this point we can revert the navigation bar to its original
                // color since the user has seen the comboBox.
                if(y < scrollValue+((frame.getHeight()/7))){
                        navigation.setBackground(Color.gray);
                        gazedComboBox = true;
                }

                // Change the background of the navigation bar if the user has been scanning for more than 10secs
                // and the user hasn't looked at the top part of the screen yet. In this situation, they might
                // the text they are looking at might not be the one they want to be reading.
                if(readingCount < 100 &&
                        (drawList.getLast().time - drawList.getFirst().time) > 5000 &&
                        !gazedComboBox){
                        navigation.setBackground(Color.blue);
                }
	}
    
    public static void main(String[] args )
    {
   		new EyeTracker();
    }
}