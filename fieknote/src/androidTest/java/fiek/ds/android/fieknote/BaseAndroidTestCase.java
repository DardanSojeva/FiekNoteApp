

package fiek.ds.android.fieknote;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import fiek.ds.android.fieknote.db.DbHelper;


public class BaseAndroidTestCase extends AndroidTestCase {

	private RenamingDelegatingContext context;


	@Override
	public void setUp() throws Exception {
		super.setUp();
		context = new RenamingDelegatingContext(getContext(), "test_");
	}


	@Override
	protected void tearDown() throws Exception {
		context.deleteDatabase(DbHelper.getInstance().getDatabaseName());
		super.tearDown();
	}

}
