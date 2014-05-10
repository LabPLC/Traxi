package codigo.labplc.mx.trackxi.buscarplaca.paginador.paginas;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.buscarplaca.bean.AutoBean;
import codigo.labplc.mx.trackxi.fonts.fonts;
import codigo.labplc.mx.trackxi.log.BeanDatosLog;

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
	
	public void init() {


		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.activity_adeudos, null);
		
		TextView adeudos_titulo_main = (TextView)view.findViewById(R.id.adeudos_titulo_main);
		adeudos_titulo_main.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_MAMEY));
		adeudos_titulo_main.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		
		
		container=(LinearLayout)view.findViewById(R.id.adeudos_ll_contenedor);
		
		HorizontalScrollView adeudos_svh = (HorizontalScrollView)view.findViewById(R.id.adeudos_svh);
		adeudos_svh.setVisibility(LinearLayout.GONE);
		
		TextView adeudos_titulo_tv_amigos=(TextView)view.findViewById(R.id.adeudos_titulo_tv_amigos);
		adeudos_titulo_tv_amigos.setVisibility(TextView.GONE);
		
		

		
		llenarAdeudo("Revista vehicular",autoBean.getDescripcion_revista(),autoBean.getImagen_revista());
		llenarAdeudo("Infracciones",autoBean.getDescripcion_infracciones(),autoBean.getImagen_infraccones());
		llenarAdeudo("Vehiculo",autoBean.getDescripcion_vehiculo(),autoBean.getImagen_vehiculo());
		llenarAdeudo("Verificaciones",autoBean.getDescripcion_verificacion(),autoBean.getImagen_verificacion());
		llenarAdeudo("Tenencia",autoBean.getDescripcion_tenencia(),autoBean.getImagen_teencia());

	}

	
	public void llenarAdeudo(String titulo, String concepto, int imagen) {
		if(concepto.equals("")){
			concepto= getResources().getString(R.string.adeudos_row_no_hay_datos);
		}
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view_row = inflater.inflate(R.layout.adeudos_row, null);

		
		
		TextView adeudos_row_titulo = (TextView)view_row.findViewById(R.id.adeudos_row_titulo);
		adeudos_row_titulo.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_GRIS_CLARO));
		adeudos_row_titulo.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		
		TextView adeudos_row_descripcion = (TextView)view_row.findViewById(R.id.adeudos_row_descripcion);
		adeudos_row_descripcion.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_MAMEY));
		adeudos_row_descripcion.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		
		ImageView adeudos_row_iv = (ImageView)view_row.findViewById(R.id.adeudos_row_iv);
		
		adeudos_row_titulo.setText(titulo);
		adeudos_row_descripcion.setText(concepto);
		if(imagen==imagen_verde){
			adeudos_row_iv.setImageResource(R.drawable.ic_launcher_paloma);
		}else if(imagen==imagen_rojo){
			adeudos_row_iv.setImageResource(R.drawable.ic_launcher_tache);
		}
		container.addView(view_row);
		
	}

	public View getView() {
		return view;
	}

}
