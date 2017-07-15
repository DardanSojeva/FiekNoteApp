package fiek.ds.android.fieknote.async.bus;

import android.util.Log;
import fiek.ds.android.fieknote.utils.Constants;


public class NotesMergeEvent {

	public final boolean keepMergedNotes;


	public NotesMergeEvent(boolean keepMergedNotes) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.keepMergedNotes = keepMergedNotes;
	}
}
