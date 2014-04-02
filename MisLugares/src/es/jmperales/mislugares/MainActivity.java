package es.jmperales.mislugares;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends ListActivity {

	private Button btnAcercaDe;
	private Button btnSalir;
	private Button btnPreferencias;
	private Button btnMostrar;
	
	public BaseAdapter adaptador;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		adaptador = new AdaptadorLugares(this);
		setListAdapter(adaptador);

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

/*	public void lanzarVistaLugar_old(View view) {
		Intent i = new Intent(this, VistaLugar.class);
		i.putExtra("id", (long) 0);
		startActivity(i);
	}

	public void lanzarVistaLugar(View view) {
		final EditText entrada = new EditText(this);
		entrada.setText("0");
		new AlertDialog.Builder(this).setTitle("Selección de lugar")
				.setMessage("indica su id:").setView(entrada)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						long id = Long.parseLong(entrada.getText().toString());
						Intent i = new Intent(MainActivity.this,
								VistaLugar.class);
						i.putExtra("id", id);
						startActivity(i);
					}
				}).setNegativeButton("Cancelar", null).show();
	}*/
	
	@Override 
	protected void onListItemClick(ListView listView, 
	                         View vista, int posicion, long id) {
	   super.onListItemClick(listView, vista, posicion, id);
	   Intent intent= new Intent(this, VistaLugar.class);
	   intent.putExtra("id", id);
	   startActivity(intent);
	}
}
