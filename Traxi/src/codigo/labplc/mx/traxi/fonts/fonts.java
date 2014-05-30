package codigo.labplc.mx.traxi.fonts;

import android.content.Context;
import android.graphics.Typeface;
import codigo.labplc.mx.traxi.R;

/**
 * clase que controla las fuentes y color de textos
 * 
 * @author mikesaurio
 *
 */
public class fonts {

	public static final int FLAG_ROJO = 1;
	public static final int FLAG_GRIS_CLARO = 2;
	public static final int FLAG_GRIS_OBSCURO = 3;
	public static final int FLAG_COLOR_BASE = 4;
	public static final int FLAG_MAMEY = 5;


	private static Context activity;

	public fonts(Context context) {
		fonts.activity = context;
	}

	/**
	 * tipo de fuente
	 * @param tipo (int) tipo de la fuente
	 * @return (Typeface) fuente
	 */
	public Typeface getTypeFace(int tipo) {
		Typeface tf = null;
		if (tipo == FLAG_GRIS_CLARO) {
			tf = Typeface.createFromAsset(activity.getAssets(),
					"fonts/HelveticaNeueLTStd-UltLt.otf");
		} else if (tipo == FLAG_GRIS_OBSCURO) {
			tf = Typeface.createFromAsset(activity.getAssets(),
					"fonts/HelveticaNeueLTStd-UltLtIt.otf");
		} else if (tipo == FLAG_ROJO) {
			tf = Typeface.createFromAsset(activity.getAssets(),
					"fonts/HelveticaNeueLTStd-Roman.otf");
		} else if (tipo == FLAG_MAMEY) {
			tf = Typeface.createFromAsset(activity.getAssets(),
					"fonts/HelveticaNeueLTStd-Bd.otf");
		} else if (tipo == FLAG_COLOR_BASE) {
			tf = Typeface.createFromAsset(activity.getAssets(),
					"fonts/HelveticaNeueLTStd-BdIt.otf");
		}
		return tf;
	}

	/**
	 * color de la fuente
	 * @param tipo (int) tipo de la fuente
	 * @return (int) color
	 */
	public int getColorTypeFace(int tipo) {

		if (tipo == FLAG_GRIS_CLARO) {
			return activity.getResources().getColor(R.color.gris_claro);
		} else if (tipo == FLAG_GRIS_OBSCURO) {
			return activity.getResources().getColor(R.color.gris_obscuro);
		} else if (tipo == FLAG_ROJO) {
			return activity.getResources().getColor(R.color.rojo_logo);
		} else if (tipo == FLAG_MAMEY) {
			return activity.getResources().getColor(R.color.mamey);
		} else if (tipo == FLAG_COLOR_BASE) {
			return activity.getResources().getColor(R.color.color_base);
		}else{
			return R.color.rojo_logo;
		}

	}

}
