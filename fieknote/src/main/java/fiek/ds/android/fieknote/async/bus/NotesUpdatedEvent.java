package fiek.ds.android.fieknote.async.bus;

import android.util.Log;
import fiek.ds.android.fieknote.utils.Constants;



public class NotesUpdatedEvent {

	public NotesUpdatedEvent() {
		Log.d(Constants.TAG, this.getClass().getName());
	}
}
