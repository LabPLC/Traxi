package codigo.labplc.mx.trackxi.registro.validador;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.expresionesregulares.RegularExpressions;

public class EditTextValidator {
	/**
	 * envia un mensaje de error cuando se ingresa mal un tipo de dato
	 * 
	 * @param errorMessage
	 *            (String) mensaje de error
	 * @param editText
	 *            (EditText) la vista de tipo {@link android.widget.EditText}
	 */
	public void setErrorMessage(Context context, String errorMessage, EditText editText) {
		int errorColor = Color.WHITE;
		ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
		
		SpannableStringBuilder ssbuilder = new SpannableStringBuilder(
				errorMessage);
		ssbuilder.setSpan(fgcspan, 0, errorMessage.length(), 0);
		editText.setError(ssbuilder);
	}
	
	public static boolean isEditTextEmpty(EditText editText) {
		return editText.getText().toString().trim().length() == 0;
	}
	
	public class CurrencyTextWatcher implements TextWatcher {
		private Context context;
		private boolean mEditing;
		private EditText et_aux;
		private boolean[] listHasErrorEditText;
		private int index;

		public CurrencyTextWatcher(Context context, EditText et_aux, boolean[] listHasErrorEditText, int index) {
			this.context = context;
			mEditing = false;
			this.et_aux = et_aux;
			this.listHasErrorEditText = listHasErrorEditText;
			this.index = index;
		}

		public synchronized void afterTextChanged(Editable s) {
			
			if (!mEditing) {
				mEditing = true;

				String expression = s.toString();

			if (!expression.equals("")) {
					int etType = Integer.parseInt(et_aux.getTag().toString());

					if (etType == RegularExpressions.KEY_IS_STRING) {
						if (!RegularExpressions.isString(expression)) {
							listHasErrorEditText[index] = true;
							
							setErrorMessage(
									context,
									context.getString(R.string.edittext_error_string),
									et_aux);
						} else {
							listHasErrorEditText[index] = false;
							
							et_aux.setError(null);
						}
					} else if (etType == RegularExpressions.KEY_IS_EMAIL) {
						if (!RegularExpressions.isEmail(expression)) {
							listHasErrorEditText[index] = true;
							
							setErrorMessage(
									context,
									context.getString(R.string.edittext_error_email),
									et_aux);
						} else {
							listHasErrorEditText[index] = false;
							
							
							et_aux.setError(null);
						}
					} else if (etType == RegularExpressions.KEY_IS_NUMBER) {
						if (!RegularExpressions.isNumber(expression)) {
							listHasErrorEditText[index] = true;
							
							setErrorMessage(
									context,
									context.getString(R.string.edittext_error_number),
									et_aux);
						} else {
							listHasErrorEditText[index] = false;
							
							et_aux.setError(null);
						}
					}else if (etType == RegularExpressions.KEY_IS_NICKNAME) {
						if (!RegularExpressions.isNickName(expression)) {
							listHasErrorEditText[index] = true;
							setErrorMessage(
									context,
									context.getString(R.string.edittext_error_nickname),
									et_aux);
						} else {
							listHasErrorEditText[index] = false;
							
							et_aux.setError(null);
						}
					}
				}

				mEditing = false;
			}
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {}
	}
}
