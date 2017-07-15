
package fiek.ds.android.fieknote.async.bus;

import android.service.notification.StatusBarNotification;
import android.util.Log;
import fiek.ds.android.fieknote.utils.Constants;


public class NotificationRemovedEvent {

	public StatusBarNotification statusBarNotification;


	public NotificationRemovedEvent(StatusBarNotification statusBarNotification) {
		Log.d(Constants.TAG, this.getClass().getName());
		this.statusBarNotification = statusBarNotification;
	}
}
