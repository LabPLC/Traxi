package codigo.labplc.mx.traxi.historico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.bd.DBHelper;
import codigo.labplc.mx.traxi.buscarplaca.bean.ViajeBean;
import codigo.labplc.mx.traxi.dialogos.Dialogos;
import codigo.labplc.mx.traxi.fonts.fonts;
import codigo.labplc.mx.traxi.log.DatosLogBean;
import codigo.labplc.mx.traxi.utils.Utils;


public class Historico_viajes extends Activity {

	private ListView list;
	ViajeBean beanViaje = null;
	private CustomList adapter;
	private AlertDialog customDialog= null;	//Creamos el dialogo generico



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historico_viajes);
		
		
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels/2;
		LinearLayout tips_activity_ll_tips =(LinearLayout)findViewById(R.id.historial_activity_ll);
		RelativeLayout.LayoutParams lp= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,height);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		tips_activity_ll_tips.setLayoutParams(lp);
		
		
		   ((TextView) findViewById(R.id.historico_viajes_titulo_tv_placa)).setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		   ((TextView) findViewById(R.id.historico_viajes_titulo_tv_calif)).setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		   ((TextView) findViewById(R.id.historico_viajes_titulo_tv_comentario)).setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		
		try {
			DBHelper	BD = new DBHelper(Historico_viajes.this);
			SQLiteDatabase bd = BD.loadDataBase(Historico_viajes.this, BD);
			beanViaje =	BD.getViajes(bd);
			BD.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		ImageView historico_activity_iv_cerrar = (ImageView)findViewById(R.id.historico_activity_iv_cerrar);
		historico_activity_iv_cerrar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
			}
		});
		
		 list=(ListView)findViewById(R.id.historico_viajes_list);
		 cargarViajes();
	     list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> parent,View view,  int position, long id) {
					showDialogEliminar(view).show();
					return false;
				}
	        });

	}
	
	
	/**
	 * carga en la lista los eventos 
	 * @return
	 */
	public boolean cargarViajes(){
		try{
			if(beanViaje!=null){
			 adapter = new CustomList(Historico_viajes.this,beanViaje.getId(),beanViaje.getPlaca(),
					 beanViaje.getHora_inicio(),beanViaje.getHora_fin(),beanViaje.getCalificacion(),beanViaje.getComentario(),
					 beanViaje.getInicio_viaje(),beanViaje.getFin_viaje());
			 list.setAdapter(adapter); 
			}else{
				list.setAdapter(null); 
			}
			    
			return true;
		}catch(Exception e){
			e.printStackTrace();
		return false;
		}
		
	}
	
	
	
	/**
	 * Dialogo para asegurar que quieres salir de la app
	 *
	 * @param Activity (actividad que llama al di‡logo)
	 * @return Dialog (regresa el dialogo creado)
	 **/
	
	public Dialog showDialogEliminar(final View v)
    {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    View view = getLayoutInflater().inflate(R.layout.dialogo_eliminar_registro, null);
	    builder.setView(view);
	    builder.setCancelable(true);
	    
	    
	    DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int wight = displaymetrics.widthPixels/8;
		LinearLayout dialogo_eliminar_ll =(LinearLayout)view.findViewById(R.id.dialogo_eliminar_ll);
		FrameLayout.LayoutParams lp= new FrameLayout.LayoutParams(wight*7,FrameLayout.LayoutParams.WRAP_CONTENT);
		dialogo_eliminar_ll.setLayoutParams(lp);
		
		
        //tipografias
	    ((Button) view.findViewById(R.id.dialogo_salir_btnAceptar)).setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
	    ((Button) view.findViewById(R.id.dialogo_salir_btnCancelar)).setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
	    
	    ((TextView) view.findViewById(R.id.dialogo_salir_tv_nombre)).setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_salir_tv_nombre)).setTextColor(getResources().getColor(R.color.color_vivos));
	    
	    ((TextView) view.findViewById(R.id.dialogo_salir_tv_subtitulo)).setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
	    ((TextView) view.findViewById(R.id.dialogo_salir_tv_subtitulo)).setTextColor(getResources().getColor(R.color.color_vivos));
	    
	    
	  //escucha del boton aceptar
        ((Button) view.findViewById(R.id.dialogo_salir_btnAceptar)).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
            	 
            	 try {
						DBHelper	BD = new DBHelper(Historico_viajes.this);
						SQLiteDatabase bd = BD.loadDataBase(Historico_viajes.this, BD);
						BD.deleteViaje(bd,v.getTag()+"");
						beanViaje= null;
						beanViaje =	BD.getViajes(bd);
						cargarViajes();
						BD.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
            	 customDialog.dismiss(); 
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
	
	
	
}
