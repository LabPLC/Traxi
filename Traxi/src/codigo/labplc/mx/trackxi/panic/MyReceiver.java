package codigo.labplc.mx.trackxi.panic;

import codigo.labplc.mx.trackxi.services.ServicioGeolocalizacion;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by mikesaurio on 04/12/13.
 */
public class MyReceiver extends BroadcastReceiver {
    private boolean screenOff;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOff = true;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            screenOff = false;
        }
        Intent i = new Intent(context, ServicioGeolocalizacion.class);
        i.putExtra("screen_state", screenOff);
        context.startService(i);
    }

}
