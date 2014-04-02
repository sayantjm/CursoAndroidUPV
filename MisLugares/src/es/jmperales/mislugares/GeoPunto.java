package es.jmperales.mislugares;

public class GeoPunto {
	private int longitud;
	private int latitud;
	
	public GeoPunto(double longitud, double latitud) {
		super();
		this.longitud = (int) (longitud * 1E6);
		this.latitud = (int) (latitud * 1E6);
	}

	@Override
	public String toString() {
		return "GeoPunto [longitud=" + longitud + ", latitud=" + latitud + "]";
	}
	
	public double distancia(GeoPunto punto) {
		final double RADIO_TIERRA = 6371000; // en metros
	      double dLat = Math.toRadians(latitud - punto.latitud);
	      double dLon = Math.toRadians(longitud - punto.longitud);
	      double lat1 = Math.toRadians(punto.latitud);
	      double lat2 = Math.toRadians(latitud);
	      double a =    Math.sin(dLat/2) * Math.sin(dLat/2) +
	                    Math.sin(dLon/2) * Math.sin(dLon/2) *
	                    Math.cos(lat1) * Math.cos(lat2);
	      double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	      return c * RADIO_TIERRA;
	}
}
