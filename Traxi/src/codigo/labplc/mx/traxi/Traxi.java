package codigo.labplc.mx.traxi;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import codigo.labplc.mx.traxi.log.HockeySender;

@ReportsCrashes(formKey = "traxi", formUri ="http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=addlog" )
public class Traxi extends Application{

	@Override
	public void onCreate() {
		
		if(getPreferencia("prefSendReport")){
		 	String  envio= "http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=addlog";
	    	ACRA.init(this);
	    	HockeySender MySender = new HockeySender(Traxi.this,envio);
	        ACRA.getErrorReporter().setReportSender(MySender);
		}
		super.onCreate();
	}

	
	/**
	 * metodo que crea y regresa ek estado de as oreferencias
	 * @param preferencia
	 * @return
	 */
	private boolean getPreferencia(String preferencia) {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		return sharedPrefs.getBoolean(preferencia, true);
	}

}
