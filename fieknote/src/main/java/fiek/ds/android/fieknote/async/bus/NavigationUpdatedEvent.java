package fiek.ds.android.fieknote.async.bus;

import android.util.Log;
import fiek.ds.android.fieknote.utils.Constants;



public class NavigationUpdatedEvent {

	public final Object navigationItem;


	public NavigationUpdatedEvent(Object navigationItem) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.navigationItem = navigationItem;
	}
}
