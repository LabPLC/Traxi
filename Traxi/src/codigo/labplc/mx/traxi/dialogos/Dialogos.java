package codigo.labplc.mx.traxi.dialogos;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.fonts.fonts;

/**
 * Clase que maneja los di‡logos 
 * @author mikesaurio
 *
 */
public class Dialogos {


	private AlertDialog customDialog= null;	//Creamos el dialogo generico

/**
 * Toast custom 
 * @param context (Activity) actividad que lo llama
 * @param text (String) texto a mostrar
 * @param duration (int) duracion del toast
 */
	public static void Toast(Activity context, String text, int duration) {
		LayoutInflater inflater = context.getLayoutInflater();
		View layouttoast = inflater.inflate(R.layout.toastcustom, (ViewGroup)context.findViewById(R.id.toastcustom));
		((TextView) layouttoast.findViewById(R.id.texttoast)).setText(text);
		((TextView) layouttoast.findViewById(R.id.texttoast)).setTextColor(context.getResources().getColor(R.color.color_vivos));
		Toast mytoast = new Toast(context);
        mytoast.setView(layouttoast);
        mytoast.setDuration(Toast.LENGTH_LONG);
        mytoast.show();
	}
	
	
	/**
	 * Log sobreescrito
	 * @param context  (Activity) actividad que lo llama
	 * @param text (String) texto a mostrar
	 * @param type (int) tipo del log
	 */
	public static void Log(Context context, String text, int type) {
		if(type == Log.DEBUG) {
			Log.d(context.getClass().getName().toString(), text);
		} else if(type == Log.ERROR) {
			Log.e(context.getClass().getName().toString(), text);
		} else if(type == Log.INFO) {
			Log.i(context.getClass().getName().toString(), text);
		} else if(type == Log.VERBOSE) {
			Log.v(context.getClass().getName().toString(), text);
		} else if(type == Log.WARN) {
			Log.w(context.getClass().getName().toString(), text);
		}
	}
	
	
	/**
	 * Dialogo que muestra el para que registrarte con datos
	 *
	 * @param Activity (actividad que llama al di‡logo)
	 * @return Dialog (regresa el dialogo creado)
	 **/
	public Dialog mostrarParaQue(Activity activity)
    {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    View view = activity.getLayoutInflater().inflate(R.layout.dialogo_paraque, null);
	    builder.setView(view);
	    builder.setCancelable(false);
        //tipografias
	    ((Button) view.findViewById(R.id.dialogo_paraque_btnAceptar)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    
	    ((TextView) view.findViewById(R.id.dialogo_paraque_tv_titulo)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_paraque_tv_titulo)).setTextColor(activity.getResources().getColor(R.color.color_vivos));
	    
	    ((TextView) view.findViewById(R.id.dialogo_paraque_tv_correo)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_paraque_tv_correo)).setTextColor(activity.getResources().getColor(R.color.color_vivos));
	    
	    
	  
	    ((TextView) view.findViewById(R.id.dialogo_paraque_tv_nombre)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_paraque_tv_nombre)).setTextColor(activity.getResources().getColor(R.color.color_vivos));
	    
	    ((TextView) view.findViewById(R.id.dialogo_paraque_tv_subtitulo)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_paraque_tv_subtitulo)).setTextColor(activity.getResources().getColor(R.color.color_vivos));
	    

	    
	  //escucha del boton aceptar
        ((Button) view.findViewById(R.id.dialogo_paraque_btnAceptar)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                customDialog.dismiss();    
            }
        });
        return (customDialog=builder.create());// return customDialog;//regresamos el di‡logo
    }   
	
	
	
	
	
	/**
	 * Dialogo para asegurar que quieres salir de la app
	 *
	 * @param Activity (actividad que llama al di‡logo)
	 * @return Dialog (regresa el dialogo creado)
	 **/
	
	public Dialog showDialogGPS(final Activity activity)
    {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    View view = activity.getLayoutInflater().inflate(R.layout.dialogo_gps, null);
	    builder.setView(view);
	    builder.setCancelable(true);
        //tipografias
	    ((Button) view.findViewById(R.id.dialogo_salir_btnAceptar)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    ((Button) view.findViewById(R.id.dialogo_salir_btnCancelar)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    
	    ((TextView) view.findViewById(R.id.dialogo_salir_tv_nombre)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_salir_tv_nombre)).setTextColor(activity.getResources().getColor(R.color.color_vivos));
	    
	    ((TextView) view.findViewById(R.id.dialogo_salir_tv_subtitulo)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_salir_tv_subtitulo)).setTextColor(activity.getResources().getColor(R.color.color_vivos));
	    
	    
	  //escucha del boton aceptar
        ((Button) view.findViewById(R.id.dialogo_salir_btnAceptar)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
            	 customDialog.dismiss(); 
            	 Intent settingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
 				settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
 				activity.startActivity(settingsIntent);
            }
        });

        ((Button) view.findViewById(R.id.dialogo_salir_btnCancelar)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                customDialog.dismiss();    
            }
        });
        return (customDialog=builder.create());// return customDialog;//regresamos el di‡logo
    }   
	
	
	
	/**
	 * Dialogo que muestra el acerca de
	 *
	 * @param Activity (actividad que llama al di‡logo)
	 * @return Dialog (regresa el dialogo creado)
	 **/
	public Dialog mostrarAercaDe(Activity activity)
    {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    View view = activity.getLayoutInflater().inflate(R.layout.dialogo_acercade, null);
	    builder.setView(view);
	    builder.setCancelable(false);
        //tipografias
	    ((Button) view.findViewById(R.id.dialogo_acercade_btnAceptar)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    
	    
	    ((TextView) view.findViewById(R.id.dialogo_acercade_tv_correo)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_acercade_tv_correo)).setTextColor(activity.getResources().getColor(R.color.color_vivos));
	    

	    
	 

	    
	  //escucha del boton aceptar
        ((Button) view.findViewById(R.id.dialogo_acercade_btnAceptar)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                customDialog.dismiss();    
            }
        });
        return (customDialog=builder.create());// return customDialog;//regresamos el di‡logo
    }   
	
}
