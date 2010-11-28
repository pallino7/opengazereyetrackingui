import java.util.Scanner;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.*;
import java.awt.Rectangle;

public class EyeTracker
{
        // If true, use mouse input instead of opengazer.
        boolean debug = true;
        private EyeTrackerComponent comp = null;

	public EyeTracker(){
	
		// Initialize the overall JFrame window
		JFrame frame = new JFrame("EyeTracker Demo");
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
    	
    	JPanel navigation = new JPanel();
    	JButton button = new JButton("help");
    	
	 	String[] files = {"BenjaminButton.txt", "Emma.txt"};
	 	JComboBox selectText = new JComboBox(files);
	 	navigation.add(selectText, BorderLayout.NORTH);
	 	navigation.add(button, BorderLayout.NORTH);
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
		   		int x = Integer.parseInt(vals[0]);
		   		int y = Integer.parseInt(vals[1]);
		   		comp.setPosition(x, y);
		   		
		   		// Scroll the scroll pane if the eye position is at the very bottom of the screen.
		   		if(y > (frame.getHeight()/7)*6){
		   			scrollArea.getVerticalScrollBar().setValue(scrollArea.getVerticalScrollBar().getValue()+20);
		   		}
		   		
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