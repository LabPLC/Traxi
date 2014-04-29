package codigo.labplc.mx.trackxi.test;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import codigo.labplc.mx.trackxi.buscarplaca.BuscaPlaca;
import codigo.labplc.mx.trackxi.paginador.Paginador;
import codigo.labplc.mx.trackxi.registro.MitaxiRegisterManuallyActivity;

import com.robotium.solo.Solo;

/*
 * Clase encargada de hacer esenarios de pruebas
 * 
 * @autor mikesaurio
 */
public class MitaxiRegisterManuallyActivity_openActivity extends	ActivityInstrumentationTestCase2<MitaxiRegisterManuallyActivity> {
	private Solo solo;

	public MitaxiRegisterManuallyActivity_openActivity() {
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
	public void testOpenNextActivity() {
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
			//	solo.clickOnButton("Registrar");
		
				
		  // register next activity that need to be monitored.
		  ActivityMonitor activityMonitor = getInstrumentation().addMonitor(BuscaPlaca.class.getName(), null, false);

		  // open current activity.
		  solo.clickOnButton("Registrar");
		  
		  Paginador nextActivity = (Paginador) getInstrumentation().waitForMonitorWithTimeout(activityMonitor, 5);
		  // next activity is opened and captured.
		  assertNotNull(nextActivity);
		  nextActivity .finish();
		}
}