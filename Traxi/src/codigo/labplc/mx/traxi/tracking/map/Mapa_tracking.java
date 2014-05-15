package codigo.labplc.mx.traxi.tracking.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.ActionBar;
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
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.califica.Califica_taxi;
import codigo.labplc.mx.traxi.configuracion.UserSettingActivity;
import codigo.labplc.mx.traxi.fonts.fonts;
import codigo.labplc.mx.traxi.log.BeanDatosLog;
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
	Double latini;
	Double lonini;
	Double latfin;
	Double lonfin;
	ArrayList<String> pointsLat;
	ArrayList<String> pointsLon;
	ArrayList<InfoPoint> InfoPoint = null;
	private LatLng taxiPosition = null;
	public static String direccion_destino= null;
	private boolean isFirstLocation= true;
	private String tiempo;
	private String distancia;
	private boolean isButtonExit = true;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapa_tracking);
		
		
		BeanDatosLog.setTagLog(TAG);
		//propiedades del action bar
		 final ActionBar ab = getActionBar();
	     ab.setDisplayShowHomeEnabled(false);
	     ab.setDisplayShowTitleEnabled(false);     

	     //instancias
	     final LayoutInflater inflater = (LayoutInflater)getSystemService("layout_inflater");
	     View view = inflater.inflate(R.layout.abs_layout,null);   
	     ((TextView) view.findViewById(R.id.abs_layout_tv_titulo)).setTypeface(new fonts(Mapa_tracking.this).getTypeFace(fonts.FLAG_MAMEY));
	     ((TextView) view.findViewById(R.id.abs_layout_tv_titulo)).setText("MI VIAJE");
	     ((TextView)findViewById(R.id.mitaxi_googlemaps_tv_destination)).setTypeface(new fonts(Mapa_tracking.this).getTypeFace(fonts.FLAG_MAMEY));
	     ((TextView)findViewById(R.id.mitaxi_googlemaps_tv_destination)).setTextColor(new fonts(Mapa_tracking.this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
	     
	     //instancias en  
	     ab.setDisplayShowCustomEnabled(true);
	     ImageView abs_layout_iv_menu = (ImageView) view.findViewById(R.id.abs_layout_iv_menu);
	     abs_layout_iv_menu.setOnClickListener(this);
	     ab.setCustomView(view,new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
	     ab.setCustomView(view);

	     //obtenemos los adicionales 
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			latitud = bundle.getDouble("latitud_inicial");	
			longitud = bundle.getDouble("longitud_inicial");
		}
		
		//escucha de botones
		mapa_tracking_terminoviaje =(Button)findViewById(R.id.mapa_tracking_terminoviaje);
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
	     mitaxi_googlemaps_btn_destino.setOnClickListener(new View.OnClickListener() {
		
	    	 @Override
			public void onClick(View v) {
		           if(!actvDestination.getText().toString().equals("")){
		        	   InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		        	   imm.hideSoftInputFromWindow(actvDestination.getWindowToken(), 0);
		        	   llenarMapaConDestino();
		           }else{
		        	  
		           }
				
			}
		});
	     
	    		
			setUpMapIfNeeded();		
			

		
	}

	
	
	 @Override
	protected void onStop() {
		 if(isButtonExit){
		ServicioGeolocalizacion.showNotification();
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
			
			// create marker
			marker = new MarkerOptions().position(new LatLng(latitud, longitud)).title(getResources().getString(R.string.mapa_tu_inicio));
			marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_chinche_llena));
			
			CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitud, longitud)).zoom(21).build();
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			 
			 marker_taxi = new MarkerOptions().position(new LatLng(latitud, longitud)).title(getResources().getString(R.string.mapa_mi_posicion));
			 marker_taxi.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_taxi));
			// adding marker
			map.addMarker(marker);
			map.addMarker(marker_taxi);	
		}
		
		public boolean setUpMap() {
			if (!checkReady()) {
	            return false;
	        } else {
	        	return true;
	        }
		}
	    
		private boolean checkReady() {
	        if (map == null) {
	            return false;
	        }
	        return true;
	    }

		/**
		 * manejo de transmiciones
		 */
		private BroadcastReceiver onBroadcast = new BroadcastReceiver() {

			@Override
			public void onReceive(Context ctxt, Intent t) {
	
				 pointsLat = t.getStringArrayListExtra("latitud");
				 pointsLon = t.getStringArrayListExtra("longitud");
				 
				// Log.d("************", pointsLat+","+pointsLon);
				 
				 latini= Double.parseDouble(pointsLat.get(0));
				 lonini= Double.parseDouble(pointsLon.get(0));
				 latfin= Double.parseDouble(pointsLat.get(pointsLat.size()-1));
				 lonfin= Double.parseDouble(pointsLon.get(pointsLon.size()-1));
				 
				 map.clear();
				 marker.position(new LatLng(latini,lonini));
			   	 map.addMarker(marker);
			   	 
			 	marker_taxi.position(new LatLng(latfin,lonfin));
					
				 if(InfoPoint!=null){//
					 try{
					 String consulta2 = "http://datos.labplc.mx/~mikesaurio/taxi.php?act=chofer&type=getGoogleData&lato="
								+latfin+"&lngo="+lonfin
								+"&latd="+InfoPoint.get(0).getDblLatitude()+"&lngd="+InfoPoint.get(0).getDblLongitude()+"&filtro=velocidad";
						String querty2 = Utils.doHttpConnection(consulta2).replaceAll("\"", "");
						String[] Squerty2 = querty2.split(",");
						tiempo = Squerty2[0];
						distancia =Squerty2[1];
						marker_taxi.title(getResources().getString(R.string.mapa_estas)+distancia+", "+tiempo+getResources().getString(R.string.mapa_tu_destino));
					 }catch(Exception e){
						 BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
					 }
					
						map.addMarker(marker_taxi);
						
						//.zoom(21);map.getCameraPosition().zoom
						CameraPosition cameraPosition;
						
							 cameraPosition = new CameraPosition.Builder().target(new LatLng(latfin, lonfin)).zoom(21).build();
							 map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

						 for (int i = 0; i < pointsLat.size() - 1; i++) {
							 LatLng src = new LatLng(Double.parseDouble(pointsLat.get(i)),Double.parseDouble(pointsLon.get(i)));
							 LatLng dest = new LatLng(Double.parseDouble(pointsLat.get(i+1)),Double.parseDouble(pointsLon.get(i+1)));
							 Polyline line = map.addPolyline(new PolylineOptions() //mMap is the Map Object
							 .add(new LatLng(src.latitude, src.longitude),
							 new LatLng(dest.latitude,dest.longitude)).width(8).color(Color.BLUE).geodesic(true));
						  }
					 
						marker_taxi_destino = new MarkerOptions().position(new LatLng(InfoPoint.get(0).getDblLatitude(), InfoPoint.get(0).getDblLongitude())).title(getResources().getString(R.string.mapa_mi_destino));
						marker_taxi_destino.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_fin_rojo));
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
							 cameraPosition = new CameraPosition.Builder().target(new LatLng(latfin, lonfin)).zoom(21).build();
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
							 .width(8).color(Color.BLUE).geodesic(true));
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
			super.onResume();
		}
		
		
		 public void clickEvent(View v) {
		        if (v.getId() == R.id.abs_layout_iv_menu) {
		            showPopup(v);
		        }

		       
		    }
	
		
		 @Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
				String str = (String) adapterView.getItemAtPosition(position);
				//Dialogues.Toast(getApplicationContext(), str, Toast.LENGTH_SHORT);
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
						 BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
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
		    public void llenarMapaConDestino(){
		    	try{
			    	String destino = actvDestination.getText().toString();
	            	direccion_destino =destino;
	            	destino = destino.replaceAll(" ", "+");
	            	String consulta = "http://maps.googleapis.com/maps/api/geocode/json?address="+destino+"&sensor=true";
					String querty = Utils.doHttpConnection(consulta);
					InfoPoint = null;
					InfoPoint = new DirectionsJSONParser().parsePoints(querty);
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
						 .width(8).color(Color.BLUE).geodesic(true));
					  }
		    	}catch(Exception e){
		    		BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
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
		            	 BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
		            }
		            return routes;
				}
				
				// Executes in UI thread, after the parsing process
				@Override
				protected void onPostExecute(List<List<HashMap<String, String>>> result) {
					ArrayList<LatLng> points = null;
					PolylineOptions lineOptions = null;
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
						BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
					}
				}			
		    }   
		    
		    
		    public void showPopup(View v) {
				PopupMenu popup = new PopupMenu(Mapa_tracking.this, v);
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

								Intent i = new Intent(Mapa_tracking.this,UserSettingActivity.class);
								startActivityForResult(i, RESULT_SETTINGS);
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
				}
				
			}

			
			
			
}
