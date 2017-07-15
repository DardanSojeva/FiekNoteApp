
package fiek.ds.android.fieknote.async.notes;

import android.os.AsyncTask;
import android.util.Log;
import java.lang.reflect.Method;
import java.util.ArrayList;
import de.greenrobot.event.EventBus;
import fiek.ds.android.fieknote.async.bus.NotesLoadedEvent;
import fiek.ds.android.fieknote.db.DbHelper;
import fiek.ds.android.fieknote.models.Note;
import fiek.ds.android.fieknote.utils.Constants;


public class NoteLoaderTask extends AsyncTask<Object, Void, ArrayList<Note>> {

	private static NoteLoaderTask instance;

	private NoteLoaderTask() {}


	public static NoteLoaderTask getInstance() {

		if (instance != null) {
			if (instance.getStatus() == Status.RUNNING && !instance.isCancelled()) {
				instance.cancel(true);
			} else if (instance.getStatus() == Status.PENDING) {
				return instance;
			}
		}

		instance = new NoteLoaderTask();
		return instance;
	}


	@Override
	protected ArrayList<Note> doInBackground(Object... params) {

		ArrayList<Note> notes = new ArrayList<>();
		String methodName = params[0].toString();
		Object methodArgs = params[1];
		DbHelper db = DbHelper.getInstance();

		// If null argument an empty list will be returned
		if (methodArgs == null) {
			return notes;
		}

		// Checks the argument class with reflection
		Class[] paramClass = new Class[]{methodArgs.getClass()};

		// Retrieves and calls the right method
		try {
			Method method = db.getClass().getDeclaredMethod(methodName, paramClass);
			notes = (ArrayList<Note>) method.invoke(db, paramClass[0].cast(methodArgs));
		} catch (Exception e) {
			Log.e(Constants.TAG, "Error retrieving notes", e);
		}

		return notes;
	}


	@Override
	protected void onPostExecute(ArrayList<Note> notes) {

		super.onPostExecute(notes);
		EventBus.getDefault().post(new NotesLoadedEvent(notes));
	}
}