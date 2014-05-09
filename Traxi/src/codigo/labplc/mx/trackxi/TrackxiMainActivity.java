package codigo.labplc.mx.trackxi;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import codigo.labplc.mx.trackxi.buscarplaca.BuscaPlacaTexto;
import codigo.labplc.mx.trackxi.log.BeanDatosLog;
import codigo.labplc.mx.trackxi.registro.MitaxiRegisterManuallyActivity;
import codigo.labplc.mx.trackxi.services.ServicioGeolocalizacion;
import codigo.labplc.mx.trackxi.tracking.map.Mapa_tracking;

public class TrackxiMainActivity extends Activity {

	 private static final long SPLASH_SCREEN_DELAY = 1000;
	 public final String TAG = this.getClass().getSimpleName();
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		//guardamos el Tag
		BeanDatosLog.setTagLog(TAG);		
		SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("uuid", "dab7fe0d-83b9-43d5-92b9-3f9ec1f1fbd9");
		editor.commit();
		
			if(ServicioGeolocalizacion.serviceIsIniciado==true){
				iniciarSplash(3);
			}else{
				iniciarSplash(1);
			}
			
	//	}
	}
	
	/**
	 * Muestra la pantalla splash, inicia la actividad principal o muestra el mapa de viaje
	 * 
	 */
	public void iniciarSplash(final int flag){
		 	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 	requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.splash_screen_trackxi);
	        
	        TimerTask task = new TimerTask() {
	            @Override
	            public void run() {
	            	if(flag==1){//Inicia la actividad de revisado de placas
	            		Intent mainIntent = new Intent().setClass(TrackxiMainActivity.this, BuscaPlacaTexto.class);
	            		 startActivity(mainIntent);
	            	}else if(flag==2){ //Inicia la actividad de registro
	    				Intent intentManually = new Intent(TrackxiMainActivity.this, MitaxiRegisterManuallyActivity.class);
	    				intentManually.putExtra("origen", "splash");
	    				startActivity(intentManually);
	    				overridePendingTransition(0,0);
	            	}else if(flag==3){//Inicia la actividad del mapa
	            		Intent intent_mapa = new Intent(TrackxiMainActivity.this, Mapa_tracking.class);
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
	
	
}
