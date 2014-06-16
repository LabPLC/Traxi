package codigo.labplc.mx.traxi;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import android.app.Application;
import codigo.labplc.mx.traxi.log.HockeySender;
import codigo.labplc.mx.traxi.utils.Utils;

/**
 * clase que extiende de Aplicacion y contola los Log 
 * @author mikesaurio
 *
 */
@ReportsCrashes(formKey = "traxi", formUri ="http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=addlog" )
public class Traxi extends Application{
	@Override
	public void onCreate() {
		
		if(Utils.getPreferencia("prefSendReport",Traxi.this.getBaseContext())){
		 	String  envio= "http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=addlog";
	    	ACRA.init(this);
	    	HockeySender MySender = new HockeySender(Traxi.this,envio);
	        ACRA.getErrorReporter().setReportSender(MySender);
		}
		super.onCreate();
	}

	
	
	
	
}
