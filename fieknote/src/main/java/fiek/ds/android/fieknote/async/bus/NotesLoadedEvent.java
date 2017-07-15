package fiek.ds.android.fieknote.async.bus;

import android.util.Log;
import fiek.ds.android.fieknote.models.Note;
import fiek.ds.android.fieknote.utils.Constants;

import java.util.ArrayList;


public class NotesLoadedEvent {

	public ArrayList<Note> notes;


	public NotesLoadedEvent(ArrayList<Note> notes) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.notes = notes;
	}
}
