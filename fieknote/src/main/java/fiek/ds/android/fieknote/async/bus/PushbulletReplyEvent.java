package fiek.ds.android.fieknote.async.bus;

import android.util.Log;
import fiek.ds.android.fieknote.utils.Constants;


public class PushbulletReplyEvent {

	public String message;

	public PushbulletReplyEvent(String message) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.message = message;
	}
}
