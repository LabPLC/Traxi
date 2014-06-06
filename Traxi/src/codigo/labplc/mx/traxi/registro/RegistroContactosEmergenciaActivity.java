

package codigo.labplc.mx.traxi.registro;


import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.configuracion.UserSettingActivity;
import codigo.labplc.mx.traxi.dialogos.Dialogos;
import codigo.labplc.mx.traxi.fonts.fonts;
import codigo.labplc.mx.traxi.log.DatosLogBean;
import codigo.labplc.mx.traxi.registro.validador.EditTextValidator;
import codigo.labplc.mx.traxi.utils.Utils;

/**
 * clase para que el usuario ingrese sus ontactos de emergencia 
 * @author mikesaurio
 *
 */
public class RegistroContactosEmergenciaActivity extends Activity implements OnClickListener{
	
	public final String TAG = this.getClass().getSimpleName();
	private LinearLayout mitaxiregistermanually_ll_contactos;
	private TextView mitaxiregistermanually_tv_agregar;
	 ArrayList<View> views = new ArrayList<View>();
	private View mitaxiregistermanually_iv_quitar;
	private boolean[] emergencia_esta_Ocupado= {false,false};//maneja los contactos
	private int RESULT_SETTINGS =10;
	private CheckBox mitaxiregistermanually_cv_paranoico;
	private  ArrayList<String> listaCels;
	 AlertDialog customDialog= null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		    
		setContentView(R.layout.activity_registro_contacto_emergencia);
		
		DatosLogBean.setTagLog(TAG);
	
		
		Utils.crearActionBar(RegistroContactosEmergenciaActivity.this, R.layout.abs_layout_back,getResources().getString(R.string.mitaxiregister_et_emergencias),15.0f);//creamos el ActionBAr
	     
		((ImageView) findViewById(R.id.abs_layout_iv_menu)).setOnClickListener(this);
		((ImageView) findViewById(R.id.abs_layout_iv_logo)).setOnClickListener(this);
	    
