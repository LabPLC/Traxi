package codigo.labplc.mx.trackxi.test;

import android.test.ActivityInstrumentationTestCase2;
import codigo.labplc.mx.traxi.buscarplaca.paginador.DatosAuto;
import codigo.labplc.mx.traxi.paginador.Paginador;
import codigo.labplc.mx.traxi.registro.MitaxiRegisterManuallyActivity;

import com.robotium.solo.Solo;

/*
 * Clase encargada de hacer esenarios de pruebas
 * 
 * @autor mikesaurio
 */
public class MitaxiRegisterManuallyActivity_REGISTRO_COMPLETO_Test extends	ActivityInstrumentationTestCase2<MitaxiRegisterManuallyActivity> {
	private Solo solo;

	public MitaxiRegisterManuallyActivity_REGISTRO_COMPLETO_Test() {
		super(MitaxiRegisterManuallyActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}


	/**
	 * prueba que valida si en el registros se llenaron todos los campos correctamente y se toma una fotografia
	 */
	public void testEditText_REGISTRO_BienEscritos() {
		// llenamos todos los editText
		solo.clickOnText("usuario");
		solo.enterText(0, "mikesaurio");
		
		solo.clickOnText("celular emergencia");
		solo.enterText(2, "5530265963");
		
		solo.clickOnText("correo emergencia");
		solo.enterText(3, "mikesaurio2@gmail.com");
		
		solo.clickOnText("celular emergencia");
		solo.enterText(4, "5590909090");
		
		solo.clickOnText("correo emergencia");
		solo.enterText(5, "mikesaurio3@gmail.com");
		
		// oprimimos el boton Registrar
		solo.clickOnButton("Registrar");
		// se debe mostrar un mensaje de error
	
		solo.searchText("Subiendo la informaci—n");
		
		if(solo.waitForActivity(Paginador.class)){
		/*	solo.clickOnText("Placa");
			solo.enterText(0, "A05601");*/
			// oprimimos el boton Registrar
			solo.clickOnButton("Foto ..");
			solo.searchText("Procesando la foto, espere....");
		
			if(solo.searchText("Procesando la foto, espere....")){
				assertTrue("did not get error msg",solo.searchText("ÁLa foto a fallado!, intenta de nuevo"));
			}else if(solo.waitForActivity(DatosAuto.class)){
				assertTrue("did not get error msg",solo.searchText("Cargando la"));
			}
		}
	}
}