package com.cn.coachs.util;


import android.content.Context;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cn.coachs.R;

public class PullToRefreshView extends LinearLayout {
    private static final String TAG = "PullToRefreshView";
    // refresh states
    private static final int PULL_TO_REFRESH = 2;
    private static final int RELEASE_TO_REFRESH = 3;
    private static final int REFRESHING = 4;
    // pull state
    private static final int PULL_UP_STATE = 0;
    private static final int PULL_DOWN_STATE = 1;
    /**
     * last y
     */
    private int mLastMotionY;
    /**
     * lock
     */
    private boolean mLock;
    /**
     * header view
     */
    private View mHeaderView;
    /**
     * footer view
     */
    private View mFooterView;
    /**
     * list or grid
     */
    private AdapterView<?> mAdapterView;
    /**
     * scrollview
     */
    private ScrollView mScrollView;
    /**
     * header view height
     */
    private int mHeaderViewHeight;
    /**
     * footer view height
     */
    private int mFooterViewHeight;
    /**
     * header view image
     */
    private ImageView mHeaderImageView;
    /**
     * footer view image
     */
    private ImageView mFooterImageView;
    /**
     * header tip text
     */
    private TextView mHeaderTextView;
    /**
     * footer tip text
     */
    private TextView mFooterTextView;
    /**
     * header refresh time
     */
    private TextView mHeaderUpdateTextView;
    /**
     * footer refresh time
     */
    // private TextView mFooterUpdateTextView;
    /**
     * header progress bar
     */
    private ProgressBar mHeaderProgressBar;
    /**
     * footer progress bar
     */
    private ProgressBar mFooterProgressBar;
    /**
     * layout inflater
     */
    private LayoutInflater mInflater;
    /**
     * header view current state
     */
    private int mHeaderState;
    /**
     * footer view current state
     */
    private int mFooterState;
    /**
     * pull state,pull up or pull down;PULL_UP_STATE or PULL_DOWN_STATE
     */
    private int mPullState;
    /**
     * Âèò‰∏∫Âêë�?�∏ãÁöÑÁÆ≠Â§�?,ÊîπÂèòÁÆ≠Â§¥Êñ�?Âêë
     */
    private RotateAnimation mFlipAnimation;
    /**
     * Âèò‰∏∫ÈÄÜÂêëÁöÑÁÆ≠Â§¥,ÊóãËΩ¨
     */
    private RotateAnimation mReverseFlipAnimation;
    /**
     * footer refresh listener
     */
    private OnFooterRefreshListener mOnFooterRefreshListener;
    /**
     * footer refresh listener
     */
    private OnHeaderRefreshListener mOnHeaderRefreshListener;
    /**
     * last update time
     */
    @SuppressWarnings("unused")
    private String mLastUpdateTime;

    public PullToRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PullToRefreshView(Context context) {
        super(context);
        init();
    }

    /**
     * init
     *
     * @param context hylin 2012-7-26‰∏äÂçà10:08:33
     * @description
     */
    private void init() {
        // Load all of the animations we need in code rather than through XML
        mFlipAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mFlipAnimation.setInterpolator(new LinearInterpolator());
        mFlipAnimation.setDuration(250);
        mFlipAnimation.setFillAfter(true);
        mReverseFlipAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
        mReverseFlipAnimation.setDuration(250);
        mReverseFlipAnimation.setFillAfter(true);

        mInflater = LayoutInflater.from(getContext());
        // header view
        // Âú®Ê≠§Ê∑ªÂä�?,‰øùËØÅÊòØÁ¨¨�?�∏Ä‰∏™Ê∑ªÂä†Âà∞linearlayoutÁöÑÊúÄ‰∏äÁ´Ø
        addHeaderView();
    }

