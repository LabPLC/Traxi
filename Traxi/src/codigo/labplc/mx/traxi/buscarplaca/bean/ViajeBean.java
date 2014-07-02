package codigo.labplc.mx.traxi.buscarplaca.bean;

public class ViajeBean {

	private int[] id;
	private String[] placa;
	private String[] hora_inicio;
	private String[] hora_fin;
	private String[] calificacion;
	private String[] comentario;
	private String[] inicio_viaje;
	private String[] fin_viaje;
	
	
	/**
	 * GET id
	 * @return (int[]) identificador
	 */
	public int[] getId() {
		return id;
	}

	/**
	 * SET id[]
	 * @param id (int[]) identificador
	 */
	public void setId(int[] id) {
		this.id = id;
	}

	/**
	 * GET placa
	 * @return (String[]) placa del viaje
	 */
	public String[] getPlaca() {
		return placa;
	}
	
	/**
	 * SET placa
	 * @param placa (String[]) placa del viaje
	 */
	public void setPlaca(String[] placa) {
		this.placa = placa;
	}
	
	/**
	 * GET hora inicio
	 * @return (String[]) hora del inicio del viaje
	 */
	public String[] getHora_inicio() {
		return hora_inicio;
	}
	
	/**
	 * SET hora inicio
	 * @param hora_inicio (String[]) hora en la que inicia el viaje
	 */
	public void setHora_inicio(String[] hora_inicio) {
		this.hora_inicio = hora_inicio;
	}
	
	/**
	 * GET hora fin
	 * @return (String[]) hora del fin del viaje
	 */
	public String[] getHora_fin() {
		return hora_fin;
	}
	/**
	 * SET hora fin
	 * @param hora_fin (String[]) hora en la que finaliza el viaje
	 */
	public void setHora_fin(String[] hora_fin) {
		this.hora_fin = hora_fin;
	}
	
	/**
	 * GET calificacion
	 * @return (String[]) calificacion del viaje
	 */
	public String[] getCalificacion() {
		return calificacion;
	}
	
	/**
	 * SET calificacion
	 * @param calificacion (String[]) calificacion del viaje
 	 */
	public void setCalificacion(String[] calificacion) {
		this.calificacion = calificacion;
	}
	
	/**
	 * GET comentario
	 * @return (String[]) comentario del viaje
	 */
	public String[] getComentario() {
		return comentario;
	}
	
	/**
	 * SET comentario
	 * @param comentario (String[]) comentario del viaje
	 */
	public void setComentario(String[] comentario) {
		this.comentario = comentario;
	}
	
	/**
	 * GET inicio del viaje
	 * @return (String[]) coordenadas donde inicio el viaje
	 */
	public String[] getInicio_viaje() {
		return inicio_viaje;
	}
	
	/**
	 * SET inicio del viaje
	 * @param inicio_viaje (String[]) coordenadas en donde inicio el viaje
	 */
	public void setInicio_viaje(String[] inicio_viaje) {
		this.inicio_viaje = inicio_viaje;
	}
	
	/**
	 * GET fin del viaje
	 * @return (String[]) coordenadas donde finalizo el viaje
	 */
	public String[] getFin_viaje() {
		return fin_viaje;
	}
	
	/**
	 * SET fin del viaje
	 * @param fin_viaje (String[]) coordenadas en donde finalizo el viaje
	 */
	public void setFin_viaje(String[] fin_viaje) {
		this.fin_viaje = fin_viaje;
	}
	
	
}
