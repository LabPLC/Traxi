package codigo.labplc.mx.traxi.buscarplaca.paginador;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.buscarplaca.BuscaPlacaTexto;
import codigo.labplc.mx.traxi.buscarplaca.bean.AutoBean;
import codigo.labplc.mx.traxi.buscarplaca.bean.ComentarioBean;
import codigo.labplc.mx.traxi.configuracion.UserSettingActivity;
import codigo.labplc.mx.traxi.dialogos.Dialogos;
import codigo.labplc.mx.traxi.facebook.FacebookLogin;
import codigo.labplc.mx.traxi.fonts.fonts;
import codigo.labplc.mx.traxi.log.BeanDatosLog;
import codigo.labplc.mx.traxi.services.ServicioGeolocalizacion;
import codigo.labplc.mx.traxi.tracking.map.Mapa_tracking;
import codigo.labplc.mx.traxi.utils.Utils;

import com.viewpagerindicator.CirclePageIndicator;

public class DatosAuto extends FragmentActivity implements OnClickListener{
	
	public final String TAG = this.getClass().getSimpleName();

	//private int PUNTOS=0;
	private int PUNTOS_APP = 80;
	private int PUNTOS_USUARIO = 0;
	private int PUNTOS_REVISTA = 50;
	private int PUNTOS_INFRACCIONES = 15;
	private int PUNTOS_TENENCIA = 5;
	private int PUNTOS_VERIFICACION = 5;
	private int PUNTOS_ANIO_VEHICULO = 5;
	private AutoBean autoBean;
	private  String placa;
	private int imagen_verde = 1;
	private int imagen_rojo = 2;
	private boolean hasRevista=true;
	private float sumaCalificacion =0.0f;
	private boolean entreComentarios=false;
	private static final int RESULT_SETTINGS = 1;
	private LocationManager mLocationManager;
	private CirclePageIndicator titleIndicator;
	private ViewPager pager = null;
	private FacebookLogin facebookLogin;
	
	
	@Override
	protected void onDestroy() {
		pager=null;
		super.onDestroy();
	}
	
	
	

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.dialogo_datos_correctos);
		
		BeanDatosLog.setTagLog(TAG);
		
		final ActionBar ab = getActionBar();
		ab.setDisplayShowHomeEnabled(false);
	    ab.setDisplayShowTitleEnabled(false);     
		final LayoutInflater inflater = (LayoutInflater)getSystemService("layout_inflater");
		
		View view = inflater.inflate(R.layout.abs_layout,null);   
		((TextView) view.findViewById(R.id.abs_layout_tv_titulo)).setTypeface(new fonts(DatosAuto.this).getTypeFace(fonts.FLAG_MAMEY));
		((TextView) view.findViewById(R.id.abs_layout_tv_titulo)).setText(getResources().getString(R.string.datos_del_taxi));
		ab.setDisplayShowCustomEnabled(true);  
	     ImageView abs_layout_iv_menu = (ImageView) view.findViewById(R.id.abs_layout_iv_menu);
	     abs_layout_iv_menu.setOnClickListener(this);
		ab.setCustomView(view,new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
		ab.setCustomView(view);
		
		
		facebookLogin = new FacebookLogin(DatosAuto.this);
		
		
		Bundle bundle = getIntent().getExtras();
		placa = bundle.getString("placa");	
		
	
		

		SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("placa", placa);
		editor.commit();		
		
		Upload nuevaTarea = new Upload();
		nuevaTarea.execute();

		Button dialogo_datos_correctos_btn_iniciar = (Button) findViewById(R.id.dialogo_datos_correctos_btn_iniciar);
		dialogo_datos_correctos_btn_iniciar.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_AMARILLO));
		dialogo_datos_correctos_btn_iniciar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					new Dialogos().showDialogGPS(DatosAuto.this).show();		
				}else{
					ServicioGeolocalizacion.taxiActivity = DatosAuto.this;
					startService(new Intent(DatosAuto.this,ServicioGeolocalizacion.class));
					Dialogos.Toast(DatosAuto.this, getResources().getString(R.string.texto_significado_el_viaje_inicio), Toast.LENGTH_LONG);
					DatosAuto.this.finish();
				}
				
			}
		});
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SETTINGS:
			showUserSettings();
			break;
		default:
		facebookLogin.getFacebook().authorizeCallback(requestCode, resultCode, data);
		break;
		
		}
		
	}
	
	
	

	/**
	 * carga todos los comentarios de una placa
	 */
	private void cargaComentarios() {
		try{
			  String Sjson=  Utils.doHttpConnection("http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=getcomentario&placa="+placa);
		      JSONObject json= (JSONObject) new JSONTokener(Sjson).nextValue();
		      JSONObject json2 = json.getJSONObject("message");
		      JSONObject jsonResponse = new JSONObject(json2.toString());
		      JSONArray cast2 = jsonResponse.getJSONArray("calificacion");
			      ArrayList<ComentarioBean> arrayComenario= new ArrayList<ComentarioBean>();
			      for (int i=0; i<cast2.length(); i++) {
			          	JSONObject oneObject = cast2.getJSONObject(i);
						 try {
							 ComentarioBean	  comentarioBean = new ComentarioBean();
							 comentarioBean.setComentario((String) oneObject.getString("comentario"));
							 Float calif =Float.parseFloat((String) oneObject.getString("calificacion"));
							 comentarioBean.setCalificacion(calif);
							 comentarioBean.setId_facebook((String) oneObject.getString("id_face"));
							 arrayComenario.add(comentarioBean);
							 sumaCalificacion+=calif;
							 entreComentarios=true;
						 } catch (JSONException e) {  
							 BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
						 }
			      }
			      autoBean.setArrayComentarioBean(arrayComenario);
			      if(entreComentarios){
			    	  float califParcial = (sumaCalificacion/cast2.length());
			    	  PUNTOS_USUARIO = (int) (califParcial * 20 /5);
			    	  autoBean.setCalificacion_usuarios(PUNTOS_USUARIO);
			      }else{
			    	  autoBean.setCalificacion_usuarios(0);
			      }
			}catch(JSONException e){
				BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
			}
	}

	/**
	 * metodo que verifica los datos de adeudos de un taxi
	 */
	private void datosVehiculo(boolean esta_en_revista) {
		try{
			  String Sjson=  Utils.doHttpConnection("http://dev.datos.labplc.mx/movilidad/vehiculos/"+placa+".json");
			      JSONObject json= (JSONObject) new JSONTokener(Sjson).nextValue();
			      JSONObject json2 = json.getJSONObject("consulta");
			      JSONObject jsonResponse = new JSONObject(json2.toString());
			      JSONObject sys  = jsonResponse.getJSONObject("tenencias");
			      
			    if(sys.getString("tieneadeudos").toString().equals("0")){
			    	autoBean.setDescripcion_tenencia(getResources().getString(R.string.sin_adeudo_tenencia));
			    	autoBean.setImagen_teencia(imagen_verde);
			    }else{
			    	autoBean.setDescripcion_tenencia(getResources().getString(R.string.con_adeudo_tenencia));
			    	autoBean.setImagen_teencia(imagen_rojo);
			    	PUNTOS_APP-=PUNTOS_TENENCIA;
			    }
			    
			    JSONArray cast = jsonResponse.getJSONArray("infracciones");
			    String situacion;
			    boolean hasInfraccion=false;
			      for (int i=0; i<cast.length(); i++) {
			          	JSONObject oneObject = cast.getJSONObject(i);
						 try {
							 situacion = (String) oneObject.getString("situacion");
							 if(!situacion.equals("Pagada")){
								 hasInfraccion=true;
							 }
							
						 } catch (JSONException e) { 
							 BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
						 }
			      }
			      if(hasInfraccion){
			    	  autoBean.setDescripcion_infracciones(getResources().getString(R.string.tiene_infraccion));
				    	autoBean.setImagen_infraccones(imagen_rojo);
				    	PUNTOS_APP-=PUNTOS_INFRACCIONES;
			      }else{
			    	  autoBean.setDescripcion_infracciones(getResources().getString(R.string.no_tiene_infraccion));
				      autoBean.setImagen_infraccones(imagen_verde);
			      }
			      JSONArray cast2 = jsonResponse.getJSONArray("verificaciones");
			      if(cast2.length()==0){
			    	  autoBean.setDescripcion_verificacion(getResources().getString(R.string.no_tiene_verificaciones));
					  autoBean.setImagen_verificacion(imagen_rojo);
					  PUNTOS_APP-=PUNTOS_VERIFICACION;
			      }
				      for (int i=0; i<cast2.length(); i++) {
				          	JSONObject oneObject = cast2.getJSONObject(i);
							 try {
								 autoBean.setDescripcion_verificacion(getResources().getString(R.string.tiene_verificaciones)+oneObject.getString("resultado").toString());
								 autoBean.setImagen_verificacion(imagen_verde);
								if(!esta_en_revista){
									 autoBean.setMarca((String) oneObject.getString("marca"));
									 autoBean.setSubmarca((String)  oneObject.getString("submarca"));
									 autoBean.setAnio((String)  oneObject.getString("modelo"));
									 
									 autoBean.setDescripcion_revista(getResources().getString(R.string.sin_revista));
									 autoBean.setImagen_revista(imagen_rojo);
									 
									 Calendar calendar = Calendar.getInstance();
									 int thisYear = calendar.get(Calendar.YEAR);
									 
									 if(thisYear-Integer.parseInt(autoBean.getAnio())<=10){
										 autoBean.setDescripcion_vehiculo(getResources().getString(R.string.carro_nuevo)+getResources().getString(R.string.Anio)+autoBean.getAnio());
										 autoBean.setImagen_vehiculo(imagen_verde);
									 }else{
										 autoBean.setDescripcion_vehiculo(getResources().getString(R.string.carro_viejo)+getResources().getString(R.string.Anio)+autoBean.getAnio());
										 autoBean.setImagen_vehiculo(imagen_rojo);
										 PUNTOS_APP-=PUNTOS_ANIO_VEHICULO;
									 }
								}
								
							 } catch (JSONException e) { 
								 BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
							 }
				      }
				
			    
			}catch(JSONException e){
				BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
			}
		
	}

	
	/**
	 * metodo que indica si esta una placa en la revista vehicular
	 * 
	 * @return true (si esta en la revista vehicular)
	 * 		   false (si algo falla o no esta en la revista)
	 */
	private boolean estaEnRevista() {
		try{
		  String Sjson=  Utils.doHttpConnection("http://mikesaurio.dev.datos.labplc.mx/movilidad/taxis/"+placa+".json");
		    String marca="",submarca="",anio="";
		    
		    JSONObject json= (JSONObject) new JSONTokener(Sjson).nextValue();
		      JSONObject json2 = json.getJSONObject("Taxi");
		      JSONObject jsonResponse = new JSONObject(json2.toString());
		      JSONObject sys  = jsonResponse.getJSONObject("concesion");
		      
		      if(sys.length()>0){
		      
					 try {
						 marca = (String) sys.getString("marca");
						 autoBean.setMarca(marca);
						 submarca = (String)  sys.getString("submarca");
						 autoBean.setSubmarca(submarca);
						 anio = (String)  sys.getString("anio");
						 autoBean.setAnio(anio);
						 
						 autoBean.setDescripcion_revista(getResources().getString(R.string.con_revista));
						 autoBean.setImagen_revista(imagen_verde);
						 
						 Calendar calendar = Calendar.getInstance();
						 int thisYear = calendar.get(Calendar.YEAR);
						 
						 if(thisYear-Integer.parseInt(anio)<=10){
							 autoBean.setDescripcion_vehiculo(getResources().getString(R.string.carro_nuevo)+getResources().getString(R.string.Anio)+anio);
							 autoBean.setImagen_vehiculo(imagen_verde);
						 }else{
							 autoBean.setDescripcion_vehiculo(getResources().getString(R.string.carro_viejo)+getResources().getString(R.string.Anio)+anio);
							 autoBean.setImagen_vehiculo(imagen_rojo);
							 PUNTOS_APP-=PUNTOS_ANIO_VEHICULO;
						 }
						 return true;
					 
					 } catch (JSONException e) { return false;}
		      }else{
		    	  return false;
		      }
		      
		}catch(JSONException e){
			BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
			return false;
		}
		
	}

	
	
	
	@Override
	public void onBackPressed() {
		Intent mainIntent = new Intent().setClass(DatosAuto.this, BuscaPlacaTexto.class);
		startActivity(mainIntent);
		pager=null;
		DatosAuto.this.finish();
		super.onBackPressed();
	}
	
	/**
	 * clase que envia por post los datos del registro
	 * 
	 * @author mikesaurio
	 * 
	 */
	class Upload extends AsyncTask<String, Void, Void> {

		private ProgressDialog pDialog;;
		public static final int HTTP_TIMEOUT = 40 * 1000;

		@Override
		protected Void doInBackground(String... params) {
			try

			{
				
				mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				titleIndicator = (CirclePageIndicator) findViewById(R.id.indicator_dialg);
				
				DatosAuto.this.pager = (ViewPager) DatosAuto.this.findViewById(R.id.pager_dialog);
				DatosAuto.this.pager.setOffscreenPageLimit(4);
				autoBean= new AutoBean();

				autoBean.setPlaca(placa);

				if(!estaEnRevista()){
					 PUNTOS_APP-=PUNTOS_REVISTA;
					 autoBean.setDescripcion_revista(getResources().getString(R.string.sin_revista));
					 autoBean.setImagen_revista(imagen_rojo);
					 hasRevista=false;
				}
				
				datosVehiculo(hasRevista);
				cargaComentarios();
				
				 int PUNTOS = (PUNTOS_APP+PUNTOS_USUARIO);
				if(PUNTOS<=25){
					autoBean.setDescripcion_calificacion_app(getResources().getString(R.string.texto_calificacion_25));
				}else if(PUNTOS<=49 && PUNTOS>25){
					autoBean.setDescripcion_calificacion_app(getResources().getString(R.string.texto_calificacion_49));
				}else if(PUNTOS<=60 && PUNTOS>49){
					autoBean.setDescripcion_calificacion_app(getResources().getString(R.string.texto_calificacion_60));
				}else if(PUNTOS<=80 && PUNTOS>60){
					autoBean.setDescripcion_calificacion_app(getResources().getString(R.string.texto_calificacion_80));
				}else if(PUNTOS<=90 && PUNTOS>80){
					autoBean.setDescripcion_calificacion_app(getResources().getString(R.string.texto_calificacion_90));
				}else if(PUNTOS>90){
					autoBean.setDescripcion_calificacion_app(getResources().getString(R.string.texto_calificacion_100));
				}
				autoBean.setCalificacion_final(PUNTOS);
				autoBean.setCalificaion_app(PUNTOS_APP);	
				
			} catch (Exception e) {
				BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
			}
			return null;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DatosAuto.this);
			pDialog.setMessage(getResources().getString(R.string.cargando_info));
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			
			// Create an adapter with the fragments we show on the ViewPager
			FragmentPagerAdapterDialog adapter = new FragmentPagerAdapterDialog(getSupportFragmentManager());
			adapter.addFragment(ScreenSlidePageFragmentDialog.newInstance(getResources().getColor(R.color.android_blue), 1,DatosAuto.this,autoBean));
			adapter.addFragment(ScreenSlidePageFragmentDialog.newInstance(getResources().getColor(R.color.android_red), 2,DatosAuto.this,autoBean));
			adapter.addFragment(ScreenSlidePageFragmentDialog.newInstance(getResources().getColor(R.color.android_darkpink), 3,DatosAuto.this,autoBean,facebookLogin));

			
			DatosAuto.this.pager.setAdapter(adapter);
			titleIndicator.setViewPager(pager);
		}

	}
	
	@Override
	 public void onClick(View v) {
	        if (v.getId() == R.id.abs_layout_iv_menu) {
	            showPopup(v);
	        }

	       
	    }
	
	    
	    public void showPopup(View v) {
			PopupMenu popup = new PopupMenu(DatosAuto.this, v);
			MenuInflater inflater = popup.getMenuInflater();
			inflater.inflate(R.menu.popup, popup.getMenu());
			int positionOfMenuItem = 0; 
			MenuItem item = popup.getMenu().getItem(positionOfMenuItem);
			SpannableString s = new SpannableString(getResources().getString(R.string.action_settings));
			s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.rojo_logo)), 0, s.length(), 0);
			item.setTitle(s);

			popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					switch (item.getItemId()) {

					case R.id.configuracion_pref:

							Intent i = new Intent(DatosAuto.this,UserSettingActivity.class);
							startActivityForResult(i, RESULT_SETTINGS);
							return true;
						
					}
					return false;
				}
			});

			popup.show();
		}

	 

		/**
		 * mustra las preferencias guardadas
		 */
		private void showUserSettings() {
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
			StringBuilder builder = new StringBuilder();
			builder.append("\n Send report:"+ sharedPrefs.getBoolean("prefSendReport", true));
		}  
	 
		
	
}
