package codigo.labplc.mx.traxi.log;

import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
import codigo.labplc.mx.traxi.Traxi;
import codigo.labplc.mx.traxi.services.ServicioGeolocalizacion;
import codigo.labplc.mx.traxi.utils.Utils;
 
/**
 * Envia el reporte de fallo en caso de que la aplicacion se rompa
 * 
 * @author mikesaurio
 *
 */
@SuppressWarnings("deprecation")
public  class HockeySender implements ReportSender {
  public static final int HTTP_TIMEOUT = 30 * 1000;
  Traxi activity;
  String url;
 
  public HockeySender(Traxi traxi, String envio) {
	  activity= traxi;
	   url = envio;
}

@Override
  public void send(CrashReportData report) throws ReportSenderException {
	
    try {
    	Intent svc = new Intent(activity, ServicioGeolocalizacion.class);
    	activity.stopService(svc);
    	System.setProperty("http.keepAlive", "false");
		HttpClient httpclient = new DefaultHttpClient();
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		final HttpParams par = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(par, HTTP_TIMEOUT);
		HttpConnectionParams.setSoTimeout(par, HTTP_TIMEOUT);
		ConnManagerParams.setTimeout(par, HTTP_TIMEOUT);
		HttpPost httppost;
		 httppost = new HttpPost(url);
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("modelo", new StringBody(BeanDatosLog.getMarcaCel()+""));
		entity.addPart("version", new StringBody(BeanDatosLog.getVersionAndroid() + ""));
		entity.addPart("tag",new StringBody(BeanDatosLog.getTagLog() + ""));
		entity.addPart("descripcion", new StringBody(BeanDatosLog.getDescripcion() + ""));
		Log.d(BeanDatosLog.getTagLog(),BeanDatosLog.getDescripcion());
		System.setProperty("http.keepAlive", "false");
		httppost.setEntity(entity);
		
		httpclient.execute(httppost);

		
    }
    catch (Exception e) {
    	BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
    } 
   
  }
 
}