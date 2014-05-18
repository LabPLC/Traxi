package codigo.labplc.mx.traxi.califica;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.TrackxiMainActivity;
import codigo.labplc.mx.traxi.buscarplaca.BuscaPlacaTexto;
import codigo.labplc.mx.traxi.dialogos.Dialogos;
import codigo.labplc.mx.traxi.fonts.fonts;
import codigo.labplc.mx.traxi.log.BeanDatosLog;
import codigo.labplc.mx.traxi.services.ServicioGeolocalizacion;
import codigo.labplc.mx.traxi.tracking.map.Mapa_tracking;
import codigo.labplc.mx.traxi.utils.Utils;

/**
 * Actividad que muestra un dialogo el cual sirve para calificar el servicio
 * @author mikesaurio
 *
 */
public class Califica_taxi extends Activity {

	public final String TAG = this.getClass().getSimpleName();
	
	Button calificar_aceptar;
	Button calificar_cancelar;
	EditText comentario;
	RatingBar rank;
	private String Scalificacion = "0";
	private String Scomentario;
	
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_califica_taxi);

		BeanDatosLog.setTagLog(TAG);
		
		//en caso de que se active si ya no existe el servicio
		if(ServicioGeolocalizacion.serviceIsIniciado!=true){
	  		Intent mainIntent = new Intent().setClass(Califica_taxi.this, TrackxiMainActivity.class);
	  		startActivity(mainIntent);
	  		finish();
		}
		
		setFinishOnTouchOutside(false);
		 Mapa_tracking.fa.finish();
		
		
		
		
	    ((TextView) findViewById(R.id.califica_taxi_tv_titulo)).setTypeface(new fonts(Califica_taxi.this).getTypeFace(fonts.FLAG_MAMEY));	
		((TextView) findViewById(R.id.califica_taxi_tv_titulo)).setTextColor(new fonts(Califica_taxi.this).getColorTypeFace(fonts.FLAG_ROJO));
	     
		((TextView) findViewById(R.id.califica_taxi_tv_titulo_calif)).setTypeface(new fonts(Califica_taxi.this).getTypeFace(fonts.FLAG_ROJO));	
		((TextView) findViewById(R.id.califica_taxi_tv_titulo_calif)).setTextColor(new fonts(Califica_taxi.this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
			
		((TextView) findViewById(R.id.califica_taxi_tv_titulo_opinion)).setTypeface(new fonts(Califica_taxi.this).getTypeFace(fonts.FLAG_ROJO));	
		((TextView) findViewById(R.id.califica_taxi_tv_titulo_opinion)).setTextColor(new fonts(Califica_taxi.this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));

		
		
		comentario = (EditText)findViewById(R.id.dialogo_califica_servicio_et_comentario);
		comentario.setTypeface(new fonts(Califica_taxi.this).getTypeFace(fonts.FLAG_ROJO));
		comentario.setTextColor(new fonts(Califica_taxi.this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		
		rank = (RatingBar)findViewById(R.id.dialogo_califica_servicio_ratingBarServicio);
		rank.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			

			public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
	 
				Scalificacion=(String.valueOf(rating));
	 
			}
		});
		calificar_aceptar =(Button)findViewById(R.id.dialogo_califica_servicio_btnAceptar);
		calificar_aceptar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				
				
				SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
				String placa = prefs.getString("placa", null);
				String face = prefs.getString("facebook","0");
				String id_usuario = prefs.getString("uuid", null);
				Scomentario=comentario.getText().toString().replaceAll(" ", "+");
				if(!Scomentario.equals("")){
					Calendar c = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd+HH:mm:ss");
					 String finViaje = sdf.format(c.getTime());
					 
					String url= "http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=addcomentario"
							+"&id_usuario="+id_usuario
							+"&calificacion="+Scalificacion
							+"&comentario="+Scomentario
							+"&placa="+placa
							+"&id_face="+face
							+"&pointinilat="+ServicioGeolocalizacion.latitud_inicial
							+"&pointinilon="+ServicioGeolocalizacion.longitud_inicial
							+"&pointfinlat="+ServicioGeolocalizacion.latitud
							+"&pointfinlon="+ServicioGeolocalizacion.longitud
							+"&horainicio="+ServicioGeolocalizacion.horaInicio
							+"&horafin="+finViaje;

					Utils.doHttpConnection(url);	
				}
				Mapa_tracking.isButtonExit= false;
			
				Intent svc = new Intent(Califica_taxi.this, ServicioGeolocalizacion.class);
				stopService(svc);
				ServicioGeolocalizacion.serviceIsIniciado=false;
				
				Dialogos.Toast(Califica_taxi.this, getResources().getString(R.string.dialogo_califica_servicio_enviar_comentario), Toast.LENGTH_LONG);
			
				Califica_taxi.this.finish();
				
			}
		});
		
	
		
	}

	

	


	@Override
	public void onBackPressed() {
	}
	
	@Override
	protected void onStart() {
		ServicioGeolocalizacion.stopNotification();
		super.onStart();
	}
	
}
