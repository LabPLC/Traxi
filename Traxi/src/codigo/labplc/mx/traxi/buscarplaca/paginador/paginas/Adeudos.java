package codigo.labplc.mx.traxi.buscarplaca.paginador.paginas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.buscarplaca.bean.AutoBean;
import codigo.labplc.mx.traxi.fonts.fonts;

/**
 * pagina que muestra en una lista los adeudos de un carro con las secretarias
 * 
 * @author mikesaurio
 *
 */
@SuppressLint("ViewConstructor")
public class Adeudos extends View {

	
	private View view;
	private View view_row;
	private Activity context;
	private LinearLayout container;
	private AutoBean autoBean;
	private int imagen_verde = 1;
	private int imagen_rojo = 2;
	
	
	public Adeudos(Activity context) {
		super(context);
		this.context = context;
	}

	public Adeudos(Activity context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public Adeudos(Activity context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}

	public void init(AutoBean autoBean){
		this.autoBean=autoBean;
		init();
	}
	
	/**
	 * init de la pagina
	 */
	public void init() {


		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.activity_adeudos, null);
		
		container=(LinearLayout)view.findViewById(R.id.adeudos_ll_contenedor);

		
		autoBean.setHasrevista_(llenarAdeudo(getResources().getString(R.string.adeudo_revista),autoBean.getDescripcion_revista(),autoBean.getImagen_revista()));
		autoBean.setHasinfracciones_(llenarAdeudo(getResources().getString(R.string.adeudo_infracciones),autoBean.getDescripcion_infracciones(),autoBean.getImagen_infraccones()));
		autoBean.setHasanio_(llenarAdeudo(getResources().getString(R.string.adeudo_anio),autoBean.getDescripcion_vehiculo(),autoBean.getImagen_vehiculo()));
		autoBean.setHasverificacion_(llenarAdeudo(getResources().getString(R.string.adeudo_verificaciones),autoBean.getDescripcion_verificacion(),autoBean.getImagen_verificacion()));
		autoBean.setHastenencia_(llenarAdeudo(getResources().getString(R.string.adeudo_tenencia),autoBean.getDescripcion_tenencia(),autoBean.getImagen_teencia()));

	}

	/**
	 * llena row de adeudo
	 * @param titulo (String) titulo del row
	 * @param concepto (String) concepto del row
	 * @param imagen (int) recurso de la imagen
	 */
	public boolean llenarAdeudo(String titulo, String concepto, int imagen) {
		boolean resp=true;
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view_row = inflater.inflate(R.layout.adeudos_row, null);
		
		TextView adeudos_row_titulo = (TextView)view_row.findViewById(R.id.adeudos_row_titulo);
		adeudos_row_titulo.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_MAMEY));
		adeudos_row_titulo.setTextColor(getResources().getColor(R.color.color_vivos));
		
		TextView adeudos_row_descripcion = (TextView)view_row.findViewById(R.id.adeudos_row_descripcion);
		adeudos_row_descripcion.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_ROJO));
		adeudos_row_descripcion.setTextColor(getResources().getColor(R.color.color_vivos));
		
		ImageView adeudos_row_iv = (ImageView)view_row.findViewById(R.id.adeudos_row_iv);
		
		adeudos_row_titulo.setText(titulo);
		adeudos_row_descripcion.setText(concepto);
		
		if(imagen==imagen_verde){
			adeudos_row_iv.setImageResource(R.drawable.ic_launcher_paloma);
			resp=true;
		}else if(imagen==imagen_rojo){
			adeudos_row_iv.setImageResource(R.drawable.ic_launcher_tache_rojo);
			resp=false;
		}
		
		if(concepto.equals("")||autoBean.getCalificacion_final()==0){
			adeudos_row_descripcion.setText(getResources().getString(R.string.adeudos_row_no_hay_datos));
			adeudos_row_iv.setImageResource(R.drawable.ic_launcher_tache_rojo);
			resp=false;
		}
		
		
		container.addView(view_row);
		return resp;
		
	}

	/**
	 * GET view
	 * @return (view) vista inflada
	 */
	public View getView() {
		return view;
	}

}
