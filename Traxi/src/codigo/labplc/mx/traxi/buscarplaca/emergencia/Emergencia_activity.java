      package codigo.labplc.mx.traxi.buscarplaca.emergencia;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.fonts.fonts;
import codigo.labplc.mx.traxi.registro.RegistroContactosEmergenciaActivity;

/**
 * clase que muestra un mensaje explicando las funciones de panico de la app
 * 
 * @author mikesaurio
 *
 */
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

	/**
	 * init de la clase
	 */
	public void init(){
		
	    ((TextView) findViewById(R.id.activity_emergencia_tv_titulo)).setTypeface(new fonts(Emergencia_activity.this).getTypeFace(fonts.FLAG_MAMEY));	
		((TextView) findViewById(R.id.activity_emergencia_tv_titulo)).setTextColor(getResources().getColor(R.color.color_vivos));
	     
		((TextView) findViewById(R.id.activity_emergencia_tv_contenido)).setTypeface(new fonts(Emergencia_activity.this).getTypeFace(fonts.FLAG_ROJO));	
		((TextView) findViewById(R.id.activity_emergencia_tv_contenido)).setTextColor(getResources().getColor(R.color.color_vivos));
		((TextView) findViewById(R.id.activity_emergencia_tv_contenido)).setText((getResources().getText(R.string.mensaje_para_panic)));
		Button activity_emergencia_btn_ok = (Button) findViewById(R.id.activity_emergencia_btn_ok);
		activity_emergencia_btn_ok.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_MAMEY));
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
		activity_emergencia_btn_cancel.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_MAMEY));
		activity_emergencia_btn_cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	/**
	 * intent que activa el llenado de contactos de emergencia
	 */
	public void datosEmergencia(){
		Intent intentManually = new Intent(Emergencia_activity.this, RegistroContactosEmergenciaActivity.class);
		intentManually.putExtra("origen", "splash");
		startActivity(intentManually);
		overridePendingTransition(0,0);
		finish();
	}

}
