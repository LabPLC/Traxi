package codigo.labplc.mx.trackxi.log;


public class BeanDatosLog {

	public static final String marcaCel_log = android.os.Build.MODEL;
	public static final int versionAndroid_log = android.os.Build.VERSION.SDK_INT;
	public static String tagLog = "Traxi";
	public static String descripcion_log = "falla";

	static	String[] mapper = new String[] { "ANDROID BASE", "ANDROID BASE 1.1",
			"CUPCAKE", "CUR_DEVELOPER","DONUT", "ECLAIR", "ECLAIR_0_1", "ECLAIR_MR1", "FROYO",
			"GINGERBREAD", "GINGERBREAD_MR1", "HONEYCOMB", "HONEYCOMB_MR1",
			"HONEYCOMB_MR2", "ICE_CREAM_SANDWICH", "ICE_CREAM_SANDWICH_MR1",
			"JELLY_BEAN","JELLY_BEAN_MR1","JELLY_BEAN_MR2","KITKAT" };

	public static String getMarcaCel() {
		return marcaCel_log;
	}

	public static  String getVersionAndroid() {
		int index = versionAndroid_log ;
		String versionName = index < mapper.length ? mapper[index]
				: "UNKNOWN_VERSION"; // > JELLY_BEAN)
		return versionName;
	}

	public static String getTagLog() {
		return tagLog;
	}

	public static void setTagLog(String tl) {
		tagLog = tl;
	}

	public static String getDescripcion() {
		return descripcion_log;
	}

	public static void setDescripcion(String des) {
		descripcion_log = des;
	}

}
