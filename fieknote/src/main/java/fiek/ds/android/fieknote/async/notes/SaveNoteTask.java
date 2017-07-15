

package fiek.ds.android.fieknote.async.notes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.util.List;
import fiek.ds.android.fieknote.FiekNote;
import fiek.ds.android.fieknote.db.DbHelper;
import fiek.ds.android.fieknote.models.Attachment;
import fiek.ds.android.fieknote.models.Note;
import fiek.ds.android.fieknote.models.listeners.OnNoteSaved;
import fiek.ds.android.fieknote.utils.Constants;
import fiek.ds.android.fieknote.utils.ReminderHelper;
import fiek.ds.android.fieknote.utils.StorageHelper;
import fiek.ds.android.fieknote.utils.date.DateUtils;


public class SaveNoteTask extends AsyncTask<Note, Void, Note> {

    private Context context;
    private boolean updateLastModification = true;
    private OnNoteSaved mOnNoteSaved;


    public SaveNoteTask(boolean updateLastModification) {
        this(null, updateLastModification);
    }


    public SaveNoteTask(OnNoteSaved mOnNoteSaved, boolean updateLastModification) {
        super();
        this.context = FiekNote.getAppContext();
        this.mOnNoteSaved = mOnNoteSaved;
        this.updateLastModification = updateLastModification;
    }


    @Override
    protected Note doInBackground(Note... params) {
        Note note = params[0];
        purgeRemovedAttachments(note);
		boolean reminderMustBeSet = DateUtils.isFuture(note.getAlarm());
        if (reminderMustBeSet) {
            note.setReminderFired(false);
        }
        note = DbHelper.getInstance().updateNote(note, updateLastModification);
		if (reminderMustBeSet) {
			ReminderHelper.addReminder(context, note);
		}
        return note;
    }


    private void purgeRemovedAttachments(Note note) {
        List<Attachment> deletedAttachments = note.getAttachmentsListOld();
        for (Attachment attachment : note.getAttachmentsList()) {
            if (attachment.getId() != null) {
                // Workaround to prevent deleting attachments if instance is changed (app restart)
                if (deletedAttachments.indexOf(attachment) == -1) {
                    attachment = getFixedAttachmentInstance(deletedAttachments, attachment);
                }
                deletedAttachments.remove(attachment);
            }
        }
        // Remove from database deleted attachments
        for (Attachment deletedAttachment : deletedAttachments) {
            StorageHelper.delete(context, deletedAttachment.getUri().getPath());
            Log.d(Constants.TAG, "Removed attachment " + deletedAttachment.getUri());
        }
    }


    private Attachment getFixedAttachmentInstance(List<Attachment> deletedAttachments, Attachment attachment) {
        for (Attachment deletedAttachment : deletedAttachments) {
            if (deletedAttachment.getId() == attachment.getId()) return deletedAttachment;
        }
        return attachment;
    }


    @Override
    protected void onPostExecute(Note note) {
        super.onPostExecute(note);
        if (this.mOnNoteSaved != null) {
            mOnNoteSaved.onNoteSaved(note);
        }
    }
}
