package codigo.labplc.mx.traxi.buscarplaca.bean;

public class ComentarioBean {
 private float calificacion = 0;
 private String comentario ="";
 private String id_face= "0";
 
 
public String getId_facebook() {
	return id_face;
}
public void setId_facebook(String id_face) {
	this.id_face = id_face;
}
public float getCalificacion() {
	return calificacion;
}
public void setCalificacion(float calificacion) {
	this.calificacion = calificacion;
}
public String getComentario() {
	return comentario;
}
public void setComentario(String comentario) {
	this.comentario = comentario;
}
}
