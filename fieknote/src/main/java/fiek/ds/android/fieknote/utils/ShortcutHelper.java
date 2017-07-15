

package fiek.ds.android.fieknote.utils;

import android.content.Context;
import android.content.Intent;

import fiek.ds.android.fieknote.FiekNote;
import fiek.ds.android.fieknote.MainActivity;
import fiek.ds.android.fieknote.helpers.date.DateHelper;
import fiek.ds.android.fieknote.models.Note;


public class ShortcutHelper {


    /**
     * Adding shortcut on Home screen
     */
    public static void addShortcut(Context context, Note note) {
        Intent shortcutIntent = new Intent(context, MainActivity.class);
        shortcutIntent.putExtra(Constants.INTENT_KEY, note.get_id());
        shortcutIntent.setAction(Constants.ACTION_SHORTCUT);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        String shortcutTitle = note.getTitle().length() > 0 ? note.getTitle() : DateHelper.getFormattedDate(note
				.getCreation(), FiekNote.getSharedPreferences().getBoolean(Constants
				.PREF_PRETTIFIED_DATES, true));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutTitle);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, fiek.ds.android.fieknote.R.drawable.ic_shortcut));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

        context.sendBroadcast(addIntent);
    }

    /**
     * Removes note shortcut from home launcher
     */
    public static void removeshortCut(Context context, Note note) {
        Intent shortcutIntent = new Intent(context, MainActivity.class);
        shortcutIntent.putExtra(Constants.INTENT_KEY, note.get_id());
        shortcutIntent.setAction(Constants.ACTION_SHORTCUT);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		String shortcutTitle = note.getTitle().length() > 0 ? note.getTitle() : DateHelper.getFormattedDate(note
				.getCreation(), FiekNote.getSharedPreferences().getBoolean(Constants
				.PREF_PRETTIFIED_DATES, true));

        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, shortcutTitle);

        addIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);
    }
}
