
package fiek.ds.android;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import fiek.ds.android.fieknote.MainActivity;

import static junit.framework.Assert.assertNotNull;


public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {


    public MainActivityTest() {
        super(MainActivity.class);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @MediumTest
    public void testRemoveExistentDetailFragment() {
        MainActivity mainActivity = getActivity();
        assertNotNull(mainActivity);
        assertSame(MainActivity.class, mainActivity.getClass());
        FragmentManager fm = mainActivity.getSupportFragmentManager();
        Fragment f = new TestFragment("tf1");
        fm.beginTransaction().add(f, "tf1").commit();
    }

    public MainActivity getActivity() {
        if (super.getActivity().getClass().isAssignableFrom(MainActivity.class)) {
            return super.getActivity();
        }
        return null;
    }
}


@SuppressLint("ValidFragment")
class TestFragment extends Fragment {
    private String tag;

    public TestFragment(String tag) {
		this.tag = tag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        assertNotNull(getActivity().getSupportFragmentManager().findFragmentByTag(this.tag));
    }
}