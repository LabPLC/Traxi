package codigo.labplc.mx.traxi.registro.bean;

/*
 * clase bean que maneja los datos del usuario
 */
public class UserBean {
	private String correo; //correo del usuario
	private String telemergencia= null; //telefono de emergencia
	private String UUID; //llave del usuario
	private String correoemergencia = null; //correo de emergencia
	private String telemergencia_2 = null; //telefono de emergencia
	private String correoemergencia_2 = null; //correo de emergencia
	private String os = "2"; //1 IOS 2 android
	
	
	public String getTelemergencia_2() {
		return telemergencia_2;
	}
	public void setTelemergencia_2(String telemergencia_2) {
		this.telemergencia_2 = telemergencia_2;
	}
	public String getCorreoemergencia_2() {
		return correoemergencia_2;
	}
	public void setCorreoemergencia_2(String correoemergencia_2) {
		this.correoemergencia_2 = correoemergencia_2;
	}

	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
	public String getTelemergencia() {
		return telemergencia;
	}
	public void setTelemergencia(String telemergencia) {
		this.telemergencia = telemergencia;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public String getCorreoemergencia() {
		return correoemergencia;
	}
	public void setCorreoemergencia(String correoemergencia) {
		this.correoemergencia = correoemergencia;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	
	
}