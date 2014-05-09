package codigo.labplc.mx.trackxi.services;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.buscarplaca.paginador.DatosAuto;
import codigo.labplc.mx.trackxi.califica.Califica_taxi;
import codigo.labplc.mx.trackxi.dialogos.Dialogos;
import codigo.labplc.mx.trackxi.log.BeanDatosLog;
import codigo.labplc.mx.trackxi.panic.MyReceiver;
import codigo.labplc.mx.trackxi.panic.PanicAlert;
import codigo.labplc.mx.trackxi.tracking.Activity_null;
import codigo.labplc.mx.trackxi.tracking.map.Mapa_tracking;
import codigo.labplc.mx.trackxi.utils.Utils;

/**
 * 
 * @author mikesaurio
 * 
 */
public class ServicioGeolocalizacion extends Service implements Runnable {
	/**
	 * Declaraci—n de variables
	 */
	
	public final String TAG = this.getClass().getSimpleName();
	public static DatosAuto taxiActivity;
	private LocationManager mLocationManager;
	private MyLocationListener mLocationListener;
	public static double latitud_inicial = 19.0f;
	public static double longitud_inicial = -99.0f;
	public static double latitud =0;
	public static double longitud=0;
	public static String horaInicio;
	public static String horaFin;
	private Location currentLocation = null;
	private boolean isFirstLocation = true;
	private Thread thread;
	ArrayList<String> pointsLat = new ArrayList<String>();
	ArrayList<String> pointsLon = new ArrayList<String>();
	private boolean isFirstTime = true;
	private Timer timer,timerParanoico;
	public static boolean serviceIsIniciado = false;
	private BroadcastReceiver mReceiver;
	private ResultReceiver resultReceiver;
	private static int countStart = -1;
	private Handler handler_time = new Handler();
	public   Handler handler_time_panic = new Handler();
	private Handler handler_panic = new Handler();
	private String uuid;
	private String telemer;
	private String correoemer;
	private String telemer2;
	private String correoemer2;
	private String placa;
	PanicAlert panic;
	public static boolean countTimer = true;
	public static  boolean panicoActivado = false;
	public boolean isSendMesagge= false;
    private String timeLocation = "0";
    private int intervaloLocation =5000;
    private int intervaloLocation_mail =25000;
    private int intervaloLocationParanoia =0;
    private boolean algoPaso=true;
   private boolean isMailFirst=true;
private boolean panico;
    
    
    

	@Override
	public void onCreate() {
		super.onCreate();

		//obtenemos la hora en la que inicia el servicio
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd+HH:mm:ss");
		 horaInicio = sdf.format(c.getTime());
	
		   
		 //escucha para la location 
		mLocationListener = new MyLocationListener();
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		
		timer = new Timer();//timer para el boton de panico
		timerParanoico = new Timer();//timer para el modo paranohico

		  SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
           panico = prefs.getBoolean("panico", false);
		intervaloLocation = getPreferencia("prefSyncFrequency");//intervalo de busqueda
		if(panico){
		intervaloLocationParanoia  = 120000;//intervalo para mostrar el mensaje paranohico
		}
		// para le panic
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mReceiver = new MyReceiver();
		registerReceiver(mReceiver, filter);
	}
	
	
	

	@Override
	public void onStart(Intent intent, int startId) {
		//PAnic
		if(isFirstTime){
			obtenerSenalGPS();
			isFirstTime=false;
			serviceIsIniciado= true;
		}
			try{
					resultReceiver = intent.getParcelableExtra("receiver");
	
					// revisamos si la pantalla esta prendida o apagada y contamos el numero de click al boton de apagado
					boolean screenOn = intent.getBooleanExtra("screen_state", false);
					// si damos m‡s de 4 click al boton de apagado se activa la alarma
					if (countStart >= 4) {
						countStart = -1;
						countTimer = true;
						setPanicoActivado(true);
						alarmaActivada();
	
					} else {
						countStart += 1;
						// contamos 10 segundos si no reiniciamos los contadores
						if (countTimer) {
							countTimer = false;
							handler_time.postDelayed(runnable, 10000);// 10 segundos de espera
						}
					}
			}catch(Exception e){
				BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
			}
				
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		if (mLocationManager != null)
			if (mLocationListener != null)
				mLocationManager.removeUpdates(mLocationListener);

		//Toast.makeText(this, "Servicio detenido ", Toast.LENGTH_SHORT).show();
		super.onDestroy();
		CancelNotification(this, 0);
		timer.cancel();
		CancelNotification(this, 1);
		timerParanoico.cancel();
		
		serviceIsIniciado= false;
		Mapa_tracking.direccion_destino= null;
		// panic
		unregisterReceiver(mReceiver);
	}

