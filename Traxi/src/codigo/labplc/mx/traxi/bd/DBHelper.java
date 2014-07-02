package codigo.labplc.mx.traxi.bd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import codigo.labplc.mx.traxi.buscarplaca.bean.ViajeBean;

public class DBHelper extends SQLiteOpenHelper {
	/** Path donde se encontrara alojada la BD en el telŽfono **/
	private static String DB_PATH = "";
	/** Nombre de la base de datos **/
	private final static String DB_NAME = "traxi";

	private SQLiteDatabase myDataBase;
	private final Context myContext;

	/** Constructor **/
	@SuppressLint("SdCardPath")
	public DBHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext = context;
		
		DB_PATH = "/data/data/" + myContext.getPackageName() + "/databases/";
	}

	
	/**
	 * Comprueba si ya existe nuestra base de datos
	 * 
	 * @return true si ya existe, false si no
	 **/
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;

		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
			
			
		} catch (Exception e) {
			Log.i("Base de datos", "falla en checkDataBAse");
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return  (checkDB!= null ? true : false);
	}
	/*private boolean checkDataBase() {
	    try {
	        String myPath = DB_PATH + DB_NAME;
	        File f = new File(myPath);
	        if (f.exists())
	            return true;
	        else
	            return false;
	    } catch (SQLiteException e) {
	        Log.e("Podcast", "There was an error", e);
	        return false;
	    }
	}*/

	/**
	 * Crea una base de datos vac’a y escribe en ella nuestra propia Base de
	 * Datos
	 **/
	public void createDataBase(Context contexto) throws IOException {

		/** Comprueba si ya existe la base de datos **/
		boolean dbExist = checkDataBase();

		if (dbExist) {
			/** Si existe la base de datos no hace nada **/
		} else {
			/**
			 * Si no existe se llama a este mŽtodo que crea una nueva base de
			 * datos en la ruta por defecto
			 **/
			this.getReadableDatabase();
			try {
				/**
				 * Copia nuestra database.sqlite en la nueva base de datos
				 * creada
				 **/
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copiando la Base de Datos");
			}
		}
	}

	/**
	 * Copia nuestra base de datos sqlite de la carpeta assets a nuestra nueva
	 * Base de Datos
	 **/
	private void copyDataBase() throws IOException {

		/** Abre nuestra base de datos del fichero **/
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		/** La direcci—n de nuestra nueva Base de Datos **/
		String outFileName = DB_PATH + DB_NAME;

		/** Abre la nueva Base de Datos **/
		OutputStream myOutput = new FileOutputStream(outFileName);

		/** Transfiere bytes desde nuestro archivo a la nueva base de datos **/
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		/** Cierra los stream **/
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	/**
	 * Abre la base de datos
	 * 
	 * @throws SQLException
	 **/
	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}


	public SQLiteDatabase loadDataBase(Context context, DBHelper helper) throws IOException {
		helper.createDataBase(context);
	    SQLiteDatabase db = helper.getWritableDatabase();
		return db;
	}
	
	/**
	 * inserta en la BDLite la informacion de un viaje
	 * @param bd
	 * @param placa
	 * @param hora_inicio
	 * @param hora_fin
	 * @param calificacion
	 * @param comentario
	 * @param inicio_viaje
	 * @param fin_viaje
	 * @return
	 */
	public boolean setViaje(SQLiteDatabase bd,String placa,String hora_inicio,String hora_fin,String calificacion,String comentario,String inicio_viaje,String fin_viaje){
		try{
			bd.execSQL("insert into historial (placa,fecha_inicio,fecha_fin,calificacion,comentario,inicio_viaje,fin_viaje) values ('"+placa+"','"+hora_inicio+"','"+hora_fin+"','"+calificacion+"','"+comentario+"','"+inicio_viaje+"','"+fin_viaje+"');");
			return true;
		}catch(Exception e){
		return false;
		}
	}
	
	
	public boolean deleteViaje(SQLiteDatabase bd,String id){
		try{
			bd.execSQL("delete from historial where id = "+id);
			return true;
		}catch(Exception e){
		return false;
		}
	}
	
	public ViajeBean getViajes(SQLiteDatabase bd){
		ViajeBean bean = null;
		Cursor c = null;
        c = bd.rawQuery("select * from historial", null);
    	
        if(c!=null&&c.getCount()>0){
        	bean = new ViajeBean();
        	int[] identificador= new int[c.getCount()];
	         String[] placa = new String[c.getCount()];
	    	 String[] hora_inicio= new String[c.getCount()];
	    	 String[] hora_fin= new String[c.getCount()];
	    	 String[] calificacion= new String[c.getCount()];
	    	 String[] comentario= new String[c.getCount()];
	    	 String[] inicio_viaje= new String[c.getCount()];
	    	 String[] fin_viaje= new String[c.getCount()];
        
	        c.moveToFirst();
	        int i =0;
	        while (!c.isAfterLast()){
	        	identificador[i]=(c.getInt(c.getColumnIndex("id")));
	        	placa[i]=(c.getString(c.getColumnIndex("placa")));
	        	hora_inicio[i]=(c.getString(c.getColumnIndex("fecha_inicio")));
	        	hora_fin[i]=(c.getString(c.getColumnIndex("fecha_fin")));
	        	calificacion[i]=(c.getString(c.getColumnIndex("calificacion")));
	        	comentario[i]=(c.getString(c.getColumnIndex("comentario")));
	        	inicio_viaje[i]=(c.getString(c.getColumnIndex("inicio_viaje")));
	        	fin_viaje[i]=(c.getString(c.getColumnIndex("fin_viaje")));
	        	c.moveToNext();
	        	i+=1;
	        }
	        if(i>0){
	        	bean.setId(identificador);
	        	bean.setPlaca(placa);
	        	bean.setHora_inicio(hora_inicio);
	        	bean.setHora_fin(hora_fin);
	        	bean.setCalificacion(calificacion);
	        	bean.setComentario(comentario);
	        	bean.setInicio_viaje(inicio_viaje);
	        	bean.setFin_viaje(fin_viaje);
	        	
	        }
        }
        c.close();
        
		return bean;
	}
	
	
}
