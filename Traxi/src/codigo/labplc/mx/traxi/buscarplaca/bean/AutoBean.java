package codigo.labplc.mx.traxi.buscarplaca.bean;

import java.util.ArrayList;
/**
 * Bean que guarda todos los datos de la consulta de placa
 * 
 * @author mikesaurio
 *
 */
public class AutoBean {
	private String marca="";
	private String submarca="";
	private String anio="";
	private String tipo="";
	private String placa;
	private int calificaion_app = 0;
	private float calificacion_usuarios =0;
	private int calificacion_final =0;
	private String descripcion_calificacion_app="";
	private String descripcion_revista="";
	private int imagen_revista = 0;
	private String descripcion_infracciones="";
	private int imagen_infraccones = 0;
	private String descripcion_vehiculo="";
	private int imagen_vehiculo = 0;
	private String descripcion_verificacion="";
	private int imagen_verificacion = 0;
	private String descripcion_tenencia="";
	private int imagen_teencia = 0;
	private ArrayList<ComentarioBean> arrayComentarioBean = new ArrayList<ComentarioBean>();
	private boolean hasrevista_ =false;
	private boolean hasinfracciones_ =false;
	private boolean hasanio_ =false;
	private boolean hasverificacion_ =false;
	private boolean hastenencia_ =false;
	
	/**
	 * GET revita
	 * @return (boolean) true si tiene revista
	 */
	public boolean isHasrevista_() {
		return hasrevista_;
	}

	/**
	 * SET revista
	 * @param hasrevista_ (boolean) true si tiene revista
	 */
	public void setHasrevista_(boolean hasrevista_) {
		this.hasrevista_ = hasrevista_;
	}

	/**
	 * GET infracciones
	 * 
	 * @return (boolean) true si no tiene infracciones
	 */
	public boolean isHasinfracciones_() {
		return hasinfracciones_;
	}

	/**
	 * SET infracciones
	 * @param hasinfracciones_ (boolean) true si no tiene infracciones
	 */
	public void setHasinfracciones_(boolean hasinfracciones_) {
		this.hasinfracciones_ = hasinfracciones_;
	}

	/**
	 * GET anio
	 * @return (true) si el anio esoptimo
	 */
	public boolean isHasanio_() {
		return hasanio_;
	}

	/**
	 * SET anio
	 * @param hasanio_ (boolean) true si el anio es optimo
	 */
	public void setHasanio_(boolean hasanio_) {
		this.hasanio_ = hasanio_;
	}

	/**
	 * GET verificacion
	 * @return (boolean) true si  tiene verificacion
	 */
	public boolean isHasverificacion_() {
		return hasverificacion_;
	}

	/**
	 * SET verificacion
	 * @param hasverificacion_ (boolean) true si tiene verificacion
	 */
	public void setHasverificacion_(boolean hasverificacion_) {
		this.hasverificacion_ = hasverificacion_;
	}

	/**
	 * GET tenencia
	 * @return (TRUE) si tiene tenencia
	 */
	public boolean isHastenencia_() {
		return hastenencia_;
	}

	/**
	 * SET tiene tenencia
	 * @param hastenencia_ (boolean) true si tiene tenencia
	 */
	public void setHastenencia_(boolean hastenencia_) {
		this.hastenencia_ = hastenencia_;
	}

	/**
	 * GET placa
	 * @return (String placa)
	 */
	public String getPlaca() {
		return placa;
	}
	
	/**
	 * SET placa
	 * @param placa (String) placa de taxi
	 */
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	
	/**
	 * GET calificacion final
	 * @return (int) calificacion promedio entre app y usuarios
	 */
	public int getCalificacion_final() {
		return calificacion_final;
	}
	
	/**
	 * SET calificacion final
	 * @param calificacion_final (int) calificacion promedio entre app y usuarios
	 */
	public void setCalificacion_final(int calificacion_final) {
		this.calificacion_final = calificacion_final;
	}
	
