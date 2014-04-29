package codigo.labplc.mx.trackxi.test;

import android.test.ActivityInstrumentationTestCase2;
import codigo.labplc.mx.trackxi.registro.MitaxiRegisterManuallyActivity;

import com.robotium.solo.Solo;

/*
 * Clase encargada de hacer esenarios de pruebas
 * 
 * @autor mikesaurio
 */
public class MitaxiRegisterManuallyActivity_editTextNOTNULL_Test extends	ActivityInstrumentationTestCase2<MitaxiRegisterManuallyActivity> {
	private Solo solo;

	public MitaxiRegisterManuallyActivity_editTextNOTNULL_Test() {
		super(MitaxiRegisterManuallyActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	/**
	 * prueba que valida si en el registro no se dejaron datos vacios
	 */
	public void testEditText_REGISTRO_LLenos() {
		// llenamos un edittext con texto
		solo.clickOnText("usuario");
		solo.enterText(0, "mikesaurio");
		// oprimimos el boton Registrar
		solo.clickOnButton("Registrar");
		// se debe mostrar un mensaje de error
		assertTrue("did not get error msg",solo.searchText("Llena todos los campos."));
	}
}