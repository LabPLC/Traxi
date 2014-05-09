

package codigo.labplc.mx.trackxi.registro;


import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.dialogos.Dialogos;
import codigo.labplc.mx.trackxi.fonts.fonts;
import codigo.labplc.mx.trackxi.log.BeanDatosLog;
import codigo.labplc.mx.trackxi.utils.Utils;

public class MitaxiRegisterManuallyActivity extends Activity {
	
	public final String TAG = this.getClass().getSimpleName();
	String origen;
	private LinearLayout mitaxiregistermanually_ll_contactos;
	private TextView mitaxiregistermanually_tv_agregar;
	 ArrayList<View> views = new ArrayList<View>();
	private View mitaxiregistermanually_iv_quitar;
	private boolean[] emergenciaOcupado= {true,true};//maneja los contactos
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		    
		setContentView(R.layout.activity_mitaxi_register_manually);
		
		BeanDatosLog.setTagLog(TAG);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			origen = bundle.getString("origen");	

		}
		
		
		 final ActionBar ab = getActionBar();
	     ab.setDisplayShowHomeEnabled(false);
	     ab.setDisplayShowTitleEnabled(false);     
	     final LayoutInflater inflater = (LayoutInflater)getSystemService("layout_inflater");
	     View view = inflater.inflate(R.layout.abs_layout,null);   
	     ((TextView) view.findViewById(R.id.abs_layout_tv_titulo)).setTypeface(new fonts(MitaxiRegisterManuallyActivity.this).getTypeFace(fonts.FLAG_MAMEY));
	     ab.setDisplayShowCustomEnabled(true);
	     ab.setCustomView(view,new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
	     ab.setCustomView(view);
		
		initUI();

	}

	/**
	 * metodo que inicia la vista
	 */
	public void initUI() {
		
		TextView mitaxiregistermanually_tv_label= (TextView)findViewById(R.id.mitaxiregistermanually_tv_label);
		 mitaxiregistermanually_tv_label.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		 mitaxiregistermanually_tv_label.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		 
		 TextView mitaxiregistermanually_tv_emergencia= (TextView)findViewById(R.id.mitaxiregistermanually_tv_emergencia);
		 mitaxiregistermanually_tv_emergencia.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		 mitaxiregistermanually_tv_emergencia.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_ROJO));
		 
		 mitaxiregistermanually_ll_contactos =(LinearLayout)findViewById(R.id.mitaxiregistermanually_ll_contactos);
		
		 mitaxiregistermanually_tv_agregar=(TextView) findViewById(R.id.mitaxiregistermanually_tv_agregar);
		 mitaxiregistermanually_tv_agregar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(emergenciaOcupado[0]||emergenciaOcupado[1]){
					addContact();
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
		 
		 
		addContact();
	}
	
	
	
	
	
	
	
	/**
	 * metodo que agrega un contacto de emergencia
	 */
	public void addContact(){
		if(emergenciaOcupado[0]){
			addView(0);
		}else if(emergenciaOcupado[1]){
			addView(1);
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
	public void addView(final int tag){
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
				//Dialogos.Toast(MitaxiRegisterManuallyActivity.this,mitaxiregistermanually_btn_contactos_2.getTag()+"" , Toast.LENGTH_SHORT);
				
			}
		});
		EditText mitaxiregistermanually_et_telemer_2 =(EditText)view.findViewById(R.id.mitaxiregistermanually_et_telemer_2);
		mitaxiregistermanually_et_telemer_2.setTag(tag+"");
		EditText mitaxiregistermanually_et_correoemer_2 =(EditText)view.findViewById(R.id.mitaxiregistermanually_et_correoemer_2);
		mitaxiregistermanually_et_correoemer_2.setTag(tag+"");
		
			views.add(view);
			mitaxiregistermanually_ll_contactos.addView(view);
			emergenciaOcupado[tag]=false;
			if(!emergenciaOcupado[0]&&!emergenciaOcupado[1]){
				mitaxiregistermanually_tv_agregar.setVisibility(TextView.INVISIBLE);
			}
	}
	
	/*
	 * 
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
			        		  et.setText(phoneNumber);
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
		if(validaEditText()){
			
			super.onBackPressed();
		}
	
	}

	public boolean validaEditText() {
		for (int i = 0, count = mitaxiregistermanually_ll_contactos.getChildCount(); i < count; ++i) {
      	  LinearLayout ll = (LinearLayout) mitaxiregistermanually_ll_contactos.getChildAt(i);
      	  LinearLayout ll2 = (LinearLayout) ll.getChildAt(0);
      	  LinearLayout ll3 = (LinearLayout) ll2.getChildAt(0);
      	  EditText et = (EditText) ll3.getChildAt(0);
      	  EditText et2 = (EditText) ll3.getChildAt(1);
      	 //validamos que no esten vacios
      	  if(et.getText().toString().equals("")){
      		  Dialogos.Toast(MitaxiRegisterManuallyActivity.this, "Debes llenar los campos", Toast.LENGTH_LONG);
      		  return false;
      	  }
    	  if(et2.getText().toString().equals("")){
    		  Dialogos.Toast(MitaxiRegisterManuallyActivity.this, "Debes llenar los campos", Toast.LENGTH_LONG);
    		  return false;
    	  }
    	  //validamos que esten bien escritos
    	  if(et.getText().toString().length()!=10){
      		  Dialogos.Toast(MitaxiRegisterManuallyActivity.this, "El celular debe ser de 10 dígitos", Toast.LENGTH_LONG);
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
 		}
	}
}