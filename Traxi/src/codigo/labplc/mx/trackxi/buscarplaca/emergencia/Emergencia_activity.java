package codigo.labplc.mx.trackxi.buscarplaca.emergencia;

import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.TrackxiMainActivity;
import codigo.labplc.mx.trackxi.R.id;
import codigo.labplc.mx.trackxi.R.layout;
import codigo.labplc.mx.trackxi.R.menu;
import codigo.labplc.mx.trackxi.califica.Califica_taxi;
import codigo.labplc.mx.trackxi.fonts.fonts;
import codigo.labplc.mx.trackxi.registro.MitaxiRegisterManuallyActivity;
import codigo.labplc.mx.trackxi.services.ServicioGeolocalizacion;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;
import android.provider.ContactsContract.Contacts;

public class Emergencia_activity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_emergencia_activity);
		
		//revisamos si ya acepto los terminos y condiciones
		
		SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
		String acepto = prefs.getString("acepto", null);
		//si aun no tiene datos guardados en preferencias lo registramos
		if(acepto == null ){
			init();
		}else{ //si ya se registro de muestra splash y luego la activity para buscar placas
		 datosEmergencia();
		}
		
	}

	public void init(){
		
	    ((TextView) findViewById(R.id.activity_emergencia_tv_titulo)).setTypeface(new fonts(Emergencia_activity.this).getTypeFace(fonts.FLAG_MAMEY));	
		((TextView) findViewById(R.id.activity_emergencia_tv_titulo)).setTextColor(new fonts(Emergencia_activity.this).getColorTypeFace(fonts.FLAG_ROJO));
	     
		((TextView) findViewById(R.id.activity_emergencia_tv_contenido)).setTypeface(new fonts(Emergencia_activity.this).getTypeFace(fonts.FLAG_ROJO));	
		((TextView) findViewById(R.id.activity_emergencia_tv_contenido)).setTextColor(new fonts(Emergencia_activity.this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));

		Button activity_emergencia_btn_ok = (Button) findViewById(R.id.activity_emergencia_btn_ok);
		activity_emergencia_btn_ok.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		activity_emergencia_btn_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("acepto", "true");
				editor.commit();
				datosEmergencia();
			}
		});
		
		Button activity_emergencia_btn_cancel = (Button) findViewById(R.id.activity_emergencia_btn_cancel);
		activity_emergencia_btn_cancel.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		activity_emergencia_btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	
	public void datosEmergencia(){
		Intent intentManually = new Intent(Emergencia_activity.this, MitaxiRegisterManuallyActivity.class);
		intentManually.putExtra("origen", "splash");
		startActivity(intentManually);
		overridePendingTransition(0,0);
		finish();
	}

}
