package com.dangdang.gx.ui.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import com.dangdang.gx.R;

public abstract class PullToRefreshBase<T extends View> extends LinearLayout {
	protected ReadyForPullDownRefreshListener mReadyForPullDownRefreshListener;

	public void setReadyForPullDownRefreshListener(
			ReadyForPullDownRefreshListener downRefreshListener) {
		this.mReadyForPullDownRefreshListener = downRefreshListener;
	}

	final class SmoothScrollRunnable implements Runnable {

		static final int ANIMATION_DURATION_MS = 190;
		static final int ANIMATION_FPS = 1000 / 60;

		private final Interpolator interpolator;
		private final int scrollToY;
		private final int scrollFromY;
		private final Handler handler;

		private boolean continueRunning = true;
		private long startTime = -1;
		private int currentY = -1;

		public SmoothScrollRunnable(Handler handler, int fromY, int toY) {
			this.handler = handler;
			this.scrollFromY = fromY;
			this.scrollToY = toY;
			this.interpolator = new AccelerateDecelerateInterpolator();
		}

		@Override
		public void run() {

			/**
			 * Only set startTime if this is the first time we're starting, else
			 * actually calculate the Y delta
			 */
			if (startTime == -1) {
				startTime = System.currentTimeMillis();
			} else {

				/**
				 * We do do all calculations in long to reduce software float
				 * calculations. We use 1000 as it gives us good accuracy and
				 * small rounding errors
				 */
				long normalizedTime = (1000 * (System.currentTimeMillis() - startTime))
						/ ANIMATION_DURATION_MS;
				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

				final int deltaY = Math
						.round((scrollFromY - scrollToY)
								* interpolator
										.getInterpolation(normalizedTime / 1000f));
				this.currentY = scrollFromY - deltaY;
				setHeaderScroll(currentY);
			}

			// If we're not at the target Y, keep going...
			if (continueRunning && scrollToY != currentY) {
				handler.postDelayed(this, ANIMATION_FPS);
			}
		}

		public void stop() {
			this.continueRunning = false;
			this.handler.removeCallbacks(this);
		}
	};

	// ===========================================================
	// Constants
	// ===========================================================

	static final float FRICTION = 2.0f;

	static final int PULL_TO_REFRESH = 0x0;
	static final int RELEASE_TO_REFRESH = 0x1;
	static final int REFRESHING = 0x2;
	static final int MANUAL_REFRESHING = 0x3;

	public static final int MODE_PULL_DOWN_TO_REFRESH = 0x1;
	public static final int MODE_PULL_UP_TO_REFRESH = 0x2;
	public static final int MODE_BOTH = 0x3;
	public static final int MODE_NONE = 0x4;

	// ===========================================================
	// Fields
	// ===========================================================

	private int touchSlop;

	private float initialMotionY;
	private float lastMotionX;
	private float lastMotionY;
	private boolean isBeingDragged = false;

	protected int state = PULL_TO_REFRESH;
	private int mode = MODE_PULL_DOWN_TO_REFRESH;
	private int currentMode;

	private boolean disableScrollingWhileRefreshing = true;

	public T refreshableView;
	private boolean isPullToRefreshEnabled = true;
	private boolean isPullUpToRefreshEnabled = true;
	private LoadingLayout headerLayout;
	private LoadingLayout footerLayout;
	private int headerHeight;
	private Context mContext;
	private final Handler handler = new Handler();

	protected OnRefreshListener onRefreshListener;

	private SmoothScrollRunnable currentSmoothScrollRunnable;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public PullToRefreshBase(Context context) {
		super(context);
		init(context, null);
	}

	public PullToRefreshBase(Context context, int mode) {
		super(context);
		this.mode = mode;
		init(context, null);
	}

	public PullToRefreshBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public PullToRefreshBase(Context context, int mode, T view) {
		super(context);
		this.mode = mode;
		init(context, null, view);
	}
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================

