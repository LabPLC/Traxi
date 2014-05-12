package codigo.labplc.mx.traxi.buscarplaca.bean;

import java.util.ArrayList;

public class AutoBean {
	private String marca="";
	private String submarca="";
	private String anio="";
	private String tipo="";
	private int calificaion_app = 0;
	private int calificacion_usuarios =0;
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
	
	
	public int getCalificacion_final() {
		return calificacion_final;
	}
	public void setCalificacion_final(int calificacion_final) {
		this.calificacion_final = calificacion_final;
	}
	public ArrayList<ComentarioBean> getArrayComentarioBean() {
		return arrayComentarioBean;
	}
	public void setArrayComentarioBean(ArrayList<ComentarioBean> arrayComentarioBean) {
		this.arrayComentarioBean = arrayComentarioBean;
	}
	public String getDescripcion_revista() {
		return descripcion_revista;
	}
	public void setDescripcion_revista(String descripcion_revista) {
		this.descripcion_revista = descripcion_revista;
	}
	public int getImagen_revista() {
		return imagen_revista;
	}
	public void setImagen_revista(int imagen_revista) {
		this.imagen_revista = imagen_revista;
	}
	public String getDescripcion_infracciones() {
		return descripcion_infracciones;
	}
	public void setDescripcion_infracciones(String descripcion_infracciones) {
		this.descripcion_infracciones = descripcion_infracciones;
	}
	public int getImagen_infraccones() {
		return imagen_infraccones;
	}
	public void setImagen_infraccones(int imagen_infraccones) {
		this.imagen_infraccones = imagen_infraccones;
	}
	public String getDescripcion_vehiculo() {
		return descripcion_vehiculo;
	}
	public void setDescripcion_vehiculo(String descripcion_vehiculo) {
		this.descripcion_vehiculo = descripcion_vehiculo;
	}
	public int getImagen_vehiculo() {
		return imagen_vehiculo;
	}
	public void setImagen_vehiculo(int imagen_vehiculo) {
		this.imagen_vehiculo = imagen_vehiculo;
	}
	public String getDescripcion_verificacion() {
		return descripcion_verificacion;
	}
	public void setDescripcion_verificacion(String descripcion_verificacion) {
		this.descripcion_verificacion = descripcion_verificacion;
	}
	public int getImagen_verificacion() {
		return imagen_verificacion;
	}
	public void setImagen_verificacion(int imagen_verificacion) {
		this.imagen_verificacion = imagen_verificacion;
	}
	public String getDescripcion_tenencia() {
		return descripcion_tenencia;
	}
	public void setDescripcion_tenencia(String descripcion_tenencia) {
		this.descripcion_tenencia = descripcion_tenencia;
	}
	public int getImagen_teencia() {
		return imagen_teencia;
	}
	public void setImagen_teencia(int imagen_teencia) {
		this.imagen_teencia = imagen_teencia;
	}
	public String getDescripcion_calificacion_app() {
		return descripcion_calificacion_app;
	}
	public void setDescripcion_calificacion_app(String descripcion_calificacion_app) {
		this.descripcion_calificacion_app = descripcion_calificacion_app;
	}
	public int getCalificacion_usuarios() {
		return calificacion_usuarios;
	}
	public void setCalificacion_usuarios(int calificacion_usuarios) {
		this.calificacion_usuarios = calificacion_usuarios;
	}
	public int getCalificaion_app() {
		return calificaion_app;
	}
	public void setCalificaion_app(int calificaion_app) {
		this.calificaion_app = calificaion_app;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getSubmarca() {
		return submarca;
	}
	public void setSubmarca(String submarca) {
		this.submarca = submarca;
	}
	public String getAnio() {
		return anio;
	}
	public void setAnio(String anio) {
		this.anio = anio;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
