package codigo.labplc.mx.trackxi.test;

import android.test.ActivityInstrumentationTestCase2;
import codigo.labplc.mx.traxi.registro.MitaxiRegisterManuallyActivity;

import com.robotium.solo.Solo;

/*
 * Clase encargada de hacer esenarios de pruebas
 * 
 * @autor mikesaurio
 */
public class MitaxiRegisterManuallyActivity_AcercaDE_Test extends	ActivityInstrumentationTestCase2<MitaxiRegisterManuallyActivity> {
	private Solo solo;

	public MitaxiRegisterManuallyActivity_AcercaDE_Test() {
		super(MitaxiRegisterManuallyActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	/**
	 * prueba que valida si se abre el acerca de
	 */
	public void testEditText_REGISTRO_LLenos() {
		// llenamos un edittext con texto
		
		// oprimimos el boton Registrar
		solo.clickOnView(solo.getView(codigo.labplc.mx.trackxi.R.id.mitaxiregistermanually_im_info));
		// se debe mostrar un mensaje de error
		assertTrue("did not get error msg",solo.searchText("AVISO"));
		assertTrue("did not get error msg",solo.searchText("DATOS NECESARIOS"));
	}
}