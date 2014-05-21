

package codigo.labplc.mx.traxi.registro;


import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.configuracion.UserSettingActivity;
import codigo.labplc.mx.traxi.dialogos.Dialogos;
import codigo.labplc.mx.traxi.fonts.fonts;
import codigo.labplc.mx.traxi.log.BeanDatosLog;
import codigo.labplc.mx.traxi.registro.validador.EditTextValidator;
import codigo.labplc.mx.traxi.utils.Utils;

public class MitaxiRegisterManuallyActivity extends Activity implements OnClickListener{
	
	public final String TAG = this.getClass().getSimpleName();
	private LinearLayout mitaxiregistermanually_ll_contactos;
	private TextView mitaxiregistermanually_tv_agregar;
	 ArrayList<View> views = new ArrayList<View>();
	private View mitaxiregistermanually_iv_quitar;
	private boolean[] emergenciaOcupado= {true,true};//maneja los contactos
	private int RESULT_SETTINGS =10;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		    
		setContentView(R.layout.activity_mitaxi_register_manually);
		
		BeanDatosLog.setTagLog(TAG);
	
		
		 final ActionBar ab = getActionBar();
	     ab.setDisplayShowHomeEnabled(false);
	     ab.setDisplayShowTitleEnabled(false);     
	     final LayoutInflater inflater = (LayoutInflater)getSystemService("layout_inflater");
	     View view = inflater.inflate(R.layout.abs_layout_back,null);   
	     ((TextView) view.findViewById(R.id.abs_layout_tv_titulo)).setText(getResources().getString(R.string.mitaxiregister_et_emergencias));
	     ((TextView) view.findViewById(R.id.abs_layout_tv_titulo)).setTypeface(new fonts(MitaxiRegisterManuallyActivity.this).getTypeFace(fonts.FLAG_MAMEY));
	     ((TextView) view.findViewById(R.id.abs_layout_tv_titulo)).setTextSize(15.0f);
	     ab.setDisplayShowCustomEnabled(true);
	     ab.setCustomView(view,new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
	     ImageView abs_layout_iv_menu = (ImageView) view.findViewById(R.id.abs_layout_iv_menu);
	     abs_layout_iv_menu.setOnClickListener(this);
	     ImageView abs_layout_iv_logo = (ImageView) view.findViewById(R.id.abs_layout_iv_logo);
	     abs_layout_iv_logo.setOnClickListener(this);
	     ab.setCustomView(view);
	     
	     
	     
	 
		
		initUI();

	}

