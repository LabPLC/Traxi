package codigo.labplc.mx.traxi.buscarplaca;
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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.configuracion.UserSettingActivity;
import codigo.labplc.mx.traxi.dialogos.Dialogos;
import codigo.labplc.mx.traxi.fonts.fonts;
import codigo.labplc.mx.traxi.log.DatosLogBean;
import codigo.labplc.mx.traxi.utils.Utils;

/**
 * toma una foto a la puerta del taxi y regresa el numero
 * @author mikesaurio
 *
 */
@SuppressWarnings("deprecation")
public class BuscaPlacaFoto extends Activity implements SurfaceHolder.Callback,OnClickListener{
	
	
	private static final int RESULT_SETTINGS = 1;
	private static final int RESULT_FOTO = 2;
	public final String TAG = this.getClass().getSimpleName();
	private Camera camera;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private boolean previewing = false;
	private Button busca_placa_btn_tomarfoto;
	private Activity context;
	private String foto;
	private String resultado="";
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_busca_placa_foto);
		context= this;
		init(this);
		
	}

	

	 
	
	public void init(Activity con){
		DatosLogBean.setTagLog(TAG);

		//creamos un directorio
		File path=getFilesDir();
		String fullPath =path.toString() + "/placas";
		final File file = new File(fullPath);
		if(!file.exists()){
			file.mkdir();
		}else{
			Utils.DeleteRecursive(file);
		}
		
		
		foto = file + "/imagen"+ Utils.getCode() + ".jpg";
		
		getWindow().setFormat(PixelFormat.UNKNOWN);
		
		
		Utils.crearActionBar(BuscaPlacaFoto.this, R.layout.abs_layout_back,getResources().getString(R.string.busca_placa_tomar_foto),0.0f);//creamos el ActionBAr
		((ImageView) findViewById(R.id.abs_layout_iv_menu)).setOnClickListener(this);
		((ImageView) findViewById(R.id.abs_layout_iv_logo)).setOnClickListener(this);
		
		
		((TextView)findViewById(R.id.busca_placa_tv_foto)).setTypeface(new fonts(context).getTypeFace(fonts.FLAG_ROJO));
		((TextView)findViewById(R.id.busca_placa_tv_foto)).setTextColor(getResources().getColor(R.color.color_vivos));

		
		
		

		surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		busca_placa_btn_tomarfoto =(Button)findViewById(R.id.busca_placa_btn_tomarfoto_foto);
		busca_placa_btn_tomarfoto.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		busca_placa_btn_tomarfoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(busca_placa_btn_tomarfoto.getText().toString().equals(getResources().getString(R.string.busca_placa_btn_tomarfoto_entendi))){
					cerrarGuia();
					SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("guia_camara", "aceptar");
					editor.commit();
					
				}else{
					try{
						if(Utils.hasInternet(context)){
							camera.takePicture(myShutterCallback,myPictureCallback_RAW, myPictureCallback_JPG);
						}else{
							Dialogos.Toast(context,getResources().getString(R.string.no_internet_connection), Toast.LENGTH_LONG);
						}
					}catch(Exception e){
						DatosLogBean.setDescripcion(Utils.getStackTrace(e));
					}
				}
			}
		});

		SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi", Context.MODE_PRIVATE);
		String guia_camara = prefs.getString("guia_camara", null);
		if(guia_camara!=null){
			cerrarGuia();
		}
		
		
		
		((ImageView) findViewById(R.id.busca_placa_iv_ayuda)).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				abrirGuia();
				((ImageView) findViewById(R.id.busca_placa_iv_ayuda)).setVisibility(ImageView.INVISIBLE);
			}
		});
	}


