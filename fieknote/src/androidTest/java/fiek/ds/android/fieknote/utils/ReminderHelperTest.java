

package fiek.ds.android.fieknote.utils;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;

import fiek.ds.android.fieknote.models.Note;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ReminderHelperTest {

	@Test
	public void testGetRequestCode() {
		Long now = Calendar.getInstance().getTimeInMillis();
		Note note = new Note();
		note.setAlarm(now);
		int requestCode = ReminderHelper.getRequestCode(note);
		int requestCode2 = ReminderHelper.getRequestCode(note);
		assertEquals(requestCode, requestCode2);
		assertTrue(String.valueOf(now).startsWith(String.valueOf(requestCode)));
	}


	public void testAddReminder() {
		Note note = buildNote();
		ReminderHelper.addReminder(InstrumentationRegistry.getTargetContext(), note);
		boolean reminderActive = ReminderHelper.checkReminder(InstrumentationRegistry.getTargetContext(), note);
		assertTrue(reminderActive);
	}


	public void testRemoveReminder() {
		Note note = buildNote();
		ReminderHelper.addReminder(InstrumentationRegistry.getTargetContext(), note);
		boolean reminderActive = ReminderHelper.checkReminder(InstrumentationRegistry.getTargetContext(), note);
		ReminderHelper.removeReminder(InstrumentationRegistry.getTargetContext(), note);
		boolean reminderRemoved = ReminderHelper.checkReminder(InstrumentationRegistry.getTargetContext(), note);
		assertTrue(reminderActive);
		assertFalse(reminderRemoved);
	}





	private Note buildNote() {
		Long now = Calendar.getInstance().getTimeInMillis();
		Note note = new Note();
		note.setCreation(now);
		note.setAlarm(now);
		return note;
	}
}
