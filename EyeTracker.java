import java.util.Scanner;
import javax.swing.*;
import java.awt.Dimension;

public class EyeTracker
{
	public EyeTracker(){
	
		// **** Initialize the overall JFrame window
		JFrame frame = new JFrame("Courier Demo");
	 	frame.setSize(800,500);
	 	frame.setMinimumSize(new Dimension(1440,900));
	 	
	 	EyeTrackerComponent comp = new EyeTrackerComponent();
	 	/*JTextField status = new JTextField("Welcome to Courier");
	 	status.setSize(20,50);*/
	 	frame.add(comp);
	 	frame.setVisible(true);
	 
		Scanner bar = new Scanner(System.in);
		String text;

		while( bar.hasNext() )
	    {
		   text = bar.nextLine();
		   String[] vals =  text.split(" ");
		   System.out.println(vals[0]+" "+vals[1]);
		   /*try{
		   	comp.setPosition(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]));
		   	
		   }catch (NumberFormatException nfe){
      			System.out.println("NumberFormatException: " + nfe.getMessage());
    		}*/
	    }
    	
	}
    
    public static void main(String[] args )
    {
   		new EyeTracker();
    }
}