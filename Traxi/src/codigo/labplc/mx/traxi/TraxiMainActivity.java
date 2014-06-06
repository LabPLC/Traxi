package codigo.labplc.mx.traxi;

import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import codigo.labplc.mx.traxi.buscarplaca.BuscaPlacaTexto;
import codigo.labplc.mx.traxi.log.DatosLogBean;
import codigo.labplc.mx.traxi.services.ServicioGeolocalizacion;
import codigo.labplc.mx.traxi.tracking.map.Mapa_tracking;
/**
 * Clase inicial 
 * 
 * @author mikesaurio
 *
 */
public class TraxiMainActivity extends Activity {

	 private static final long SPLASH_SCREEN_DELAY = 1000;
	 public final String TAG = this.getClass().getSimpleName();
	 private String UUID_local= null;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		//guardamos un Tag
		DatosLogBean.setTagLog(TAG);	
	
		//generamos la llave del dispositivo
			SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi", Context.MODE_PRIVATE);
			UUID_local = prefs.getString("uuid", null);
			if(UUID_local==null){
				UUID_local=UUID.randomUUID().toString();
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("uuid", UUID_local);
				editor.commit();
			}
		
			
			if(ServicioGeolocalizacion.serviceIsIniciado==true){
				iniciarSplash(3);
			}else{
				iniciarSplash(1);
			}
			

	}
	
	/**
	 * Muestra la pantalla splash, inicia la actividad principal o muestra el mapa de viaje
	 * 
	 */
	public void iniciarSplash(final int flag){
		 	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 	requestWindowFeature(Window.FEATURE_NO_TITLE);
		 	
	        setContentView(R.layout.splash_screen_traxi);
	        
	        TimerTask task = new TimerTask() {
	            @Override
	            public void run() {
	            	if(flag==1){//Inicia la actividad de revisado de placas
	            		Intent mainIntent = new Intent().setClass(TraxiMainActivity.this, BuscaPlacaTexto.class);
	            		 startActivity(mainIntent);
	            	}else if(flag==3){//Inicia la actividad del mapa
	            		Intent intent_mapa = new Intent(TraxiMainActivity.this, Mapa_tracking.class);
	            		intent_mapa.putExtra("latitud_inicial", ServicioGeolocalizacion.latitud_inicial);
	            		intent_mapa.putExtra("longitud_inicial", ServicioGeolocalizacion.longitud_inicial);
	            		startActivity(intent_mapa);
	            	}
	                finish();
	            }
	        };
	        Timer timer = new Timer();
	        timer.schedule(task, SPLASH_SCREEN_DELAY);
	    }

	
	@Override
	public void onBackPressed() {
	}
	
	
	
	
}
