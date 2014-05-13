package org.example.localizacion;

import java.util.List;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements LocationListener {
   private static final long TIEMPO_MIN = 10 * 1000 ; // 10 segundos
   private static final long DISTANCIA_MIN = 5 ; // 5 metros
   private static final String[] A = { "n/d", "preciso", "impreciso" };
   private static final String[] P = { "n/d", "bajo", "medio","alto" };
   private static final String[] E = { "fuera de servicio",
                        "temporalmente no disponible ","disponible" };
   private LocationManager manejador;
   private String proveedor;
   private TextView salida;
 
   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      salida = (TextView) findViewById(R.id.salida);

      manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
      log("Proveedores de localizaci�n: \n ");
      muestraProveedores();

      Criteria criterio = new Criteria();
      criterio.setCostAllowed(false);
      criterio.setAltitudeRequired(false);
      criterio.setAccuracy(Criteria.ACCURACY_FINE);
      proveedor = manejador.getBestProvider(criterio, true);
      log("Mejor proveedor: " + proveedor + "\n");
      log("Comenzamos con la �ltima localizaci�n conocida:");
      Location localizacion = manejador.getLastKnownLocation(proveedor);
      muestraLocaliz(localizacion);
   }
   
// M�todos del ciclo de vida de la actividad
   @Override    protected void onResume() {
         super.onResume();
         // Activamos notificaciones de localizaci�n
         manejador.requestLocationUpdates(proveedor, TIEMPO_MIN, DISTANCIA_MIN, this);
   }

   @Override    protected void onPause() {
         super.onPause();
         manejador.removeUpdates(this);
   }

//M�todos de la interfaz LocationListener
   public void onLocationChanged(Location location) {
          log("Nueva localizaci�n: ");
         muestraLocaliz(location);
   }

   public void onProviderDisabled(String proveedor) {
         log("Proveedor deshabilitado: " + proveedor + "\n");
   }

   public void onProviderEnabled(String proveedor) {
         log("Proveedor habilitado: " + proveedor + "\n");
   }

   public void onStatusChanged(String proveedor, int estado,
         Bundle extras) {
         log("Cambia estado proveedor: " + proveedor + ", estado="
                       + E[Math.max(0,estado)] + ", extras=" + extras +"\n");
   }

//M�todos para mostrar informaci�n
   private void log(String cadena) {
         salida.append(cadena + "\n");
   }

   private void muestraLocaliz(Location localizacion) {
         if (localizacion == null)
                log("Localizaci�n desconocida\n");
         else
                log(localizacion.toString() + "\n");
   }

   private void muestraProveedores() {
         log("Proveedor de localizaci�n: \n");
         List<String> proveedores = manejador.getAllProviders();
         for (String proveedor : proveedores) {
                muestraProveedor(proveedor);
         }
   }

   private void muestraProveedor(String proveedor) {
         LocationProvider info = manejador.getProvider(proveedor);
         log("LocationProvider[ "+"getName=" + info.getName()
                + ", isProviderEnabled=" 
                + manejador.isProviderEnabled(proveedor)+", getAccuracy="
                + A[Math.max(0, info.getAccuracy())]+ ", getPowerRequirement="
                + P[Math.max(0, info.getPowerRequirement())]
                +", hasMonetaryCost=" + info.hasMonetaryCost()
                + ", requiresCell=" + info.requiresCell()
                + ", requiresNetwork=" + info.requiresNetwork()
                + ", requiresSatellite=" + info.requiresSatellite()
                + ", supportsAltitude=" + info.supportsAltitude()
                + ", supportsBearing=" + info.supportsBearing()
                + ", supportsSpeed=" + info.supportsSpeed()+" ]\n");
   }
}
