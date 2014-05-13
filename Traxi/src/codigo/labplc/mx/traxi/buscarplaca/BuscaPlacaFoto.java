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

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.media.CamcorderProfile;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
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
import codigo.labplc.mx.traxi.buscarplaca.paginador.DatosAuto;
import codigo.labplc.mx.traxi.configuracion.UserSettingActivity;
import codigo.labplc.mx.traxi.dialogos.Dialogos;
import codigo.labplc.mx.traxi.fonts.fonts;
import codigo.labplc.mx.traxi.log.BeanDatosLog;
import codigo.labplc.mx.traxi.utils.Utils;

public class BuscaPlacaFoto extends Activity implements SurfaceHolder.Callback,OnClickListener{
	
	
	private static final int RESULT_SETTINGS = 1;
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
		BeanDatosLog.setTagLog(TAG);

		foto = Environment.getExternalStorageDirectory() + "/imagen"+ Utils.getCode() + ".jpg";
		
		getWindow().setFormat(PixelFormat.UNKNOWN);
		
		 final ActionBar ab = getActionBar();
	     ab.setDisplayShowHomeEnabled(false);
	     ab.setDisplayShowTitleEnabled(false);     
	     final LayoutInflater inflater = (LayoutInflater)getSystemService("layout_inflater");
	     View view = inflater.inflate(R.layout.abs_layout,null);   
	     ((TextView) view.findViewById(R.id.abs_layout_tv_titulo)).setTypeface(new fonts(BuscaPlacaFoto.this).getTypeFace(fonts.FLAG_MAMEY));
	     ab.setDisplayShowCustomEnabled(true);
	     
	     ImageView abs_layout_iv_menu = (ImageView) view.findViewById(R.id.abs_layout_iv_menu);
	     abs_layout_iv_menu.setOnClickListener(this);
	     ab.setCustomView(view,new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
	     ab.setCustomView(view);

		((TextView)findViewById(R.id.inicio_de_trabajo_tv_foto)).setTypeface(new fonts(context).getTypeFace(fonts.FLAG_ROJO));
		((TextView)findViewById(R.id.inicio_de_trabajo_tv_foto)).setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));


		surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		busca_placa_btn_tomarfoto =(Button)findViewById(R.id.busca_placa_btn_tomarfoto_foto);
		busca_placa_btn_tomarfoto.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_AMARILLO));

		busca_placa_btn_tomarfoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try{
					if(Utils.hasInternet(context)){
						camera.takePicture(myShutterCallback,myPictureCallback_RAW, myPictureCallback_JPG);
					}else{
						Dialogos.Toast(context, "No tienes Internet", Toast.LENGTH_LONG);
					}
				}catch(Exception e){
					BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
				}
			}
		});


		
	
		
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
					//Bitmap bMapRotate = Bitmap.createBitmap(bitmapPicture, 0, 0,bitmapPicture.getWidth(), bitmapPicture.getHeight(), mat, true);
					int alto_num = bitmapPicture.getHeight()/12;
					Bitmap esizedbitmap1 = Bitmap.createBitmap(bitmapPicture,0,(alto_num*5),bitmapPicture.getWidth(),(alto_num*2),mat,true);
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
						BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
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
						CamcorderProfile profile ;
						Camera.Parameters parameters = camera.getParameters();
						camera.setParameters(parameters);
						camera.setPreviewDisplay(holder);
						camera.startPreview();
						}
					}
					catch (IOException e) {
						BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
					}
					if (camera != null) {
						try {

							camera.setPreviewDisplay(surfaceHolder);
							camera.startPreview();
							previewing = true;
						} catch (IOException e) {
							BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
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
							BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
							    try {
							    	camera = Camera.open(0);
							    } catch (RuntimeException f) {
									BeanDatosLog.setDescripcion(Utils.getStackTrace(f));
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
						BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
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
							BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
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
							Intent intent= new Intent().setClass(context,DatosAuto.class);
							intent.putExtra("placa", resultado);
							context.startActivityForResult(intent, 0);
							context.finish();
						}
						pDialog.dismiss();
					}
				}

				
				@Override
				public void onClick(View v) {
				        if (v.getId() == R.id.abs_layout_iv_menu) {
				            showPopup(v);
				        }

				       
				    }
				
				 public void showPopup(View v) {
						PopupMenu popup = new PopupMenu(BuscaPlacaFoto.this, v);
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
