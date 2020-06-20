package partieApplicative;

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
	
	/**
	 * M�thode permettant de r�cup�rer la valeur de l'anomalie de temp�rature pour une zone donn�e � une ann�e donn�e.
	 * @param latitude
	 * @param longitude
	 * @param annee
	 * @return
	 */
	public float anomalie(int latitude, int longitude, int annee) {
		double indiceLat = (latitude + 88) * 22.5;
		double indiceLon = (longitude + 178) * 0.25;
		int indice = (int)(indiceLat + indiceLon);
		return listeZones.get(indice).getListeAnomalies().get(annee - 1880);
	}
	
	/**
	 * M�thode permettant de r�cup�rer la liste des anomalies de temp�rature de toutes les zones pour une ann�e donn�e.
	 * (Les zones sont dans l'ordre de lecture du fichier)
	 * @param annee
	 * @return
	 */
	public List<Float> anomaliesZones(int annee) {
		List<Float> liste = new ArrayList<Float>();
		for (Zone zone : listeZones ) {
			liste.add(zone.getListeAnomalies().get(annee - 1880));
		}
		return liste;
	}
	
	/**
	 * M�thode permettant de r�cup�rer la liste des anomalies de temp�ratures de toutes les ann�es pour une zone donn�e.
	 * (La liste est dans l'ordre croissant des ann�es)
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public List<Float> anomaliesAnnees(int latitude, int longitude) {
		double indiceLat = (latitude + 88) * 22.5;
		double indiceLon = (longitude + 178) * 0.25;
		int indice = (int)(indiceLat + indiceLon);
		return listeZones.get(indice).getListeAnomalies();
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
