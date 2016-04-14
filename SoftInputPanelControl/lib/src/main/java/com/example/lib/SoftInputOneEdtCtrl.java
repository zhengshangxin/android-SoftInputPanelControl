package com.example.lib;

import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

/**
 * @author zhengshangxin.
 *
 * @see {@link SoftInputCtrlAbs}
 */
public class SoftInputOneEdtCtrl extends SoftInputCtrlAbs {

    public SoftInputOneEdtCtrl(FragmentActivity activity) {
        super(activity);
    }

    @Override
    public void register(View viewBaseline, View viewToMove, final EditText... editTexts) {
        super.register(viewBaseline, viewToMove, editTexts);
        if (editTexts == null || editTexts.length < 1) return;

        editTexts[0].setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mOnFocusChangeListeners != null &&
                        mOnFocusChangeListeners.length == editTexts.length) {
                    if (mOnFocusChangeListeners[0] != null)
                        mOnFocusChangeListeners[0].onFocusChange(v, hasFocus);
                }
            }
        });
    }

    @Override
    public void onGlobalLayout() {
        int bottom = getNewBottom();
        int oldBottom = getOldBottom();

        if (bottom < oldBottom) {
            postMove();
        } else if (bottom > oldBottom) {
            resetPosition();
        }
    }
}
