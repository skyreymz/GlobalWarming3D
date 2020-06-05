package climatechange;

import java.util.List;

public class Zone {
	private int latitude;
	private int longitude;
	private List<Float> listeAnomalies;
	
	public Zone(int latitude, int longitude, List<Float> listeAnomalies) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.listeAnomalies = listeAnomalies;
	}

	public int getLatitude() {
		return latitude;
	}

	public int getLongitude() {
		return longitude;
	}

	public List<Float> getListeAnomalies() {
		return listeAnomalies;
	}
}
