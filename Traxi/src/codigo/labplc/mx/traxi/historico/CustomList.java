package codigo.labplc.mx.traxi.historico;

import java.util.regex.Pattern;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.fonts.fonts;

/**
 * clase que personaliza la lista de eventos
 * Created by mikesaurio on 05/05/14.
 */
@SuppressWarnings("unused")
public class CustomList extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] placa;
	private final String[] hora_inicio;
    private final String[] hora_fin;
    private final String[] calificacion;
    private final String[] comentario;
    private final String[] inicio_viaje;
    private final String[] fin_viaje;


    /**
     * cosntructor
     * @param context (Activity)
     * @param placa
     * @param hora_inicio
     * @param hora_fin
     * @param calificacion
     * @param comentario
     * @param inicio_viaje
     * @param fin_viaje
     */
    public CustomList(Activity context, String[] placa,String[] hora_inicio, String[] hora_fin, String[] calificacion,String[] comentario, String[] inicio_viaje, String[] fin_viaje) {
    	  super(context, R.layout.row_list_historial, placa);
          this.context = context;
          this.placa=placa;
          this.hora_inicio=hora_inicio;
          this.hora_fin=hora_fin;
          this.calificacion=calificacion;
          this.comentario=comentario;
          this.inicio_viaje=inicio_viaje;
          this.fin_viaje=fin_viaje;
	}


	@Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.row_list_historial, null, true);
        TextView txtPlaca = (TextView) rowView.findViewById(R.id.row_historial_tv_placa);
        txtPlaca.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_ROJO));
        txtPlaca.setText(placa[position]);
        
        TextView txtcalificacion = (TextView) rowView.findViewById(R.id.row_historial_tv_fecha);
        txtcalificacion.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_ROJO));
        txtcalificacion.setText(hora_fin[position].toString().replaceAll(Pattern.quote("+"), " "));
        
        TextView txtComentario = (TextView) rowView.findViewById(R.id.row_historial_tv_comentario);
        txtComentario.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_ROJO));
        txtComentario.setText(calificacion[position]+": "+comentario[position]);
       
        
        return rowView;
    }
    
   
	
   
}