

package fiek.ds.android.fieknote.async;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import java.util.List;
import fiek.ds.android.fieknote.BaseActivity;
import fiek.ds.android.fieknote.FiekNote;
import fiek.ds.android.fieknote.db.DbHelper;
import fiek.ds.android.fieknote.models.Note;
import fiek.ds.android.fieknote.utils.Constants;
import fiek.ds.android.fieknote.utils.ReminderHelper;


public class AlarmRestoreOnRebootService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(Constants.TAG, "System rebooted: service refreshing reminders");
        Context mContext = getApplicationContext();

        BaseActivity.notifyAppWidgets(mContext);

        List<Note> notes = DbHelper.getInstance().getNotesWithReminderNotFired();
        Log.d(Constants.TAG, "Found " + notes.size() + " reminders");
        for (Note note : notes) {
            ReminderHelper.addReminder(FiekNote.getAppContext(), note);
        }
        return Service.START_NOT_STICKY;
    }

}