	/**
	 * GET comentariosBean
	 * @return (ArrayList<ComentarioBean>) comentarios de una placa
	 */
	public ArrayList<ComentarioBean> getArrayComentarioBean() {
		return arrayComentarioBean;
	}
	
	/**
	 * SET comentariosBean
	 * @param arrayComentarioBean (ArrayList<ComentarioBean>) comentarios de una placa
	 */
	public void setArrayComentarioBean(ArrayList<ComentarioBean> arrayComentarioBean) {
		this.arrayComentarioBean = arrayComentarioBean;
	}
	
	/**
	 * GET descripcion revista
	 * @return (String) descripcion para la revista vehicular
	 */
	public String getDescripcion_revista() {
		return descripcion_revista;
	}
	
	/**
	 * SET descripcion revista
	 * @param descripcion_revista (String) asignar la descripcion de la revista
	 */
	public void setDescripcion_revista(String descripcion_revista) {
		this.descripcion_revista = descripcion_revista;
	}
	
	/**
	 * GET imagen revista
	 * @return (int) imagen que se le asignara a la calificacion
	 */
	public int getImagen_revista() {
		return imagen_revista;
	}
	
	/**
	 * SET imagen revista
	 * @param imagen_revista (int) imagen que se le asignara a la calificacion
	 */
	public void setImagen_revista(int imagen_revista) {
		this.imagen_revista = imagen_revista;
	}
	
	/**
	 * GET descripcion
	 * @return (String) descripcion para las infracciones
	 */
	public String getDescripcion_infracciones() {
		return descripcion_infracciones;
	}
	
	/**
	 * SET descripcion infracciones
	 * @param descripcion_infracciones (String)  asignar la descripcion de las infracciones
	 */
	public void setDescripcion_infracciones(String descripcion_infracciones) {
		this.descripcion_infracciones = descripcion_infracciones;
	}
	
	/**
	 * GET imagen infracciones
	 * @return (int) imagen que se le asignara a las infracciones
	 */
	public int getImagen_infraccones() {
		return imagen_infraccones;
	}
	
	/**
	 * SET imagen infracciones
	 * @param imagen_infraccones (int) imagen que se le asignara a las infracciones
	 */
	public void setImagen_infraccones(int imagen_infraccones) {
		this.imagen_infraccones = imagen_infraccones;
	}
	
	/**
	 * GET descripcion vehiculo
	 * @return (String) descripcion de un vehiculo
	 */
	public String getDescripcion_vehiculo() {
		return descripcion_vehiculo;
	}
	
	/**
	 * SET descripcion de un vehiculo
	 * @param descripcion_vehiculo (String) asigna la descripcion de un vehiculo
	 */
	public void setDescripcion_vehiculo(String descripcion_vehiculo) {
		this.descripcion_vehiculo = descripcion_vehiculo;
	}
	
	/**
	 * GET imagen vehiculo
	 * @return (int) imagen que se le asignara a un vehiculo
	 */
	public int getImagen_vehiculo() {
		return imagen_vehiculo;
	}
	
	/**
	 * SET imagen vehiculo
	 * @param Imagen_vehiculo (int) imagen que se le asignara a un vehiculo
	 */
	public void setImagen_vehiculo(int imagen_vehiculo) {
		this.imagen_vehiculo = imagen_vehiculo;
	}
	
	/**
	 * SET descripcion verificacion
	 * @return (String) descripcion de la verificacion del vehiculo
	 */
	public String getDescripcion_verificacion() {
		return descripcion_verificacion;
	}
	
	/**
	 * SET descripcion verfificacion
	 * @param descripcion_verificacion (String) descripcion de la verificacion del vehiculo
	 */
	public void setDescripcion_verificacion(String descripcion_verificacion) {
		this.descripcion_verificacion = descripcion_verificacion;
	}
	
	/**
	 * GET imagen verificacion
	 *  @return (int) imagen que se le asignara a la verificacion
	 */
	public int getImagen_verificacion() {
		return imagen_verificacion;
	}
	
