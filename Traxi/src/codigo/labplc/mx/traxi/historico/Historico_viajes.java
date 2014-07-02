package codigo.labplc.mx.traxi.historico;

import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.bd.DBHelper;
import codigo.labplc.mx.traxi.buscarplaca.bean.ViajeBean;
import codigo.labplc.mx.traxi.fonts.fonts;


public class Historico_viajes extends Activity {

	private ListView list;
	ViajeBean beanViaje = null;
	private CustomList adapter;


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
				public boolean onItemLongClick(AdapterView<?> parent,
						View view,  int position, long id) {
					
				        
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
			 adapter = new CustomList(Historico_viajes.this,beanViaje.getPlaca(),
					 beanViaje.getHora_inicio(),beanViaje.getHora_fin(),beanViaje.getCalificacion(),beanViaje.getComentario(),
					 beanViaje.getInicio_viaje(),beanViaje.getFin_viaje());
			 list.setAdapter(adapter);    
			return true;
		}catch(Exception e){
			e.printStackTrace();
		return false;
		}
		
	}
}
