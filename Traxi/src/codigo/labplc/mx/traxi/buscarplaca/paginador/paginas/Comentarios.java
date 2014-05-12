package codigo.labplc.mx.traxi.buscarplaca.paginador.paginas;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.traxi.R;
import codigo.labplc.mx.traxi.buscarplaca.bean.AutoBean;
import codigo.labplc.mx.traxi.dialogos.Dialogos;
import codigo.labplc.mx.traxi.facebook.FacebookLogin;
import codigo.labplc.mx.traxi.facebook.FacebookLogin.OnGetFriendsFacebookListener;
import codigo.labplc.mx.traxi.facebook.FacebookLogin.OnLoginFacebookListener;
import codigo.labplc.mx.traxi.fonts.fonts;

import com.facebook.Response;
import com.facebook.model.GraphUser;

public class Comentarios extends View {

	private View view;
	private View view_row;
	private Activity context;
	private LinearLayout container;
	private AutoBean autoBean;
	LinearLayout adeudos_ll_contenedor_fotos;
	private FacebookLogin facebookLogin;
	private Button btnLogin;
	private boolean foundFriend = true;
	String id_usuario_face;
	
	public Comentarios(Activity context) {
		super(context);
		this.context = context;
	}

	public Comentarios(Activity context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	@SuppressLint("Instantiatable")
	public Comentarios(Activity context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}
	
	public void init(AutoBean autoBean,FacebookLogin facebookLogin){
		this.autoBean=autoBean;
		this.facebookLogin=facebookLogin;
		init();
	}
	

	public void init() {
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.activity_adeudos, null);
		
		TextView adeudos_titulo_main = (TextView)view.findViewById(R.id.adeudos_titulo_main);
		adeudos_titulo_main.setText(getResources().getString(R.string.titulo_tres_comentarios));
		adeudos_titulo_main.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_MAMEY));
		adeudos_titulo_main.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		container=(LinearLayout)view.findViewById(R.id.adeudos_ll_contenedor);
		
		adeudos_ll_contenedor_fotos = (LinearLayout)view.findViewById(R.id.adeudos_ll_contenedor_fotos);
		
		
		TextView adeudos_titulo_tv_amigos=(TextView)view.findViewById(R.id.adeudos_titulo_tv_amigos);
		adeudos_titulo_tv_amigos.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_MAMEY));
		adeudos_titulo_tv_amigos.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		
		btnLogin = (Button)view.findViewById(R.id.mitaxiregistermanually_btn_facebook);
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
			
				facebookLogin.loginFacebook();
				facebookLogin.setOnLoginFacebookListener(new OnLoginFacebookListener() {
					@Override
					public void onLoginFacebook(boolean status) {
						loginFacebook(status);
					}
				});
			}
		});
		
		for(int i = 0;i< autoBean.getArrayComentarioBean().size();i++){
		llenarComentario(autoBean.getArrayComentarioBean().get(i).getComentario(),autoBean.getArrayComentarioBean().get(i).getCalificacion(),i);
		}
		if(autoBean.getArrayComentarioBean().size()<=0){
			TextView tv = new TextView(context);
			tv.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_ROJO));
			tv.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
			LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1);
			lp.gravity= Gravity.CENTER;
			tv.setLayoutParams(lp);
			tv.setText(getResources().getString(R.string.sin_comentario));
			container.addView(tv);
		}
		
		if(facebookLogin.isSession()){
			facebookLogin.loginFacebook();
			facebookLogin.setOnLoginFacebookListener(new OnLoginFacebookListener() {
				@Override
				public void onLoginFacebook(boolean status) {
					loginFacebook(status);
				}
			});
		}
		

	}

	
	public void llenarComentario( String concepto, float valor,int i) {
	final	LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view_row = inflater.inflate(R.layout.comentarios_row, null);
		
	/*	if(id_user.equals(id_usuario_uuid)){
			Dialogos.Toast(context, "me encontre", Toast.LENGTH_LONG);
		}*/ 
	final	TextView comentarios_row_tv_descripcion = (TextView)view_row.findViewById(R.id.comentarios_row_tv_descripcion);
	comentarios_row_tv_descripcion.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_MAMEY));
	comentarios_row_tv_descripcion.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
	comentarios_row_tv_descripcion.setText(concepto);

		
	final ImageView rating1_comentarios = (ImageView)view_row.findViewById(R.id.rating1_comentarios);
	rating1_comentarios.setTag(i+"img1");
	final ImageView rating2_comentarios = (ImageView)view_row.findViewById(R.id.rating2_comentarios);
	rating1_comentarios.setTag(i+"img2");
	final ImageView rating3_comentarios = (ImageView)view_row.findViewById(R.id.rating3_comentarios);
	rating1_comentarios.setTag(i+"img3");
	final ImageView rating4_comentarios = (ImageView)view_row.findViewById(R.id.rating4_comentarios);
	rating1_comentarios.setTag(i+"img4");
	final ImageView rating5_comentarios = (ImageView)view_row.findViewById(R.id.rating5_comentarios);
	rating1_comentarios.setTag(i+"img5");
		
		
		if(valor==0.5){
		rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star3));
		}
		if(valor==1.0){
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
		}
		if(valor==1.5){
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star3));
			}
		if(valor==2.0){
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			}
		if(valor==2.5){
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star3));
			}
		if(valor==3.0){
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			}
		if(valor==3.5){
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating4_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star3));
			}
		if(valor==4.0){
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating4_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			}
		if(valor==4.5){
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating4_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating5_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star3));
			}
		if(valor==5.0){
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating4_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			rating5_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			}
		container.addView(view_row,i);
		
	}

	/**
	 * Login in to Facebook
	 * 
	 * @param status
	 */
	public void loginFacebook(boolean status) {
		if(status) {
			SharedPreferences prefs = context.getSharedPreferences("MisPreferenciasTrackxi", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("facebook", facebookLogin.getUserId().toString());
			editor.commit();
			getListOfFriends(facebookLogin.getUserId());
			
		} else {
			Dialogos.Toast(context,getResources().getString(R.string.falla_facebook), Toast.LENGTH_SHORT);
		}
	}
	
	/**
	 * Get a friends list from an user id
	 * 
	 * @param userId
	 */
	public void getListOfFriends(String userId) {
		facebookLogin.getListOfFriends();
		facebookLogin.setOnGetFriendsFacebookListener(new OnGetFriendsFacebookListener() {
			@Override
			public void onGetFriendsFacebook(List<GraphUser> users, Response response) {
				int i=-1;
				String usuarios=null;
				for (GraphUser user : users) {
					i+=1;
					if(i==0){
						adeudos_ll_contenedor_fotos.removeAllViews();
					}
					for(int j = 0;j< autoBean.getArrayComentarioBean().size();j++){
						if(autoBean.getArrayComentarioBean().get(j).getId_facebook().equals(user.getId())){
							if(foundFriend){
								adeudos_ll_contenedor_fotos.removeAllViews();
								foundFriend=false;
							}
							View viewFriend = addUserFriend(user,i,autoBean.getArrayComentarioBean().get(j).getCalificacion());
								if(viewFriend != null) {
									adeudos_ll_contenedor_fotos.addView(viewFriend);
						}
				}
					}
				}
				if(foundFriend||i==-1){
					btnLogin.setVisibility(Button.GONE);
					TextView tv = new TextView(context);
					tv.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_ROJO));
					tv.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
					LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT,1);
					lp.gravity= Gravity.CENTER;
					tv.setLayoutParams(lp);
					tv.setText(getResources().getString(R.string.face_no_amigos));
					adeudos_ll_contenedor_fotos.addView(tv);
				}
			}
		});
	}
	
	/**
	 * Add a user friend view to the layout
	 * 
	 * @param user
	 * @param calif 
	 * @return a view
	 */
	public View addUserFriend(final GraphUser user, int id, final float calif) {
		View viewFriend = context.getLayoutInflater().inflate(R.layout.listitem, null);
		ImageView ivFriendImageProfile = (ImageView) viewFriend.findViewById(R.id.iv_FriendImageProfile);
		ivFriendImageProfile.setTag(id);
		ivFriendImageProfile.setId(id);
		ivFriendImageProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Dialogos.Toast(context, user.getName()+getResources().getString(R.string.le_puso)+calif +getResources().getString(R.string.estrellas), Toast.LENGTH_LONG);
			}
		});
		facebookLogin.loadImageProfileToImageView(user.getId(), ivFriendImageProfile);
		return viewFriend;
	}

	
	/*
	 * Regresa la vista ya inflada 
	 */
	public View getView() {
		return view;
	}
	
	
	

}
