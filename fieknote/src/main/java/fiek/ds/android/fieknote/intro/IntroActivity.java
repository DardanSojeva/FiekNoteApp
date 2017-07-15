package fiek.ds.android.fieknote.intro;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro2;

import fiek.ds.android.fieknote.FiekNote;
import fiek.ds.android.fieknote.utils.Constants;


public class IntroActivity extends AppIntro2 {

	@Override
	public void init(Bundle savedInstanceState) {
		addSlide(new IntroSlide1(), getApplicationContext());
		addSlide(new IntroSlide2(), getApplicationContext());
		addSlide(new IntroSlide3(), getApplicationContext());
		addSlide(new IntroSlide4(), getApplicationContext());
		addSlide(new IntroSlide5(), getApplicationContext());
		addSlide(new IntroSlide6(), getApplicationContext());
	}


	@Override
	public void onDonePressed() {
		FiekNote.getAppContext().getSharedPreferences(Constants.PREFS_NAME, Context.MODE_MULTI_PROCESS).edit()
				.putBoolean(Constants.PREF_TOUR_COMPLETE, true).apply();
		finish();
	}


	public static boolean mustRun() {
		return !FiekNote.isDebugBuild() && !FiekNote.getAppContext().getSharedPreferences(Constants.PREFS_NAME,
				Context.MODE_MULTI_PROCESS).getBoolean(Constants.PREF_TOUR_COMPLETE, false);
	}


	@Override
	public void onBackPressed() {
		// Does nothing, you HAVE TO SEE THE INTRO!
	}

	public static class IntroSlide1 extends Fragment {
	}

	public static class IntroSlide2 extends Fragment {
	}

	public static class IntroSlide3 extends Fragment {
	}

	public  static class IntroSlide4 extends Fragment {
	}

	public static class IntroSlide5 extends Fragment {
	}

	public static class IntroSlide6 extends Fragment {
	}
}