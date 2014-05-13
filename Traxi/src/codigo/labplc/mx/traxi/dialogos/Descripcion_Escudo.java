package codigo.labplc.mx.traxi.dialogos;

import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.R.id;
import codigo.labplc.mx.traxi.R.layout;
import codigo.labplc.mx.traxi.R.menu;
import codigo.labplc.mx.traxi.buscarplaca.tips.Tips_activity;
import codigo.labplc.mx.traxi.fonts.fonts;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Build;

public class Descripcion_Escudo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_descripcion__escudo);

		Bundle bundle = getIntent().getExtras();
		String texto = bundle.getString("descripcion_escudo");	

	    ((TextView) findViewById(R.id.descripcion_escudo_tv_titulo)).setTypeface(new fonts(Descripcion_Escudo.this).getTypeFace(fonts.FLAG_MAMEY));	
		((TextView) findViewById(R.id.descripcion_escudo_tv_titulo)).setTextColor(new fonts(Descripcion_Escudo.this).getColorTypeFace(fonts.FLAG_ROJO));
		

		
		TextView tv_contenido = (TextView) findViewById(R.id.descripcion_escudo_tv_contenido);
		tv_contenido.setTypeface(new fonts(Descripcion_Escudo.this).getTypeFace(fonts.FLAG_ROJO));	
		tv_contenido.setTextColor(new fonts(Descripcion_Escudo.this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		tv_contenido.setText(texto);
		
		RelativeLayout	relative_full_descripcion_escudo	=(RelativeLayout)findViewById(R.id.relative_full_descripcion_escudo);
		relative_full_descripcion_escudo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
		
	
	}

	

}