	/**
	 * SET imagen verificacion
	 * @param imagen_verificacion (int) imagen que se le asignara a un vehiculo
	 */
	public void setImagen_verificacion(int imagen_verificacion) {
		this.imagen_verificacion = imagen_verificacion;
	}
	
	/**
	 * GET descripcion tenencia
	 * @return (String) descripcion de la tenencia del vehiculo
	 */
	public String getDescripcion_tenencia() {
		return descripcion_tenencia;
	}
	
	/**
	 * SET Descripcion tenencia
	 * @param descripcion_tenencia (String) descripcion de la tenencia de un vehiculo
	 */
	public void setDescripcion_tenencia(String descripcion_tenencia) {
		this.descripcion_tenencia = descripcion_tenencia;
	}
	
	/**
	 * GET imagen tenencia
	 * @return (int) imagen que se le asignara a la tenencia
	 */
	public int getImagen_teencia() {
		return imagen_teencia;
	}
	
	/**
	 * SET imagen tenencia
	 * @param imagen_teencia (int) imagen que se le asignara a la tenencia
	 */
	public void setImagen_teencia(int imagen_teencia) {
		this.imagen_teencia = imagen_teencia;
	}
	
	/**
	 * GET descripcion calificacion
	 * @return (String) descripcion de la calificacion de un vehiculo dada por la app
	 */
	public String getDescripcion_calificacion_app() {
		return descripcion_calificacion_app;
	}
	
	/**
	 * SET descripcion calificacion
	 * @param descripcion_calificacion_app (String) descripcion de la calificacion de un vehiculo dada por la app
	 */
	public void setDescripcion_calificacion_app(String descripcion_calificacion_app) {
		this.descripcion_calificacion_app = descripcion_calificacion_app;
	}
	
	/**
	 * GET calificacion usuarios
	 * @return (int)  calificacion de un vehiculo dada por los usuarios
	 */
	public float getCalificacion_usuarios() {
		return calificacion_usuarios;
	}
	
	/**
	 * SET calificacion usuarios
	 * @param Calificacion_usuarios (int)  calificacion de un vehiculo dada por los usuarios
	 */
	public void setCalificacion_usuarios(float califParcial) {
		this.calificacion_usuarios = califParcial;
	}
	
	
	/**
	 * GET calificacion app
	 * @return (int)  calificacion de un vehiculo dada por la app
	 */
	public int getCalificaion_app() {
		return calificaion_app;
	}
	
	/**
	 * SET calificacion app
	 * @param Calificacion_app (int)  calificacion de un vehiculo dada por la ap
	 */
	public void setCalificaion_app(int calificaion_app) {
		this.calificaion_app = calificaion_app;
	}
	
	/**
	 * GET marca
	 * @return (String) marca del vehiculo
	 */
	public String getMarca() {
		return marca;
	}
	
	/**
	 * SET marca
	 * @param marca (String) marca del vehiculo
	 */
	public void setMarca(String marca) {
		this.marca = marca;
	}
	
	/**
	 * GET submarca
	 * @return (String) submarca del vehiculo
	 */
	public String getSubmarca() {
		return submarca;
	}
	
	/**
	 * SET submarca
	 * @param submarca (String) submarca del vehiculo
	 */
	public void setSubmarca(String submarca) {
		this.submarca = submarca;
	}
	
	/**
	 * GET anio del vehiculo
	 * @return (String) anio del vehiculo
	 */
	public String getAnio() {
		return anio;
	}
	
	/**
	 * SET anio 
	 * @param anio (String) anio del vehiculo
	 */
	public void setAnio(String anio) {
		this.anio = anio;
	}
	
	/**
	 * GET tipo
	 * @return (String) tipo de vehiculo
	 */
	public String getTipo() {
		return tipo;
	}
	
	/**
	 * SET tipo
	 * @param tipo (String) tipo del vehiculo
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
