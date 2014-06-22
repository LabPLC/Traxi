package codigo.labplc.mx.traxi.configuracion;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import codigo.labplc.mx.traxi.R;

/**
 * crea las preferencias de la configuracion de la app
 * 
 * @author mikesaurio
 *
 */
@SuppressWarnings("deprecation")
public class UserSettingActivity extends PreferenceActivity {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.settings);
		

	}
}