	/**
	 * Deprecated. Use {@link #getRefreshableView()} from now on.
	 * 
	 * @deprecated
	 * @return The Refreshable View which is currently wrapped
	 */
	public final T getAdapterView() {
		return refreshableView;
	}

	/**
	 * Get the Wrapped Refreshable View. Anything returned here has already been
	 * added to the content view.
	 * 
	 * @return The View which is currently wrapped
	 */
	public final T getRefreshableView() {
		return refreshableView;
	}

	/**
	 * Whether Pull-to-Refresh is enabled
	 * 
	 * @return enabled
	 */
	public final boolean isPullToRefreshEnabled() {
		return isPullToRefreshEnabled;
	}

	/**
	 * Whether Pull_up-to-Refresh is enabled
	 * 
	 * @return enabled
	 */
	public final boolean isPullUpToRefreshEnabled() {
		return isPullUpToRefreshEnabled;
	}

	/**
	 * Returns whether the widget has disabled scrolling on the Refreshable View
	 * while refreshing.
	 *
	 */
	public final boolean isDisableScrollingWhileRefreshing() {
		return disableScrollingWhileRefreshing;
	}

	/**
	 * Returns whether the Widget is currently in the Refreshing state
	 * 
	 * @return true if the Widget is currently refreshing
	 */
	public final boolean isRefreshing() {
		return state == REFRESHING || state == MANUAL_REFRESHING;
	}

	/**
	 * By default the Widget disabled scrolling on the Refreshable View while
	 * refreshing. This method can change this behaviour.
	 * 
	 * @param disableScrollingWhileRefreshing
	 *            - true if you want to disable scrolling while refreshing
	 */
	public final void setDisableScrollingWhileRefreshing(
			boolean disableScrollingWhileRefreshing) {
		this.disableScrollingWhileRefreshing = disableScrollingWhileRefreshing;
	}

	/**
	 * Mark the current Refresh as complete. Will Reset the UI and hide the
	 * Refreshing View
	 */
	public void onRefreshComplete() {
		if (state != PULL_TO_REFRESH) {
			resetHeader();
		}
	}

	/**
	 * Set OnRefreshListener for the Widget
	 * 
	 * @param listener
	 *            - Listener to be used when the Widget is set to Refresh
	 */
	public final void setOnRefreshListener(OnRefreshListener listener) {
		onRefreshListener = listener;
	}

	/**
	 * A mutator to enable/disable Pull-to-Refresh for the current View
	 * 
	 * @param enable
	 *            Whether Pull-To-Refresh should be used
	 */
	public final void setPullToRefreshEnabled(boolean enable) {
		this.isPullToRefreshEnabled = enable;
	}

	/**
	 * A mutator to enable/disable Pull-to-Refresh for the current View
	 * 
	 * @param enable
	 *            Whether Pull-To-Refresh should be used
	 */
	public final void setPullUpToRefreshEnabled(boolean enable) {
		this.isPullUpToRefreshEnabled = enable;
	}

	/**
	 * Set Text to show when the Widget is being pulled, and will refresh when
	 * released
	 * 
	 * @param releaseLabel
	 *            - String to display
	 */
	public void setReleaseLabel(String releaseLabel) {
		if (null != headerLayout) {
			headerLayout.setReleaseLabel(releaseLabel);
		}
		if (null != footerLayout) {
			footerLayout.setReleaseLabel(releaseLabel);
		}
	}

	/**
	 * Set Text to show when the Widget is being Pulled
	 * 
	 * @param pullLabel
	 *            - String to display
	 */
	public void setPullLabel(String pullLabel) {
		if (null != headerLayout) {
			headerLayout.setPullLabel(pullLabel);
		}
		if (null != footerLayout) {
			footerLayout.setPullLabel(pullLabel);
		}
	}

