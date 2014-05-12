package codigo.labplc.mx.traxi.buscarplaca;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.buscarplaca.emergencia.Emergencia_activity;
import codigo.labplc.mx.traxi.buscarplaca.paginador.DatosAuto;
import codigo.labplc.mx.traxi.buscarplaca.tips.Tips_activity;
import codigo.labplc.mx.traxi.configuracion.UserSettingActivity;
import codigo.labplc.mx.traxi.dialogos.Dialogos;
import codigo.labplc.mx.traxi.fonts.fonts;
import codigo.labplc.mx.traxi.log.BeanDatosLog;
import codigo.labplc.mx.traxi.utils.Utils;

public class BuscaPlacaTexto extends Activity implements OnTouchListener,OnClickListener {

	private static final int RESULT_SETTINGS = 1; //resultado del menu
	public final String TAG = this.getClass().getSimpleName();
	private Activity context;
	private EditText placa;
	private String Splaca;

	
	// teclado
	private Button mBack;
	ImageView abs_layout_iv_menu;
	private static RelativeLayout mLayout;
	private static RelativeLayout mKLayout;
	private boolean isEdit = true;
	private Button mB[] = new Button[13];
	public static boolean tecladoIs = false;

	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_busca_placa);
		context = this;
		init(this);

	}

	/*
	 * iniciamos la vista
	 */
	public void init(Activity con) {
		BeanDatosLog.setTagLog(TAG);

		getWindow().setFormat(PixelFormat.UNKNOWN);

		final ActionBar ab = getActionBar();
		ab.setDisplayShowHomeEnabled(false);
		ab.setDisplayShowTitleEnabled(false);
		final LayoutInflater inflater = (LayoutInflater) getSystemService("layout_inflater");
		View view = inflater.inflate(R.layout.abs_layout, null);
		((TextView) view.findViewById(R.id.abs_layout_tv_titulo)).setTypeface(new fonts(BuscaPlacaTexto.this).getTypeFace(fonts.FLAG_MAMEY));
		ab.setDisplayShowCustomEnabled(true);
		ab.setCustomView(view, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
		ab.setCustomView(view);

		((TextView) findViewById(R.id.inicio_de_trabajo_tv_nombre)).setTypeface(new fonts(context).getTypeFace(fonts.FLAG_ROJO));
		((TextView) findViewById(R.id.inicio_de_trabajo_tv_nombre)).setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));

		mLayout = (RelativeLayout) findViewById(R.id.xK1);
		mKLayout = (RelativeLayout) findViewById(R.id.xKeyBoard);

		mBack = (Button) findViewById(R.id.back);
		mBack.setOnClickListener(this);
		
		abs_layout_iv_menu =(ImageView) findViewById(R.id.abs_layout_iv_menu);
		abs_layout_iv_menu.setOnClickListener(this);

		setKeys();
		
		try {
			LettersTrue();
		} catch (XmlPullParserException e) {
			BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
		} catch (IOException e) {
			BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
		}

		placa = (EditText) findViewById(R.id.inicio_de_trabajo_et_placa);
		placa.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME| InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
		placa.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_ROJO));
		placa.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		placa.setOnTouchListener(this);
		placa.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				Splaca = placa.getText().toString();

				if (Splaca.length() == 6) {
					// buscando datos del carro en el servidor
					try {
						// cerramos el teclado
						if (Utils.hasInternet(context)) {
							InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(placa.getWindowToken(),	0);
							Intent intent = new Intent().setClass(context,DatosAuto.class);
							intent.putExtra("placa", Splaca);
							context.startActivityForResult(intent, 0);
							context.finish();
							placa.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
						} else {
							Dialogos.Toast(context, "No tienes Internet",Toast.LENGTH_LONG);
						}
						placa.setText("");

					} catch (Exception e) {
						BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
						placa.setText("");
					}
				} else {
					if (Splaca.length() >= 1) {
						try {
							NumbersTrue();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
						}
					} else if (placa.length() < 1) {
						try {
							LettersTrue();
						} catch (XmlPullParserException e) {
							// TODO Auto-generated catch block
							BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
						}
					} 
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		
		Button activity_buscar_placa_btn_emergencia = (Button) findViewById(R.id.activity_buscar_placa_btn_emergencia);
		activity_buscar_placa_btn_emergencia.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentManually = new Intent(BuscaPlacaTexto.this,Emergencia_activity.class);
				startActivity(intentManually);
				/*Intent intentManually = new Intent(BuscaPlacaTexto.this,MitaxiRegisterManuallyActivity.class);
				intentManually.putExtra("origen", "menu");
				startActivity(intentManually);
				overridePendingTransition(0, 0);*/
			}
		});
		
		Button activity_buscar_placa_btn_foto = (Button) findViewById(R.id.activity_buscar_placa_btn_foto);
		activity_buscar_placa_btn_foto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentManually = new Intent(BuscaPlacaTexto.this,BuscaPlacaFoto.class);
				startActivity(intentManually);
			}
		});
		
		Button activity_buscar_placa_btn_tips = (Button) findViewById(R.id.activity_buscar_placa_btn_tips);
		activity_buscar_placa_btn_tips.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentManually = new Intent(BuscaPlacaTexto.this,Tips_activity.class);
				startActivity(intentManually);
			}
		});
		
		
		
		//
		activarTeclado();
	}

	// //para e teclado
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v == placa) {
			activarTeclado();

		}

		return true;
	}
