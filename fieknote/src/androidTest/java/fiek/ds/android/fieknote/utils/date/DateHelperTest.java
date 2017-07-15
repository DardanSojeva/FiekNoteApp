

package fiek.ds.android.fieknote.utils.date;

import android.test.InstrumentationTestCase;

import org.junit.Assert;

import java.util.Calendar;

import fiek.ds.android.fieknote.helpers.date.DateHelper;


public class DateHelperTest extends InstrumentationTestCase {

	long TEN_MINUTES = 10 * 60 * 1000;


	public void testNextReminderFromRecurrenceRule() {
		long currentTime = Calendar.getInstance().getTimeInMillis();
		long reminder = Calendar.getInstance().getTimeInMillis() + TEN_MINUTES;

		// Daily test
		String rruleDaily = "FREQ=DAILY;COUNT=30;WKST=MO";
		long nextReminder = DateHelper.nextReminderFromRecurrenceRule(reminder, currentTime, rruleDaily);
		Assert.assertNotEquals(nextReminder, 0);
		Assert.assertEquals((nextReminder - reminder) / 60 / 60 / 1000, 24-1);

		// 3-Daily test
		String rruleDaily2 = "FREQ=DAILY;COUNT=30;INTERVAL=3";
		long nextReminder2 = DateHelper.nextReminderFromRecurrenceRule(reminder, currentTime, rruleDaily2);
		Assert.assertNotEquals(nextReminder2, 0);
		Assert.assertEquals((nextReminder2 - reminder) / 60 / 60 / 1000, 3*24 - 1);


	}

}