    private void addHeaderView() {
        // header view
        mHeaderView = mInflater.inflate(R.layout.refresh_header, this, false);

        mHeaderImageView = (ImageView) mHeaderView
                .findViewById(R.id.pull_to_refresh_image);
        mHeaderTextView = (TextView) mHeaderView
                .findViewById(R.id.pull_to_refresh_text);
        mHeaderUpdateTextView = (TextView) mHeaderView
                .findViewById(R.id.pull_to_refresh_updated_at);
        mHeaderProgressBar = (ProgressBar) mHeaderView
                .findViewById(R.id.pull_to_refresh_progress);
        // header layout
        measureView(mHeaderView);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                mHeaderViewHeight);
        // ËÆæÁΩÆtopMarginÁöÑÂÄº‰∏∫Ë¥üÁöÑheader
        // ViewÈ´òÂ∫�?,Âç≥Â∞ÜÂÖ∂ÈöêËóèÂú®ÊúÄ�?�∏äÊñπ
        params.topMargin = -(mHeaderViewHeight);
        // mHeaderView.setLayoutParams(params1);
        addView(mHeaderView, params);

    }

    private void addFooterView() {
        // footer view
        mFooterView = mInflater.inflate(R.layout.refresh_footer, this, false);
        mFooterImageView = (ImageView) mFooterView
                .findViewById(R.id.pull_to_load_image);
        mFooterTextView = (TextView) mFooterView
                .findViewById(R.id.pull_to_load_text);
        mFooterProgressBar = (ProgressBar) mFooterView
                .findViewById(R.id.pull_to_load_progress);
        // footer layout
        measureView(mFooterView);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                mFooterViewHeight);
        // int top = getHeight();
        // params.topMargin
        // =getHeight();//Âú®ËøôÈáågetHeight()==0,‰ΩÜÂú®onInterceptTouchEvent()ÊñπÊ≥ïÈáågetHeight()Â∑≤ÁªèÊúâÂÄº‰∫Ü,‰∏çÂÜçÊòØ0;
        // getHeight()‰ªÄ�?��?àÊó∂ÂÄô�?�ºöËµãÂÄ�?,Á®çÂÄôÂÜçÁ†îÁ©∂‰∏Ä‰∏ã
        // Áî±‰∫éÊòØÁ∫øÊÄßÂ∏ÉÂ±ÄÂèØ‰ª�?�Áõ¥Êé�?�Ê∑ªÂä�?,Âè™Ë¶ÅAdapterViewÁöÑÈ´òÂ∫¶ÊòØMATCH_PARENT,ÈÇ£‰�?àfooter
        // viewÂ∞±�?�ºöË¢´Ê∑ªÂä†Âà∞ÊúÄÂêé,Âπ∂ÈöêËó�?
        addView(mFooterView, params);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // footer view Âú®Ê≠§Ê∑ªÂä†�?�øùËØÅÊ∑ªÂä†Âà∞linearlayout‰∏≠ÁöÑÊúÄÂê�?
        addFooterView();
        initContentAdapterView();
    }

    /**
     * init AdapterView like ListView,GridView and so on;or init ScrollView
     *
     * @description hylin 2012-7-30‰∏ãÂçà8:48:12
     */
    private void initContentAdapterView() {
        int count = getChildCount();
        if (count < 3) {
            throw new IllegalArgumentException(
                    "this layout must contain 3 child views,and AdapterView or ScrollView must in the second position!");
        }
        View view = null;
        for (int i = 0; i < count - 1; ++i) {
            view = getChildAt(i);
            if (view instanceof AdapterView<?>) {
                mAdapterView = (AdapterView<?>) view;
            }
            if (view instanceof ScrollView) {
                // finish later
                mScrollView = (ScrollView) view;
            }
        }
        if (mAdapterView == null && mScrollView == null) {
            throw new IllegalArgumentException(
                    "must contain a AdapterView or ScrollView in this layout!");
        }
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int y = (int) e.getRawY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // È¶ñÂÖàÊã¶Êà™down‰∫ã‰ª∂,ËÆ∞ÂΩïyÂùêÊ†�?
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                // deltaY > 0 ÊòØÂêë‰∏ãËøêÂä®,< 0ÊòØÂêë‰∏äËøêÂä®
                int deltaY = y - mLastMotionY;
                if (isRefreshViewScroll(deltaY)) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return false;
    }

    /*
     * Â¶ÇÊûúÂú®onInterceptTouchEvent()ÊñπÊ≥ï�?�∏≠Ê≤°ÊúâÊã¶Êà�?(Âç�?
     * onInterceptTouchEvent()ÊñπÊ≥ï�?�∏�? return false)ÂàôÁî±PullToRefreshView
     * ÁöÑÂ
     * ≠êViewÊù•Â§ÑÁê�?;Âê¶ÂàôÁî±‰∏ãÈù¢ÁöÑÊñπÊ≥ïÊù�?�Â§ÑÁê�?(Âç≥Áî±PullToRefreshViewËá
     * ™Â∑±Êù•Â§ÑÁê�?)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mLock) {
            return true;
        }
        int y = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // onInterceptTouchEventÂ∑≤ÁªèËÆ∞ÂΩ�?
                // mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaY = y - mLastMotionY;
                if (mPullState == PULL_DOWN_STATE) {
                    // PullToRefreshViewÊâßË°å‰∏ãÊãâ
                    Log.i(TAG, " pull down!parent view move!");
                    headerPrepareToRefresh(deltaY);
                    // setHeaderPadding(-mHeaderViewHeight);
                } else if (mPullState == PULL_UP_STATE) {
                    // PullToRefreshViewÊâßË°å‰∏äÊãâ
                    Log.i(TAG, "pull up!parent view move!");
                    footerPrepareToRefresh(deltaY);
                }
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int topMargin = getHeaderTopMargin();
                if (mPullState == PULL_DOWN_STATE) {
                    if (topMargin >= 0) {
                        // ÂºÄÂßãÂà∑Êñ∞
                        headerRefreshing();
                    } else {
                        // ËøòÊ≤°ÊúâÊâßË°åÂà∑Êñ∞ÔºåÈáçÊñ∞ÈöêËóè
                        setHeaderTopMargin(-mHeaderViewHeight);
                    }
                } else if (mPullState == PULL_UP_STATE) {
                    if (Math.abs(topMargin) >= mHeaderViewHeight
                            + mFooterViewHeight) {
                        // ÂºÄÂßãÊâßË°åfooter Âà∑Êñ∞
                        footerRefreshing();
                    } else {
                        // ËøòÊ≤°ÊúâÊâßË°åÂà∑Êñ∞ÔºåÈáçÊñ∞ÈöêËóè
                        setHeaderTopMargin(-mHeaderViewHeight);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * ÊòØÂê¶Â∫îËØ�?�Âà∞‰∫ÜÁà∂View,Âç≥PullToRefreshViewÊªëÂä®
     *
     * @param deltaY , deltaY > 0 ÊòØÂêë‰∏ãËøêÂä®,< 0ÊòØÂêë‰∏äËøêÂä®
     * @return
     */
    private boolean isRefreshViewScroll(int deltaY) {
        if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {
            return false;
        }
        // ÂØπ‰∫éListViewÂíåGridView
        if (mAdapterView != null) {
            // Â≠êview(ListView or GridView)ÊªëÂä®Âà∞ÊúÄÈ°∂Á´Ø
            if (deltaY > 0) {

                View child = mAdapterView.getChildAt(0);
                if (child == null) {
                    // Â¶ÇÊûúmAdapterView‰∏≠Ê≤°ÊúâÊï∞Êç�?,‰∏çÊã¶Êà�?
                    return false;
                }
                if (mAdapterView.getFirstVisiblePosition() == 0
                        && child.getTop() == 0) {
                    mPullState = PULL_DOWN_STATE;
                    return true;
                }
                int top = child.getTop();
                int padding = mAdapterView.getPaddingTop();
                if (mAdapterView.getFirstVisiblePosition() == 0
                        && Math.abs(top - padding) <= 8) {// ËøôÈáå‰�?ãÂâçÁî®3ÂèØ‰ª�?�Âà§Êñ≠,‰ΩÜÁé∞Âú®‰∏çË°å,ËøòÊ≤°ÊâæÂà∞ÂéüÂõ�?
                    mPullState = PULL_DOWN_STATE;
                    return true;
                }

            } else if (deltaY < 0) {
                View lastChild = mAdapterView.getChildAt(mAdapterView
                        .getChildCount() - 1);
                if (lastChild == null) {
                    // Â¶ÇÊûúmAdapterView‰∏≠Ê≤°ÊúâÊï∞Êç�?,‰∏çÊã¶Êà�?
                    return false;
                }
                // ÊúÄÂêé‰∏Ä‰∏™Â≠êviewÁöÑBottomÂ∞è�?�∫éÁà∂ViewÁöÑÈ´òÂ∫¶ËØ¥ÊòémAdapterViewÁöÑÊï∞ÊçÆÊ≤°ÊúâÂ°´Êª°Áà∂view,
                // Á≠â�?�∫éÁà∂ViewÁöÑÈ´òÂ∫¶ËØ¥ÊòémAdapterViewÂ∑≤ÁªèÊªëÂä®Âà∞ÊúÄÂê�?
                if (lastChild.getBottom() <= getHeight()
                        && mAdapterView.getLastVisiblePosition() == mAdapterView
                        .getCount() - 1) {
                    mPullState = PULL_UP_STATE;
                    return true;
                }
            }
        }
        // ÂØπ‰∫éScrollView
        if (mScrollView != null) {
            // Â≠êscroll viewÊªëÂä®Âà∞ÊúÄÈ°∂Á´Ø
            View child = mScrollView.getChildAt(0);
            if (deltaY > 0 && mScrollView.getScrollY() == 0) {
                mPullState = PULL_DOWN_STATE;
                return true;
            } else if (deltaY < 0
                    && child.getMeasuredHeight() <= getHeight()
                    + mScrollView.getScrollY()) {
                mPullState = PULL_UP_STATE;
                return true;
            }
        }
        return false;
    }

    /**
     * header ÂáÜÂ§áÂà∑Êñ∞,ÊâãÊåáÁßªÂä®ËøáÁ®ã,ËøòÊ≤°ÊúâÈáäÊî�?
     *
     * @param deltaY ,ÊâãÊåáÊªëÂä®ÁöÑË∑ùÁ¶�?
     */
    private void headerPrepareToRefresh(int deltaY) {
        int newTopMargin = changingHeaderViewTopMargin(deltaY);
        // ÂΩìheader
        // viewÁöÑtopMargin>=0Êó∂ÔºåËØ¥ÊòéÂ∑≤ÁªèÂÆåÂÖ®ÊòæÁ§∫Âá∫Êù�?��?�∫Ü,‰øÆÊî�?header
        // view ÁöÑÊèêÁ§∫Áä∂ÊÄÅ
        if (newTopMargin >= 0 && mHeaderState != RELEASE_TO_REFRESH) {
            mHeaderTextView.setText(R.string.pull_to_refresh_release_label);
            mHeaderUpdateTextView.setVisibility(View.VISIBLE);
            mHeaderImageView.clearAnimation();
            mHeaderImageView.startAnimation(mFlipAnimation);
            mHeaderState = RELEASE_TO_REFRESH;
        } else if (newTopMargin < 0 && newTopMargin > -mHeaderViewHeight) {// ÊãñÂä®Êó∂Ê≤°ÊúâÈáäÊîæ
            mHeaderImageView.clearAnimation();
            mHeaderImageView.startAnimation(mFlipAnimation);
            // mHeaderImageView.
            mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);
            mHeaderState = PULL_TO_REFRESH;
        }
    }

    /**
     * footer ÂáÜÂ§áÂà∑Êñ∞,ÊâãÊåáÁßªÂä®ËøáÁ®ã,ËøòÊ≤°ÊúâÈáäÊî�? ÁßªÂä®footer
     * viewÈ´òÂ∫¶ÂêåÊ�?�∑ÂíåÁßªÂä®header view
     * È´òÂ∫¶ÊòØ�?�∏ÄÊ†∑ÔºåÈÉΩÊòØÈÄöËøá‰øÆÊî�?header
     * viewÁöÑtopmarginÁöÑÂÄºÊù•ËææÂà∞
     *
     * @param deltaY ,ÊâãÊåáÊªëÂä®ÁöÑË∑ùÁ¶�?
     */
    private void footerPrepareToRefresh(int deltaY) {
        int newTopMargin = changingHeaderViewTopMargin(deltaY);
        // Â¶ÇÊûúheader view topMargin ÁöÑÁªùÂØπÂÄºÂ§ß‰∫éÊàñÁ≠â�?�∫éheader +
        // footer ÁöÑÈ´òÂ∫�?
        // ËØ¥Êòéfooter view ÂÆåÂÖ®ÊòæÁ§∫Âá∫Êù•�?�∫ÜÔºå‰øÆÊî�?footer view
        // ÁöÑÊèêÁ§∫Áä∂ÊÄÅ
        if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight)
                && mFooterState != RELEASE_TO_REFRESH) {
            mFooterTextView
                    .setText(R.string.pull_to_refresh_footer_release_label);
            mFooterImageView.clearAnimation();
            mFooterImageView.startAnimation(mFlipAnimation);
            mFooterState = RELEASE_TO_REFRESH;
        } else if (Math.abs(newTopMargin) < (mHeaderViewHeight + mFooterViewHeight)) {
            mFooterImageView.clearAnimation();
            mFooterImageView.startAnimation(mFlipAnimation);
            mFooterTextView.setText(R.string.pull_to_refresh_footer_pull_label);
            mFooterState = PULL_TO_REFRESH;
        }
    }

    /**
     * ‰øÆÊî�?Header view top marginÁöÑÂÄº
     *
     * @param deltaY
     * @return hylin 2012-7-31‰∏ãÂçà1:14:31
     * @description
     */
    private int changingHeaderViewTopMargin(int deltaY) {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        float newTopMargin = params.topMargin + deltaY * 0.3f;
        // ËøôÈáåÂØπ‰∏äÊãâÂÅö‰∏Ä‰∏ãÈôêÂà�?,Âõ†�?�∏∫ÂΩìÂâç�?�∏äÊãâÂêéÁÑ∂Âêé�?�∏çÈáäÊîæÊâãÊåáÁõ¥Êé•�?�∏ãÊãâ,‰ºöÊää�?�∏ãÊãâÂà∑Êñ∞ÁªôËß¶Âèë‰∫Ü,ÊÑüË∞¢ÁΩëÂèãyufengzungzheÁöÑÊåáÂá�?
        // Ë°®Á§∫Â¶ÇÊûúÊòØÂú®�?�∏äÊãâÂêé‰∏ÄÊÆµË∑ùÁ¶�?,ÁÑ∂ÂêéÁõ¥Êé�?��?�∏ãÊãâ
        if (deltaY > 0 && mPullState == PULL_UP_STATE
                && Math.abs(params.topMargin) <= mHeaderViewHeight) {
            return params.topMargin;
        }
        // ÂêåÊ†∑Âú�?,ÂØπ‰∏ãÊãâÂÅö‰∏Ä‰∏ãÈôêÂà�?,ÈÅøÂÖçÂá∫Áé∞Ë∑ü�?�∏äÊãâÊìç‰ΩúÊó∂‰∏ÄÊ†∑ÁöÑbug
        if (deltaY < 0 && mPullState == PULL_DOWN_STATE
                && Math.abs(params.topMargin) >= mHeaderViewHeight) {
            return params.topMargin;
        }
        params.topMargin = (int) newTopMargin;
        mHeaderView.setLayoutParams(params);
        invalidate();
        return params.topMargin;
    }

    /**
     * header refreshing
     *
     * @description hylin 2012-7-31‰∏äÂçà9:10:12
     */
    private void headerRefreshing() {
        mHeaderState = REFRESHING;
        setHeaderTopMargin(0);
        mHeaderImageView.setVisibility(View.GONE);
        mHeaderImageView.clearAnimation();
        mHeaderImageView.setImageDrawable(null);
        mHeaderProgressBar.setVisibility(View.VISIBLE);
        mHeaderTextView.setText(R.string.pull_to_refresh_refreshing_label);
        if (mOnHeaderRefreshListener != null) {
            mOnHeaderRefreshListener.onHeaderRefresh(this);
        }
    }

    /**
     * footer refreshing
     *
     * @description hylin 2012-7-31‰∏äÂçà9:09:59
     */
    private void footerRefreshing() {
        mFooterState = REFRESHING;
        int top = mHeaderViewHeight + mFooterViewHeight;
        setHeaderTopMargin(-top);
        mFooterImageView.setVisibility(View.GONE);
        mFooterImageView.clearAnimation();
        mFooterImageView.setImageDrawable(null);
        mFooterProgressBar.setVisibility(View.VISIBLE);
        mFooterTextView
                .setText(R.string.pull_to_refresh_footer_refreshing_label);
        if (mOnFooterRefreshListener != null) {
            mOnFooterRefreshListener.onFooterRefresh(this);
        }
    }

    /**
     * ËÆæÁΩÆheader view ÁöÑtopMarginÁöÑÂÄº
     *
     * @param topMargin Ôºå‰∏�?0Êó∂ÔºåËØ¥Êòéheader view ÂàöÂ•ΩÂÆåÂÖ®ÊòæÁ§∫Âá∫Êù�?�Ôº�?
     *                  ‰∏�?-mHeaderViewHeightÊó∂ÔºåËØ¥ÊòéÂÆåÂÖ®ÈöêËóè�?�∫Ü hylin
     *                  2012-7-31‰∏äÂçà11:24:06
     * @description
     */
    private void setHeaderTopMargin(int topMargin) {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        params.topMargin = topMargin;
        mHeaderView.setLayoutParams(params);
        invalidate();
    }

    /**
     * header view ÂÆåÊàêÊõ¥Êñ∞ÂêéÊÅ¢Â§çÂàùÂßãÁä∂ÊÄÅ
     *
     * @description hylin 2012-7-31‰∏äÂçà11:54:23
     */
    public void onHeaderRefreshComplete() {
        setHeaderTopMargin(-mHeaderViewHeight);
        mHeaderImageView.setVisibility(View.VISIBLE);
        mHeaderImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow);
        mHeaderTextView.setText(R.string.pull_to_refresh_pull_label);
        mHeaderProgressBar.setVisibility(View.GONE);

        Time localTime = new Time("Asia/Hong_Kong");
        localTime.setToNow();
        String date = localTime.format("%m-%d %H:%M");

        mHeaderUpdateTextView.setText("更新于：" + date);
        mHeaderState = PULL_TO_REFRESH;
    }

    /**
     * Resets the list to a normal state after a refresh.
     *
     * @param lastUpdated Last updated at.
     */
    public void onHeaderRefreshComplete(CharSequence lastUpdated) {
        setLastUpdated(lastUpdated);
        onHeaderRefreshComplete();
    }

    /**
     * footer view ÂÆåÊàêÊõ¥Êñ∞ÂêéÊÅ¢Â§çÂàùÂßãÁä∂ÊÄÅ
     */
    public void onFooterRefreshComplete() {
        setHeaderTopMargin(-mHeaderViewHeight);
        mFooterImageView.setVisibility(View.VISIBLE);
        mFooterImageView.setImageResource(R.drawable.ic_pulltorefresh_arrow_up);
        mFooterTextView.setText(R.string.pull_to_refresh_footer_pull_label);
        mFooterProgressBar.setVisibility(View.GONE);
        // mHeaderUpdateTextView.setText("");
        mFooterState = PULL_TO_REFRESH;
    }

    /**
     * Set a text to represent when the list was last updated.
     *
     * @param lastUpdated Last updated at.
     */
    public void setLastUpdated(CharSequence lastUpdated) {
        if (lastUpdated != null) {
            mHeaderUpdateTextView.setVisibility(View.VISIBLE);
            mHeaderUpdateTextView.setText(lastUpdated);
        } else {
            mHeaderUpdateTextView.setVisibility(View.GONE);
        }
    }

    /**
     * Ëé∑ÂèñÂΩìÂâçheader view ÁöÑtopMargin
     *
     * @return hylin 2012-7-31‰∏äÂçà11:22:50
     * @description
     */
    private int getHeaderTopMargin() {
        LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
        return params.topMargin;
    }

    /**
     * lock
     *
     * @description hylin 2012-7-27‰∏ãÂçà6:52:25
     */
    @SuppressWarnings("unused")
    private void lock() {
        mLock = true;
    }

    /**
     * unlock
     *
     * @description hylin 2012-7-27‰∏ãÂçà6:53:18
     */
    @SuppressWarnings("unused")
    private void unlock() {
        mLock = false;
    }

    /**
     * set headerRefreshListener
     *
     * @param headerRefreshListener hylin 2012-7-31‰∏äÂçà11:43:58
     * @description
     */
    public void setOnHeaderRefreshListener(
            OnHeaderRefreshListener headerRefreshListener) {
        mOnHeaderRefreshListener = headerRefreshListener;
    }

    public void setOnFooterRefreshListener(
            OnFooterRefreshListener footerRefreshListener) {
        mOnFooterRefreshListener = footerRefreshListener;
    }

    /**
     * Interface definition for a callback to be invoked when list/grid footer
     * view should be refreshed.
     */
    public interface OnFooterRefreshListener {
        public void onFooterRefresh(PullToRefreshView view);
    }

    /**
     * Interface definition for a callback to be invoked when list/grid header
     * view should be refreshed.
     */
    public interface OnHeaderRefreshListener {
        public void onHeaderRefresh(PullToRefreshView view);
    }
}