/*
 * muestra el teclado
 */
	public void activarTeclado() {
		tecladoIs = true;
		hideDefaultKeyboard();
		enableKeyboard();
		
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.abs_layout_iv_menu) {
			showPopup(v);
		}else if (v != mBack) {
			addText(v);
		} else if (v == mBack) {
			isBack(v);
		}
		
	}

	private void isBack(View v) {
			CharSequence cc = placa.getText();
			if (cc != null && cc.length() > 0) {
				{
					placa.setText("");
					placa.append(cc.subSequence(0, cc.length() - 1));
				}

			}
		
	}


	private void addText(View v) {
			String b = "";
			b = (String) v.getTag();
			if (b != null) {
				placa.append(b);
			}
		

	}

	// activamos el teclado personalizado
	private void enableKeyboard() {

		mLayout.setVisibility(RelativeLayout.VISIBLE);
		mKLayout.setVisibility(RelativeLayout.VISIBLE);

	}

	// Disable customized keyboard
	public static void disableKeyboard() {
		mLayout.setVisibility(RelativeLayout.INVISIBLE);
		mKLayout.setVisibility(RelativeLayout.INVISIBLE);

	}

	private void hideDefaultKeyboard() {
		context.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

	}

	private void setKeys() {

		mB[0] = (Button) findViewById(R.id.xA);
		mB[1] = (Button) findViewById(R.id.xB);
		mB[2] = (Button) findViewById(R.id.xM);
		mB[3] = (Button) findViewById(R.id.x1);
		mB[4] = (Button) findViewById(R.id.x2);
		mB[5] = (Button) findViewById(R.id.x3);
		mB[6] = (Button) findViewById(R.id.x4);
		mB[7] = (Button) findViewById(R.id.x5);
		mB[8] = (Button) findViewById(R.id.x6);
		mB[9] = (Button) findViewById(R.id.x7);
		mB[10] = (Button) findViewById(R.id.x8);
		mB[11] = (Button) findViewById(R.id.x9);
		mB[12] = (Button) findViewById(R.id.x0);

		for (int i = 0; i < mB.length; i++) {
			mB[i].setOnClickListener(this);
		}
	}

	private void LettersTrue() throws XmlPullParserException, IOException {
		XmlResourceParser parser = getResources().getXml(R.drawable.selector_txt_boton_redondo);
	    ColorStateList colors = ColorStateList.createFromXml(getResources(), parser);
		
	    mBack.setEnabled(false);
		mBack.setBackground(null);
		mBack.setTextColor(getResources().getColor(R.color.gris_obscuro));
		mB[0].setEnabled(true);
		mB[0].setBackground(getResources().getDrawable(R.drawable.selector_btn_generic));
		mB[0].setTextColor(colors);
		mB[1].setEnabled(true);
		mB[1].setBackground(getResources().getDrawable(R.drawable.selector_btn_generic));
		mB[1].setTextColor(colors);
		mB[2].setEnabled(true);
		mB[2].setBackground(getResources().getDrawable(	R.drawable.selector_btn_generic));
		mB[2].setTextColor(colors);
		mB[3].setEnabled(false);
		mB[3].setBackground(null);
		mB[3].setTextColor(getResources().getColor(R.color.gris_obscuro));
		mB[4].setEnabled(false);
		mB[4].setBackground(null);
		mB[4].setTextColor(getResources().getColor(R.color.gris_obscuro));
		mB[5].setEnabled(false);
		mB[5].setBackground(null);
		mB[5].setTextColor(getResources().getColor(R.color.gris_obscuro));
		mB[6].setEnabled(false);
		mB[6].setBackground(null);
		mB[6].setTextColor(getResources().getColor(R.color.gris_obscuro));
		mB[7].setEnabled(false);
		mB[7].setBackground(null);
		mB[7].setTextColor(getResources().getColor(R.color.gris_obscuro));
		mB[8].setEnabled(false);
		mB[8].setBackground(null);
		mB[8].setTextColor(getResources().getColor(R.color.gris_obscuro));
		mB[9].setEnabled(false);
		mB[9].setBackground(null);
		mB[9].setTextColor(getResources().getColor(R.color.gris_obscuro));
		mB[10].setEnabled(false);
		mB[10].setBackground(null);
		mB[10].setTextColor(getResources().getColor(R.color.gris_obscuro));
		mB[11].setEnabled(false);
		mB[11].setBackground(null);
		mB[11].setTextColor(getResources().getColor(R.color.gris_obscuro));
		mB[12].setEnabled(false);
		mB[12].setBackground(null);
		mB[12].setTextColor(getResources().getColor(R.color.gris_obscuro));
	}

	private void NumbersTrue() throws XmlPullParserException, IOException {
		XmlResourceParser parser = getResources().getXml(R.drawable.selector_txt_boton_redondo);
	    ColorStateList colors = ColorStateList.createFromXml(getResources(), parser);
	    
		mBack.setEnabled(true);
		mBack.setBackground(getResources().getDrawable(R.drawable.selector_btn_generic));
		mBack.setTextColor(colors);

		mB[0].setEnabled(false);
		mB[0].setBackground(null);
		mB[0].setTextColor(getResources().getColor(R.color.gris_obscuro));
		mB[1].setEnabled(false);
		mB[1].setBackground(null);
		mB[1].setTextColor(getResources().getColor(R.color.gris_obscuro));
		mB[2].setEnabled(false);
		mB[2].setBackground(null);
		mB[2].setTextColor(getResources().getColor(R.color.gris_obscuro));
		mB[3].setEnabled(true);
		mB[3].setBackground(getResources().getDrawable(R.drawable.selector_btn_generic));
		mB[3].setTextColor(colors);
		mB[4].setEnabled(true);
		mB[4].setBackground(getResources().getDrawable(	R.drawable.selector_btn_generic));
		mB[4].setTextColor(colors);
		mB[5].setEnabled(true);
		mB[5].setBackground(getResources().getDrawable(	R.drawable.selector_btn_generic));
		mB[5].setTextColor(colors);
		mB[6].setEnabled(true);
		mB[6].setBackground(getResources().getDrawable(R.drawable.selector_btn_generic));
		mB[6].setTextColor(colors);
		mB[7].setEnabled(true);
		mB[7].setBackground(getResources().getDrawable(R.drawable.selector_btn_generic));
		mB[7].setTextColor(colors);
		mB[8].setEnabled(true);
		mB[8].setBackground(getResources().getDrawable(R.drawable.selector_btn_generic));
		mB[8].setTextColor(colors);
		mB[9].setEnabled(true);
		mB[9].setBackground(getResources().getDrawable(R.drawable.selector_btn_generic));
		mB[9].setTextColor(colors);
		mB[10].setEnabled(true);
		mB[10].setBackground(getResources().getDrawable(R.drawable.selector_btn_generic));
		mB[10].setTextColor(colors);
		mB[11].setEnabled(true);
		mB[11].setBackground(getResources().getDrawable(R.drawable.selector_btn_generic));
		mB[11].setTextColor(colors);
		mB[12].setEnabled(true);
		mB[12].setBackground(getResources().getDrawable(R.drawable.selector_btn_generic));
		mB[12].setTextColor(colors);
	}

	
	
	@Override
	public void onBackPressed() {
	/*	if (BuscaPlacaTexto.tecladoIs) {
			BuscaPlacaTexto.disableKeyboard();
			BuscaPlacaTexto.tecladoIs = false;
		} else {*/
			super.onBackPressed();
		//}
	}

	
	public void showPopup(View v) {
		PopupMenu popup = new PopupMenu(BuscaPlacaTexto.this, v);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.popup, popup.getMenu());
		int positionOfMenuItem = 0; 
		MenuItem item = popup.getMenu().getItem(positionOfMenuItem);
		SpannableString s = new SpannableString(getResources().getString(R.string.action_settings));
		s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.rojo_logo)), 0, s.length(), 0);
		item.setTitle(s);
		positionOfMenuItem = 1; // or whatever...
		item = popup.getMenu().getItem(positionOfMenuItem);
		SpannableString s3 = new SpannableString(getResources().getString(R.string.action_help));
		s3.setSpan(	new ForegroundColorSpan(getResources().getColor(R.color.rojo_logo)), 0, s3.length(), 0);
		item.setTitle(s3);

		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {

				case R.id.configuracion:
					Intent i = new Intent(BuscaPlacaTexto.this,UserSettingActivity.class);
					startActivityForResult(i, RESULT_SETTINGS);
					return true;

			//	case R.id.cuenta:
				
				//	return true;

				}
				return false;
			}
		});

		popup.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RESULT_SETTINGS:
			showUserSettings();
			break;
		}
	}

	/**
	 * mustra las preferencias guardadas
	 */
	private void showUserSettings() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		StringBuilder builder = new StringBuilder();
		builder.append("\n Send report:"+ sharedPrefs.getBoolean("prefSendReport", true));
		builder.append("\n Sync Frequency: "+ sharedPrefs.getString("prefSyncFrequency", "NULL"));
		builder.append("\n Sync FrequencyMensajes: "+ sharedPrefs.getString("prefSyncFrequencyParanoia", "NULL"));
	}


}
