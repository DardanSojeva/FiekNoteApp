
package fiek.ds.android.fieknote;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Arrays;
import java.util.Calendar;

import fiek.ds.android.fieknote.async.notes.SaveNoteTask;
import fiek.ds.android.fieknote.helpers.date.DateHelper;
import fiek.ds.android.fieknote.models.Note;
import fiek.ds.android.fieknote.models.listeners.OnReminderPickedListener;
import fiek.ds.android.fieknote.utils.Constants;
import fiek.ds.android.fieknote.utils.ReminderHelper;
import fiek.ds.android.fieknote.utils.date.DateUtils;
import fiek.ds.android.fieknote.utils.date.ReminderPickers;


public class SnoozeActivity extends ActionBarActivity implements OnReminderPickedListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Note note;
    private Note[] notes;
    private ReminderPickers onDateSetListener;
    private ReminderPickers onTimeSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getParcelableExtra(Constants.INTENT_NOTE) != null) {
            note = getIntent().getParcelableExtra(Constants.INTENT_NOTE);
            manageNotification(getSharedPreferences(Constants.PREFS_NAME, MODE_MULTI_PROCESS));
        } else {
            Object[] notesObjs = (Object[]) getIntent().getExtras().get(Constants.INTENT_NOTE);
            notes = Arrays.copyOf(notesObjs, notesObjs.length, Note[].class);
            postpone(getSharedPreferences(Constants.PREFS_NAME, MODE_MULTI_PROCESS), DateUtils.getNextMinute(), null);
        }
    }


    private void manageNotification(SharedPreferences prefs) {
        if (Constants.ACTION_DISMISS.equals(getIntent().getAction())) {
            setNextRecurrentReminder(note);
            finish();
        } else if (Constants.ACTION_SNOOZE.equals(getIntent().getAction())) {
            String snoozeDelay = prefs.getString("settings_notification_snooze_delay", Constants.PREF_SNOOZE_DEFAULT);
            long newReminder = Calendar.getInstance().getTimeInMillis() + Integer.parseInt(snoozeDelay) * 60 * 1000;
            updateNoteReminder(newReminder, note);
            finish();
        } else if (Constants.ACTION_POSTPONE.equals(getIntent().getAction())) {
            postpone(prefs, Long.parseLong(note.getAlarm()), note.getRecurrenceRule());
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Constants.INTENT_KEY, note.get_id());
            intent.setAction(Constants.ACTION_NOTIFICATION_CLICK);
            startActivity(intent);
            finish();
        }
        removeNotification(note);
    }


    private void postpone(SharedPreferences prefs, Long alarm, String recurrenceRule) {
        int pickerType = prefs.getBoolean("settings_simple_calendar", false) ? ReminderPickers.TYPE_AOSP :
				ReminderPickers.TYPE_GOOGLE;
        ReminderPickers reminderPicker = new ReminderPickers(this, this, pickerType);
        reminderPicker.pick(alarm, recurrenceRule);
        onDateSetListener = reminderPicker;
        onTimeSetListener = reminderPicker;
    }


    private void removeNotification(Note note) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(String.valueOf(note.get_id()), 0);
    }


    @Override
    public void onReminderPicked(long reminder) {
        if (this.note != null) {
            this.note.setAlarm(reminder);
        } else {
            for (Note note : this.notes) {
                note.setAlarm(reminder);
            }
        }
    }

    @Override
    public void onRecurrenceReminderPicked(String recurrenceRule) {
        if (this.note != null) {
            this.note.setRecurrenceRule(recurrenceRule);
            setNextRecurrentReminder(this.note);
        } else {
            for (Note note : this.notes) {
                note.setRecurrenceRule(recurrenceRule);
                setNextRecurrentReminder(note);
            }
            setResult(RESULT_OK, getIntent());
        }
        finish();
    }


    public static void setNextRecurrentReminder(Note note) {
        if (!TextUtils.isEmpty(note.getRecurrenceRule())) {
            long nextReminder = DateHelper.nextReminderFromRecurrenceRule(Long.parseLong(note.getAlarm()), note
					.getRecurrenceRule());
            if (nextReminder > 0) {
                updateNoteReminder(nextReminder, note, true);
            }
        } else {
            new SaveNoteTask(false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, note);
        }
    }


    private static void updateNoteReminder(long reminder, Note note) {
        updateNoteReminder(reminder, note, false);
    }


    private static void updateNoteReminder(long reminder, Note noteToUpdate, boolean updateNote) {
        if (updateNote) {
            noteToUpdate.setAlarm(reminder);
            new SaveNoteTask(false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, noteToUpdate);
        } else {
            ReminderHelper.addReminder(FiekNote.getAppContext(), noteToUpdate, reminder);
			ReminderHelper.showReminderMessage(noteToUpdate.getAlarm());
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        onDateSetListener.onDateSet(view, year, monthOfYear, dayOfMonth);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        onTimeSetListener.onTimeSet(view, hourOfDay, minute);
    }
}
