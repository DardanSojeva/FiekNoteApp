

package fiek.ds.android.fieknote.widget;


import android.app.PendingIntent;
import android.content.Context;
import android.util.SparseArray;
import android.widget.RemoteViews;


public class SimpleWidgetProvider extends WidgetProvider {


    @Override
    protected RemoteViews getRemoteViews(Context mContext, int widgetId, boolean isSmall, boolean isSingleLine, 
                                         SparseArray<PendingIntent> pendingIntentsMap) {
        RemoteViews views;
        if (isSmall) {
            views = new RemoteViews(mContext.getPackageName(), fiek.ds.android.fieknote.R.layout.widget_layout_small);
            views.setOnClickPendingIntent(fiek.ds.android.fieknote.R.id.list, pendingIntentsMap.get(fiek.ds.android.fieknote.R.id.list));
        } else {
            views = new RemoteViews(mContext.getPackageName(), fiek.ds.android.fieknote.R.layout.widget_layout);
            views.setOnClickPendingIntent(fiek.ds.android.fieknote.R.id.add, pendingIntentsMap.get(fiek.ds.android.fieknote.R.id.add));
            views.setOnClickPendingIntent(fiek.ds.android.fieknote.R.id.list, pendingIntentsMap.get(fiek.ds.android.fieknote.R.id.list));
            views.setOnClickPendingIntent(fiek.ds.android.fieknote.R.id.camera, pendingIntentsMap.get(fiek.ds.android.fieknote.R.id.camera));
        }
        return views;
    }
}