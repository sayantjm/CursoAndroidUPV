package es.jmperales.mislugares;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity implements LocationListener {

	private static final long DOS_MINUTOS = 2 * 60 * 1000;

	private Button btnAcercaDe;
	private Button btnSalir;
	private Button btnPreferencias;
	private Button btnMostrar;
	// private MediaPlayer mp; //Descomentar para poner música

	public BaseAdapter adaptador;

	// Variables para la localizacion
	private LocationManager manejador;
	private Location mejorLocaliz;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		adaptador = new AdaptadorLugares(this);
		setListAdapter(adaptador);

		// mp = MediaPlayer.create(this, R.raw.audio); //Descomentar para poner
		// música
		// mp.start();//Descomentar para poner música

		// Inicializamos la localizacion
		manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			actualizaMejorLocaliz(manejador
					.getLastKnownLocation(LocationManager.GPS_PROVIDER));
		}
		if (manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			actualizaMejorLocaliz(manejador
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.acercaDe:
			lanzarAcercaDe(null);
			break;
		case R.id.config:
			lanzarPreferencias(null);
			break;
		case R.id.menu_mapa:
			Intent i = new Intent(this, Mapa.class);
			startActivity(i);
			break;
		}
		return true;
	}

	public void lanzarAcercaDe(View view) {
		Intent i = new Intent(this, AcercaDe.class);
		startActivity(i);
	}

	public void lanzarPreferencias(View view) {
		Intent i = new Intent(this, Preferencias.class);
		startActivity(i);
	}

	public void mostrarPreferencias(View view) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(this);
		String s = "notificaciones: " + pref.getBoolean("notificaciones", true)
				+ ", distancia mínima: " + pref.getString("distancia", "?");
		Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
	}

	/*
	 * public void lanzarVistaLugar_old(View view) { Intent i = new Intent(this,
	 * VistaLugar.class); i.putExtra("id", (long) 0); startActivity(i); }
	 * 
	 * public void lanzarVistaLugar(View view) { final EditText entrada = new
	 * EditText(this); entrada.setText("0"); new
	 * AlertDialog.Builder(this).setTitle("Selección de lugar")
	 * .setMessage("indica su id:").setView(entrada) .setPositiveButton("OK",
	 * new DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) { long
	 * id = Long.parseLong(entrada.getText().toString()); Intent i = new
	 * Intent(MainActivity.this, VistaLugar.class); i.putExtra("id", id);
	 * startActivity(i); } }).setNegativeButton("Cancelar", null).show(); }
	 */

	@Override
	protected void onListItemClick(ListView listView, View vista, int posicion,
			long id) {
		super.onListItemClick(listView, vista, posicion, id);
		Intent intent = new Intent(this, VistaLugar.class);
		intent.putExtra("id", id);
		startActivity(intent);
	}

	/**
	 * Estado de la aplicación
	 */

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// mp.start();//Descomentar para poner música
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();// Descomentar para poner música
		manejador.removeUpdates(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// mp.pause();//Descomentar para poner música
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		// mp.start();//Descomentar para poner música
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		activarProveedores();
	}

	private void activarProveedores() {
		manejador = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (manejador.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			manejador.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					20 * 1000, 5, this);
		}

		if (manejador.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			manejador.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					10 * 1000, 10, this);
		}
	}

	/**
	 * Métodos para implementar el LocationListener
	 */

	@Override
	public void onLocationChanged(Location location) {
		Log.d(Lugares.TAG, "Nueva localización: " + location);
		actualizaMejorLocaliz(location);
	}

	@Override
	public void onProviderDisabled(String proveedor) {
		Log.d(Lugares.TAG, "Se deshabilita: " + proveedor);
		activarProveedores();
	}

	@Override
	public void onProviderEnabled(String proveedor) {
		Log.d(Lugares.TAG, "Se habilita: " + proveedor);
		activarProveedores();
	}

	@Override
	public void onStatusChanged(String proveedor, int estado, Bundle extras) {
		Log.d(Lugares.TAG, "Cambia estado: " + proveedor);
		activarProveedores();
	}

	/**
	 * Método para actualizar la localizacion
	 */

	private void actualizaMejorLocaliz(Location localiz) {
		if (localiz != null) {
			if (mejorLocaliz == null
					|| localiz.getAccuracy() < 2 * mejorLocaliz.getAccuracy()
					|| localiz.getTime() - mejorLocaliz.getTime() > DOS_MINUTOS) {
				Log.d(Lugares.TAG, "Nueva mejor localización");
				mejorLocaliz = localiz;
				Lugares.posicionActual.setLatitud(localiz.getLatitude());
				Lugares.posicionActual.setLongitud(localiz.getLongitude());

				adaptador.notifyDataSetChanged();
			}
		}
	}
}
