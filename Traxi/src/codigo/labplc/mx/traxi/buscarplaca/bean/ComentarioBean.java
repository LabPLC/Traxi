package codigo.labplc.mx.traxi.buscarplaca.bean;

/**
 * Bean que guarda los comentarios de una placa
 * 
 * @author mikesaurio
 *
 */
public class ComentarioBean {
 private float calificacion = 0;
 private String comentario ="";
 private String id_face= "0";
 private String fecha_comentario=null;
 
 /**
  * GET fecha comentario
  * @return (String) fecha del comentario
  */
public String getFecha_comentario() {
	return fecha_comentario;
}

/**
 * SET fecha comentario
 * @param fecha_comentario (String) fecha del comentario
 */
public void setFecha_comentario(String fecha_comentario) {
	this.fecha_comentario = fecha_comentario;
}

/**
 * GET id facebook
 * @return (String) id de facebook
 */
public String getId_facebook() {
	return id_face;
}

/**
 * SET id facebook
 * @param id_face (String) id de facebook
 */
public void setId_facebook(String id_face) {
	this.id_face = id_face;
}

/**
 * GET calificacion
 * @return (float) calificacion dada por el usuario
 */
public float getCalificacion() {
	return calificacion;
}

/**
 * SET calificacion
 * @param calificacion (float) calificacion dada por el usuario
 */
public void setCalificacion(float calificacion) {
	this.calificacion = calificacion;
}

/**
 * GET comentario 
 * @return (String) comentario dado por el usuario
 */
public String getComentario() {
	return comentario;
}

/**
 * SET comenatario
 * @param comentario (String) comantario dado por el usuario
 */
public void setComentario(String comentario) {
	this.comentario = comentario;
}
}
