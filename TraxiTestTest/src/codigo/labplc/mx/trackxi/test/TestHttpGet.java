package codigo.labplc.mx.trackxi.test;

import junit.framework.TestCase;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;


/**
 * clase que prueba todos los WebServices
 * 
 * @author mikesaurio
 *
 */
public class TestHttpGet extends TestCase {
	private String PLACA_PRUEBA ="A05601";
	private String TAG = this.getClass().getSimpleName();
	
	 
    public void testHttpGet() {
    	
    	//Comentarios de una placa
    	pruebasGet("http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=getcomentario&placa="+PLACA_PRUEBA,0);
    	
    	//Datos de un vehiculo
    	pruebasGet("http://dev.datos.labplc.mx/movilidad/vehiculos/"+PLACA_PRUEBA+".json",1);
    	
    	//Si un taxi esta en la revista vehicular
    	pruebasGet("http://mikesaurio.dev.datos.labplc.mx/movilidad/taxis/"+PLACA_PRUEBA+".json",2);
    	
    	//obtiene distancia y tiempo de un trayecto
    	pruebasGet("http://datos.labplc.mx/~mikesaurio/taxi.php?act=chofer&type=getGoogleData&lato=19.4323221&lngo=-99.123212&latd=19.2345654&lngd=-99.654322&filtro=velocidad"
    			,3);
   
    	//obtiene coordenadas de una direccion
    	pruebasGet("http://aps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&sensor&sensor=true", 4);
    
    	//calificar el servicio
    	pruebasGet("http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=addcomentario"
							+"&id_usuario="+0
							+"&calificacion="+5
							+"&comentario="+"bien"
							+"&placa="+PLACA_PRUEBA
							+"&id_face="+0
							+"&pointinilat="+19.4323221
							+"&pointinilon="+-99.123212
							+"&pointfinlat="+19.2345654
							+"&pointfinlon="+-99.654322
							+"&horainicio="+"2014-01-01+12:12:12"
							+"&horafin="+"2014-01-01+12:20:01", 5);
    	
    }
    
    
    /**
     * Metodo que hace test a metodos GET
     * 
     * @param url String (Url a consultar)
     */
    public void pruebasGet(String url, int key){
    	 HttpGet httpget = new HttpGet(url);
         HttpClient httpclient = new DefaultHttpClient();
         HttpResponse response = null;
         try {
             response = httpclient.execute(httpget);
             Log.d(TAG,key+": "+response.toString());
         } catch(Exception e) {
             fail("Excepcion");
         }
         assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode() );
    	
    }
 
}