package codigo.labplc.mx.traxi.log;

/**
 * Bean que auxilia a ACRA en caso de que la app falle
 * 
 * @author mikesaurio
 *
 */
public class DatosLogBean {

	public static final String marcaCel_log = android.os.Build.MODEL;
	public static final int versionAndroid_log = android.os.Build.VERSION.SDK_INT;
	public static String tagLog = "Traxi";
	public static String descripcion_log = "falla";

	static	String[] mapper = new String[] { "ANDROID BASE", "ANDROID BASE 1.1",
			"CUPCAKE", "CUR_DEVELOPER","DONUT", "ECLAIR", "ECLAIR_0_1", "ECLAIR_MR1", "FROYO",
			"GINGERBREAD", "GINGERBREAD_MR1", "HONEYCOMB", "HONEYCOMB_MR1",
			"HONEYCOMB_MR2", "ICE_CREAM_SANDWICH", "ICE_CREAM_SANDWICH_MR1",
			"JELLY_BEAN","JELLY_BEAN_MR1","JELLY_BEAN_MR2","KITKAT" };

	/**
	 * GET marcaCel
	 * @return (String) marca del celular
	 */
	public static String getMarcaCel() {
		return marcaCel_log;
	}

	/**
	 * GET version android
	 * @return (String) version de android del celular
	 */
	public static  String getVersionAndroid() {
		int index = versionAndroid_log ;
		String versionName = index < mapper.length ? mapper[index]
				: "UNKNOWN_VERSION"; // > JELLY_BEAN)
		return versionName;
	}

	/**
	 * GET TagLog
	 * @return (String) TAG en donde fallo 
	 */
	public static String getTagLog() {
		return tagLog;
	}

	/**
	 * SET TAGLog
	 * @param tl (String) TAG de la clase
	 */
	public static void setTagLog(String tl) {
		tagLog = tl;
	}

	/**
	 * GET descripcion
	 * @return (String) descripcion de la falla
	 */
	public static String getDescripcion() {
		return descripcion_log;
	}

	/**
	 * SET descripcion
	 * @param des (String) descripcion de la falla
	 */
	public static void setDescripcion(String des) {
		descripcion_log = des;
	}

}
