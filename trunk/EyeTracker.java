import java.util.Scanner;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.*;

public class EyeTracker
{
        // If true, use mouse input instead of opengazer.
        boolean debug = false;

	public EyeTracker(){
	
		// Initialize the overall JFrame window
		JFrame frame = new JFrame("Courier Demo");
	 	frame.setSize(1400,800);
	 	frame.setLayout(new BorderLayout());
	 	
	 	// Create the new EyeTrackerComponent
	 	final EyeTrackerComponent comp = new EyeTrackerComponent();
	 	comp.setPreferredSize(new Dimension(frame.getWidth()-20,frame.getHeight()));
	 	
	 	// Add the eyeTrackerComponent to a scrollable area
	 	JScrollPane scrollArea = new JScrollPane(comp, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );	 	
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
                                comp.setPosition(e.getX(), e.getY());
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
		   		comp.setPosition(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]));
		   }catch (NumberFormatException nfe){
      			System.out.println("NumberFormatException: " + nfe.getMessage());
                   }
                 }
                }
    	
	}
    
    public static void main(String[] args )
    {
   		new EyeTracker();
    }
}