/**
 * elimina 
 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	public void cerrarGuia() {
		((ImageView) findViewById(R.id.busca_placa_iv_ayuda)).setVisibility(ImageView.VISIBLE);
		busca_placa_btn_tomarfoto.setText(getResources().getString(R.string.busca_placa_btn_tomarfoto));
		((LinearLayout)findViewById(R.id.busca_placa_ll_guia)).setVisibility(LinearLayout.GONE);
		surfaceView.setBackground(null);
	}

	/**
	 * abrir 
	 */
		@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		public void abrirGuia() {
			busca_placa_btn_tomarfoto.setText(getResources().getString(R.string.busca_placa_btn_tomarfoto_entendi));
			((LinearLayout)findViewById(R.id.busca_placa_ll_guia)).setVisibility(LinearLayout.VISIBLE);
			surfaceView.setBackground(getResources().getDrawable(R.drawable.carro));
		}


	ShutterCallback myShutterCallback = new ShutterCallback(){

		@Override
		public void onShutter() {

		}};

		PictureCallback myPictureCallback_RAW = new PictureCallback(){

			@Override
			public void onPictureTaken(byte[] arg0, Camera arg1) {

			}};

			PictureCallback myPictureCallback_JPG = new PictureCallback(){

				@Override
				public void onPictureTaken(byte[] arg0, Camera arg1) {
					Bitmap bitmapPicture  =  BitmapFactory.decodeByteArray(arg0, 0, arg0.length);	
					System.gc();
					Runtime.getRuntime().gc();
					Matrix mat = new Matrix();
					mat.postRotate(90);
					int alto_num = bitmapPicture.getWidth()/12;
					Bitmap esizedbitmap1 = Bitmap.createBitmap(bitmapPicture,(alto_num*5),0,(alto_num*2),bitmapPicture.getHeight(),mat,true);
					Bitmap	resized = Utils.toGrayscale(Bitmap.createScaledBitmap(esizedbitmap1,(int)(esizedbitmap1.getWidth()*0.5), (int)(esizedbitmap1.getHeight()*0.5), true));
					try{
						File file = new File(foto);
						FileOutputStream fOut = new FileOutputStream(file);
						resized.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
						fOut.flush();
						fOut.close();
						Uploaded nuevaTareas = new Uploaded();
						nuevaTareas.execute(foto);
					
						if (camera != null) {
							camera.setPreviewDisplay(surfaceHolder);
							camera.startPreview();
							previewing = true;
						}
						
					}
					catch (Exception e) {
						DatosLogBean.setDescripcion(Utils.getStackTrace(e));
					}
				}};

				@Override
				public void surfaceChanged(SurfaceHolder holder, int format, int width,
						int height) {

					if (previewing) {
						camera.stopPreview();
						previewing = false;
					}
					try {
						if (camera != null) {
					//	CamcorderProfile profile ;
						Camera.Parameters parameters = camera.getParameters();
						camera.setParameters(parameters);
						camera.setPreviewDisplay(holder);
						camera.startPreview();
						}
					}
					catch (IOException e) {
						DatosLogBean.setDescripcion(Utils.getStackTrace(e));
					}
					if (camera != null) {
						try {

							camera.setPreviewDisplay(surfaceHolder);
							camera.startPreview();
							previewing = true;
						} catch (IOException e) {
							DatosLogBean.setDescripcion(Utils.getStackTrace(e));
						}
					}
					if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
					{   
						if (camera != null) {
						camera.setDisplayOrientation(90);
						}
					}
					if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
					{        
						if (camera != null) {
						camera.setDisplayOrientation(180);
						}
					}
				}

				@Override
				public void surfaceCreated(SurfaceHolder holder) {
					if(camera==null){
						try{
							camera = Camera.open();
							
						}catch(Exception e){
							DatosLogBean.setDescripcion(Utils.getStackTrace(e));
							    try {
							    	camera = Camera.open(0);
							    } catch (RuntimeException f) {
									DatosLogBean.setDescripcion(Utils.getStackTrace(f));
							  }
							
						}
					}


				}

				@Override
				public void surfaceDestroyed(SurfaceHolder holder) {
					try{
					camera.stopPreview();
					camera.release();
					camera = null;
					previewing = false;
					}catch(Exception e){
						DatosLogBean.setDescripcion(Utils.getStackTrace(e));
					}
				}

				

