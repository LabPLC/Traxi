package codigo.labplc.mx.trackxi.paginador;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.configuracion.UserSettingActivity;
import codigo.labplc.mx.trackxi.fonts.fonts;
import codigo.labplc.mx.trackxi.log.BeanDatosLog;
import codigo.labplc.mx.trackxi.registro.MitaxiRegisterManuallyActivity;

import com.viewpagerindicator.CirclePageIndicator;

/**
 * 
 * @author amatellanes
 * 
 */
public class Paginador extends FragmentActivity {
	private static final int RESULT_SETTINGS = 1;
	 public final String TAG = this.getClass().getSimpleName();
	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next pages.
	 */
	ViewPager pager = null;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	MyFragmentPagerAdapter pagerAdapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.main);

		BeanDatosLog.setTagLog(TAG);	
	
	     final ActionBar ab = getActionBar();
	     ab.setDisplayShowHomeEnabled(false);
	     ab.setDisplayShowTitleEnabled(false);     
	     final LayoutInflater inflater = (LayoutInflater)getSystemService("layout_inflater");
	     View view = inflater.inflate(R.layout.abs_layout,null);   
	     ((TextView) view.findViewById(R.id.abs_layout_tv_titulo)).setTypeface(new fonts(Paginador.this).getTypeFace(fonts.FLAG_MAMEY));
	     ab.setDisplayShowCustomEnabled(true);
	     
	     ab.setCustomView(view,new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
	     ab.setCustomView(view);


		// Instantiate a ViewPager
		this.pager = (ViewPager) this.findViewById(R.id.pager);

		// Set a custom animation
		// this.pager.setPageTransformer(true, new ZoomOutPageTransformer());

		// Create an adapter with the fragments we show on the ViewPager
		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(ScreenSlidePageFragment.newInstance(getResources().getColor(R.color.android_blue), 1,Paginador.this));
		//adapter.addFragment(ScreenSlidePageFragmentMap.newInstance(getResources().getColor(R.color.android_red), 2,Paginador.this));
	//	adapter.addFragment(ScreenSlidePageFragment.newInstance(getResources().getColor(R.color.android_blue), 3,Paginador.this));
		this.pager.setAdapter(adapter);

		// Bind the title indicator to the adapter
		CirclePageIndicator titleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		titleIndicator.setBackgroundColor(Color.WHITE);
		titleIndicator.setViewPager(pager);

	}

	@Override
	public void onBackPressed() {

		// Return to previous page when we press back button
		if (this.pager.getCurrentItem() == 0)
			super.onBackPressed();
		else
			this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);

	}
	 public void clickEvent(View v) {
	        if (v.getId() == R.id.abs_layout_iv_menu) {
	            showPopup(v);
	        }

	       
	    }
	
	 public void showPopup(View v) {
		    PopupMenu popup = new PopupMenu(Paginador.this, v);
		    MenuInflater inflater = popup.getMenuInflater();
		    inflater.inflate(R.menu.popup, popup.getMenu());
		    int positionOfMenuItem = 0; // or whatever...
		    MenuItem item = popup.getMenu().getItem(positionOfMenuItem);
		    SpannableString s = new SpannableString(getResources().getString(R.string.action_settings));
		    s.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.rojo_logo)), 0, s.length(), 0);
		    item.setTitle(s);
		    positionOfMenuItem = 1; // or whatever...
		    item = popup.getMenu().getItem(positionOfMenuItem);
		    SpannableString s2 = new SpannableString(getResources().getString(R.string.action_cuenta));
		    s2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.rojo_logo)), 0, s2.length(), 0);
		    item.setTitle(s2);
		    positionOfMenuItem = 2; // or whatever...
		    item = popup.getMenu().getItem(positionOfMenuItem);
		    SpannableString s3 = new SpannableString(getResources().getString(R.string.action_help));
		    s3.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.rojo_logo)), 0, s3.length(), 0);
		    item.setTitle(s3);
		    
		    
		    
		    
		    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					 switch (item.getItemId()) {
					 
				case R.id.configuracion:
					Intent i = new Intent(Paginador.this, UserSettingActivity.class);
					startActivityForResult(i, RESULT_SETTINGS);
					return true;

				case R.id.cuenta:
					Intent intentManually = new Intent(Paginador.this, MitaxiRegisterManuallyActivity.class);
    				intentManually.putExtra("origen", "menu");
    				startActivity(intentManually);
    				overridePendingTransition(0,0);
					return true;

				}
					 return false;
				}
			});
		    
		    popup.show();
		}
	 
	
	 @Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			switch (requestCode) {
			case RESULT_SETTINGS:
				showUserSettings();
				break;
			}
		}
	 
	 private void showUserSettings() {
			SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
			StringBuilder builder = new StringBuilder();
			builder.append("\n Send report:"+ sharedPrefs.getBoolean("prefSendReport", true));
			builder.append("\n Sync Frequency: "+ sharedPrefs.getString("prefSyncFrequency", "NULL"));
			builder.append("\n Sync FrequencyMensajes: "+ sharedPrefs.getString("prefSyncFrequencyParanoia", "NULL"));
		}
		
}