	@Override
	public IBinder onBind(Intent intencion) {
		return null;
	}

/*	/**
	 * handler
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// mLocationManager.removeUpdates(mLocationListener);
			updateLocation(currentLocation);
		}
	};

	/**
	 * metodo para actualizar la localizaci—n
	 * 
	 * @param currentLocation
	 * @return void
	 */
	public void updateLocation(Location currentLocation) {
		if (currentLocation != null) {
			latitud = Double.parseDouble(currentLocation.getLatitude() + "");
			longitud = Double.parseDouble(currentLocation.getLongitude() + "");
			
		//	Log.d(TAG, "latitud"+latitud);
		//	Log.d(TAG, "longitud"+longitud);

			if (isFirstLocation) {
				latitud_inicial = latitud;
				longitud_inicial = longitud;
				isFirstLocation = false;
				showNotification();
				//si es que activo el nivel paranoico
				if(panico){
					mensajeParanoico();
				}
			}          

			pointsLat.add(latitud + "");
			pointsLon.add(longitud + "");
			
			Intent intent = new Intent("key");
			intent.putExtra("latitud", pointsLat);
			intent.putExtra("longitud", pointsLon);
			getApplicationContext().sendBroadcast(intent);

			if(isSendMesagge){

				enviaCorreo();
				isSendMesagge=false;
				
				//CancelNotification(this, 1);
				//timerParanoico.cancel();
			}
		}
	}

