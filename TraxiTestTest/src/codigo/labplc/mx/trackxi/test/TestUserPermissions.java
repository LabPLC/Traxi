package codigo.labplc.mx.trackxi.test;

import android.content.pm.PackageManager;
import android.test.AndroidTestCase;

/**
 * revisa que todos los permisos esten incluidos en el manifest
 * @author mikesaurio
 *
 */
public class TestUserPermissions extends AndroidTestCase{
	private static String PAKETE_TRAXI ="codigo.labplc.mx.trackxi";
	
	/**
	 * Metodo que valida si estan los permisos de interner
	 *<uses-permission android:name="android.permission.INTERNET" />
	 */
    public void testPermissionsINTERNET_isInManifest() {
        PackageManager pm = this.getContext().getPackageManager();
        int res = pm.checkPermission("android.permission.INTERNET", PAKETE_TRAXI); 
        assertEquals(res, PackageManager.PERMISSION_GRANTED );
    }
    
    /**
	 * Metodo que valida si estan los permisos de almacenamiento
	 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
	 */
    public void testPermissionsWRITE_EXTERNAL_STORAGE_isInManifest() {
        PackageManager pm = this.getContext().getPackageManager();
        int res = pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", PAKETE_TRAXI); 
        assertEquals(res, PackageManager.PERMISSION_GRANTED );
    }
    
    /**
	 * Metodo que valida si estan los permisos de localizacion
	 *<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
	 * 
	 */
    public void testPermissionsACCESS_COARSE_LOCATION_isInManifest() {
        PackageManager pm = this.getContext().getPackageManager();
        int res = pm.checkPermission("android.permission.ACCESS_COARSE_LOCATION", PAKETE_TRAXI); 
        assertEquals(res, PackageManager.PERMISSION_GRANTED );
    }
    
    /**
	 * Metodo que valida si estan los permisos de localizacion fina
	 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
	 * 
	 */
    public void testPermissionsACCESS_FINE_LOCATION_isInManifest() {
        PackageManager pm = this.getContext().getPackageManager();
        int res = pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", PAKETE_TRAXI); 
        assertEquals(res, PackageManager.PERMISSION_GRANTED );
    }
    
    /**
	 * Metodo que valida si estan los permisos de estado de la red
	 *     <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
	 * 
	 */
    public void testPermissionsACCESS_NETWORK_STATE_isInManifest() {
        PackageManager pm = this.getContext().getPackageManager();
        int res = pm.checkPermission("android.permission.ACCESS_NETWORK_STATE", PAKETE_TRAXI); 
        assertEquals(res, PackageManager.PERMISSION_GRANTED );
    }
    
    /**
	 * Metodo que valida si estan los permisos de obtien la cuenta del usuario
	 *     <uses-permission android:name="android.permission.GET_ACCOUNTS" />
	 * 
	 */
    public void testPermissionsGET_ACCOUNTS_isInManifest() {
        PackageManager pm = this.getContext().getPackageManager();
        int res = pm.checkPermission("android.permission.GET_ACCOUNTS", PAKETE_TRAXI); 
        assertEquals(res, PackageManager.PERMISSION_GRANTED );
    }
    
    /**
	 * Metodo que valida si estan los permisos de obtiene las credenciales
	 *     <uses-permission android:name="android.permission.USE_CREDENTIALS" />
	 * 
	 */
    public void testPermissionsUSE_CREDENTIALS_isInManifest() {
        PackageManager pm = this.getContext().getPackageManager();
        int res = pm.checkPermission("android.permission.USE_CREDENTIALS", PAKETE_TRAXI); 
        assertEquals(res, PackageManager.PERMISSION_GRANTED );
    }
    
    /**
	 * Metodo que valida si estan los permisos de vibrado
	 *   <uses-permission android:name="android.permission.VIBRATE" />
	 * 
	 */
    public void testPermissionsVIBRATE_isInManifest() {
        PackageManager pm = this.getContext().getPackageManager();
        int res = pm.checkPermission("android.permission.VIBRATE", PAKETE_TRAXI); 
        assertEquals(res, PackageManager.PERMISSION_GRANTED );
    }
    
    /**
	 * Metodo que valida si estan los permisos de envio de mensajes SMS
	 *     <uses-permission android:name="android.permission.SEND_SMS" />
	 * 
	 */
    public void testPermissionsSEND_SMSSEND_SMS_isInManifest() {
        PackageManager pm = this.getContext().getPackageManager();
        int res = pm.checkPermission("android.permission.SEND_SMS", PAKETE_TRAXI); 
        assertEquals(res, PackageManager.PERMISSION_GRANTED );
    }
    
    /**
	 * Metodo que valida si estan los permisos de uso de la camara
	 *     <uses-permission android:name="android.permission.CAMERA" />
	 * 
	 */
    public void testPermissionsCAMERA_isInManifest() {
        PackageManager pm = this.getContext().getPackageManager();
        int res = pm.checkPermission("android.permission.CAMERA", PAKETE_TRAXI); 
        assertEquals(res, PackageManager.PERMISSION_GRANTED );
    }
    
    /**
	 * Metodo que valida si estan los permisos de leer los contactos del cel
	 *     <uses-permission android:name="android.permission.READ_CONTACTS" />
	 * 
	 */
    public void testPermissionsREAD_CONTACTS_isInManifest() {
        PackageManager pm = this.getContext().getPackageManager();
        int res = pm.checkPermission("android.permission.READ_CONTACTS", PAKETE_TRAXI); 
        assertEquals(res, PackageManager.PERMISSION_GRANTED );
    }
    
    /**
	 * Metodo que valida si estan los permisos de cerrar el teclado
	 *     <uses-permission android:name="android.permission.DISABLE_KEYGUARD" />
	 * 
	 */
    public void testPermissionsDISABLE_KEYGUARD_isInManifest() {
        PackageManager pm = this.getContext().getPackageManager();
        int res = pm.checkPermission("android.permission.DISABLE_KEYGUARD", PAKETE_TRAXI); 
        assertEquals(res, PackageManager.PERMISSION_GRANTED );
    }
    
    
    /**
	 * Metodo que valida si estan los permisos de leer los GServices
	 *     <uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES" />

	 */
    public void testPermissionsautoREAD_GSERVICES_isInManifest() {
        PackageManager pm = this.getContext().getPackageManager();
        int res = pm.checkPermission("com.google.android.providers.gsf.permission.READ_GSERVICES", PAKETE_TRAXI); 
        assertEquals(res, PackageManager.PERMISSION_GRANTED );
    }
    
}