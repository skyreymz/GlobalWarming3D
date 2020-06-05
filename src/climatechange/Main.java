package climatechange;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Main {
	try {
		FileReader file = new FileReader("name_fichier.ext");
		BufferedReader bufRead = new BufferedReader(file);

		String line = bufRead.readLine();
		while ( line != null) {
		   	String[] array = line.split(",");
		   
		    int id = Integer.parseInt(array[0]);
		    float val = Float.parseFloat(array[6]);
		        		
		    line = bufRead.readLine();
		}

		bufRead.close();
		file.close();
		
	} catch (IOException e) {
		e.printStackTrace();
	}
}