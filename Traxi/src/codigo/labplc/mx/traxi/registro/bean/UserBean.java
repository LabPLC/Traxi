package codigo.labplc.mx.traxi.registro.bean;

/**
 * clase bean que maneja los datos del usuario
 * 
 * @author mikesaurio
 *
 */
public class UserBean {
	private String correo; //correo del usuario
	private String telemergencia= null; //telefono de emergencia
	private String UUID; //llave del usuario
	private String correoemergencia = null; //correo de emergencia
	private String telemergencia_2 = null; //telefono de emergencia
	private String correoemergencia_2 = null; //correo de emergencia
	private String os = "2"; //1 IOS 2 android
	
	/**
	 * GET telefono de emergencia 2
	 * @return (String) segundo telefono de emergencia
	 */
	public String getTelemergencia_2() {
		return telemergencia_2;
	}
	
	/**
	 * SET telefono de emergencia 2
	 * @param telemergencia_2 (String) 
	 */
	public void setTelemergencia_2(String telemergencia_2) {
		this.telemergencia_2 = telemergencia_2;
	}
	
	/**
	 * GET correo emergencia 2
	 * @return (String) segundo correo de emergencia
	 */
	public String getCorreoemergencia_2() {
		return correoemergencia_2;
	}
	
	/**
	 * SET correo emergencia 2
	 * @param correoemergencia_2 (String) segundo correo de emergencia
	 */
	public void setCorreoemergencia_2(String correoemergencia_2) {
		this.correoemergencia_2 = correoemergencia_2;
	}

	/**
	 * GET correo
	 * @return (String) correo de emergencia
	 */
	public String getCorreo() {
		return correo;
	}
	
	/**
	 * SET correo
	 * @param correo (String) correo de emergencia
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
	/**
	 * GET telefono
	 * @return (String) telefono de emergencia
	 */
	public String getTelemergencia() {
		return telemergencia;
	}
	 /**
	  * SET telefono de emergencia
	  * @param telemergencia (String) telefono de emergencia
	  */
	public void setTelemergencia(String telemergencia) {
		this.telemergencia = telemergencia;
	}
	
	/**
	 * GET UUID 
	 * @return (String) llave del usuario
	 */
	public String getUUID() {
		return UUID;
	}
	
	/**
	 * SET UUID
	 * @param uUID (String) llave del usuario
	 */
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	
	/**
	 * GET correo emergencia
	 * @return (String) correo del contacto de emergancia
	 */ 
	public String getCorreoemergencia() {
		return correoemergencia;
	}
	
	/**
	 * SET correo emergancia
	 * @param correoemergencia (String) correo del contacto e emergencia
	 */
	public void setCorreoemergencia(String correoemergencia) {
		this.correoemergencia = correoemergencia;
	}
	
	/**
	 * GET OS
	 * @return (String) 1=IOS, 2 =Android
	 */
	public String getOs() {
		return os;
	}
	
	/**
	 * SET OS
	 * @param os (String) 1 =IOS, 2= Android 
	 */
	public void setOs(String os) {
		this.os = os;
	}
	
	
}