package com.example.lib;

import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * @author zhengshangxin.
 *
 * @see {@link SoftInputCtrlAbs}
 */
public class SoftInputTwoEdtCtrl extends SoftInputCtrlAbs {

    private EditText mEdtFirst;
    private EditText mEdtSecond;

    private boolean mEdtActionNext = false;
    private boolean mEdtSecondHasFocus = false;

    private OnEditorActionListener mOnEditorActionListener = new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                performActionNextEdtFirst();
            }
            return false;
        }
    };

    public SoftInputTwoEdtCtrl(FragmentActivity activity) {
        super(activity);
    }

    private synchronized void performActionNextEdtFirst() {
        mEdtActionNext = true;
        postMove();
    }

    @Override
    public void register(View viewBaseline, View viewToMove, final EditText... editTexts) {
        super.register(viewBaseline, viewToMove, editTexts);
        if (editTexts == null || editTexts.length < 2) return;
        mEdtFirst = editTexts[0];
        mEdtSecond = editTexts[1];
        mEdtActionNext = false;
        mEdtSecondHasFocus = false;

        mEdtFirst.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mOnFocusChangeListeners != null &&
                        mOnFocusChangeListeners.length == editTexts.length) {
                    if (mOnFocusChangeListeners[0] != null)
                        mOnFocusChangeListeners[0].onFocusChange(v, hasFocus);
                }
            }
        });

        mEdtSecond.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (mOnFocusChangeListeners != null &&
                        mOnFocusChangeListeners.length == editTexts.length) {
                    if (mOnFocusChangeListeners[1] != null)
                        mOnFocusChangeListeners[1].onFocusChange(v, hasFocus);
                }
                mEdtSecondHasFocus = hasFocus;
                postMove();
            }
        });
        mEdtFirst.setOnEditorActionListener(mOnEditorActionListener);
    }

    @Override
    public void onGlobalLayout() {
        int bottom = getNewBottom();
        int oldBottom = getOldBottom();

        if (bottom < oldBottom) {
            if (!mEdtSecondHasFocus) return;
            postMove();

        } else if (bottom > oldBottom) {
            resetPosition();

        } else if (mEdtActionNext && mEdtSecondHasFocus) {
            mEdtActionNext = false;
        }
    }

    @Override
    public void reset() {
        super.reset();
        mEdtFirst = null;
        mEdtSecond = null;

        mEdtActionNext = false;
        mEdtSecondHasFocus = false;
    }
}
