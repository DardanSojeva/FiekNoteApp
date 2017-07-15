

package fiek.ds.android.fieknote.async.notes;

import java.util.List;
import fiek.ds.android.fieknote.db.DbHelper;
import fiek.ds.android.fieknote.models.Category;
import fiek.ds.android.fieknote.models.Note;


public class NoteProcessorCategorize extends NoteProcessor {

    Category category;


    public NoteProcessorCategorize(List<Note> notes, Category category) {
        super(notes);
        this.category = category;
    }


    @Override
    protected void processNote(Note note) {
        note.setCategory(category);
        DbHelper.getInstance().updateNote(note, false);
    }
}
