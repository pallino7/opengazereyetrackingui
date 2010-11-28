import java.util.Scanner;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.io.*;
import java.awt.Rectangle;
import java.awt.*;

public class EyeTracker
{
    // If true, use mouse input instead of opengazer.
   	boolean debug = true;
    private EyeTrackerComponent comp = null;
    private JFrame frame;
    private JScrollPane scrollArea;

	public EyeTracker(){
	
		// Initialize the overall JFrame window
		frame = new JFrame("Courier Demo");
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
    	//JButton button = new JButton("ALERT!");
    	//button.setVisible(false);
	 	String[] files = {"BenjaminButton.txt", "Emma.txt"};
	 	JComboBox selectText = new JComboBox(files);
	 	navigation.add(selectText, BorderLayout.NORTH);
	 	//navigation.add(button, BorderLayout.NORTH);
	 	frame.add(navigation, BorderLayout.NORTH);
	 	
	 	selectText.addActionListener(new ActionListener(){
	 		public void actionPerformed(ActionEvent e) {
        		JComboBox cb = (JComboBox)e.getSource();
        		String textFile = (String)cb.getSelectedItem();
        		if(comp != null){
	 				comp.updateText(getTextFromFile(textFile));
	 			}
        	}
	 	});
	 	
	 	// Create the new EyeTrackerComponent
	 	comp = new EyeTrackerComponent(getTextFromFile("BenjaminButton.txt"));
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
                            int x = e.getX();
		   					int y = e.getY();
		   					comp.setPosition(x, y);
		   		
		   					// Scroll the scroll pane if the eye position is at the very bottom of the screen.
		   					if(y > (frame.getHeight()/7)*6){
		   						scrollArea.getVerticalScrollBar().setValue(scrollArea.getVerticalScrollBar().getValue()+20);
		   					}
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
		   		/*if(y < 100){
		   			button.setVisible(true);
		   		}else{
		   			navigation.setBackground(Color.gray);
		   		}*/
		   		
		   }catch (NumberFormatException nfe){
      			System.out.println("NumberFormatException: " + nfe.getMessage());
           }
         }
       }
	}
    
    public String getTextFromFile(String file){
    	String returnString = "";
    	try{
	 		FileReader fro = new FileReader( "ExampleTexts/"+file );
	 		BufferedReader in = new BufferedReader(fro);
			String stringRead = in.readLine();
	 		while(stringRead != null){
	 			returnString += stringRead;
	 			stringRead = in.readLine();
	 		}
	 	}catch(Exception e){
	 		System.out.println(e);
	 	}
    	return returnString;
    }
    
    public static void main(String[] args )
    {
   		new EyeTracker();
    }
}