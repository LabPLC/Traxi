package codigo.labplc.mx.traxi.tracking.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.SlidingDrawer;
import android.widget.Toast;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.TraxiMainActivity;
import codigo.labplc.mx.traxi.califica.Califica_taxi;
import codigo.labplc.mx.traxi.configuracion.UserSettingActivity;
import codigo.labplc.mx.traxi.dialogos.Dialogos;
import codigo.labplc.mx.traxi.fonts.fonts;
import codigo.labplc.mx.traxi.log.DatosLogBean;
import codigo.labplc.mx.traxi.services.ServicioGeolocalizacion;
import codigo.labplc.mx.traxi.utils.Utils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Clase mapa muestra el trayecto y ayuda a llegar al destino al usuario
 * 
 * @author mikesaurio
 *
 */
@SuppressWarnings("deprecation")
public class Mapa_tracking extends Activity implements OnItemClickListener, OnClickListener {
	
	
	
	@Override
	protected void onStart() {
		ServicioGeolocalizacion.stopNotification();
		
		super.onStart();
	}

	public final String TAG = this.getClass().getSimpleName();
	
	private static final int RESULT_SETTINGS = 1; //resultado del menu
	private GoogleMap map;
	private double latitud=0;
	private double longitud=0;
	private MarkerOptions marker;
	private MarkerOptions marker_taxi;
	private Button mapa_tracking_terminoviaje;
	private AutoCompleteTextView actvDestination;
	private MarkerOptions marker_taxi_destino;
	Double latini=0.0;
	Double lonini=0.0;
	Double  latfin=0.0;
	Double  lonfin=0.0;
	ArrayList<String> pointsLat;
	ArrayList<String> pointsLon;
	ArrayList<InfoPointBean> InfoPoint = null;
	public static String direccion_destino= null;
	private boolean isFirstLocation= true;
	private String tiempo;
	private String distancia;
	public static boolean isButtonExit = true;
	private SlidingDrawer Drawer;
	public static Activity fa;
	Handler updateBarHandler;
	private ProgressDialog pDialog;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapa_tracking);
		DatosLogBean.setTagLog(TAG);
		 
		
		fa = Mapa_tracking.this;
		//en caso de que se active si ya no existe el servicio
				if(ServicioGeolocalizacion.serviceIsIniciado!=true||ServicioGeolocalizacion.taxiActivity==null){
					Mapa_tracking.isButtonExit= false;
					ServicioGeolocalizacion.CancelNotification(Mapa_tracking.this, 0);
					Intent svc = new Intent(Mapa_tracking.this, ServicioGeolocalizacion.class);
					stopService(svc);
			  		Intent mainIntent = new Intent().setClass(Mapa_tracking.this, TraxiMainActivity.class);
			  		startActivity(mainIntent);
			  		finish();
				}
		
		//propiedades del action bar
				Utils.crearActionBar(Mapa_tracking.this, R.layout.abs_layout_compartir,getResources().getString(R.string.app_name),0.0f);//creamos el ActionBAr
				((ImageView) findViewById(R.id.abs_layout_iv_menu)).setOnClickListener(this);
				((ImageView) findViewById(R.id.abs_layout_iv_compratir)).setOnClickListener(this);
				

	     //obtenemos los adicionales 
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			latitud = bundle.getDouble("latitud_inicial");	
			longitud = bundle.getDouble("longitud_inicial");

		}
		
		
		ImageButton mitaxi_googlemaps_ibtn_gps = (ImageButton)findViewById(R.id.mitaxi_googlemaps_ibtn_gps);
		mitaxi_googlemaps_ibtn_gps.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CameraPosition cameraPosition;
				if(latfin==0.0){
					 cameraPosition = new CameraPosition.Builder().target(new LatLng(latitud, longitud)).zoom(18).build();
					
				}else{
					 cameraPosition = new CameraPosition.Builder().target(new LatLng(latfin, lonfin)).zoom(18).build();
				}
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			}
		});
		
		
		
		 CheckBox mitaxi_googlemaps_cv_paranoico = (CheckBox) findViewById(R.id.mitaxi_googlemaps_cv_paranoico); 
		
		 SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
		 String telemer_local = prefs.getString("telemer", null);
		    if(telemer_local==null){//revisamos si tiene por lo menos un contactos de emergencia
	        	LinearLayout mitaxi_googlemaps_ll_paranoico =(LinearLayout)findViewById(R.id.mitaxi_googlemaps_ll_paranoico);
	        	mitaxi_googlemaps_ll_paranoico.setVisibility(LinearLayout.GONE);
	        }else{
	        	boolean panic = prefs.getBoolean("panico", false);
	        	mitaxi_googlemaps_cv_paranoico.setChecked(panic); 
	        }
         
         mitaxi_googlemaps_cv_paranoico.setOnCheckedChangeListener(new OnCheckedChangeListener() { 

		 @Override 
		 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { 
			 SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
			 if (buttonView.isChecked()){ 
					editor.putBoolean("panico", true);
					editor.commit();
			 } 
			 else{
				 editor.putBoolean("panico", false);
				 editor.commit();
			 } 

		 }
		 });
		
		

		 Drawer = (SlidingDrawer) findViewById (R.id.drawer);

	        final ImageView  tab = (ImageView) findViewById (R.id.mitaxi_googlemaps_iv_abrir);

	        Drawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {

	            @Override
	            public void onDrawerOpened() {
	                tab.setImageResource(R.drawable.ic_launcher_cerrar);

	            }
	        });

	        Drawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {

	            @Override
	            public void onDrawerClosed() {
	                tab.setImageResource(R.drawable.ic_launcher_abrir);

	            }
	        });

		
		
		//escucha de botones
		mapa_tracking_terminoviaje =(Button)findViewById(R.id.mapa_tracking_terminoviaje);
		mapa_tracking_terminoviaje.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		mapa_tracking_terminoviaje.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isButtonExit= false;
				Intent intent_califica = new Intent(Mapa_tracking.this, Califica_taxi.class);
				startActivity(intent_califica);
				finish();
			}
		});
		
		
		actvDestination = (AutoCompleteTextView) findViewById(R.id.mitaxi_googlemaps_actv_destination);
	    actvDestination.setAdapter(new PlacesAutocompleteAdapter(this, R.layout.places_list_item));
	    if(direccion_destino!=null){
	    	actvDestination.setText(direccion_destino);
	    	actvDestination.setFocusable(false);
	    	//llenarMapaConDestino();
	    }
	    actvDestination.setOnItemClickListener(this);
		
	     Button mitaxi_googlemaps_btn_destino =(Button)findViewById(R.id.mitaxi_googlemaps_btn_destino);
	     mitaxi_googlemaps_btn_destino.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
	     mitaxi_googlemaps_btn_destino.setOnClickListener(new View.OnClickListener() {
		
	    	 @Override
			public void onClick(View v) {
		           if(!actvDestination.getText().toString().equals("")){
				    	String destino = actvDestination.getText().toString();
		            	direccion_destino =destino;
		            	destino = destino.replaceAll(" ", "+");
		            	String consulta = "http://maps.googleapis.com/maps/api/geocode/json?address="+destino+"&sensor=true";
						String querty = Utils.doHttpConnection(consulta);
						InfoPoint = null;
						InfoPoint = new DirectionsJSONParser().parsePoints(querty);
		        		if(Utils.getDistanceMeters(InfoPoint.get(0).getDblLatitude(), InfoPoint.get(0).getDblLongitude(),latfin, lonfin)<=18000) {
				        	   llenarMapaConDestino();
						}else{
								actvDestination.setText("");
								direccion_destino =null;
								InfoPoint = null;
								Dialogos.Toast(Mapa_tracking.this, getResources().getString(R.string.Mapa_tracking_llena_direccion_lejos), Toast.LENGTH_LONG);
						}
        			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			        imm.hideSoftInputFromWindow(actvDestination.getWindowToken(), 0);
		       		Drawer.animateClose();
		           }else{
		        	  Dialogos.Toast(Mapa_tracking.this, getResources().getString(R.string.Mapa_tracking_llena_direccion), Toast.LENGTH_LONG);
		           }
				
			}
		});
	     
	     
	   
	    
	     if(lonfin==0.0){
	    	 updateBarHandler = new Handler();
	    	 launchRingDialog();
	     }
	    		
		setUpMapIfNeeded();		
	}

	
	


	@Override
	protected void onStop() {
		if(isButtonExit){
		ServicioGeolocalizacion.showNotification();
		isButtonExit= false;
		 }
		
		super.onStop();
	}



	private void setUpMapIfNeeded() {
			if (map == null) {
				map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mitaxi_trip_map)).getMap();
				if (map != null) {
					if(setUpMap()) {
						initMap();
					}
				}
			}
		}
		
		public void initMap() {
			map.setMyLocationEnabled(false);//quitar circulo azul;
			map.setBuildingsEnabled(true);
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			map.getUiSettings().setZoomControlsEnabled(true); //ZOOM
			map.getUiSettings().setCompassEnabled(true); //COMPASS
			map.getUiSettings().setZoomGesturesEnabled(true); //GESTURES ZOOM
			map.getUiSettings().setRotateGesturesEnabled(true); //ROTATE GESTURES
			map.getUiSettings().setScrollGesturesEnabled(true); //SCROLL GESTURES
			map.getUiSettings().setTiltGesturesEnabled(true); //TILT GESTURES
			map.getUiSettings().setZoomControlsEnabled(false);
			
			// create marker
			marker = new MarkerOptions().position(new LatLng(latitud, longitud)).title(getResources().getString(R.string.mapa_tu_inicio));
			marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_chinche_llena));
			
			CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitud, longitud)).zoom(18).build();
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			 
			 marker_taxi = new MarkerOptions().position(new LatLng(latitud, longitud)).title(getResources().getString(R.string.mapa_mi_posicion));
			 marker_taxi.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_taxi));
			// adding marker
			map.addMarker(marker);
			map.addMarker(marker_taxi);	
		}
		
		/**
		 * revisa si el mapa esta 
		 * @return (boolean) true si el mapa esta listo 
		 */
		public boolean setUpMap() {
			if (!checkReady()) {
	            return false;
	        } else {
	        	return true;
	        }
		}
	    
		/**
		 * revisa si el mapa esta listo
		 * @return (boolea) si esta listo TRUE
		 */
		private boolean checkReady() {
	        if (map == null) {
	            return false;
	        }
	        return true;
	    }

		/**
		 * manejo de transmiciones
		 */
		 @SuppressWarnings("unused")
		private BroadcastReceiver onBroadcast = new BroadcastReceiver() {

			@Override
			public void onReceive(Context ctxt, Intent t) {
	
				 pointsLat = t.getStringArrayListExtra("latitud");
				 pointsLon = t.getStringArrayListExtra("longitud");
				 latini= Double.parseDouble(pointsLat.get(0));
				 lonini= Double.parseDouble(pointsLon.get(0));
				 latfin= Double.parseDouble(pointsLat.get(pointsLat.size()-1));
				 lonfin= Double.parseDouble(pointsLon.get(pointsLon.size()-1));
				 
				 map.clear();
				 marker.position(new LatLng(latini,lonini));
			   	 map.addMarker(marker);
			   	 
			 	marker_taxi.position(new LatLng(latfin,lonfin));
			 	
			 	if(pDialog!=null){
			 		pDialog.dismiss();
			 	}
				 if(InfoPoint!=null){//
					 try{
					 String consulta2 = "http://codigo.labplc.mx/~mikesaurio/taxi.php?act=chofer&type=getGoogleData&lato="
								+latfin+"&lngo="+lonfin
								+"&latd="+InfoPoint.get(0).getDblLatitude()+"&lngd="+InfoPoint.get(0).getDblLongitude()+"&filtro=velocidad";
						String querty2 = Utils.doHttpConnection(consulta2).replaceAll("\"", "");
						String[] Squerty2 = querty2.split(",");
						tiempo = Squerty2[0];
						distancia =Squerty2[1];
						marker_taxi.title(getResources().getString(R.string.mapa_estas)+" "+distancia+", "+tiempo+" "+getResources().getString(R.string.mapa_tu_destino));
					 }catch(Exception e){
						 DatosLogBean.setDescripcion(Utils.getStackTrace(e));
					 }
					
						map.addMarker(marker_taxi);
						
						//.zoom(21);map.getCameraPosition().zoom
						CameraPosition cameraPosition;
						if(isFirstLocation){
							 cameraPosition = new CameraPosition.Builder().target(new LatLng(latfin, lonfin)).zoom(18).build();
							 isFirstLocation= false;
						}else{
							 cameraPosition = new CameraPosition.Builder().target(new LatLng(latfin, lonfin)).zoom(map.getCameraPosition().zoom).build();
							
						}

						 for (int i = 0; i < pointsLat.size() - 1; i++) {
							 LatLng src = new LatLng(Double.parseDouble(pointsLat.get(i)),Double.parseDouble(pointsLon.get(i)));
							 LatLng dest = new LatLng(Double.parseDouble(pointsLat.get(i+1)),Double.parseDouble(pointsLon.get(i+1)));
			
							Polyline line = map.addPolyline(new PolylineOptions() //mMap is the Map Object
							 .add(new LatLng(src.latitude, src.longitude),
							 new LatLng(dest.latitude,dest.longitude)).width(8).color(getResources().getColor(R.color.generic_verde)).geodesic(true));
						  }
					 
						marker_taxi_destino = new MarkerOptions().position(new LatLng(InfoPoint.get(0).getDblLatitude(), InfoPoint.get(0).getDblLongitude())).title(getResources().getString(R.string.mapa_mi_destino));
						marker_taxi_destino.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_fin_viaje));
						map.addMarker(marker_taxi_destino);	
						
							String url = new DirectionsJSONParser().getDirectionsUrl(new LatLng(latfin,lonfin),new LatLng(InfoPoint.get(0).getDblLatitude(), InfoPoint.get(0).getDblLongitude()));					
							DownloadTask downloadTask = new DownloadTask();
							downloadTask.execute(url);
					}else if(direccion_destino!=null){
						llenarMapaConDestino();
					}else{
						map.addMarker(marker_taxi);
						
						//.zoom(21);map.getCameraPosition().zoom
						CameraPosition cameraPosition;
						if(isFirstLocation){
							 cameraPosition = new CameraPosition.Builder().target(new LatLng(latfin, lonfin)).zoom(18).build();
							 isFirstLocation= false;
						}else{
							 cameraPosition = new CameraPosition.Builder().target(new LatLng(latfin, lonfin)).zoom(map.getCameraPosition().zoom).build();
							
						}
						map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

						 for (int i = 0; i < pointsLat.size() - 1; i++) {
							 LatLng src = new LatLng(Double.parseDouble(pointsLat.get(i)),Double.parseDouble(pointsLon.get(i)));
							 LatLng dest = new LatLng(Double.parseDouble(pointsLat.get(i+1)),Double.parseDouble(pointsLon.get(i+1)));
							 Polyline line = map.addPolyline(new PolylineOptions() //mMap is the Map Object
							 .add(new LatLng(src.latitude, src.longitude),
							 new LatLng(dest.latitude,dest.longitude))
							 .width(8).color(getResources().getColor(R.color.generic_verde)).geodesic(true));
						  }
						
					}
			}
		};

	
	
		
		@Override
		protected void onPause() {
			unregisterReceiver(onBroadcast);
			super.onPause();
		}

		@Override
		protected void onResume() {
			registerReceiver(onBroadcast, new IntentFilter("key"));
			isButtonExit= true;
			ServicioGeolocalizacion.stopNotification();
			ServicioGeolocalizacion.CancelNotification(Mapa_tracking.this, 0);
			super.onResume();
		}
		
		/**
		 * Evento Click
		 * @param v
		 */
		 public void clickEvent(View v) {
		        if (v.getId() == R.id.abs_layout_iv_menu) {
		            showPopup(v);
		        }

		       
		    }
	
		
		 @Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				@SuppressWarnings("unused")
				String str = (String) adapterView.getItemAtPosition(position);
			}
		 
		
				 
		// Fetches data from url passed
			private class DownloadTask extends AsyncTask<String, Void, String>{			
						
				// Downloading data in non-ui thread
				@Override
				protected String doInBackground(String... url) {
						
					// For storing data from web service
					String data = "";
							
					try{
						// Fetching the data from web service
						data = new DirectionsJSONParser().downloadUrl(url[0]);
					}catch(Exception e){
						 DatosLogBean.setDescripcion(Utils.getStackTrace(e));
					}
					return data;		
				}
				
				// Executes in UI thread, after the execution of
				// doInBackground()
				@Override
				protected void onPostExecute(String result) {			
					super.onPostExecute(result);			
					
					ParserTask parserTask = new ParserTask();
					
					// Invokes the thread for parsing the JSON data
					parserTask.execute(result);
						
				}		
			}
			
			 
		    /**
		     * llena la ruta destino cuando ya se a puesto alguno
		     */
			 @SuppressWarnings("unused")
		    public void llenarMapaConDestino(){
		    	try{

					map.clear();
					marker_taxi_destino = new MarkerOptions().position(new LatLng(InfoPoint.get(0).getDblLatitude(), InfoPoint.get(0).getDblLongitude())).title(getResources().getString(R.string.mapa_mi_destino));
					marker_taxi_destino.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_fin_viaje));
					map.addMarker(marker_taxi_destino);
					marker.position(new LatLng(latini,lonini));
					map.addMarker(marker);
					marker_taxi.position(new LatLng(latfin,lonfin));
					map.addMarker(marker_taxi);
					//.zoom(21);
					CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latfin, lonfin)).zoom(map.getCameraPosition().zoom).build();
					map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
					 for (int i = 0; i < pointsLat.size() - 1; i++) {
						 LatLng src = new LatLng(Double.parseDouble(pointsLat.get(i)),Double.parseDouble(pointsLon.get(i)));
						 LatLng dest = new LatLng(Double.parseDouble(pointsLat.get(i+1)),Double.parseDouble(pointsLon.get(i+1)));
						 Polyline line = map.addPolyline(new PolylineOptions() //mMap is the Map Object
						 .add(new LatLng(src.latitude, src.longitude),
						 new LatLng(dest.latitude,dest.longitude))
						 .width(8).color(getResources().getColor(R.color.generic_verde)).geodesic(true));
					  }
		    	}catch(Exception e){
		    		DatosLogBean.setDescripcion(Utils.getStackTrace(e));
		    	}
		    }
		

		    /** A class to parse the Google Places in JSON format */
		    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
		    	
		    	// Parsing the data in non-ui thread    	
				@Override
				protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
					
					JSONObject jObject;	
					List<List<HashMap<String, String>>> routes = null;			           
		            
		            try{
		            	jObject = new JSONObject(jsonData[0]);
		            	DirectionsJSONParser parser = new DirectionsJSONParser();
		            	
		            	// Starts parsing data
		            	routes = parser.parse(jObject);    
		            }catch(Exception e){
		            	 DatosLogBean.setDescripcion(Utils.getStackTrace(e));
		            }
		            return routes;
				}
				
				// Executes in UI thread, after the parsing process
				@Override
				protected void onPostExecute(List<List<HashMap<String, String>>> result) {
					ArrayList<LatLng> points = null;
					PolylineOptions lineOptions = null;
					@SuppressWarnings("unused")
					MarkerOptions markerOptions = new MarkerOptions();
					
					// Traversing through all the routes
					for(int i=0;i<result.size();i++){
						points = new ArrayList<LatLng>();
						lineOptions = new PolylineOptions();
						
						// Fetching i-th route
						List<HashMap<String, String>> path = result.get(i);
						
						// Fetching all the points in i-th route
						for(int j=0;j<path.size();j++){
							HashMap<String,String> point = path.get(j);					
							
							double lat = Double.parseDouble(point.get("lat"));
							double lng = Double.parseDouble(point.get("lng"));
							LatLng position = new LatLng(lat, lng);	
							
							points.add(position);						
						}
						
						// Adding all the points in the route to LineOptions
						lineOptions.addAll(points);
						lineOptions.width(8);
						lineOptions.color(Color.RED);	
						
					}
					
					// Drawing polyline in the Google Map for the i-th route
					try{
					map.addPolyline(lineOptions);		
					}catch(Exception e){
						DatosLogBean.setDescripcion(Utils.getStackTrace(e));
					}
				}			
		    }   
		    
		    
		    @Override
			protected void onDestroy() {
			if(pDialog!=null){
		    	pDialog.dismiss();
		    	}
				super.onDestroy();
			}


		    /**
		     * pupop en forma de menu
		     * @param v
		     */
			public void showPopup(View v) {
				PopupMenu popup = new PopupMenu(Mapa_tracking.this, v);
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

								Intent i = new Intent(Mapa_tracking.this,UserSettingActivity.class);
								startActivityForResult(i, RESULT_SETTINGS);
								return true;
								
						case R.id.configuracion_acerca_de:
							new Dialogos().mostrarAercaDe(Mapa_tracking.this).show();
							return true;
							
						}
						return false;
					}
				});

				popup.show();
			}

		    @Override
			protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				super.onActivityResult(requestCode, resultCode, data);
				switch (requestCode) {
				case RESULT_SETTINGS:
				
					showUserSettings();
				
					break;
				}
			}

			/**
			 * mustra las preferencias guardadas
			 */
			private void showUserSettings() {
				SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
				StringBuilder builder = new StringBuilder();
				builder.append("\n Send report:"+ sharedPrefs.getBoolean("prefSendReport", true));
			}  
			
			
			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.abs_layout_iv_menu) {
					showPopup(v);
				}else if(v.getId()==R.id.abs_layout_iv_compratir){
					 SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
					 String placa = prefs.getString("placa", null);
					
					Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
					sharingIntent.setType("text/plain");
					String shareBody = getResources().getString(R.string.mensaje_compartir) +" "+placa +" #Traxi";
					sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "TRAXI");
					sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
					startActivity(Intent.createChooser(sharingIntent, "Share via"));
				}
				
				
			}

			
			/**
			 * crea el dialogo de espera al cargar el mpa
			 */
			public void launchRingDialog() {

				pDialog = new ProgressDialog(Mapa_tracking.this);
				pDialog.setCanceledOnTouchOutside(false);
				pDialog.setMessage(getResources().getString(R.string.texto_significado_el_viaje_inicio));
				pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				pDialog.setCancelable(false);
				pDialog.show();
			}	
			
			
	
}
