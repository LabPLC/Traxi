package codigo.labplc.mx.traxi.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.buscarplaca.paginador.DatosAuto;
import codigo.labplc.mx.traxi.califica.Califica_taxi;
import codigo.labplc.mx.traxi.dialogos.Dialogos;
import codigo.labplc.mx.traxi.log.DatosLogBean;
import codigo.labplc.mx.traxi.panic.MyReceiver;
import codigo.labplc.mx.traxi.panic.PanicAlert;
import codigo.labplc.mx.traxi.tracking.Activity_null;
import codigo.labplc.mx.traxi.tracking.map.Mapa_tracking;
import codigo.labplc.mx.traxi.utils.Utils;

/**
 * Servicio que controla todos los procesos en segundo plano
 * 
 * @author mikesaurio
 * 
 */

@SuppressLint({"SimpleDateFormat","HandlerLeak","NewApi"})
@SuppressWarnings({"deprecation","unused"})

public class ServicioGeolocalizacion extends Service implements Runnable {
	
	/*
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
   private boolean isActivado= false;
   private boolean flag_una_vez = true;
   public static Service serv_;
   private String tipo_locacion= LocationManager.GPS_PROVIDER;
   private String mPhoneNumber;

    
    

	
	@Override
	public void onCreate() {
		super.onCreate();
 
		serv_ = ServicioGeolocalizacion.this;
		
		//obtenemos la hora en la que inicia el servicio
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd+HH:mm:ss");
		 horaInicio = sdf.format(c.getTime());
	
		
		 if(Utils.getPreferencia("prefBusquedaFina",this.getBaseContext(),true)){
			 tipo_locacion= LocationManager.GPS_PROVIDER;
		 }else{
			 tipo_locacion= LocationManager.NETWORK_PROVIDER; 
		 }  
		 
		 //escucha para la location 
		mLocationListener = new MyLocationListener();
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		
		timer = new Timer();//timer para el boton de panico

		// para le panic
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mReceiver = new MyReceiver();
		registerReceiver(mReceiver, filter);
	}
	
	
	

	@Override
	public void onStart(Intent intent, int startId) {
		//Panic
		if(isFirstTime){
			obtenerSenalGPS();
			isFirstTime=false;
			serviceIsIniciado= true;
		}
			try{
					resultReceiver = intent.getParcelableExtra("receiver");
	
					// revisamos si la pantalla esta prendida o apagada y contamos el numero de click al boton de apagado
					boolean screenOn = intent.getBooleanExtra("screen_state", false);
					if(!screenOn&&flag_una_vez ){ //si la pantalla esta apagada son 5 veces prendida son 6 veces
						countStart = 0;
					}
					// si damos m‡s de 4 click al boton de apagado se activa la alarma
					if (countStart >= 4) {
						countStart = -1;
						countTimer = true;
						flag_una_vez=true;
						SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
				        String telemer_local = prefs.getString("telemer", null);
				        if(telemer_local!=null){//revisamos si tiene por lo menos un contactos de emergencia
							setPanicoActivado(true);
							alarmaActivada();
				        }
					} else {
						flag_una_vez=false;
						countStart += 1;
						// contamos 10 segundos si no reiniciamos los contadores
						if (countTimer) {
							countTimer = false;
							handler_time.postDelayed(runnable, 10000);// 10 segundos de espera
						}
					}
			}catch(Exception e){
				DatosLogBean.setDescripcion(Utils.getStackTrace(e));
			}
				
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		  super.onDestroy();
		if (mLocationManager != null)
			if (mLocationListener != null){
				mLocationManager.removeUpdates(mLocationListener);
				mLocationManager=null;
			}

		
		CancelNotification(this, 0);
		timer.cancel();
		CancelNotification(this, 1);
		
		if(timerParanoico!=null){
			timerParanoico.cancel();
		}
		
		serviceIsIniciado= false;
		 Mapa_tracking.isButtonExit = true;
		Mapa_tracking.direccion_destino= null;
		
	
		// panic
		unregisterReceiver(mReceiver);
	
	      
	}

	@Override
	public IBinder onBind(Intent intencion) {
		return null;
	}


	/**
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


			if (isFirstLocation) {
				latitud_inicial = latitud;
				longitud_inicial = longitud;
				isFirstLocation = false;
			//	showNotification();//mike
			}  
			
			SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
	        panico = prefs.getBoolean("panico", false);
	        if(panico&&!isActivado){//si se da clic para activar modo paranoico
	        	timerParanoico = new Timer();//timer para el modo paranohico
	        	intervaloLocationParanoia  = 120000;
				mensajeParanoico();
			}else if(!panico&&isActivado){//si se desactiva el modo paranoico
				isActivado=false;
				intervaloLocationParanoia =0;
				CancelNotification(this, 1);
				timerParanoico.cancel();//dejamos de mostrar si esta bien dado que no lo est‡
				timerParanoico.purge();
				timerParanoico = null;
				
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


	/**
	 * Hilo de la aplicacion para cargar las cordenadas del usuario
	 */
	public void run() {
		
		if (mLocationManager.isProviderEnabled(tipo_locacion)) {
			Looper.prepare();
			mLocationManager.requestLocationUpdates(tipo_locacion, intervaloLocation, 1, mLocationListener);
			Looper.loop();
			Looper.myLooper().quit();
		} else {
			if(taxiActivity!=null){
				taxiActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Dialogos.Toast(taxiActivity,getResources().getString(R.string.GPS_OFF), Toast.LENGTH_LONG);
						
					}
				});
			}
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
	 *  SET Metodo para asignar las cordenadas del usuario
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
try{
		Intent intent_mapa = new Intent(taxiActivity, Mapa_tracking.class);
		intent_mapa.putExtra("latitud_inicial", ServicioGeolocalizacion.latitud_inicial);
		intent_mapa.putExtra("longitud_inicial", ServicioGeolocalizacion.longitud_inicial);
		
		PendingIntent pIntent = PendingIntent.getActivity(taxiActivity, 0, intent_mapa,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_CANCEL_CURRENT);

		Intent intent_califica = new Intent(taxiActivity, Califica_taxi.class);
		PendingIntent pIntent_cal = PendingIntent.getActivity(taxiActivity, 0,intent_califica, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_CANCEL_CURRENT);
		

		Notification noti = new Notification.Builder(taxiActivity)
				.setContentTitle("Traxi")
				.setContentText(taxiActivity.getResources().getString(R.string.notificacion_que_quieres_hacer))
				.setSmallIcon(R.drawable.ic_launcher)
		
				.addAction(R.drawable.ic_launcher_chinche, taxiActivity.getResources().getString(R.string.notificacion_viaje), pIntent)
				.addAction(R.drawable.ic_launcher_fin_viaje_blanco, taxiActivity.getResources().getString(R.string.notificacion_finalizar),pIntent_cal)
				.build();
		
		
		noti.flags += Notification.FLAG_ONGOING_EVENT;
		
		NotificationManager notificationManager = (NotificationManager) taxiActivity.getSystemService(NOTIFICATION_SERVICE);
		
		notificationManager.notify(0, noti);
		}catch(Exception e){
				Mapa_tracking.isButtonExit= false;
				serv_.stopSelf();
		}
		
	}

	/**
	 * cancela una notificacion
	 * @param ctx (Contexto)
	 * @param notifyId (int) id de la notificacion
	 */
	public static void CancelNotification(Context ctx, int notifyId) {
	try{
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
		nMgr.cancel(notifyId);
	}catch(Exception e){
		DatosLogBean.setDescripcion(Utils.getStackTrace(e));
	}
	}


	 /**
     * hilo que al pasar el tiempo reeinicia los valores
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //reiniciamos los contadores
        	flag_una_vez=true;
            countStart = -1;
            countTimer = true;
        }
    };

    /**
     * holo que envia los correos
     */
    public void enviaCorreo(){
    	timer.scheduleAtFixedRate(new TimerTask() {

    	    @Override
    	    public void run() {
    	    	//nos sercioramos que envie un mail a la vez
    	    	if(taxiActivity!=null){
    if(isMailFirst){
    	if(correoemer!=null){
    	    	panic.sendMail("TRAXI",
    	    			getResources().getString(R.string.panic_estoy_en_peligro)+placa+
    	    			getResources().getString(R.string.panic_ubicacion)+latitud+","+longitud+getResources().getString(R.string.panic_bateria)+ 
    	    			panic.getLevelBattery()+"%"+" "+mPhoneNumber+getResources().getString(R.string.panic_mensaje_cuerpo),
						getResources().getString(R.string.correo), 
						correoemer);
    	}
    	    	isMailFirst=false;
    }else{
    	if(correoemer2!=null){
    	    	panic.sendMail("TRAXI",
    	    			getResources().getString(R.string.panic_estoy_en_peligro)+placa+
    	    			getResources().getString(R.string.panic_ubicacion)+latitud+","+longitud+getResources().getString(R.string.panic_bateria)+ 
    	    			panic.getLevelBattery()+"%"+" "+mPhoneNumber+getResources().getString(R.string.panic_mensaje_cuerpo),
						getResources().getString(R.string.correo), 
						correoemer2);
    	}
    	    	isMailFirst=true;
    }
    	    	}else{
    	    		timer.cancel();
    	    	}
    	    }
    	},
    	0,
    	intervaloLocation_mail);

    }
    
    
    /**
     *  hilo que muestra la notificacion en modo paranoico
     */
   public void mensajeParanoico(){
	   isActivado=true;
    	timerParanoico.scheduleAtFixedRate(new TimerTask() {
    	    @Override
    	    public void run() {
    	    	showNotificationPanic();
    	    	
    	    }
    	},
    	intervaloLocationParanoia,
    	intervaloLocationParanoia);

    }
    
   /**
    * muestra la notificacion de panico
    */

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
    
    /**
     * SET panico activado
     * @param flag(boolean) true si esta activado
     */
    public static  void setPanicoActivado(boolean flag)
	{
    	ServicioGeolocalizacion.panicoActivado=flag;
    	
    	
	}
    
    /**
     * GET panico activado
     * @return (boolean) true si esta activado
     */
    public  boolean getPanicoActivado()
   	{
       return	ServicioGeolocalizacion.panicoActivado;
   	}
    
    /**
     * detiene la notificacion cuando se abre la actividad mapa
     */
    public static void  stopNotification(){
    	CancelNotification(taxiActivity, 0);
    }
    
    

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
    		   Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
			   v.vibrate(3000);
			   TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
   	    	 mPhoneNumber = tMgr.getLine1Number();
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
	           if(telemer!=null){
	        	   panic.sendSMS(telemer,mensajeEmer);
	           }
	          if(telemer2!=null){
	           panic.sendSMS(telemer2,mensajeEmer);
	          }
	           isSendMesagge=true;
	           algoPaso=false;
	         
	           //cambiar los tiempos de panico al minimo
	           intervaloLocation =5000;
			
			CancelNotification(this, 1);
			timerParanoico.cancel();//dejamos de mostrar si esta bien dado que no lo est‡
		}
     }
    	}catch(Exception e){
    		DatosLogBean.setDescripcion(Utils.getStackTrace(e));
    	}
     }
     
     
  
    
}