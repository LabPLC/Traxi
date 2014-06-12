package codigo.labplc.mx.traxi.dialogos;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.fonts.fonts;

/**
 * actividad que muestra que significa la calificacion del escudo
 * 
 * @author mikesaurio
 *
 */
public class Descripcion_Escudo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_descripcion__escudo);

		Bundle bundle = getIntent().getExtras();
		String texto = bundle.getString("descripcion_escudo");	

	  /*  ((TextView) findViewById(R.id.descripcion_escudo_tv_titulo)).setTypeface(new fonts(Descripcion_Escudo.this).getTypeFace(fonts.FLAG_MAMEY));	
		((TextView) findViewById(R.id.descripcion_escudo_tv_titulo)).setTextColor(getResources().getColor(R.color.color_vivos));
		*/
		
		((TextView) findViewById(R.id.descripcion_escudo_tv_contenido)).setTypeface(new fonts(Descripcion_Escudo.this).getTypeFace(fonts.FLAG_ROJO));	
		((TextView) findViewById(R.id.descripcion_escudo_tv_contenido)).setTextColor(getResources().getColor(R.color.color_vivos));
		((TextView) findViewById(R.id.descripcion_escudo_tv_contenido)).setText(texto);
		
		RelativeLayout	relative_full_descripcion_escudo	=(RelativeLayout)findViewById(R.id.relative_full_descripcion_escudo);
		relative_full_descripcion_escudo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		
	
	}

	

}
