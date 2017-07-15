package fiek.ds.android.fieknote;

import android.support.v4.app.Fragment;
import com.squareup.leakcanary.RefWatcher;


public class BaseFragment extends Fragment {


	@Override
	public void onStart() {
		super.onStart();
		((FiekNote)getActivity().getApplication()).getAnalyticsHelper().trackScreenView(getClass().getName());
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
		RefWatcher refWatcher = FiekNote.getRefWatcher();
		refWatcher.watch(this);
	}

}
