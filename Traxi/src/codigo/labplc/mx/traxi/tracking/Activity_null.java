package codigo.labplc.mx.traxi.tracking;

import android.app.Activity;
import android.os.Bundle;
import codigo.labplc.mx.traxi.services.ServicioGeolocalizacion;

/**
 * clase que se abre al dar clic a la notificacion modo paranoico avisa que el usuario no esta en peligor
 * 
 * @author mikesaurio
 *
 */
public class Activity_null extends Activity {

	public static int cuenta = 0;
	public static int cuenta_dest = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.finish();

	}

	@Override
	protected void onDestroy() {
		if (cuenta > 0) {
			ServicioGeolocalizacion.setPanicoActivado(false);
			cuenta = 0;
		} else {
			cuenta += 1;
		}
		super.onDestroy();
	}

}
