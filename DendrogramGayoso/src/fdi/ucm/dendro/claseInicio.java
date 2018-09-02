package fdi.ucm.dendro;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;



public class claseInicio {

	
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		 DCollection col = new DCollection();
		   col.load(new FileReader(args[0]));
		   System.out.println("Collection loaded");
//		   NavigationAction[] actions = makeActions(col);
	}
}
