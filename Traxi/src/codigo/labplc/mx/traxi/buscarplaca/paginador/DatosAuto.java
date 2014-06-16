package codigo.labplc.mx.traxi.buscarplaca.paginador;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
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
import codigo.labplc.mx.traxi.log.DatosLogBean;
import codigo.labplc.mx.traxi.services.ServicioGeolocalizacion;
import codigo.labplc.mx.traxi.tracking.map.Mapa_tracking;
import codigo.labplc.mx.traxi.utils.Utils;

import com.viewpagerindicator.CirclePageIndicator;

/**
 * FragmentActivity que funge como paginador
 * 
 * @author mikesaurio
 *
 */
public class DatosAuto extends FragmentActivity implements OnClickListener  {
	
	public final String TAG = this.getClass().getSimpleName();

	//private int PUNTOS=0;
	private int PUNTOS_APP = 95;
	private int PUNTOS_USUARIO = 0;
	private int PUNTOS_REVISTA = 50;
	private int PUNTOS_INFRACCIONES = 25;
	private int PUNTOS_TENENCIA = 5;
	private int PUNTOS_VERIFICACION = 5;
	private int PUNTOS_ANIO_VEHICULO = 10;
	private AutoBean autoBean;
	private  String placa;
	private int imagen_verde = 1;
	private int imagen_rojo = 2;
	private boolean hasRevista=true;
	private float sumaCalificacion =0.0f;
	private static final int RESULT_SETTINGS = 1;
	private LocationManager mLocationManager;
	private CirclePageIndicator titleIndicator;
	private ViewPager pager = null;
	private FacebookLogin facebookLogin;
	public static TextView abs_layout_tv_titulo_datosAutos;
	private boolean hasVerificacion=false;
	


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.dialogo_datos_correctos);
		
		DatosLogBean.setTagLog(TAG);
		
		final ActionBar ab = getActionBar();
		ab.setDisplayShowHomeEnabled(false);
	    ab.setDisplayShowTitleEnabled(false);     
		final LayoutInflater inflater = (LayoutInflater)getSystemService("layout_inflater");
		View view = inflater.inflate(R.layout.abs_layout_back,null);   
		abs_layout_tv_titulo_datosAutos=(TextView)view.findViewById(R.id.abs_layout_tv_titulo);
		abs_layout_tv_titulo_datosAutos.setTypeface(new fonts(DatosAuto.this).getTypeFace(fonts.FLAG_MAMEY));
		abs_layout_tv_titulo_datosAutos.setText(getResources().getString(R.string.datos_del_taxi));
		ab.setDisplayShowCustomEnabled(true);  
	    ImageView abs_layout_iv_menu = (ImageView) view.findViewById(R.id.abs_layout_iv_menu);
	    abs_layout_iv_menu.setOnClickListener(this);
	    ImageView abs_layout_iv_logo = (ImageView) view.findViewById(R.id.abs_layout_iv_logo);
	    abs_layout_iv_logo.setOnClickListener(this);
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
		
		
		
		Button dialogo_datos_correctos_btn_noViajo = (Button) findViewById(R.id.dialogo_datos_correctos_btn_noViajo);
		dialogo_datos_correctos_btn_noViajo.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		dialogo_datos_correctos_btn_noViajo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi", Context.MODE_PRIVATE);
				String face = prefs.getString("facebook", "0");
				String UUID_local = prefs.getString("uuid", null);
				try{
				String url= "http://codigo.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=addnoviaje"
						+"&id_usuario="+UUID_local
						+"&id_face="+face
						+"&calificacion_final="+autoBean.getCalificacion_final()
						+"&placa="+placa
						+"&revista="+autoBean.isHasrevista_()
						+"&infraccion="+autoBean.isHasinfracciones_()
						+"&anio="+autoBean.isHasanio_()
						+"&verificacion="+autoBean.isHasverificacion_()
						+"&tenencia="+autoBean.isHastenencia_();

				Utils.doHttpConnection(url);	
				}catch(Exception e){
					DatosLogBean.setDescripcion(Utils.getStackTrace(e));
				}
				back();
			}
		});

		Button dialogo_datos_correctos_btn_iniciar = (Button) findViewById(R.id.dialogo_datos_correctos_btn_iniciar);
		dialogo_datos_correctos_btn_iniciar.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		dialogo_datos_correctos_btn_iniciar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(Utils.getPreferencia("prefBusquedaFina",DatosAuto.this.getBaseContext(),false)){
					if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
						new Dialogos().showDialogGPS(DatosAuto.this).show();		
					}else{
						ServicioGeolocalizacion.taxiActivity = DatosAuto.this;
						startService(new Intent(DatosAuto.this,ServicioGeolocalizacion.class));
						
						Intent intent_mapa = new Intent(DatosAuto.this, Mapa_tracking.class);
						intent_mapa.putExtra("latitud_inicial", 19.0);
						intent_mapa.putExtra("longitud_inicial", -99.0);
						startActivity(intent_mapa);
						
						DatosAuto.this.finish();
					}
				}else{
					ServicioGeolocalizacion.taxiActivity = DatosAuto.this;
					startService(new Intent(DatosAuto.this,ServicioGeolocalizacion.class));
					
					Intent intent_mapa = new Intent(DatosAuto.this, Mapa_tracking.class);
					intent_mapa.putExtra("latitud_inicial", 19.0);
					intent_mapa.putExtra("longitud_inicial", -99.0);
					startActivity(intent_mapa);
					
					DatosAuto.this.finish();
				}
	
				
			}
		});
		
	}
	
	
	@SuppressWarnings("deprecation")
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
			  String Sjson=  Utils.doHttpConnection("http://codigo.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=getcomentario&placa="+placa);
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
							 comentarioBean.setFecha_comentario((String) oneObject.getString("hora_fin"));
							 arrayComenario.add(comentarioBean);
							 sumaCalificacion+=calif;
						 } catch (JSONException e) {  
							 DatosLogBean.setDescripcion(Utils.getStackTrace(e));
						 }
			      }
			      autoBean.setArrayComentarioBean(arrayComenario);
			      if(cast2.length()>0){
			    	  float califParcial = (sumaCalificacion/cast2.length());
			    	  PUNTOS_USUARIO =usuarioCalifica(califParcial); //(int) (califParcial * 20 /5);
			    	  autoBean.setCalificacion_usuarios(califParcial);
			      }else{
			    	  autoBean.setCalificacion_usuarios(0);
			      }
			}catch(JSONException e){
				DatosLogBean.setDescripcion(Utils.getStackTrace(e));
			}
	}

	/**
	 * metodo que verifica los datos de adeudos de un taxi
	 */
	@SuppressLint("SimpleDateFormat")
	private void datosVehiculo(boolean esta_en_revista) {
		try{
			  String Sjson=  Utils.doHttpConnection("http://datos.labplc.mx/movilidad/vehiculos/"+placa+".json");
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
							 DatosLogBean.setDescripcion(Utils.getStackTrace(e));
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
					 Date lm = new Date();
					 String lasmod = new SimpleDateFormat("yyyy-MM-dd").format(lm);
					 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					
				      for (int i=0; i<cast2.length(); i++) {
				          	JSONObject oneObject = cast2.getJSONObject(i);
							 try {
								 Date date1 = formatter.parse(lasmod);
								 Date date2 = formatter.parse(oneObject.getString("vigencia").toString());
								 int comparison = date2.compareTo(date1);
								 if(comparison==1||comparison==0){
									 autoBean.setDescripcion_verificacion(getResources().getString(R.string.tiene_verificaciones)+" "+oneObject.getString("resultado").toString());
									 autoBean.setImagen_verificacion(imagen_verde); 
									 hasVerificacion=true;
								 }else{
									 autoBean.setDescripcion_verificacion(getResources().getString(R.string.no_tiene_verificaciones));
									 autoBean.setImagen_verificacion(imagen_rojo);
									 hasVerificacion=false;
									 
								 }
								if(!esta_en_revista){
									 autoBean.setMarca((String) oneObject.getString("marca"));
									 autoBean.setSubmarca((String)  oneObject.getString("submarca"));
									 autoBean.setAnio((String)  oneObject.getString("modelo"));
									 
									 autoBean.setDescripcion_revista(getResources().getString(R.string.sin_revista));
									 autoBean.setImagen_revista(imagen_rojo);
									 
									 Calendar calendar = Calendar.getInstance();
									 int thisYear = calendar.get(Calendar.YEAR);
									 
									 if(thisYear-Integer.parseInt(autoBean.getAnio())<=10){
										 autoBean.setDescripcion_vehiculo(getResources().getString(R.string.carro_nuevo)+" "+getResources().getString(R.string.Anio)+" "+autoBean.getAnio());
										 autoBean.setImagen_vehiculo(imagen_verde);
									 }else{
										 autoBean.setDescripcion_vehiculo(getResources().getString(R.string.carro_viejo)+" "+getResources().getString(R.string.Anio)+" "+autoBean.getAnio());
										 autoBean.setImagen_vehiculo(imagen_rojo);
										 PUNTOS_APP-=PUNTOS_ANIO_VEHICULO;
									 }
								}
								break;
							 } catch (JSONException e) { 
								 DatosLogBean.setDescripcion(Utils.getStackTrace(e));
							 } catch (ParseException e) {
								 DatosLogBean.setDescripcion(Utils.getStackTrace(e));
							}
				      }
				      if(!hasVerificacion){
				    	  PUNTOS_APP-=PUNTOS_VERIFICACION;
				      }
			    
			}catch(JSONException e){
				DatosLogBean.setDescripcion(Utils.getStackTrace(e));
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
		  String Sjson=  Utils.doHttpConnection("http://datos.labplc.mx/movilidad/taxis/"+placa+".json");
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
							 autoBean.setDescripcion_vehiculo(getResources().getString(R.string.carro_nuevo)+" "+getResources().getString(R.string.Anio)+" "+anio);
							 autoBean.setImagen_vehiculo(imagen_verde);
						 }else{
							 autoBean.setDescripcion_vehiculo(getResources().getString(R.string.carro_viejo)+" "+getResources().getString(R.string.Anio)+" "+anio);
							 autoBean.setImagen_vehiculo(imagen_rojo);
							 PUNTOS_APP-=PUNTOS_ANIO_VEHICULO;
						 }
						 return true;
					 
					 } catch (JSONException e) { return false;}
		      }else{
		    	  return false;
		      }
		      
		}catch(JSONException e){
			DatosLogBean.setDescripcion(Utils.getStackTrace(e));
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
	 * al dar atras en la actividad
	 */
	public void back(){
		Intent mainIntent = new Intent().setClass(DatosAuto.this, BuscaPlacaTexto.class);
		startActivity(mainIntent);
		pager=null;
		DatosAuto.this.finish();
		Dialogos.Toast(DatosAuto.this, getResources().getString(R.string.mapa_inicio_de_viaje_no_tomado), Toast.LENGTH_LONG);
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
				DatosLogBean.setDescripcion(Utils.getStackTrace(e));
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
	        }else if (v.getId() == R.id.abs_layout_iv_logo) {
	    		Intent mainIntent = new Intent().setClass(DatosAuto.this, BuscaPlacaTexto.class);
	    		startActivity(mainIntent);
	    		pager=null;
	    		DatosAuto.this.finish();
	        	super.onBackPressed();
			}

	       
	    }
	
	@Override
	protected void onDestroy() {
		pager=null;
		super.onDestroy();
	}
	
	
	
	
	    /**
	     * muestra popup en forma de menu
	     * @param v
	     */
	    public void showPopup(View v) {
			PopupMenu popup = new PopupMenu(DatosAuto.this, v);
			MenuInflater inflater = popup.getMenuInflater();
			inflater.inflate(R.menu.popup, popup.getMenu());
			int positionOfMenuItem = 0; 
			MenuItem item = popup.getMenu().getItem(positionOfMenuItem);
			SpannableString s = new SpannableString(getResources().getString(R.string.action_settings));
			s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_vivos)), 0, s.length(), 0);
			item.setTitle(s);

			popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					switch (item.getItemId()) {

					case R.id.configuracion_pref:

							Intent i = new Intent(DatosAuto.this,UserSettingActivity.class);
							startActivityForResult(i, RESULT_SETTINGS);
							return true;
							
					case R.id.configuracion_acerca_de:
						new Dialogos().mostrarAercaDe(DatosAuto.this).show();
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




		/**
		 * ponderacion para las calificaciones de los usuarios
		 * @param parcial
		 * @return
		 */
		public int usuarioCalifica(float parcial) {
			if(parcial<0.5)
				return -15;
			if(parcial<1.0&&parcial>=0.5)
				return -13;
			if(parcial<1.5&&parcial>=1.0)
				return -10;
			if(parcial<2.0&&parcial>=1.5)
				return -8;
			if(parcial<2.5&&parcial>=2.0)
				return -5;
			if(parcial<3.0&&parcial>=2.5)
				return -3;
			if(parcial<3.5&&parcial>=3.0)
				return 1;
			if(parcial<4.0&&parcial>=3.5)
				return 2;
			if(parcial<4.5&&parcial>=4.0)
				return 3;
			if(parcial<5.0&&parcial>=4.5)
				return 4;
			if(parcial>=5)
				return 5;
			
			return 0;
		}


		
	
}
