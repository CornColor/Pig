package browser.pig.cn.pig.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * created by dan
 */
public class SideGroupLayout extends ViewGroup{
    public static final String TAG = "android_xw";

    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;
    private boolean mIsBeingDragged;
    protected int mFirstItemHeight;
    private int mScrollY;
    public boolean mScrollToEnd;

    private VelocityTracker mVelocityTracker;
    private int mMinimumFlingVelocity;
    private int mMaximumFlingVelocity;

    private Scroller mScroller;
    private boolean mCanScroller;

    public SideGroupLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mCanScroller = true;
        ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();

        mMinimumFlingVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumFlingVelocity = configuration.getScaledMaximumFlingVelocity();

        mScroller = new Scroller(context);
        reset();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = 0;

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
                height += child.getMeasuredHeight();
            }
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int height = 0;
        mFirstItemHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view.getVisibility() != View.GONE) {
                view.layout(0, height, getWidth(), height + view.getMeasuredHeight());
                height += view.getMeasuredHeight();
                if (i == 0) {
                    mFirstItemHeight = height;
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mFirstItemHeight == 0 || !mCanScroller) {
            mScrollToEnd = true;
            return super.onInterceptTouchEvent(ev);
        }

        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE) && (mIsBeingDragged)) {
            return true;
        }
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                final float x = ev.getX();
                final float y = ev.getY();
                final int xDiff = (int) Math.abs(x - mLastMotionX);
                final int yDiff = (int) Math.abs(y - mLastMotionY);
                // Log.i("TAG", "mScrollY == mFirstItemHeight:" + (mScrollY ==
                // mFirstItemHeight));
                if (mScrollY == mFirstItemHeight) {
                  //  boolean isScrollY = yDiff > xDiff && y > mLastMotionY && mAction != null && mAction.isGroupScroll();
                    boolean isScrollY = yDiff > xDiff && y > mLastMotionY;
                    return isScrollY;
                } else if (yDiff > mTouchSlop * 2 && yDiff >= xDiff) {
                    mIsBeingDragged = true;
                    mLastMotionY = y;
                }
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                mLastMotionX = ev.getX();
                mLastMotionY = ev.getY();
                mIsBeingDragged = false;
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                break;
        }
        return mIsBeingDragged;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mFirstItemHeight == 0 || !mCanScroller) {
            mScrollToEnd = true;
            return super.onTouchEvent(event);
        }
        addVelocityTracker(event);

        final int action = event.getAction();
        final float y = event.getY();
        final float x = event.getX();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 获取相对屏幕的坐标，即以屏幕左上角为原点
                break;
            case MotionEvent.ACTION_MOVE:
                final float scrollX = mLastMotionX - x;
                final float scrollY = mLastMotionY - y;
                onScroll((int) scrollX, (int) scrollY);
                scrollTo(0, mScrollY);
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumFlingVelocity);
                final float velocityX = mVelocityTracker.getXVelocity();
                final float velocityY = mVelocityTracker.getYVelocity();
                if (Math.abs(velocityY) > mMinimumFlingVelocity * 3 && Math.abs(velocityY) > Math.abs(velocityX)) {
                    onFling(velocityX, velocityY);
                }
                cancel();
                break;
        }
        return true;
    }

    private void onScroll(int scrollX, int scrollY) {
        if (scrollY > 0) {
            if (mScrollY == mFirstItemHeight)
                return;
            if (mScrollY + scrollY >= mFirstItemHeight) {
                mScrollY = mFirstItemHeight;
            } else {
                mScrollY = mScrollY + scrollY;
            }
        } else if (scrollY < 0) {
            if (mScrollY > 0) {
                scrollY = Math.abs(scrollY);
                if (mScrollY - scrollY <= 0) {
                    mScrollY = 0;
                } else {
                    mScrollY = mScrollY - scrollY;
                }
            }
        }
        mScrollToEnd = mScrollY == mFirstItemHeight;
    }

    private void onFling(float velocityX, float velocityY) {
        int dy = 0;
        if (velocityY > 0) {
            dy = -mScrollY;
        } else {
            dy = (int) (mFirstItemHeight - getScrollY());
        }

        float ratio = getRatio(Math.abs(velocityY));
        dy = (int) (dy * ratio);
        onScroll(0, dy);
        if (mFirstItemHeight > 0) {
            mScroller.startScroll(0, getScrollY(), 0, dy, 500 * Math.abs(dy) / mFirstItemHeight);
        }
        postInvalidate();
    }

    protected float getRatio(float velocityY) {
        return 1;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            onScrollChanged(getScrollX(), getScrollY(), 0, 0);
            postInvalidate();
        }
    }

    private void addVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(event);
    }

    private void cancel() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
        mIsBeingDragged = false;
    }

    private void reset() {
        mScrollToEnd = false;
    }

    public boolean isScrollToEnd() {
        return mScrollToEnd;
    }

//    private OnGroupScrollListener mAction;

//    public void setOnGroupScrollListener(OnGroupScrollListener action) {
//        this.mAction = action;
//    }


    public void setCanScroller(boolean canScroller) {
        this.mCanScroller = canScroller;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        if (mAction != null) {
//            mAction.onScrollChanged(l, t);
//        }
    }

    public void onActivityDestory() {
        reset();
        mScroller = null;
        mScrollToEnd = false;
    }

    public interface OnGroupScrollListener{
        public boolean isChildScroll();
        public boolean isGroupScroll();
        public void onScrollChanged(int left, int top);
    }

}
