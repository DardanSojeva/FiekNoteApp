

package fiek.ds.android.fieknote.async.notes;

import java.util.List;
import fiek.ds.android.fieknote.FiekNote;
import fiek.ds.android.fieknote.db.DbHelper;
import fiek.ds.android.fieknote.models.Note;
import fiek.ds.android.fieknote.utils.ReminderHelper;
import fiek.ds.android.fieknote.utils.ShortcutHelper;


public class NoteProcessorTrash extends NoteProcessor {

    boolean trash;


    public NoteProcessorTrash(List<Note> notes, boolean trash) {
        super(notes);
        this.trash = trash;
    }


    @Override
    protected void processNote(Note note) {
        if (trash) {
            ShortcutHelper.removeshortCut(FiekNote.getAppContext(), note);
            ReminderHelper.removeReminder(FiekNote.getAppContext(), note);
        } else {
            ReminderHelper.addReminder(FiekNote.getAppContext(), note);
        }
        DbHelper.getInstance().trashNote(note, trash);
    }
}
