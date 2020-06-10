package climatechange;

import java.util.List;
import java.util.ArrayList;

public class Terre {
	private float minAnomalie;
	private float maxAnomalie;
	private List<String> listeAnnees;
	private List<Zone> listeZones;
	
	public Terre(float minAnomalie, float maxAnomalie, List<String> listeAnnees, List<Zone> listeZones) {
		this.minAnomalie = minAnomalie;
		this.maxAnomalie = maxAnomalie;
		this.listeAnnees = listeAnnees;
		this.listeZones = listeZones;
	}
	
	public float anomalie(int latitude, int longitude, int annee) {
		double indiceLat = (latitude + 88) * 22.5;
		double indiceLon = (longitude + 178) * 0.25;
		int indice = (int)(indiceLat + indiceLon);
		return listeZones.get(indice).getListeAnomalies().get(annee - 1880);
	}
	
	public List<Float> anomaliesAnnees(int latitude, int longitude) {
		double indiceLat = (latitude + 88) * 22.5;
		double indiceLon = (longitude + 178) * 0.25;
		int indice = (int)(indiceLat + indiceLon);
		return listeZones.get(indice).getListeAnomalies();
	}
	
	public List<Float> anomaliesZones(int annee) {
		List<Float> liste = new ArrayList<Float>();
		for (Zone zone : listeZones ) {
			liste.add(zone.getListeAnomalies().get(annee - 1880));
		}
		return liste;
	}

	public float getMinAnomalie() {
		return minAnomalie;
	}

	public float getMaxAnomalie() {
		return maxAnomalie;
	}

	public List<String> getListeAnnees() {
		return listeAnnees;
	}

	public List<Zone> getListeZones() {
		return listeZones;
	}
}
