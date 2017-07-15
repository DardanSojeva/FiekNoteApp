package fiek.ds.android.fieknote;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends ActionBarActivity {

	@Bind(fiek.ds.android.fieknote.R.id.toolbar) Toolbar toolbar;
	@Bind(fiek.ds.android.fieknote.R.id.crouton_handle) ViewGroup croutonViewContainer;

    private List<Fragment> backStack = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fiek.ds.android.fieknote.R.layout.activity_settings);
		ButterKnife.bind(this);
        initUI();
        getFragmentManager().beginTransaction().replace(fiek.ds.android.fieknote.R.id.content_frame, new SettingsFragment()).commit();
    }


    void initUI() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    void switchToScreen(String key) {
        SettingsFragment sf = new SettingsFragment();
        Bundle b = new Bundle();
        b.putString(SettingsFragment.XML_NAME, key);
        sf.setArguments(b);
        backStack.add(getFragmentManager().findFragmentById(fiek.ds.android.fieknote.R.id.content_frame));
        replaceFragment(sf);
    }


    private void replaceFragment(Fragment sf) {
        getFragmentManager().beginTransaction().setCustomAnimations(fiek.ds.android.fieknote.R.animator.fade_in, fiek.ds.android.fieknote.R.animator.fade_out,
                fiek.ds.android.fieknote.R.animator.fade_in, fiek.ds.android.fieknote.R.animator.fade_out).replace(fiek.ds.android.fieknote.R.id.content_frame, sf).commit();
    }


    @Override
    public void onBackPressed() {
        if (backStack.size() > 0) {
            replaceFragment(backStack.remove(backStack.size() - 1));
        } else {
            super.onBackPressed();
        }
    }


	public void showMessage(int messageId, Style style) {
		showMessage(getString(messageId), style);
	}


	public void showMessage(String message, Style style) {
		// ViewGroup used to show Crouton keeping compatibility with the new Toolbar
		Crouton.makeText(this, message, style, croutonViewContainer).show();
	}
}
