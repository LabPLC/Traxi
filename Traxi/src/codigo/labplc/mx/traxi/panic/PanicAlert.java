package codigo.labplc.mx.traxi.panic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.StrictMode;
import android.os.Vibrator;
import android.telephony.SmsManager;
import codigo.labplc.mx.traxi.log.DatosLogBean;
import codigo.labplc.mx.traxi.utils.Utils;
/**
 * Clase que envia los SMS y correos al haber un mensaje de panico
 * @author mikesaurio
 *
 */
public class PanicAlert {

	Context context;
	int levelBattery = 0;
	
	public PanicAlert(Context context) {
       this.context=context;
    }

	/**
	 * al activarse el selular vibra
	 */
    public void activate() {
    	Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    	v.vibrate(3000);
    	

    }
    
      /**
       * obtiene el nivel de la bateria
       * @return (int) nivel de bateria
       */
     public int getLevelBattery(){
    	 Intent i = new ContextWrapper(context).registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    	return i.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
     }
     
     
     /**
      * envia un SMS
      * @param phoneNumber (String) numero de emergencia
      * @param message (String) mensaje de emergencia
      */ 
	@SuppressLint("NewApi")
	public void sendSMS(String phoneNumber, String message)
     { 
    	
    	 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // Android 4.4 and up
    	 {
    	     //validacion para kitkat
    	     
    	 }else{
    		SmsManager smsManager = SmsManager.getDefault();
       	 smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    	}
    	 

      }

     /**
      * envia un correo de emergencia
      * @param cabecera (String) titulo del correo
      * @param mensaje (String) mensaje del correo
      * @param correoRemitente (String) correo que envia
      * @param correoDestino (String) correo que recibe
      */
     public void sendMail(String cabecera,String mensaje,String correoRemitente,String correoDestino ){
    	 try {   
    		 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    		 StrictMode.setThreadPolicy(policy); 
             GMailSender sender = new GMailSender(correoRemitente,Utils.getMAilKey(context));
             sender.sendMail(cabecera, mensaje,correoRemitente, correoDestino);  
         } catch (Exception e) {   
        	 DatosLogBean.setDescripcion(Utils.getStackTrace(e));  
         } 
     }
}
