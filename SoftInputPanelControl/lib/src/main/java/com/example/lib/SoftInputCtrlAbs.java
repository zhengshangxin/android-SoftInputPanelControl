package com.example.lib;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.EditText;

/**
 * @author zhengshangxin.
 */
public abstract class SoftInputCtrlAbs
        implements OnLayoutChangeListener, OnGlobalLayoutListener, Runnable {

    protected FragmentActivity mActivity;
    protected View mViewBaseline;
    protected View mViewToMove;
    protected volatile boolean mHasMovedUp = false;
    protected OnFocusChangeListener[] mOnFocusChangeListeners;
    private volatile int mNewBottom;
    private volatile int mOldBottom;
    private Handler mHandler;

    public SoftInputCtrlAbs(FragmentActivity activity) {
        mActivity = activity;
        View rootView = mActivity.getWindow().findViewById(android.R.id.content);
        rootView.addOnLayoutChangeListener(this);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /**
     *
     * @param viewBaseline Base line view.
     * @param viewToMove   The view to move.
     * @param editTexts    The edit text list will be controlled.
     */
    public void register(View viewBaseline, View viewToMove, EditText... editTexts) {
        mViewToMove = viewToMove;
        mViewBaseline = viewBaseline;
    }

    public void registerFocusChangeListener(OnFocusChangeListener... focusChangeListeners) {
        mOnFocusChangeListeners = focusChangeListeners;
    }

    public void unregister() {
        if (mActivity != null) {
            View rootView = mActivity.getWindow().findViewById(android.R.id.content);
            rootView.removeOnLayoutChangeListener(this);
            rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
        reset();
    }

    public void postMove() {
        if (mHandler == null) {
            mHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    moveUp();
                    return true;
                }
            });
        }
        mHandler.post(this);
    }

    @Override
    public void run() {
        if (mHandler != null) mHandler.sendEmptyMessage(0);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft,
            int oldTop, int oldRight, int oldBottom) {
        mNewBottom = bottom;
        mOldBottom = oldBottom;
    }

    public int getNewBottom() {
        return mNewBottom;
    }

    public int getOldBottom() {
        return mOldBottom;
    }

    protected synchronized void moveUp() {
        if (mViewToMove == null || mViewBaseline == null) return;
        int[] locationViewBaseline = new int[2];
        mViewBaseline.getLocationOnScreen(locationViewBaseline);
        int[] locationViewToMove = new int[2];
        mViewToMove.getLocationOnScreen(locationViewToMove);
        Rect rectViewToMove = new Rect();
        mViewToMove.getGlobalVisibleRect(rectViewToMove);

        int moveTopDistance = rectViewToMove.bottom - locationViewBaseline[1] -
                mViewBaseline.getHeight();

        if (moveTopDistance > 0 || mHasMovedUp) return;
        if (moveTopDistance < 0) {
            mHasMovedUp = true;
        }
        mViewToMove.setPadding(mViewToMove.getPaddingLeft(), moveTopDistance,
                mViewToMove.getPaddingRight(), mViewToMove.getPaddingBottom());
    }

    public void reset() {
        mViewToMove = null;
        mViewBaseline = null;
        mNewBottom = 0;
        mOldBottom = 0;
        mHasMovedUp = false;
        mActivity = null;
    }

    public void resetPosition() {
        if (mViewToMove != null) {
            mViewToMove.setPadding(mViewToMove.getPaddingLeft(), 0, mViewToMove.getPaddingRight(),
                    mViewToMove.getPaddingBottom());
            mHasMovedUp = false;
        }
    }
}
