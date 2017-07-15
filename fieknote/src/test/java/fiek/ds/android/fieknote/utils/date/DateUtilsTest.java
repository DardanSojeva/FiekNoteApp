

package fiek.ds.android.fieknote.utils.date;

import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Locale;


public class DateUtilsTest {

	@Test
	public void prettyTime() {
		long now = Calendar.getInstance().getTimeInMillis();

		String prettyTime = DateUtils.prettyTime(now, Locale.ENGLISH);
		Assert.assertEquals(prettyTime.toLowerCase(), "moments ago");

		prettyTime = DateUtils.prettyTime(now + 10 * 60 * 1000, Locale.ENGLISH);
		Assert.assertEquals(prettyTime.toLowerCase(), "10 minutes from now");

		prettyTime = DateUtils.prettyTime(now + 24 * 60 * 60 * 1000, Locale.ITALIAN);
		Assert.assertEquals(prettyTime.toLowerCase(), "Pas 24 oreve");

		prettyTime = DateUtils.prettyTime(now + 25 * 60 * 60 * 1000, Locale.ITALIAN);
		Assert.assertEquals(prettyTime.toLowerCase(), "Pas 1 dite");

		prettyTime = DateUtils.prettyTime(null, Locale.JAPANESE);
		Assert.assertNotNull(prettyTime.toLowerCase());
		Assert.assertEquals(prettyTime.toLowerCase().length(), 0);
	}

}