/**
 * Calse que envia la foto por POST al servidor para ser analizada regresa el OCR
 * @author mikesaurio
 *
 */
				class Uploaded extends AsyncTask<String, Void, Void> {

					private ProgressDialog pDialog;
					private String miFoto = "";
					
					public static final int HTTP_TIMEOUT = 60 * 1000;
					@Override
					protected Void doInBackground(String... params) {
						miFoto = (String) params[0];
						try

						{
							HttpContext localContext = new BasicHttpContext();
							HttpClient httpclient = new DefaultHttpClient();
							StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
							StrictMode.setThreadPolicy(policy);
							final HttpParams par = httpclient.getParams();
							HttpConnectionParams.setConnectionTimeout(par, HTTP_TIMEOUT);
							HttpConnectionParams.setSoTimeout(par, HTTP_TIMEOUT);
							ConnManagerParams.setTimeout(par, HTTP_TIMEOUT);
							HttpPost  httppost = new HttpPost("http://codigo.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=identificaplaca");		
							MultipartEntity entity = new MultipartEntity();


							File file = new File(miFoto);
							Bitmap myBitmap =BitmapFactory.decodeFile(file.getAbsolutePath());
							FileOutputStream fOut = new FileOutputStream(file);
							myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
							fOut.flush();
							fOut.close();

							entity.addPart("foto", new FileBody(file));
							System.setProperty("http.keepAlive", "false");
							httppost.setEntity(entity);
							HttpResponse response = null;
							response = httpclient.execute(httppost, localContext);
							BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
							StringBuffer sb = new StringBuffer("");
							String linea = "";
							String NL = System.getProperty("line.separator");

							while ((linea = in.readLine()) != null) {
								sb.append(linea + NL);
							}
							in.close();
							resultado = sb.toString();
							httpclient = null;
							response = null;
							if (resultado != null&&!resultado.equals("")){
								resultado=resultado.replaceAll("\n", "").replaceAll(" ", "").replaceAll("\"", "");
								if(resultado.length()!=6){
									resultado="falla";
								}
							} else {
								Log.d("error 202", "resultado: "+resultado);
								resultado="falla";
							}
							file.delete();
						} catch (Exception e) {
							DatosLogBean.setDescripcion(Utils.getStackTrace(e));
						}
						return null;
					}

					protected void onPreExecute() {
						super.onPreExecute();
						pDialog = new ProgressDialog(context);
						pDialog.setCanceledOnTouchOutside(false);
						pDialog.setMessage(getResources().getString(R.string.cargando_foto));
						pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
						pDialog.setCancelable(true);
						pDialog.show();
					}

					protected void onPostExecute(Void result) {
						super.onPostExecute(result);
						if(resultado.equals("falla")){
							Dialogos.Toast(context, getResources().getString(R.string.foto_fallida), Toast.LENGTH_LONG);
						}else{
							Dialogos.Toast(context, resultado, Toast.LENGTH_LONG);
							
							Intent returnIntent = new Intent();
							returnIntent.putExtra("result",resultado);
							setResult(RESULT_FOTO,returnIntent);
							finish();
							
							
							
						}
						pDialog.dismiss();
					}
				}

				
				@Override
				public void onClick(View v) {
				        if (v.getId() == R.id.abs_layout_iv_menu) {
				            showPopup(v);
				        }else if (v.getId() == R.id.abs_layout_iv_logo) {
				        	atras();
						}

				       
				    }
				
				
				
				/**
				 * sobreEscritura de onBack press
				 */
				public void atras(){
					Intent returnIntent = new Intent();
					returnIntent.putExtra("result",resultado);
					setResult(RESULT_FOTO,returnIntent);
					finish();
				}
				
				
				
				 @Override
				public void onBackPressed() {
					 atras();
				}



				/**
				 *  
				 * @param v
				 */
				public void showPopup(View v) {
						PopupMenu popup = new PopupMenu(BuscaPlacaFoto.this, v);
						MenuInflater inflater = popup.getMenuInflater();
						inflater.inflate(R.menu.popup, popup.getMenu());
						int positionOfMenuItem = 0; 
						MenuItem item = popup.getMenu().getItem(positionOfMenuItem);
						SpannableString s = new SpannableString(getResources().getString(R.string.action_settings));
						s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_base)), 0, s.length(), 0);
						item.setTitle(s);

						popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(MenuItem item) {
								switch (item.getItemId()) {

								case R.id.configuracion_pref:

										Intent i = new Intent(BuscaPlacaFoto.this,UserSettingActivity.class);
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

					
					
				
}
