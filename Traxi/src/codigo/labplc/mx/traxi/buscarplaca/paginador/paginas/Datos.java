package codigo.labplc.mx.traxi.buscarplaca.paginador.paginas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.buscarplaca.bean.AutoBean;
import codigo.labplc.mx.traxi.buscarplaca.paginador.paginas.termometro.ShieldView;
import codigo.labplc.mx.traxi.dialogos.Descripcion_Escudo;
import codigo.labplc.mx.traxi.fonts.fonts;
/**
 * pagina Que muestra la evaluacion general a un taxi
 * @author mikesaurio
 *
 */
@SuppressLint("ViewConstructor")
public class Datos extends View {

	private TextView marca;
	private LinearLayout container;
	private View view;
	private Activity context;
	private AutoBean autoBean;
	
	
	public Datos(Activity context) {
		super(context);
		this.context=context;
	}
	public Datos(Activity context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
	}

	public Datos(Activity context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context=context;
	}


	
	
	public void init(AutoBean autoB){
		this.autoBean=autoB;

		LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		view = inflater.inflate(R.layout.activity_datos, null);
		
		((TextView) view.findViewById(R.id.datos_tv_niveles_confianza)).setTypeface(new fonts(context).getTypeFace(fonts.FLAG_MAMEY));	
		((TextView) view.findViewById(R.id.datos_tv_niveles_confianza)).setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		
		TextView datos_tv_titulo = (TextView)view.findViewById(R.id.datos_tv_titulo);
		datos_tv_titulo.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_GRIS_CLARO));
		datos_tv_titulo.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		
		TextView datos_tv_placa  = (TextView)view.findViewById(R.id.datos_tv_placa);
		datos_tv_placa.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_MAMEY));
		datos_tv_placa.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		datos_tv_placa.setText(autoBean.getPlaca());
		
		ImageView datos_iv_info =(ImageView)view.findViewById(R.id.datos_iv_info);
		datos_iv_info.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,Descripcion_Escudo.class);
				intent.putExtra("descripcion_escudo", autoBean.getDescripcion_calificacion_app());
				context.startActivity(intent);
				
			}
		});
		
		
		marca = (TextView)view.findViewById(R.id.datos_tv_marca);
		marca.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_MAMEY));
		marca.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));

		
		container = (LinearLayout)view.findViewById(R.id.Thermometer_Container);
	
		marca.setText(autoBean.getMarca()+", ");
		marca.append(autoBean.getSubmarca()+", ");
		marca.append(autoBean.getAnio());
		
		if(marca.getText().toString().equals(", , ")){
			marca.setText(getResources().getString(R.string.adeudos_row_no_hay_datos));
			autoBean.setCalificacion_final(0);
		}
		
		crearTermometro();

		
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("ResourceAsColor")
	public void crearTermometro(){
		
		LinearLayout verticalLayout = new LinearLayout(context);
		verticalLayout.setOrientation(LinearLayout.VERTICAL);
		verticalLayout.setGravity(Gravity.CENTER);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		verticalLayout.setLayoutParams(params);
		 
		final ShieldView shield = new ShieldView(context);
		Display display = context.getWindowManager().getDefaultDisplay(); 
		int actionBarHeight = context.getActionBar().getHeight();
		int newProgress = shield.getProgressWithJump(autoBean.getCalificacion_final(), ShieldView.JUMP_PROGRESS_ANIMATION); // Progress with jump

		int size= display.getHeight()-actionBarHeight-actionBarHeight;
		if(newProgress<=30){
			shield.initUI(size/2,size/2,R.color.rojo_logo);
		}else if(newProgress <=70 && newProgress>30){
				shield.initUI(size/2,size/2,R.color.generic_naranja);
		}else if(newProgress>70){
				shield.initUI(size/2,size/2,R.color.android_green);
			}
		
		shield.setProgress(autoBean.getCalificacion_final());
		verticalLayout.addView(shield);
		container.addView(verticalLayout);
	}
	
	/**
	 * GET view
	 * @return (View) vista inflada 
	 */
	public View getView(){
		return view;
	}
	
	
	

}
