
package fiek.ds.android.fieknote;

import android.support.v7.widget.Toolbar;


public class AboutActivity extends BaseActivity {
/*
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(fiek.ds.android.fieknote.R.layout.activity_about);

        WebView webview = (WebView) findViewById(fiek.ds.android.fieknote.R.id.webview);
        webview.loadUrl("file:///android_asset/html/about.html");

        initUI();
    }
*/

	@Override
	public void onStart() {
		((FiekNote)getApplication()).getAnalyticsHelper().trackScreenView(getClass().getName());
		super.onStart();
	}


    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }


    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(fiek.ds.android.fieknote.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        assert toolbar != null;
        toolbar.setNavigationOnClickListener(v -> onNavigateUp());
    }

}
