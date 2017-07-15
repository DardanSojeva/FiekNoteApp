

package fiek.ds.android.fieknote.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;


public class IntentChecker {

    /**
     * Checks intent and features availability
     *
     * @param ctx
     * @param intent
     * @param features
     * @return
     */
    public static boolean isAvailable(Context ctx, Intent intent, String[] features) {
        boolean res = true;
        final PackageManager mgr = ctx.getPackageManager();
        // Intent resolver
        List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        res = list.size() > 0;
        // Features
        if (features != null) {
            for (String feature : features) {
                res = res && mgr.hasSystemFeature(feature);
            }
        }
        return res;
    }


	/**
	 * Checks Intent's action
	 *
	 * @param i      Intent to ckeck
	 * @param action Action to compare with
	 * @return
	 */
	public static boolean checkAction(Intent i, String action) {
		return action.equals(i.getAction());
	}


	/**
	 * Checks Intent's actions
	 *
	 * @param i      Intent to ckeck
	 * @param actions Multiple actions to compare with
	 * @return
	 */
	public static boolean checkAction(Intent i, String... actions) {
		for (String action : actions) {
			if (checkAction(i, action)) return true;
		}
		return false;
	}
}
