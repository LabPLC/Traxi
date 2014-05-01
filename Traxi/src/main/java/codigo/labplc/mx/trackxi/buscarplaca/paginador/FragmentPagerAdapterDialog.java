package codigo.labplc.mx.trackxi.buscarplaca.paginador;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * @author amatellanes
 * 
 */
public class FragmentPagerAdapterDialog extends FragmentPagerAdapter {

	// List of fragments which are going to set in the view pager widget
	List<Fragment> fragments;

	/**
	 * Constructor
	 * 
	 * @param fm
	 *            interface for interacting with Fragment objects inside of an
	 *            Activity
	 */
	public FragmentPagerAdapterDialog(FragmentManager fm) {
		super(fm);
		this.fragments = new ArrayList<Fragment>();
	}

	/**
	 * Add a new fragment in the list.
	 * 
	 * @param fragment
	 *            a new fragment
	 */
	public void addFragment(Fragment fragment) {
		this.fragments.add(fragment);
	}

	@Override
	public Fragment getItem(int arg0) {
		return this.fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return this.fragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		int posicion = position +1;
		if(posicion==1){
			return "General";
		}else if(posicion==2){
			return "Detalles";
		}else if(posicion==3){
			return "M‡s";
		}
	
		return "falla";
	}
}
