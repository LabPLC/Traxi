package codigo.labplc.mx.traxi.buscarplaca.paginador.paginas;

import java.util.ArrayList;
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
/**
 * pagina que muestra los comentarios y su calificacion
 * 
 * @author mikesaurio
 *
 */
@SuppressLint("ViewConstructor")
public class Comentarios extends View {

	private View view;
	private View view_row;
	private Activity context;
	private LinearLayout container;
	private AutoBean autoBean;
	LinearLayout comentarios_ll_contenedor_fotos;
	private FacebookLogin facebookLogin;
	private Button btnLogin;
	private boolean foundFriend = true;
	String id_usuario_face;
	float valorTotal =0.0f;
	private TextView comentarios_tv_cinco_estrellas;
	private ArrayList<String>array_id_facebook = new ArrayList<String>();

	
	public Comentarios(Activity context) {
		super(context);
		this.context = context;
	}

	public Comentarios(Activity context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public Comentarios(Activity context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}
	
	
	
	
	/**
	 * init comentarios
	 * @param autoBean (AutoBean) informacion de la placa
	 * @param facelog (FacebookLogin) login de facebook
	 */
	public void init(AutoBean autoBean,FacebookLogin facelog){
		this.autoBean=autoBean;
		this.facebookLogin=facelog;
		
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.activity_comentarios, null);
		container=(LinearLayout)view.findViewById(R.id.comentarios_ll_contenedor);
		
		comentarios_ll_contenedor_fotos = (LinearLayout)view.findViewById(R.id.comentarios_ll_contenedor_fotos);
		
		
		TextView adeudos_titulo_tv_amigos=(TextView)view.findViewById(R.id.comentarios_titulo_tv_amigos);
		adeudos_titulo_tv_amigos.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_MAMEY));
		adeudos_titulo_tv_amigos.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		
		btnLogin = (Button)view.findViewById(R.id.comentarios_btn_facebook);
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
		llenarComentario(autoBean.getArrayComentarioBean().get(i).getComentario(),
				autoBean.getArrayComentarioBean().get(i).getCalificacion(),i,autoBean.getArrayComentarioBean().get(i).getFecha_comentario());
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
			
		}else{
			TextView comentarios_tv_cuantos=(TextView)view.findViewById(R.id.comentarios_tv_cuantos);
			comentarios_tv_cuantos.setText("("+autoBean.getArrayComentarioBean().size()+")");
			
			comentarios_tv_cinco_estrellas =(TextView)view.findViewById(R.id.comentarios_tv_cinco_estrellas);
	
			llenarEstrellas(valorTotal/autoBean.getArrayComentarioBean().size());
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

	/**
	 * llena las estrellas de evaluacion del chofer 
	 * @param valor (float) calificacion final
	 */
	public void llenarEstrellas(float valor) {
		 ImageView rating1_comentarios = (ImageView)view.findViewById(R.id.rating1_comentarios);
		 ImageView rating2_comentarios = (ImageView)view.findViewById(R.id.rating2_comentarios);
		 ImageView rating3_comentarios = (ImageView)view.findViewById(R.id.rating3_comentarios);
		 ImageView rating4_comentarios = (ImageView)view.findViewById(R.id.rating4_comentarios);
		 ImageView rating5_comentarios = (ImageView)view.findViewById(R.id.rating5_comentarios);
		 

			if(valor<=0.5){
			comentarios_tv_cinco_estrellas.setText("Calificaci—n "+0.5+" de 5.0");
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star3));
			}
			if(valor<=1.0&&valor>0.5){
				comentarios_tv_cinco_estrellas.setText("Calificaci—n "+1.0+" de 5.0");
				rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			}
			if(valor<=1.5&&valor>1.0){
				comentarios_tv_cinco_estrellas.setText("Calificaci—n "+1.5+" de 5.0");
				rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star3));
				}
			if(valor<=2.0&&valor>1.5){
				comentarios_tv_cinco_estrellas.setText("Calificaci—n "+2.0+" de 5.0");
				rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				}
			if(valor<=2.5&&valor>2.0){
				comentarios_tv_cinco_estrellas.setText("Calificaci—n "+2.5+" de 5.0");
				rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star3));
				}
			if(valor<=3.0&&valor>2.5){
				comentarios_tv_cinco_estrellas.setText("Calificaci—n "+3.0+" de 5.0");
				rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				}
			if(valor<=3.5&&valor>3.0){
				comentarios_tv_cinco_estrellas.setText("Calificaci—n "+3.5+" de 5.0");
				rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating4_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star3));
				}
			if(valor<=4.0&&valor>3.5){
				comentarios_tv_cinco_estrellas.setText("Calificaci—n "+4.0+" de 5.0");
				rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating4_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				}
			if(valor<=4.5&&valor>4.0){
				comentarios_tv_cinco_estrellas.setText("Calificaci—n "+4.5+" de 5.0");
				rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating4_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating5_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star3));
				}
			if(valor>4.5){
				comentarios_tv_cinco_estrellas.setText("Calificaci—n "+5.0+" de 5.0");
				rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating4_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				rating5_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
				}
		
	}

	/**
	 * llena los comentarios row a row
	 * @param concepto (String) comentario
	 * @param valor (float) calificacion
	 * @param i (int) id del row
	 * @param hora (String) hora del comentario
	 */
	public void llenarComentario( String concepto, float valor,int i, String hora) {
	final	LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view_row = inflater.inflate(R.layout.comentarios_row, null); 
	final	TextView comentarios_row_tv_descripcion = (TextView)view_row.findViewById(R.id.comentarios_row_tv_descripcion);
	comentarios_row_tv_descripcion.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_MAMEY));
	comentarios_row_tv_descripcion.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
	comentarios_row_tv_descripcion.setText(concepto);
	
	final	TextView comentarios_row_tv_horario = (TextView)view_row.findViewById(R.id.comentarios_row_tv_horario);
	comentarios_row_tv_horario.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_MAMEY));
	comentarios_row_tv_horario.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_CLARO));
	comentarios_row_tv_horario.setText(hora);
		
	valorTotal+=valor;
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
				if(users!=null){
					for (GraphUser user : users) {
						i+=1;
						if(i==0){
							comentarios_ll_contenedor_fotos.removeAllViews();
						}
						for(int j = 0;j< autoBean.getArrayComentarioBean().size();j++){
							if(autoBean.getArrayComentarioBean().get(j).getId_facebook().equals(user.getId())){
								if(foundFriend){
									comentarios_ll_contenedor_fotos.removeAllViews();
									foundFriend=false;
								}
								View viewFriend = addUserFriend(user,i,autoBean.getArrayComentarioBean().get(j).getComentario());
									if(viewFriend != null) {
										comentarios_ll_contenedor_fotos.addView(viewFriend);
							}
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
					comentarios_ll_contenedor_fotos.addView(tv);
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
	public View addUserFriend(final GraphUser user, int id, final String calif) {
		View viewFriend = context.getLayoutInflater().inflate(R.layout.listitem, null);
		ImageView ivFriendImageProfile = (ImageView) viewFriend.findViewById(R.id.iv_FriendImageProfile);
		ivFriendImageProfile.setTag(id);
		ivFriendImageProfile.setId(id);
		ivFriendImageProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Dialogos.Toast(context, user.getName()+": "+calif , Toast.LENGTH_LONG);
			}
		});
		if(array_id_facebook.contains(user.getId())){
			return null;
		}else{
			array_id_facebook.add(user.getId());
			facebookLogin.loadImageProfileToImageView(user.getId(), ivFriendImageProfile);
			return viewFriend;
		}
	}

	
	/**
	 * GET view
	 * @return (view) vista inflada
	 */
	public View getView() {
		return view;
	}
	
	
	

}
