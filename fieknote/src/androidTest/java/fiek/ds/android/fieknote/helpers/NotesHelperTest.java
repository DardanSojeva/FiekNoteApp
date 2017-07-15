

package fiek.ds.android.fieknote.helpers;

import android.support.test.runner.AndroidJUnit4;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import fiek.ds.android.fieknote.models.Note;
import fiek.ds.android.fieknote.utils.Constants;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class NotesHelperTest {

	@Test
	public void mergeNotes() {

		int notesNumber = 3;
		List<Note> notes = new ArrayList<>();
		for (int i = 0; i < notesNumber; i++) {
			Note note = new Note();
			note.setTitle("Merged note " + i + " title");
			note.setContent("Merged note " + i + " content");
			notes.add(note);
		}
		Note mergeNote = NotesHelper.mergeNotes(notes, false);

		assertNotNull(mergeNote);
		assertTrue(mergeNote.getTitle().equals("Merged note 0 title"));
		assertTrue(mergeNote.getContent().contains("Merged note 0 content"));
		assertTrue(mergeNote.getContent().contains("Merged note 1 content"));
		assertTrue(mergeNote.getContent().contains("Merged note 2 content"));
		assertEquals(StringUtils.countMatches(mergeNote.getContent(), Constants.MERGED_NOTES_SEPARATOR), 2);
	}
}