		initUI();

	}

	
	@Override
	protected void onPause() {

		super.onPause();
	}


	/**
	 * metodo que inicia la vista
	 */
	public void initUI() {
		
		TextView mitaxiregistermanually_tv_label= (TextView)findViewById(R.id.mitaxiregistermanually_tv_label);
		 mitaxiregistermanually_tv_label.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_MAMEY));
		 mitaxiregistermanually_tv_label.setTextColor(getResources().getColor(R.color.color_vivos));
		 
		 TextView mitaxiregistermanually_tv_paranoico_texto= (TextView)findViewById(R.id.mitaxiregistermanually_tv_paranoico_texto);
		 mitaxiregistermanually_tv_paranoico_texto.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		 mitaxiregistermanually_tv_paranoico_texto.setTextColor(getResources().getColor(R.color.color_vivos));
		 
		 mitaxiregistermanually_ll_contactos =(LinearLayout)findViewById(R.id.mitaxiregistermanually_ll_contactos);
		
		 mitaxiregistermanually_tv_agregar=(TextView) findViewById(R.id.mitaxiregistermanually_tv_agregar);
		 mitaxiregistermanually_tv_agregar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!emergencia_esta_Ocupado[0]||!emergencia_esta_Ocupado[1]){
					addContact(null,null);
				}
			}
		});
		 
	

		 
		 ImageView mitaxiregistermanually_iv_ayuda=(ImageView)findViewById(R.id.mitaxiregistermanually_iv_ayuda);
		 mitaxiregistermanually_iv_ayuda.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			new	Dialogos().mostrarParaQue(RegistroContactosEmergenciaActivity.this).show();
				
			}
		});
		 
		 
		  mitaxiregistermanually_cv_paranoico = (CheckBox) findViewById(R.id.mitaxiregistermanually_cv_paranoico); 
		  SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
          boolean panic = prefs.getBoolean("panico", false);
          mitaxiregistermanually_cv_paranoico.setChecked(panic); 
        	  
          
		 mitaxiregistermanually_cv_paranoico.setOnCheckedChangeListener(new OnCheckedChangeListener() { 

		 @Override 
		 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { 

			 if (buttonView.isChecked()){ 
				 if(validaEditText(R.string.Registro_manual_datos_paranoico_sin_contac)){
					 if((!emergencia_esta_Ocupado[0]&&!emergencia_esta_Ocupado[1])){
						 buttonView.setChecked(false);
						 Dialogos.Toast(RegistroContactosEmergenciaActivity.this,getResources().getString(R.string.Registro_manual_datos_paranoico_sin_contac) , Toast.LENGTH_LONG);
				 	}
				 }else{
					 buttonView.setChecked(false);
				 }
			 }

		 }
		 });
		 
		 Button mitaxiregistermanually_btn_guardar =(Button)findViewById(R.id.mitaxiregistermanually_btn_guardar);
		 mitaxiregistermanually_btn_guardar.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		 mitaxiregistermanually_btn_guardar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				back();
			}
		});
		 
		 
		llenarContactos();
		
	}
	
	
	
	
	
	
	/**
	 * revisa y manda a llenar los contactos segun falte
	 */
	public void llenarContactos() {
		 SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
         String telemer = prefs.getString("telemer", null);
         String correoemer = prefs.getString("correoemer", null);
         String telemer2 = prefs.getString("telemer2", null);
         String correoemer2 = prefs.getString("correoemer2", null);
      
		addContact(telemer,correoemer);

		if(telemer2!=null){
			addContact(telemer2,correoemer2);
		}
	}

	/**
	 * metodo que agrega un contacto de emergencia
	 */
	public void addContact(String tel, String corr){
	
		if(!emergencia_esta_Ocupado[0]){
			addView(0,tel,corr);
		}else if(!emergencia_esta_Ocupado[1]){
			addView(1,tel,corr);
		}
	} 
	


	/**
	 * metodo que elimina un contacto de emergencia
	 * @param id
	 */
	public void removeContact(String tag){
		for(int i=0;i<views.size();i++){
			if(tag.equals(views.get(i).getTag()+"")){
				mitaxiregistermanually_ll_contactos.removeView(views.get(i));
				views.remove(i);
				emergencia_esta_Ocupado[Integer.parseInt(tag)]=false;
				if(emergencia_esta_Ocupado[0]||emergencia_esta_Ocupado[1]){
					mitaxiregistermanually_tv_agregar.setVisibility(TextView.VISIBLE);
				}
				if((!emergencia_esta_Ocupado[0]&&!emergencia_esta_Ocupado[1])){{
					if(mitaxiregistermanually_cv_paranoico.isChecked()){
						mitaxiregistermanually_cv_paranoico.setChecked(false);
						Dialogos.Toast(RegistroContactosEmergenciaActivity.this,getResources().getString(R.string.Registro_manual_datos_paranoico_sin_contac) , Toast.LENGTH_LONG);
					}
				}
			
				}
				if(!validaEditText(R.string.Registro_manual_datos_paranoico_sin_contac)){
					mitaxiregistermanually_cv_paranoico.setChecked(false);
				}
			}
		}
	}
	
	/**
	 * agrega la vista del contacto de emergencua
	 */
	public void addView(final int tag,String tel,String corr){
	//	mitaxiregistermanually_cv_paranoico.setChecked(false);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.row_contact, null);
		view.setTag(tag+"");
		mitaxiregistermanually_iv_quitar=(ImageView)view.findViewById(R.id.mitaxiregistermanually_iv_quitar);
		mitaxiregistermanually_iv_quitar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
						removeContact(tag+"");
				}
			});
		
		final ImageButton mitaxiregistermanually_btn_contactos_2=(ImageButton)view.findViewById(R.id.mitaxiregistermanually_btn_contactos_2);
		mitaxiregistermanually_btn_contactos_2.setTag(tag+"");
		mitaxiregistermanually_btn_contactos_2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,Contacts.CONTENT_URI);
				startActivityForResult(intent, tag);		
			}
		});
		
	final EditText mitaxiregistermanually_et_telemer_2 =(EditText)view.findViewById(R.id.mitaxiregistermanually_et_telemer_2);
		mitaxiregistermanually_et_telemer_2.setTag(tag+"");
		if(tel!=null){
			mitaxiregistermanually_et_telemer_2.setText(tel);
		}
		mitaxiregistermanually_et_telemer_2.addTextChangedListener(new TextWatcher() {
			 @Override
			 public void onTextChanged(CharSequence s, int start, int before, int count) {}
			  @Override
			  public void beforeTextChanged(CharSequence s, int start, int count,int after) {}

			  @Override
			  public void afterTextChanged(Editable s) {
			     String valor= mitaxiregistermanually_et_telemer_2 .getText().toString();
			     try{	
				  new EditTextValidator();
				EditTextValidator.esNumero(valor, 10, 0, false);
			     }catch(Exception e){
			    	 try{
			    	 if(valor.length() != 0){	 
			    		 mitaxiregistermanually_et_telemer_2 .getText().delete(valor.length() - 1, valor.length());
			    	 }
			    	 }catch(Exception m){
			    		 DatosLogBean.setDescripcion(Utils.getStackTrace(m));
			    	 }
				 
			     }	
			 }
		});
	

		
		EditText mitaxiregistermanually_et_correoemer_2 =(EditText)view.findViewById(R.id.mitaxiregistermanually_et_correoemer_2);
		mitaxiregistermanually_et_correoemer_2.setTag(tag+"");
		if(corr!=null){
			mitaxiregistermanually_et_correoemer_2.setText(corr);
		}
		views.add(view);
		mitaxiregistermanually_ll_contactos.addView(view);
		emergencia_esta_Ocupado[tag]=true;
		if(emergencia_esta_Ocupado[0]&&emergencia_esta_Ocupado[1]){
			mitaxiregistermanually_tv_agregar.setVisibility(TextView.INVISIBLE);
		}

	}
	
	/**
	 * metodo que llena tanto el numero celular como correo de emergencia con los contactos del usuario
	 * @param intent
	 * @param tag
	 */
	public void getContactInfo(Intent intent, int tag)
	{
		try{
			llenaCorreo(tag+"","");
			llenaCelular(tag+"", "");
			  @SuppressWarnings("deprecation")
			Cursor   cursor =  managedQuery(intent.getData(), null, null, null, null);      
			  if(!cursor.isClosed()&&cursor!=null){
			   while (cursor.moveToNext()) 
			   {           
			       String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			       String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
		
			       if ( hasPhone.equalsIgnoreCase("1")){
			           hasPhone = "true";
			           
			       }else{
			           hasPhone = "false" ;
			       }
			       if (Boolean.parseBoolean(hasPhone)) 
			       {
			        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
			       listaCels= new ArrayList<String>();
			        while (phones.moveToNext()) 
			        {
			     
			          String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			          phoneNumber=  phoneNumber.replaceAll(" ", "");
			          
			          final char c = phoneNumber.charAt(0);
			          if(c=='+'){
			        	  try{
			        		  phoneNumber =  phoneNumber.substring(3, 13); 
			        	  }catch(Exception e){
			        		  DatosLogBean.setDescripcion(Utils.getStackTrace(e));
			        	  }
			          }
			          
			          listaCels.add(phoneNumber);
			        }
			        if(listaCels.size()==1){ //si tiene solo un telefono
			        	llenaCelular(tag+"",listaCels.get(0)); 
			        	
			        }else if(listaCels.size()==0){//si no tiene telefono
			        	llenaCelular(tag+"","");
			        }else{
			        	dialogoLista(tag+"");
			        }
			        phones.close();
			       }
		
			       // Find Email Addresses
			       Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);

			       while (emails.moveToNext()) 
			       {
			        String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			       
			        	llenaCorreo(tag+"",emailAddress);
			        
			        break;
			       }
			       
			       emails.close();
			       break;
			  }  
			  }
		}catch(Exception e){
			DatosLogBean.setDescripcion(Utils.getStackTrace(e));
		}
	}
	

	@Override
	public void onBackPressed() {
		back_dialog();
	
	}

	/**
	 * sobrescribe el metodo onBackPressed 
	 */
	public void back_dialog(){
		Dialogos.Toast(RegistroContactosEmergenciaActivity.this, getResources().getString(R.string.mitaxiregister_validar_contactos_datos_no_guardados), Toast.LENGTH_SHORT);
		super.onBackPressed();
		
	}
	
	
	/**
	 * sobrescribe el metodo onBackPressed y guardamos
	 */
	public void back(){
		if(validaEditText(R.string.Registro_manual_llena_todos_los_campos)){
			if(guardaLasPreferencias()){
					Dialogos.Toast(RegistroContactosEmergenciaActivity.this, getResources().getString(R.string.Registro_manual_datos_guardados), Toast.LENGTH_SHORT);
					super.onBackPressed();
				
			}
		}
	}

	/**
	 * guarda en preferencias los contactos de emergencia 
	 * @return
	 */
	public boolean guardaLasPreferencias() {
		try{
				//pone en blanco las preferencias
					SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString("telemer", null);
					editor.putString("correoemer",null);
					editor.putString("telemer2", null);
					editor.putString("correoemer2",null);
					editor.commit();
					
				//llena las preferencias de nuevo
					
					for (int i = 0, count = mitaxiregistermanually_ll_contactos.getChildCount(); i < count; ++i) {
					      	  LinearLayout ll = (LinearLayout) mitaxiregistermanually_ll_contactos.getChildAt(i);
					      	  LinearLayout ll2 = (LinearLayout) ll.getChildAt(0);
					      	  LinearLayout ll3 = (LinearLayout) ll2.getChildAt(0);
					      	  EditText et = (EditText) ll3.getChildAt(0);
					      	  EditText et2 = (EditText) ll3.getChildAt(1);
					      	if(i==0){
					      	  editor.putString("telemer", et.getText().toString());
					      	  editor.putString("correoemer",et2.getText().toString());
					      	
					      	}
					      	if(i==1){
						      	  editor.putString("telemer2", et.getText().toString());
						      	  editor.putString("correoemer2",et2.getText().toString());
						     }
					}
					
					if (mitaxiregistermanually_cv_paranoico.isChecked()){ 
						editor.putBoolean("panico", true);
					}else{
						editor.putBoolean("panico", false);
					}
						editor.commit();
					editor.commit();
		return true;		
		}catch(Exception e){
			return false;
		}	
	}

	/**
	 * valida todos los editText 
	 * @return (true) si el editText esta bie llenado
	 */
	public boolean validaEditText(int texto) {
		for (int i = 0, count = mitaxiregistermanually_ll_contactos.getChildCount(); i < count; ++i) {
      	  LinearLayout ll = (LinearLayout) mitaxiregistermanually_ll_contactos.getChildAt(i);
      	  LinearLayout ll2 = (LinearLayout) ll.getChildAt(0);
      	  LinearLayout ll3 = (LinearLayout) ll2.getChildAt(0);
      	  EditText et = (EditText) ll3.getChildAt(0);
      	  EditText et2 = (EditText) ll3.getChildAt(1);
      	 //validamos que no esten vacios
      	  if(et.getText().toString().equals("")){
      		  Dialogos.Toast(RegistroContactosEmergenciaActivity.this, getResources().getString(texto), Toast.LENGTH_LONG);
      		  return false;
      	  }
    	  if(et2.getText().toString().equals("")){
    		  Dialogos.Toast(RegistroContactosEmergenciaActivity.this, getResources().getString(texto), Toast.LENGTH_LONG);
    		  return false;
    	  }
    	  //validamos que esten bien escritos
    	  if(et.getText().toString().length()!=10){
      		  Dialogos.Toast(RegistroContactosEmergenciaActivity.this,getResources().getString(R.string.Registro_manual_llena_bien_el_celular), Toast.LENGTH_LONG);
      		  return false;
      	  }
    	  if(Utils.isNumeric(et.getText().toString())){
    		  Dialogos.Toast(RegistroContactosEmergenciaActivity.this,getResources().getString(R.string.Registro_manual_llena_bien_el_celular_signo), Toast.LENGTH_LONG);
      		  return false;  
    	  }
    	  
    	  if(!EditTextValidator.esCorreo(et2)){
			  et2.setError(getResources().getString(R.string.edittext_error_email));
			  return false;
		  }
        }
		
		
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {
 			getContactInfo(data,0);
 		}else if (requestCode == 1) {
 			getContactInfo(data,1);
 		}else if(requestCode==RESULT_SETTINGS){
 			showUserSettings();
 		}
	}
	

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.abs_layout_iv_menu) {
			showPopup(v);
		} else if (v.getId() == R.id.abs_layout_iv_logo) {
			
			back_dialog();
		}
		
	}

	       
	    /**
	     * muestra popup en forma de menu
	     * @param v
	     */
	public void showPopup(View v) {
		PopupMenu popup = new PopupMenu(RegistroContactosEmergenciaActivity.this, v);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.popup, popup.getMenu());
		int positionOfMenuItem = 0; 
		MenuItem item = popup.getMenu().getItem(positionOfMenuItem);
		SpannableString s = new SpannableString(getResources().getString(R.string.action_settings));
		s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_vivos)), 0, s.length(), 0);
		item.setTitle(s);

		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {

				case R.id.configuracion_pref:

						Intent i = new Intent(RegistroContactosEmergenciaActivity.this,UserSettingActivity.class);
						startActivityForResult(i, RESULT_SETTINGS);
						return true;
					
				}
				return false;
			}
		});

		popup.show();
	}

	
	/**
	 * llena el telefono dinamicamente
	 * @param tag  (String) tag de la vista
	 * @param tel (String) telefono para mostrar
	 */
	public void llenaCelular(String tag, String tel){
		for (int i = 0, count = mitaxiregistermanually_ll_contactos.getChildCount(); i < count; ++i) {
      	  LinearLayout ll = (LinearLayout) mitaxiregistermanually_ll_contactos.getChildAt(i);
      	  LinearLayout ll2 = (LinearLayout) ll.getChildAt(0);
      	  LinearLayout ll3 = (LinearLayout) ll2.getChildAt(0);
      	  EditText et = (EditText) ll3.getChildAt(0);
      	  if(et.getTag().toString().equals(tag)){
      		  et.setText(tel);
      	  }
        }
	}
	
	/**
	 * llena el correo dinamicamente
	 * @param tag  (String) tag de la vista
	 * @param tel (String) correo para mostrar
	 */
	public void llenaCorreo(String tag, String correo){
		  for (int i = 0, count = mitaxiregistermanually_ll_contactos.getChildCount(); i < count; ++i) {
        	  LinearLayout ll = (LinearLayout) mitaxiregistermanually_ll_contactos.getChildAt(i);
        	  LinearLayout ll2 = (LinearLayout) ll.getChildAt(0);
        	  LinearLayout ll3 = (LinearLayout) ll2.getChildAt(0);
        	  EditText et = (EditText) ll3.getChildAt(1);
        	  if(et.getTag().toString().equals(tag)){
        		  et.setText(correo);
        	  }
          }
	}
	

	/**
	 * agrega la vista del contacto de emergencua
	 */
	public void dialogoLista(final String tag){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    View view = getLayoutInflater().inflate(R.layout.dialogo_contactos, null);
	    builder.setView(view);
	    builder.setCancelable(true);
		final ListView listview = (ListView) view.findViewById(R.id.dialogo_contacto_lv_contactos);
		final MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, listaCels);
	    listview.setAdapter(adapter);
	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    @Override
	    public void onItemClick(AdapterView<?> parent, final View view,int position, long id) {
	    final String item = (String) parent.getItemAtPosition(position);
	    llenaCelular(tag, item.replaceAll(" ",""));
	         customDialog.dismiss();
	       }
	     });
	     customDialog=builder.create();
	     customDialog.show();
	}
	
	
	/**
	 * mustra las preferencias guardadas
	 */
	private void showUserSettings() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		StringBuilder builder = new StringBuilder();
		builder.append("\n Send report:"+ sharedPrefs.getBoolean("prefSendReport", true));
	}
	
	
	
	
	

}