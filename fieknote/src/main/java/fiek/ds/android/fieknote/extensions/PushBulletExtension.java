

package fiek.ds.android.fieknote.extensions;

import android.util.Log;
import com.pushbullet.android.extension.MessagingExtension;
import de.greenrobot.event.EventBus;
import fiek.ds.android.fieknote.async.bus.PushbulletReplyEvent;
import fiek.ds.android.fieknote.utils.Constants;


public class PushBulletExtension extends MessagingExtension {

    private static final String TAG = "PushBulletExtension";


    @Override
    protected void onMessageReceived(final String conversationIden, final String message) {
        Log.i(Constants.TAG, "Pushbullet MessagingExtension: onMessageReceived(" + conversationIden + ", " + message
                + ")");
        EventBus.getDefault().post(new PushbulletReplyEvent(message));
//        MainActivity runningMainActivity = MainActivity.getInstance();
//        if (runningMainActivity != null && !runningMainActivity.isFinishing()) {
//            runningMainActivity.onPushBulletReply(message);
//        }
    }


    @Override
    protected void onConversationDismissed(final String conversationIden) {
        Log.i(Constants.TAG, "Pushbullet MessagingExtension: onConversationDismissed(" + conversationIden + ")");
    }
}