	/**
	 * metodo que inicia la vista
	 */
	public void initUI() {
		
		TextView mitaxiregistermanually_tv_label= (TextView)findViewById(R.id.mitaxiregistermanually_tv_label);
		 mitaxiregistermanually_tv_label.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_MAMEY));
		 mitaxiregistermanually_tv_label.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		 
		/* TextView mitaxiregistermanually_tv_emergencia= (TextView)findViewById(R.id.mitaxiregistermanually_tv_emergencia);
		 mitaxiregistermanually_tv_emergencia.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		 mitaxiregistermanually_tv_emergencia.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_ROJO));*/
		 
		 mitaxiregistermanually_ll_contactos =(LinearLayout)findViewById(R.id.mitaxiregistermanually_ll_contactos);
		
		 mitaxiregistermanually_tv_agregar=(TextView) findViewById(R.id.mitaxiregistermanually_tv_agregar);
		 mitaxiregistermanually_tv_agregar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(emergenciaOcupado[0]||emergenciaOcupado[1]){
					addContact(null,null);
				}
			}
		});
		 
	

		 
		 ImageView mitaxiregistermanually_iv_ayuda=(ImageView)findViewById(R.id.mitaxiregistermanually_iv_ayuda);
		 mitaxiregistermanually_iv_ayuda.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			new	Dialogos().mostrarParaQue(MitaxiRegisterManuallyActivity.this).show();
				
			}
		});
		 
		 
		 CheckBox mitaxiregistermanually_cv_paranoico = (CheckBox) findViewById(R.id.mitaxiregistermanually_cv_paranoico); 
		  SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
          boolean panic = prefs.getBoolean("panico", false);
        	  mitaxiregistermanually_cv_paranoico.setChecked(panic); 
        	  
          
		 mitaxiregistermanually_cv_paranoico.setOnCheckedChangeListener(new OnCheckedChangeListener() { 

		 @Override 
		 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { 
			 SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = prefs.edit();
			 if (buttonView.isChecked()){ 
					editor.putBoolean("panico", true);
					editor.commit();
			 } 
			 else{
				 editor.putBoolean("panico", false);
				 editor.commit();
			 } 

		 }
		 });
		 
		llenarContactos();
		
	}
	
	
	
	
	
	
	
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
		if(emergenciaOcupado[0]){
			addView(0,tel,corr);
		}else if(emergenciaOcupado[1]){
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
				emergenciaOcupado[i]=true;
				if(emergenciaOcupado[0]||emergenciaOcupado[1]){
				mitaxiregistermanually_tv_agregar.setVisibility(TextView.VISIBLE);
				}		
			}
		}
	}
	
	/**
	 * agrega la vista del contacto de emergencua
	 */
	public void addView(final int tag,String tel,String corr){
		
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
			    	 if(valor.length() != 0){	 
			    		 mitaxiregistermanually_et_telemer_2 .getText().delete(valor.length() - 1, valor.length());
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
		emergenciaOcupado[tag]=false;
		if(!emergenciaOcupado[0]&&!emergenciaOcupado[1]){
			mitaxiregistermanually_tv_agregar.setVisibility(TextView.INVISIBLE);
		}
	}
	
	/*
	 * metodo que llena tanto el numero celular como correo de emergencia con los contactos del usuario
	 */
	public void getContactInfo(Intent intent, int tag)
	{
		try{
			  Cursor   cursor =  managedQuery(intent.getData(), null, null, null, null);      
			  if(!cursor.isClosed()&&cursor!=null){
			   while (cursor.moveToNext()) 
			   {           
			       String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			       String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)); 
			       String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
		
			       if ( hasPhone.equalsIgnoreCase("1")){
			           hasPhone = "true";
			           
			       }else{
			           hasPhone = "false" ;
			       }
			       if (Boolean.parseBoolean(hasPhone)) 
			       {
			        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
			        while (phones.moveToNext()) 
			        {
			          String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			          for (int i = 0, count = mitaxiregistermanually_ll_contactos.getChildCount(); i < count; ++i) {
			        	  LinearLayout ll = (LinearLayout) mitaxiregistermanually_ll_contactos.getChildAt(i);
			        	  LinearLayout ll2 = (LinearLayout) ll.getChildAt(0);
			        	  LinearLayout ll3 = (LinearLayout) ll2.getChildAt(0);
			        	  EditText et = (EditText) ll3.getChildAt(0);
			        	  if(et.getTag().toString().equals(tag+"")){
			        		  et.setText(phoneNumber.replaceAll(" ", ""));
			        	  }
			          }
			          
			      
			          break;
			        }
			        phones.close();
			       }
		
			       // Find Email Addresses
			       Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);
			       while (emails.moveToNext()) 
			       {
			        String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			       
			        for (int i = 0, count = mitaxiregistermanually_ll_contactos.getChildCount(); i < count; ++i) {
			        	  LinearLayout ll = (LinearLayout) mitaxiregistermanually_ll_contactos.getChildAt(i);
			        	  LinearLayout ll2 = (LinearLayout) ll.getChildAt(0);
			        	  LinearLayout ll3 = (LinearLayout) ll2.getChildAt(0);
			        	  EditText et = (EditText) ll3.getChildAt(1);
			        	  if(et.getTag().toString().equals(tag+"")){
			        		  et.setText(emailAddress);
			        	  }
			          }
			        
			        break;
			       }
			       
			       emails.close();
			       break;
			  }  
			  }
		}catch(Exception e){
			BeanDatosLog.setDescripcion(Utils.getStackTrace(e));
		}
	}
	

	@Override
	public void onBackPressed() {
		back();
	
	}

	
	public void back(){
		if(validaEditText()){
			if(guardaLasPreferencias()){
				Dialogos.Toast(MitaxiRegisterManuallyActivity.this, MitaxiRegisterManuallyActivity.this.getResources().getString(R.string.Registro_manual_datos_guardados), Toast.LENGTH_SHORT);
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
					editor.commit();
		return true;		
		}catch(Exception e){
			return false;
		}	
	}

	/**
	 * valida todos los editText 
	 * @return
	 */
	public boolean validaEditText() {
		for (int i = 0, count = mitaxiregistermanually_ll_contactos.getChildCount(); i < count; ++i) {
      	  LinearLayout ll = (LinearLayout) mitaxiregistermanually_ll_contactos.getChildAt(i);
      	  LinearLayout ll2 = (LinearLayout) ll.getChildAt(0);
      	  LinearLayout ll3 = (LinearLayout) ll2.getChildAt(0);
      	  EditText et = (EditText) ll3.getChildAt(0);
      	  EditText et2 = (EditText) ll3.getChildAt(1);
      	 //validamos que no esten vacios
      	  if(et.getText().toString().equals("")){
      		  Dialogos.Toast(MitaxiRegisterManuallyActivity.this, getResources().getString(R.string.Registro_manual_llena_todos_los_campos), Toast.LENGTH_LONG);
      		  return false;
      	  }
    	  if(et2.getText().toString().equals("")){
    		  Dialogos.Toast(MitaxiRegisterManuallyActivity.this, getResources().getString(R.string.Registro_manual_llena_todos_los_campos), Toast.LENGTH_LONG);
    		  return false;
    	  }
    	  //validamos que esten bien escritos
    	  if(et.getText().toString().length()!=10){
      		  Dialogos.Toast(MitaxiRegisterManuallyActivity.this,getResources().getString(R.string.Registro_manual_llena_bien_el_celular), Toast.LENGTH_LONG);
      		  return false;
      	  }
    	  if(Utils.isNumeric(et.getText().toString())){
    		  Dialogos.Toast(MitaxiRegisterManuallyActivity.this,getResources().getString(R.string.Registro_manual_llena_bien_el_celular_signo), Toast.LENGTH_LONG);
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
			
			back();
		}
		
	}

	       
	    
	public void showPopup(View v) {
		PopupMenu popup = new PopupMenu(MitaxiRegisterManuallyActivity.this, v);
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.popup, popup.getMenu());
		int positionOfMenuItem = 0; 
		MenuItem item = popup.getMenu().getItem(positionOfMenuItem);
		SpannableString s = new SpannableString(getResources().getString(R.string.action_settings));
		s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.rojo_logo)), 0, s.length(), 0);
		item.setTitle(s);

		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {

				case R.id.configuracion_pref:

						Intent i = new Intent(MitaxiRegisterManuallyActivity.this,UserSettingActivity.class);
						startActivityForResult(i, RESULT_SETTINGS);
						return true;
					
				}
				return false;
			}
		});

		popup.show();
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