

package fiek.ds.android.fieknote.async.notes;

import java.util.List;
import fiek.ds.android.fieknote.db.DbHelper;
import fiek.ds.android.fieknote.models.Note;


public class NoteProcessorArchive extends NoteProcessor {

    boolean archive;


    public NoteProcessorArchive(List<Note> notes, boolean archive) {
        super(notes);
        this.archive = archive;
    }


    @Override
    protected void processNote(Note note) {
        DbHelper.getInstance().archiveNote(note, archive);
    }
}
