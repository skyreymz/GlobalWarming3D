package partieApplicative;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Read_file {
	
	/**
	 * Fonction consistant en la récupération des données du fichier csv des anomalies de températures pour les stocker dans une instance de la classe Terre.
	 * @param path
	 * @return une instance de la classe Terre avec toutes les données du fichier csv
	 */
	public static Terre getDataFromCSVFile(String path) {
		// Variables pour les paramètres d'entrée de la classe Terre
		Float maxAnomalie = Float.NEGATIVE_INFINITY;
		Float minAnomalie = Float.POSITIVE_INFINITY;
		List<String> listeAnnees = new ArrayList<String>();
		List<Zone> listeZones = new ArrayList<Zone>();
		
		try {
			FileReader file = new FileReader(path);
			BufferedReader bufRead = new BufferedReader(file);
		
			// Première ligne du csv
			String line = bufRead.readLine();
			String[] tab = line.split(",");
			String latitude = tab[0]; // La première valeur de la première ligne correspond à "lat"
			String longitude = tab[1]; // La seconde valeur de la première ligne correspond à "lon"
			for (int i = 2 ; i < tab.length ; i = i + 1) { //Les autres valeurs correspondent aux années
				listeAnnees.add(tab[i]);
			}

			// Deuxième ligne du csv jusqu'à la fin
			line = bufRead.readLine();
			while ( line != null) {
			   	String[] array = line.split(",");
			   
			    int lat = Integer.parseInt(array[0]); // La première valeur correspond à la latitude
			    int lon = Integer.parseInt(array[1]); // La seconde valeur correspond à la longitude
			    List<Float> listeAnomalies = new ArrayList<Float>(); 
			    
			    for (int i = 2 ; i < tab.length ; i = i + 1) { // Les autres valeurs correspondent aux anomalies de températures pour chaque année
			    	try {
			    		float valeur = Float.parseFloat(array[i]);
			    		listeAnomalies.add(valeur);
			    		
			    		if (maxAnomalie < valeur) {
			    			maxAnomalie = valeur;
			    		}
			    		if (minAnomalie > valeur) {
			    			minAnomalie = valeur;
			    		}
			    	} catch (java.lang.NumberFormatException erreur) { // La valeur n'est pas un nombre
			    			listeAnomalies.add(Float.NaN);
			    	}
			    }
			    Zone zone = new Zone(lat, lon, listeAnomalies);
			    listeZones.add(zone);
			    
			    line = bufRead.readLine();
			}
			bufRead.close();
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Terre(minAnomalie, maxAnomalie, listeAnnees, listeZones);
	}
}