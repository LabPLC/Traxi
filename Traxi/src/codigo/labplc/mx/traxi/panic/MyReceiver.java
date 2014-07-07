package codigo.labplc.mx.traxi.panic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.log.DatosLogBean;
import codigo.labplc.mx.traxi.services.ServicioGeolocalizacion;
import codigo.labplc.mx.traxi.utils.Utils;

/**
 * revisa si la pantalla esta encendida o apagada
 * Created by mikesaurio on 04/12/13.
 */
public class MyReceiver extends BroadcastReceiver {
    private boolean screenOff;

    @Override
    public void onReceive(Context context, Intent intent) {
    	if(Intent.ACTION_SHUTDOWN.equalsIgnoreCase(intent.getAction())) {
	       	  SharedPreferences prefs = context.getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
	      	  TelephonyManager tMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
	      	  
	          
	          try {  
	        	  if(!prefs.getString("placa", null).toString().equals("null")){
		     		 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		     		 StrictMode.setThreadPolicy(policy); 
		             GMailSender sender = new GMailSender(context.getResources().getString(R.string.correo),Utils.getMAilKey(context));
		             sender.sendMail("TRAXI", context.getResources().getString(R.string.panic_cell_off)+prefs.getString("placa", null)+context.getResources().getString(R.string.panic_bateria)+ 
			    			new PanicAlert(context).getLevelBattery()+"%"+", "+tMgr.getLine1Number()+context.getResources().getString(R.string.panic_mensaje_cuerpo),
			    			context.getResources().getString(R.string.correo), prefs.getString("correoemer", null)); 
		            
		             SharedPreferences.Editor editor = prefs.edit();
						editor.putString("placa", null);
						editor.commit();
	        	  } 
	        } catch (Exception e) {   
	         	 DatosLogBean.setDescripcion(Utils.getStackTrace(e));  
	          } 
    	}
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOff = true;
            comunicacion(context);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOff = false;
            comunicacion(context);
        }
    }
    
    public void comunicacion(Context context){
    	Intent i = new Intent(context, ServicioGeolocalizacion.class);
        i.putExtra("screen_state", screenOff);
        context.startService(i);
    }

}
