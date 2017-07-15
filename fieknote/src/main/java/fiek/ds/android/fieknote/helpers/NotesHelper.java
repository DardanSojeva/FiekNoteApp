package fiek.ds.android.fieknote.helpers;

import android.text.TextUtils;

import fiek.ds.android.fieknote.FiekNote;
import fiek.ds.android.fieknote.models.Attachment;
import fiek.ds.android.fieknote.models.Category;
import fiek.ds.android.fieknote.models.Note;
import fiek.ds.android.fieknote.utils.Constants;
import fiek.ds.android.fieknote.utils.StorageHelper;
import org.apache.commons.lang.ObjectUtils;

import java.util.ArrayList;
import java.util.List;


public class NotesHelper {

    public static StringBuilder appendContent(Note note, StringBuilder content) {
        if (content.length() > 0
                && (!TextUtils.isEmpty(note.getTitle()) || !TextUtils.isEmpty(note.getContent()))) {
            content.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"))
                    .append(Constants.MERGED_NOTES_SEPARATOR).append(System.getProperty("line.separator"))
                    .append(System.getProperty("line.separator"));
        }
        if (!TextUtils.isEmpty(note.getTitle())) {
            content.append(note.getTitle());
        }
        if (!TextUtils.isEmpty(note.getTitle()) && !TextUtils.isEmpty(note.getContent())) {
            content.append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        }
        if (!TextUtils.isEmpty(note.getContent())) {
            content.append(note.getContent());
        }
        return content;
    }

    public static void addAttachments(boolean keepMergedNotes, Note note, ArrayList<Attachment> attachments) {
        if (keepMergedNotes) {
            for (Attachment attachment : note.getAttachmentsList()) {
                attachments.add(StorageHelper.createAttachmentFromUri(FiekNote.getAppContext(), attachment.getUri
                        ()));
            }
        } else {
            attachments.addAll(note.getAttachmentsList());
        }
    }

	public static Note mergeNotes(List<Note> notes, boolean keepMergedNotes) {
		Note mergedNote = null;
		boolean locked = false;
		StringBuilder content = new StringBuilder();
		ArrayList<Attachment> attachments = new ArrayList<Attachment>();
		Category category = null;
		String reminder = null;
		String reminderRecurrenceRule = null;
		Double latitude = null, longitude = null;

		for (Note note : notes) {

			if (mergedNote == null) {
				mergedNote = new Note();
				mergedNote.setTitle(note.getTitle());
				content.append(note.getContent());
			} else {
                content = appendContent(note, content);
			}

			locked = locked || note.isLocked();

			category = (Category) ObjectUtils.defaultIfNull(category, note.getCategory());

			String currentReminder = note.getAlarm();
			if (!TextUtils.isEmpty(currentReminder) && reminder == null) {
				reminder = currentReminder;
				reminderRecurrenceRule = note.getRecurrenceRule();
			}

			latitude = (Double) ObjectUtils.defaultIfNull(latitude, note.getLatitude());
			longitude = (Double) ObjectUtils.defaultIfNull(longitude, note.getLongitude());

			addAttachments(keepMergedNotes, note, attachments);
		}

        mergedNote.setContent(content.toString());
        mergedNote.setLocked(locked);
        mergedNote.setCategory(category);
        mergedNote.setAlarm(reminder);
        mergedNote.setRecurrenceRule(reminderRecurrenceRule);
        mergedNote.setLatitude(latitude);
        mergedNote.setLongitude(longitude);
        mergedNote.setAttachmentsList(attachments);

        return mergedNote;
	}

}
