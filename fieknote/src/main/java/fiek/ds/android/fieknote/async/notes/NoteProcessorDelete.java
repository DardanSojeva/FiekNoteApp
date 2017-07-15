

package fiek.ds.android.fieknote.async.notes;

import java.util.List;
import fiek.ds.android.fieknote.FiekNote;
import fiek.ds.android.fieknote.db.DbHelper;
import fiek.ds.android.fieknote.models.Attachment;
import fiek.ds.android.fieknote.models.Note;
import fiek.ds.android.fieknote.utils.StorageHelper;


public class NoteProcessorDelete extends NoteProcessor {


	private final boolean keepAttachments;


	public NoteProcessorDelete(List<Note> notes) {
		this(notes, false);
	}


	public NoteProcessorDelete(List<Note> notes, boolean keepAttachments) {
		super(notes);
		this.keepAttachments = keepAttachments;
	}


	@Override
	protected void processNote(Note note) {
		DbHelper db = DbHelper.getInstance();
		if (db.deleteNote(note) && !keepAttachments) {
			for (Attachment mAttachment : note.getAttachmentsList()) {
				StorageHelper.deleteExternalStoragePrivateFile(FiekNote.getAppContext(), mAttachment.getUri()
						.getLastPathSegment());
			}
		}
	}
}
