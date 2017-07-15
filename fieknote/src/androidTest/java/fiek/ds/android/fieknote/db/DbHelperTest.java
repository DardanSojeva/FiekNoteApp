
package fiek.ds.android.fieknote.db;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

import fiek.ds.android.fieknote.models.Note;


public class DbHelperTest extends AndroidTestCase {

	private RenamingDelegatingContext context;
	private DbHelper dbHelper;


	@Override
	public void setUp() throws Exception {
		super.setUp();
		context = new RenamingDelegatingContext(getContext(), "test_");
		dbHelper = DbHelper.getInstance(context);
	}


	@Override
	protected void tearDown() throws Exception {
		context.deleteDatabase(dbHelper.getDatabaseName());
		super.tearDown();
	}


	public void testGetNotesByTag() {
		Note note = new Note();
		note.setTitle("title with #tag inside");
		note.setContent("useless content");
		dbHelper.updateNote(note, true);
		Note note1 = new Note();
		note1.setTitle("simple title");
		note1.setContent("content with #tag");
		dbHelper.updateNote(note1, true);
		Note note2 = new Note();
		note2.setTitle("title without tags in it");
		note2.setContent("some \n #tagged content");
		dbHelper.updateNote(note2, true);
		assertEquals(dbHelper.getNotesByTag("#tag").size(), 2);
		assertEquals(dbHelper.getNotesByTag("#tagged").size(), 1);
	}
}
