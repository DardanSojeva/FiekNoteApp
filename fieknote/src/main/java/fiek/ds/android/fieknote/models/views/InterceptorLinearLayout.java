

package fiek.ds.android.fieknote.models.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import fiek.ds.android.fieknote.models.listeners.OnViewTouchedListener;


public class InterceptorLinearLayout extends LinearLayout {

    private OnViewTouchedListener mOnViewTouchedListener;


    public InterceptorLinearLayout(Context context) {
        super(context);
    }


    public InterceptorLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mOnViewTouchedListener != null) {
            mOnViewTouchedListener.onViewTouchOccurred(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }


    public void setOnViewTouchedListener(OnViewTouchedListener mOnViewTouchedListener) {
        this.mOnViewTouchedListener = mOnViewTouchedListener;
    }

}
