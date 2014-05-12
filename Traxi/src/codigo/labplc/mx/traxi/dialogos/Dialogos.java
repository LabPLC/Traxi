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
 * Clase que maneja los diálogos 
 * @author mikesaurio
 *
 */
public class Dialogos {


	private AlertDialog customDialog= null;	//Creamos el dialogo generico


	public static void Toast(Activity context, String text, int duration) {
		LayoutInflater inflater = context.getLayoutInflater();
		View layouttoast = inflater.inflate(R.layout.toastcustom, (ViewGroup)context.findViewById(R.id.toastcustom));
		((TextView) layouttoast.findViewById(R.id.texttoast)).setText(text);
		((TextView) layouttoast.findViewById(R.id.texttoast)).setTextColor(context.getResources().getColor(R.color.rojo_logo));
		Toast mytoast = new Toast(context);
        mytoast.setView(layouttoast);
        mytoast.setDuration(Toast.LENGTH_LONG);
        mytoast.show();
		//Toast.makeText(context, text, duration).show();
	}
	
	
	
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
	 * @param Activity (actividad que llama al diálogo)
	 * @return Dialog (regresa el dialogo creado)
	 **/
	@SuppressWarnings("static-access")
	public Dialog mostrarParaQue(Activity activity)
    {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    View view = activity.getLayoutInflater().inflate(R.layout.dialogo_paraque, null);
	    builder.setView(view);
	    builder.setCancelable(false);
        //tipografias
	    ((Button) view.findViewById(R.id.dialogo_paraque_btnAceptar)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_AMARILLO));
	    
	    ((TextView) view.findViewById(R.id.dialogo_paraque_tv_titulo)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_paraque_tv_titulo)).setTextColor(new fonts(activity).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
	    
	    ((TextView) view.findViewById(R.id.dialogo_paraque_tv_correo)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_paraque_tv_correo)).setTextColor(new fonts(activity).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
	    
	    
	  
	    ((TextView) view.findViewById(R.id.dialogo_paraque_tv_nombre)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_paraque_tv_nombre)).setTextColor(new fonts(activity).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
	    
	    ((TextView) view.findViewById(R.id.dialogo_paraque_tv_subtitulo)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_paraque_tv_subtitulo)).setTextColor(new fonts(activity).getColorTypeFace(fonts.FLAG_ROJO));
	    

	    
	  //escucha del boton aceptar
        ((Button) view.findViewById(R.id.dialogo_paraque_btnAceptar)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                customDialog.dismiss();    
            }
        });
        return (customDialog=builder.create());// return customDialog;//regresamos el diálogo
    }   
	
	/**
	 * Dialogo para asegurar que quieres salir de la app
	 *
	 * @param Activity (actividad que llama al diálogo)
	 * @return Dialog (regresa el dialogo creado)
	 **/
	
	public Dialog seguroQuiereSalir(final Activity activity)
    {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    View view = activity.getLayoutInflater().inflate(R.layout.dialogo_salir, null);
	    builder.setView(view);
	    builder.setCancelable(true);
        //tipografias
	    ((Button) view.findViewById(R.id.dialogo_salir_btnAceptar)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_AMARILLO));
	    ((Button) view.findViewById(R.id.dialogo_salir_btnCancelar)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_AMARILLO));
	    
	    ((TextView) view.findViewById(R.id.dialogo_salir_tv_titulo)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_salir_tv_titulo)).setTextColor(new fonts(activity).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
	    
	    
	    ((TextView) view.findViewById(R.id.dialogo_salir_tv_nombre)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_salir_tv_nombre)).setTextColor(new fonts(activity).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
	  //escucha del boton aceptar
        ((Button) view.findViewById(R.id.dialogo_salir_btnAceptar)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
            	 customDialog.dismiss(); 
            	activity.finish();
            }
        });

        ((Button) view.findViewById(R.id.dialogo_salir_btnCancelar)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                customDialog.dismiss();    
            }
        });
        return (customDialog=builder.create());// return customDialog;//regresamos el diálogo
    }   
	
	
	
	/**
	 * Dialogo para asegurar que quieres salir de la app
	 *
	 * @param Activity (actividad que llama al diálogo)
	 * @return Dialog (regresa el dialogo creado)
	 **/
	
	public Dialog showDialogGPS(final Activity activity)
    {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    View view = activity.getLayoutInflater().inflate(R.layout.dialogo_gps, null);
	    builder.setView(view);
	    builder.setCancelable(true);
        //tipografias
	    ((Button) view.findViewById(R.id.dialogo_salir_btnAceptar)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_AMARILLO));
	    ((Button) view.findViewById(R.id.dialogo_salir_btnCancelar)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_AMARILLO));
	    
	    ((TextView) view.findViewById(R.id.dialogo_salir_tv_titulo)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_salir_tv_titulo)).setTextColor(new fonts(activity).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
	    
	    
	    ((TextView) view.findViewById(R.id.dialogo_salir_tv_nombre)).setTypeface(new fonts(activity).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_salir_tv_nombre)).setTextColor(new fonts(activity).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
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
        return (customDialog=builder.create());// return customDialog;//regresamos el diálogo
    }   
	
	/**
	 * Muestra diálogo en dado caso que el GPS esté apagado
	 * 
	 * @param titulo Título del diálogo
	 * @param message Mensaje del diálogo
	 */
	/*public void showDialogGPS(final Activity act,String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setTitle(title);
        builder.setMessage(message);
		builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent settingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				act.startActivity(settingsIntent);
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
	}*/
	
	
	
}
