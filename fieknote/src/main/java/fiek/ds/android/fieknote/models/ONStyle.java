

package fiek.ds.android.fieknote.models;

import android.view.Gravity;
import android.widget.LinearLayout.LayoutParams;
import de.keyboardsurfer.android.widget.crouton.Configuration;
import de.keyboardsurfer.android.widget.crouton.Style;


public class ONStyle {

    public static final int DURATION_INFINITE = -1;
    public static final int DURATION_SHORT = 950;
    public static final int DURATION_MEDIUM = 1650;
    public static final int DURATION_LONG = 2300;

    public static final Configuration CONFIGURATION;
    public static final Style ALERT;
    public static final Style WARN;
    public static final Style CONFIRM;
    public static final Style INFO;

    public static final int ALERT_COLOR = fiek.ds.android.fieknote.R.color.alert;
    public static final int WARN_COLOR = fiek.ds.android.fieknote.R.color.warning;
    public static final int CONFIRM_COLOR = fiek.ds.android.fieknote.R.color.confirm;
    public static final int INFO_COLOR = fiek.ds.android.fieknote.R.color.info;


    static {
        CONFIGURATION = new Configuration.Builder()
                .setDuration(DURATION_SHORT)
                .setInAnimation(fiek.ds.android.fieknote.R.animator.fade_in_support)
                .setOutAnimation(fiek.ds.android.fieknote.R.animator.fade_out_support)
                .build();
        ALERT = new Style.Builder()
                .setBackgroundColor(ALERT_COLOR)
                .setHeight(LayoutParams.MATCH_PARENT)
                .setGravity(Gravity.CENTER)
                .setTextAppearance(fiek.ds.android.fieknote.R.style.crouton_text)
                .setConfiguration(CONFIGURATION)
                .build();
        WARN = new Style.Builder()
                .setBackgroundColor(WARN_COLOR)
                .setHeight(LayoutParams.MATCH_PARENT)
                .setGravity(Gravity.CENTER)
                .setTextAppearance(fiek.ds.android.fieknote.R.style.crouton_text)
                .setConfiguration(CONFIGURATION)
                .build();
        CONFIRM = new Style.Builder()
                .setBackgroundColor(CONFIRM_COLOR)
                .setHeight(LayoutParams.MATCH_PARENT)
                .setGravity(Gravity.CENTER)
                .setTextAppearance(fiek.ds.android.fieknote.R.style.crouton_text)
                .setConfiguration(CONFIGURATION)
                .build();
        INFO = new Style.Builder()
                .setBackgroundColor(INFO_COLOR)
                .setHeight(LayoutParams.MATCH_PARENT)
                .setGravity(Gravity.CENTER)
                .setTextAppearance(fiek.ds.android.fieknote.R.style.crouton_text)
                .setConfiguration(CONFIGURATION)
                .build();
    }
}