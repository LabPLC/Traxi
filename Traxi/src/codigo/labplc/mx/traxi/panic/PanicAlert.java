package codigo.labplc.mx.traxi.panic;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
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
	public void sendSMS(String phoneNumber, String message)
     { 
    	
    try{
		 SmsManager smsManager = SmsManager.getDefault();
       	 smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }catch(Exception e){
    	DatosLogBean.setDescripcion(Utils.getStackTrace(e)); 
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
    		// Log.d("***********", "intentando enviar correo");
    		 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
    		 StrictMode.setThreadPolicy(policy); 
             GMailSender sender = new GMailSender(correoRemitente,Utils.getMAilKey(context));
             sender.sendMail(cabecera, mensaje,correoRemitente, correoDestino);  
         } catch (Exception e) {  
        	 e.getMessage();
        	 
        	 DatosLogBean.setDescripcion(Utils.getStackTrace(e));  
         } 
     }
}
