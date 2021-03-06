
package fiek.ds.android.fieknote;

import android.test.ActivityInstrumentationTestCase2;
import com.neopixl.pixlui.components.textview.TextView;
import com.robotium.solo.Solo;
import com.robotium.solo.Timeout;


public class NoteWithReminderAndSketch extends ActivityInstrumentationTestCase2<MainActivity> {

    private Solo solo;


    public NoteWithReminderAndSketch() {
        super(MainActivity.class);
    }


    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation());
        getActivity();
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }


    public void testRun() {
        //Wait for activity: 'MainActivity'
        solo.waitForActivity(MainActivity.class, 4000);
        //Set default small timeout to 30522 milliseconds
        Timeout.setSmallTimeout(30522);
        //Click on ImageView
        solo.clickLongOnView(solo.getView(fiek.ds.android.fieknote.R.id.fab_expand_menu_button));
        //Click on LinearLayout
        solo.clickOnView(solo.getView(fiek.ds.android.fieknote.R.id.reminder_layout));
        // Check that no reminder is set
        assertEquals(((TextView) solo.getView(fiek.ds.android.fieknote.R.id.datetime)).getText().toString().length()
                , 0);
        //Wait for dialog
        solo.waitForDialogToOpen(5000);
        //Click on Done
        solo.clickOnView(solo.getView(fiek.ds.android.fieknote.R.id.done));
        //Wait for dialog
        solo.waitForDialogToOpen(5000);
        //Click on Done
        solo.clickOnView(solo.getView(fiek.ds.android.fieknote.R.id.done_button));
        //Wait for dialog
        solo.waitForDialogToOpen(5000);
        //Click on Done in recurrent reminders fragment
        solo.clickOnView(solo.getView(fiek.ds.android.fieknote.R.id.done));
        // Check if a reminder is present
        assertNotNull(((TextView) solo.getView(fiek.ds.android.fieknote.R.id.datetime)).getText().toString());
        //Click on Empty Text View
        solo.clickOnView(solo.getView(fiek.ds.android.fieknote.R.id.menu_attachment));
        //Click on Sketch
        solo.clickOnView(solo.getView(fiek.ds.android.fieknote.R.id.sketch));
        // Draw a line
        solo.drag(222.79372F, 470.5643F, 590.6923F, 1048.4539F, 40);
        //Click on ImageView
        solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
        //Wait for thumbnail
        solo.sleep(1000);
        //Click on RelativeLayout
        solo.clickInList(1, 0);
        //Wait for activity: 'it.feio.android.fieknote.GalleryActivity'
        assertTrue("it.feio.android.fieknote.GalleryActivity is not found!", solo.waitForActivity(GalleryActivity.class));
        //Click on ImageView
        solo.clickOnView(solo.getView(android.widget.ImageView.class, 0));
        //Click on ImageView
        solo.clickOnView(solo.getView(android.widget.ImageView.class, 0));
        //Click on ImageView
        solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
        //Click on ImageView
        solo.clickOnView(solo.getView(android.widget.ImageButton.class, 0));
    }
}