	public boolean yaPasaronMinutos(){
		
		
		return false;
	}
	/**
	 * Hilo de la aplicacion para cargar las cordenadas del usuario
	 */
	public void run() {
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Looper.prepare();
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, intervaloLocation, 1, mLocationListener);
			Looper.loop();
			Looper.myLooper().quit();
		} else {
			taxiActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Dialogos.Toast(taxiActivity,
							getResources().getString(R.string.GPS_OFF), Toast.LENGTH_LONG);
				}
			});
		}
	}

	/**
	 * Metodo para Obtener la se–al del GPS
	 */
	private void obtenerSenalGPS() {
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Metodo para asignar las cordenadas del usuario
	 * */
	private void setCurrentLocation(Location loc) {
		currentLocation = loc;
	}

	/**
	 * Metodo para obtener las cordenadas del GPS
	 */
	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location loc) {
			// Log.d("finura",loc.getAccuracy()+"");
			if (loc != null) {
				setCurrentLocation(loc);
				handler.sendEmptyMessage(0);
			}
		}

		/**
		 * metodo que revisa si el GPS esta apagado
		 */
		public void onProviderDisabled(String provider) {
			taxiActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Dialogos.Toast(taxiActivity,
							getResources().getString(R.string.GPS_OFF), Toast.LENGTH_LONG);
				}
			});
		}

		// @Override
		public void onProviderEnabled(String provider) {
		}

		// @Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	public static  void showNotification() {
		// notification is selected
		Vibrator v = (Vibrator) taxiActivity.getSystemService(Context.VIBRATOR_SERVICE);
    	v.vibrate(3000);

		Intent intent_mapa = new Intent(taxiActivity, Mapa_tracking.class);
		intent_mapa.putExtra("latitud_inicial", ServicioGeolocalizacion.latitud_inicial);
		intent_mapa.putExtra("longitud_inicial", ServicioGeolocalizacion.longitud_inicial);
		PendingIntent pIntent = PendingIntent.getActivity(taxiActivity, 0, intent_mapa,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_CANCEL_CURRENT);

		Intent intent_califica = new Intent(taxiActivity, Califica_taxi.class);
		PendingIntent pIntent_cal = PendingIntent.getActivity(taxiActivity, 0,intent_califica, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification noti = new Notification.Builder(taxiActivity)
				.setContentTitle("Traxi").setContentText(taxiActivity.getResources().getString(R.string.notificacion_que_quieres_hacer))
				.setSmallIcon(R.drawable.ic_launcher)
		
				// .setContentIntent(pIntent)
				.addAction(R.drawable.ic_launcher_chinche, taxiActivity.getResources().getString(R.string.notificacion_viaje), pIntent)
				.addAction(R.drawable.ic_launcher_fin_viaje, taxiActivity.getResources().getString(R.string.notificacion_finalizar),
						pIntent_cal).build();
		
		
		noti.flags += Notification.FLAG_ONGOING_EVENT;
		
		NotificationManager notificationManager = (NotificationManager) taxiActivity.getSystemService(NOTIFICATION_SERVICE);
		// noti.flags |= Notification.FLAG_AUTO_CANCEL;
		
	
		notificationManager.notify(0, noti);
		
		
	}

	public static void CancelNotification(Context ctx, int notifyId) {
	try{
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
		nMgr.cancel(notifyId);
	}catch(Exception e){
		BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
	}
	}

	// panic
	 /**
     * hilo que al pasar el tiempo reeinicia los valores
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //reiniciamos los contadores
            countStart = -1;
            countTimer = true;
        }
    };

    
    public void enviaCorreo(){
    	timer.scheduleAtFixedRate(new TimerTask() {

    	    @Override
    	    public void run() {
    	    	//nos sercioramos que envie un mail a la vez
    if(isMailFirst){
    	    	panic.sendMail("TRAXI",
    	    			getResources().getString(R.string.panic_estoy_en_peligro)+placa+
    	    			getResources().getString(R.string.panic_ubicacion)+latitud+","+longitud+getResources().getString(R.string.panic_bateria)+ 
    	    			panic.getLevelBattery()+"%",
						getResources().getString(R.string.correo), 
						correoemer);
    	    	isMailFirst=false;
    }else{
    	    	panic.sendMail("TRAXI",
    	    			getResources().getString(R.string.panic_estoy_en_peligro)+placa+
    	    			getResources().getString(R.string.panic_ubicacion)+latitud+","+longitud+getResources().getString(R.string.panic_bateria)+ 
    	    			panic.getLevelBattery()+"%",
						getResources().getString(R.string.correo), 
						correoemer2);
    	    	isMailFirst=true;
    }
    	    }
    	},
    	0,
    	intervaloLocation_mail);

    }
    
    
   public void mensajeParanoico(){
    	timerParanoico.scheduleAtFixedRate(new TimerTask() {
    	    @Override
    	    public void run() {
    	    	showNotificationPanic();
    	    	
    	    }
    	},
    	intervaloLocationParanoia,
    	intervaloLocationParanoia);

    }
    
   public void showNotificationPanic() {
		// notification is selected
		Vibrator v = (Vibrator) taxiActivity.getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(4000);
		 
		String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        
		 int icon = R.drawable.ic_launcher;
         CharSequence tickerText = "Traxi";
         long when = System.currentTimeMillis();
         int requestID = (int) System.currentTimeMillis();
         Notification notification = new Notification(icon, tickerText, when);
         Context context = getApplicationContext();
         Intent notificationIntent = new Intent(this, Activity_null.class);

         PendingIntent contentIntent = PendingIntent.getActivity(this, requestID, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
         notification.setLatestEventInfo(context,getResources().getString(R.string.panic_todo_bien), getResources().getString(R.string.panic_tocame), contentIntent);
                 notification.flags += Notification.FLAG_ONGOING_EVENT;
                 notification.flags += Notification.FLAG_AUTO_CANCEL;
                 AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                 alarmManager.set(AlarmManager.RTC_WAKEUP,0, contentIntent);
                 
             mNotificationManager.notify(1, notification);
         
             //creamos un hilo de espera si en un minuto no se cancela este hilo se lanzara el mensaje de emergencia
             
             setPanicoActivado(true);
             handler_time_panic.postDelayed(runnable_panic, 60000);// 1 minuto d eespera
       
	}
  

    /**
     * obtiene el valor de frecuencia de las preferencia 
     * @param preferencia
     * @return (int)intervalo de tiempo
     */
    public int getPreferencia(String preferencia){
    	SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		timeLocation = sharedPrefs.getString(preferencia, "0");
		if(timeLocation.equals("0")){
			return 10000;
		}else if(timeLocation.equals("1")){
			return 60000;
		}else if(timeLocation.equals("2")){
			return 120000;
		}else if(timeLocation.equals("5")){
			return 300000;
		}else{
			return 10000;
		}
    } 
    
    public static  void setPanicoActivado(boolean flag)
	{
    	ServicioGeolocalizacion.panicoActivado=flag;
    	
    	
	}
    
    public  boolean getPanicoActivado()
   	{
       return	ServicioGeolocalizacion.panicoActivado;
   	}
    
    //detiene la notificacion cuando se abre la actividad mapa
    public static void  stopNotification(){
    	CancelNotification(taxiActivity, 0);
    }
    
    
 // panic
 	 /**
      * hilo que al pasar el tiempo reeinicia los valores
      */
     private  Runnable runnable_panic = new Runnable() {
         @Override
         public void run() {
        	 	//despues de 1 min se lanza el mensaje de panico
        	 alarmaActivada();
         }
     };
     
     /**
      * cuando se active el panico el tiempo de busqueda cambia al minimo
      */
     public void alarmaActivada() {
    	 
    	try{
    	 if(getPanicoActivado()){
    	 if(algoPaso){
    		// Log.d(TAG, "enviando mensaje de panico");
    		   Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
			   v.vibrate(3000);
			   SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
	           uuid = prefs.getString("uuid", null);
	           telemer = prefs.getString("telemer", null);
	           correoemer = prefs.getString("correoemer", null);
	           telemer2 = prefs.getString("telemer2", null);
	           correoemer2 = prefs.getString("correoemer2", null);
	           placa = prefs.getString("placa", null); 
	           panic = new PanicAlert(ServicioGeolocalizacion.this.getApplicationContext());
	           panic.activate();
	           String mensajeEmer= getResources().getString(R.string.sms_emer);
	           panic.sendSMS(telemer,mensajeEmer);
	           panic.sendSMS(telemer2,mensajeEmer);
	           isSendMesagge=true;
	           algoPaso=false;
	         
	           //cambiar los tiempos de panico al minimo
	           intervaloLocation =5000;
			
			CancelNotification(this, 1);
			timerParanoico.cancel();//dejamos de mostrar si esta bien dado que no lo est‡
		}
     }
    	}catch(Exception e){
    		BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
    	}
     }
     

    
}