	/**
	 * Set Text to show when the Widget is refreshing
	 * 
	 * @param refreshingLabel
	 *            - String to display
	 */
	public void setRefreshingLabel(String refreshingLabel) {
		if (null != headerLayout) {
			headerLayout.setRefreshingLabel(refreshingLabel);
		}
		if (null != footerLayout) {
			footerLayout.setRefreshingLabel(refreshingLabel);
		}
	}

	public final void setRefreshing() {
		this.setRefreshing(true);
	}

	/**
	 * Sets the Widget to be in the refresh state. The UI will be updated to
	 * show the 'Refreshing' view.
	 * 
	 * @param doScroll
	 *            - true if you want to force a scroll to the Refreshing view.
	 */
	public final void setRefreshing(boolean doScroll) {
		if (!isRefreshing()) {
			setRefreshingInternal(doScroll);
			state = MANUAL_REFRESHING;
		}
	}

	public final boolean hasPullFromTop() {
		return currentMode != MODE_PULL_UP_TO_REFRESH && currentMode != MODE_NONE;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public final boolean onTouchEvent(MotionEvent event) {
		// if (!isPullToRefreshEnabled) {
		// return false;
		// }

		if (isRefreshing() && disableScrollingWhileRefreshing) {
			return true;
		}

		if (event.getAction() == MotionEvent.ACTION_DOWN
				&& event.getEdgeFlags() != 0) {
			return false;
		}

		switch (event.getAction()) {

		case MotionEvent.ACTION_MOVE: {
			if (isBeingDragged) {
				lastMotionY = event.getY();
				this.pullEvent();
				return true;
			}
			break;
		}

		case MotionEvent.ACTION_DOWN: {
			if (isReadyForPull()) {
				lastMotionY = initialMotionY = event.getY();
				return true;
			}
			break;
		}

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP: {
			if (isBeingDragged) {
				isBeingDragged = false;

				if (state == RELEASE_TO_REFRESH && null != onRefreshListener) {
					setRefreshingInternal(true);
					// onRefreshListener.onRefresh(currentMode);
					if (currentMode == MODE_PULL_UP_TO_REFRESH) {
						onRefreshListener.onPullUpRefresh();
					} else if (currentMode == MODE_PULL_DOWN_TO_REFRESH) {
						onRefreshListener.onPullDownRefresh();
					}
				} else {
					smoothScrollTo(0);
				}
				return true;
			}
			break;
		}
		}

		return false;
	}

	@Override
	public final boolean onInterceptTouchEvent(MotionEvent event) {
		// if (!isPullToRefreshEnabled) {
		// return false;
		// }
		if (isRefreshing() && disableScrollingWhileRefreshing) {
			return true;
		}

		final int action = event.getAction();

		if (action == MotionEvent.ACTION_CANCEL
				|| action == MotionEvent.ACTION_UP) {
			isBeingDragged = false;
			return false;
		}

		if (action != MotionEvent.ACTION_DOWN && isBeingDragged) {
			return true;
		}

		switch (action) {
		case MotionEvent.ACTION_MOVE: {
			if (isReadyForPull()) {

				final float y = event.getY();
				final float dy = y - lastMotionY;
				final float yDiff = Math.abs(dy);
				final float xDiff = Math.abs(event.getX() - lastMotionX);

				if (yDiff > touchSlop && yDiff > xDiff) {
					if ((mode == MODE_PULL_DOWN_TO_REFRESH || mode == MODE_BOTH)
							&& dy >= 0.0001f && isReadyForPullDown()) {
						lastMotionY = y;
						isBeingDragged = true;
						currentMode = MODE_PULL_DOWN_TO_REFRESH;
						if (mReadyForPullDownRefreshListener != null) {
							mReadyForPullDownRefreshListener
									.onReadyForPullDown(false);
						}
					} else if ((mode == MODE_PULL_UP_TO_REFRESH || mode == MODE_BOTH)
							&& dy <= 0.0001f && isReadyForPullUp()) {
						lastMotionY = y;
						isBeingDragged = true;
						currentMode = MODE_PULL_UP_TO_REFRESH;
					}
				}
			}
			break;
		}
		case MotionEvent.ACTION_DOWN: {
			if (isReadyForPull()) {
				lastMotionY = initialMotionY = event.getY();
				lastMotionX = event.getX();
				isBeingDragged = false;
			}
			break;
		}
		}

		return isBeingDragged;
	}

	protected void addRefreshableView(Context context, T refreshableView) {
//		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//				ViewGroup.LayoutParams.MATCH_PARENT,
//				ViewGroup.LayoutParams.MATCH_PARENT);
//		addView(refreshableView, params);
		
		addView(refreshableView, new LayoutParams(
				LayoutParams.MATCH_PARENT, 0, 1.0f));
	}

	/**
	 * This is implemented by derived classes to return the created View. If you
	 * need to use a custom View (such as a custom ListView), override this
	 * method and return an instance of your custom class.
	 * 
	 * Be sure to set the ID of the view in this method, especially if you're
	 * using a ListActivity or ListFragment.
	 * 
	 * @param context
	 * @param attrs
	 *            AttributeSet from wrapped class. Means that anything you
	 *            include in the XML layout declaration will be routed to the
	 *            created View
	 * @return New instance of the Refreshable View
	 */
	protected abstract T createRefreshableView(Context context,
			AttributeSet attrs);

	protected final int getCurrentMode() {
		return currentMode;
	}

	protected final LoadingLayout getFooterLayout() {
		return footerLayout;
	}

	protected final LoadingLayout getHeaderLayout() {
		return headerLayout;
	}

	protected final int getHeaderHeight() {
		return headerHeight;
	}

	protected final int getMode() {
		return mode;
	}

	/**
	 * Implemented by derived class to return whether the View is in a state
	 * where the user can Pull to Refresh by scrolling down.
	 * 
	 * @return true if the View is currently the correct state (for example, top
	 *         of a ListView)
	 */
	protected abstract boolean isReadyForPullDown();

	/**
	 * Implemented by derived class to return whether the View is in a state
	 * where the user can Pull to Refresh by scrolling up.
	 * 
	 * @return true if the View is currently in the correct state (for example,
	 *         bottom of a ListView)
	 */
	protected abstract boolean isReadyForPullUp();

	// ===========================================================
	// Methods
	// ===========================================================

	protected void resetHeader() {
		state = PULL_TO_REFRESH;
		isBeingDragged = false;

		if (null != headerLayout) {
			headerLayout.reset();
		}
		if (null != footerLayout) {
			footerLayout.reset();
		}

		smoothScrollTo(0);
	}

	protected void setRefreshingInternal(boolean doScroll) {
		state = REFRESHING;

		if (null != headerLayout && currentMode == MODE_PULL_DOWN_TO_REFRESH) {
			headerLayout.refreshing();
		}
		if (null != footerLayout && currentMode == MODE_PULL_UP_TO_REFRESH) {
			footerLayout.refreshing();
		}

		if (doScroll) {
			smoothScrollTo(currentMode == MODE_PULL_DOWN_TO_REFRESH ? -headerHeight
					: headerHeight);
		}
	}

	protected final void setHeaderScroll(int y) {
		scrollTo(0, y);
	}

	protected final void smoothScrollTo(int y) {
		if (null != currentSmoothScrollRunnable) {
			currentSmoothScrollRunnable.stop();
		}

		if (this.getScrollY() != y) {
			this.currentSmoothScrollRunnable = new SmoothScrollRunnable(
					handler, getScrollY(), y);
			handler.post(currentSmoothScrollRunnable);
		}
	}
	
	private void init(Context context, AttributeSet attrs){
		init(context, attrs, null);
	}

	private void init(Context context, AttributeSet attrs, T view) {
		mContext = context;
		setOrientation(LinearLayout.VERTICAL);

		touchSlop = ViewConfiguration.getTouchSlop();

		// By passing the attrs, we can add ListView/GridView params via XML
		if(view != null)
			refreshableView = view;
		else
			refreshableView = this.createRefreshableView(context, attrs);
		this.addRefreshableView(context, refreshableView);

		// Add Loading Views
		if (mode == MODE_PULL_DOWN_TO_REFRESH || mode == MODE_BOTH) {
			headerLayout = getDownLoadingLayout(context);
			addView(headerLayout, 0, new LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			measureView(headerLayout);
			headerHeight = headerLayout.getMeasuredHeight();
		} 
		if (mode == MODE_PULL_UP_TO_REFRESH || mode == MODE_BOTH) {
			footerLayout = getUpLoadingLayout(context);
			addView(footerLayout, new LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			measureView(footerLayout);
			headerHeight = footerLayout.getMeasuredHeight();
		} 
		if (mode == MODE_NONE){
			headerHeight = 0;
		}

		// Hide Loading Views
		switch (mode) {
		case MODE_BOTH:
			setPadding(0, -headerHeight, 0, -headerHeight);
			break;
		case MODE_PULL_UP_TO_REFRESH:
			setPadding(0, 0, 0, -headerHeight);
			break;
		case MODE_PULL_DOWN_TO_REFRESH:
		default:
			setPadding(0, -headerHeight, 0, 0);
			break;
		}

		// If we're not using MODE_BOTH, then just set currentMode to current
		// mode
		if (mode != MODE_BOTH) {
			currentMode = mode;
		}
	}

	protected LoadingLayout getDownLoadingLayout(Context context) {
		String pullLabel = context.getString(R.string.pull_to_refresh_pull_label);
		String releaseLabel = context.getString(R.string.pull_to_refresh_release_label);
		String refreshingLabel = context.getString(R.string.pull_to_refresh_refreshing_label);
		return new ProgressLoadingLayout(context, MODE_PULL_DOWN_TO_REFRESH, releaseLabel, pullLabel, refreshingLabel);
	}

	protected LoadingLayout getUpLoadingLayout(Context context) {
		String pullLabel = context.getString(R.string.pull_up_to_refresh_pull_label);
		String releaseLabel = context.getString(R.string.pull_up_to_refresh_release_label);
		String refreshingLabel = context.getString(R.string.pull_to_refresh_refreshing_label);
		return new ProgressLoadingLayout(context, MODE_PULL_UP_TO_REFRESH, releaseLabel, pullLabel, refreshingLabel);
	}

	public void changeMode(int mode){
		switch(this.mode){
		case MODE_PULL_DOWN_TO_REFRESH:
			this.removeView(headerLayout);
			break;
		case MODE_BOTH:
			this.removeView(headerLayout);
			this.removeView(footerLayout);
			break;
		case MODE_PULL_UP_TO_REFRESH:
			this.removeView(footerLayout);
			break;
		}
		headerLayout = null;
		footerLayout = null;
		
		// Add Loading Views
		if (mode == MODE_PULL_DOWN_TO_REFRESH || mode == MODE_BOTH) {
			headerLayout = getDownLoadingLayout(mContext);
			addView(headerLayout, 0, new LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			measureView(headerLayout);
			headerHeight = headerLayout.getMeasuredHeight();
		}
		if (mode == MODE_PULL_UP_TO_REFRESH || mode == MODE_BOTH) {
			footerLayout = getUpLoadingLayout(mContext);
			addView(footerLayout, new LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT));
			measureView(footerLayout);
			headerHeight = footerLayout.getMeasuredHeight();
		}
		if (mode == MODE_NONE){
			headerHeight = 0;
		}

		// Hide Loading Views
		switch (mode) {
		case MODE_BOTH:
			setPadding(0, -headerHeight, 0, -headerHeight);
			break;
		case MODE_PULL_UP_TO_REFRESH:
			setPadding(0, 0, 0, -headerHeight);
			break;
		case MODE_PULL_DOWN_TO_REFRESH:
		default:
			setPadding(0, -headerHeight, 0, 0);
			break;
		}
		this.mode = mode;
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
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

	/**
	 * Actions a Pull Event
	 * 
	 * @return true if the Event has been handled, false if there has been no
	 *         change
	 */
	private boolean pullEvent() {

		final int newHeight;
		final int oldHeight = this.getScrollY();

		switch (currentMode) {
		case MODE_PULL_UP_TO_REFRESH:
			newHeight = Math.round(Math.max(initialMotionY - lastMotionY, 0)
					/ FRICTION);
			// newHeight = Math.round((initialMotionY - lastMotionY) /
			// FRICTION);
			break;
		case MODE_NONE:
			newHeight = 0;
			break;
		default:
			newHeight = Math.round(Math.min(initialMotionY - lastMotionY, 0)
					/ FRICTION);
			// newHeight = Math.round((initialMotionY - lastMotionY) /
			// FRICTION);			
			headerLayout.scrollPullHeader(Math.abs(newHeight), headerHeight);
			
			break;
		}

		setHeaderScroll(newHeight);
		
		if (newHeight != 0) {
			if (state == PULL_TO_REFRESH) {
				if (headerHeight < Math.abs(newHeight)) {
					state = RELEASE_TO_REFRESH;

					switch (currentMode) {
					case MODE_PULL_UP_TO_REFRESH:
						footerLayout.releaseToRefresh();
						break;
					case MODE_PULL_DOWN_TO_REFRESH:
						headerLayout.releaseToRefresh();
						break;
					}

					return true;
				} else {
					switch (currentMode) {
					case MODE_PULL_UP_TO_REFRESH:
						footerLayout.setRefreshValid(MODE_PULL_UP_TO_REFRESH);
						break;
					case MODE_PULL_DOWN_TO_REFRESH:
						headerLayout.setRefreshValid(MODE_PULL_DOWN_TO_REFRESH);
						break;
					}
					return true;
				}

			}

			else if (state == RELEASE_TO_REFRESH
					&& headerHeight >= Math.abs(newHeight)) {
				state = PULL_TO_REFRESH;

				switch (currentMode) {
				case MODE_PULL_UP_TO_REFRESH:
					footerLayout.pullToRefresh();
					break;
				case MODE_PULL_DOWN_TO_REFRESH:
					headerLayout.pullToRefresh();
					break;
				}

				return true;
			}
		}

		return oldHeight != newHeight;
	}

	private boolean isReadyForPull() {
		switch (mode) {
		case MODE_PULL_DOWN_TO_REFRESH:
			return isReadyForPullDown();
		case MODE_PULL_UP_TO_REFRESH:
			return isReadyForPullUp();
		case MODE_BOTH:
			return isReadyForPullUp() || isReadyForPullDown();
		}
		return false;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static interface OnRefreshListener {

		// public void onRefresh(int mode);
		public void onPullDownRefresh();

		public void onPullUpRefresh();

	}

	public static interface OnLastItemVisibleListener {

		public void onLastItemVisible();

	}

	@Override
	public void setLongClickable(boolean longClickable) {
		getRefreshableView().setLongClickable(longClickable);
	}

	public void setRefreshMode(int mode) {
		if(this.mode == mode)
			return;
		changeMode(mode);
//		this.mode = mode;
	}

	public void setCurrentMode(int curMode) {
		this.currentMode = curMode;
	}

	public interface ReadyForPullDownRefreshListener {
		public void onReadyForPullDown(boolean onTouch);
	}
	
	public void setHeaderBackgroundColor(int color){
		if(headerLayout != null)
			headerLayout.setLoadingBackgroundColor(color);
	}
	
	public void setFooterBackgroundColor(int color){
		if(footerLayout != null)
			footerLayout.setLoadingBackgroundColor(color);
	}
}
