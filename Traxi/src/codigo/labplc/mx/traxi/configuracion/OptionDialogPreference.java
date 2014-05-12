package codigo.labplc.mx.traxi.configuracion;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

/**
 * The OptionDialogPreference will display a dialog, and will persist the
 * <code>true</code> when pressing the positive button and <code>false</code>
 * otherwise. It will persist to the android:key specified in xml-preference.
 */
public class OptionDialogPreference extends DialogPreference {

    public OptionDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        persistBoolean(positiveResult);
    }

}