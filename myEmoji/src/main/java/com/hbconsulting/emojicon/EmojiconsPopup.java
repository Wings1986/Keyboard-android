/*
 * Copyright 2019 by iGold
 *
 */

package com.hbconsulting.emojicon;

import github.ankushsachdeva.emojicon.R;

import java.util.Arrays;
import java.util.List;

import com.hbconsulting.emojicon.EmojiconGridView.OnEmojiconClickedListener;
import com.hbconsulting.emojicon.emoji.Category2;
import com.hbconsulting.emojicon.emoji.Category3;
import com.hbconsulting.emojicon.emoji.Category4;
import com.hbconsulting.emojicon.emoji.Category5;
import com.hbconsulting.emojicon.emoji.Emojicon;
import com.hbconsulting.emojicon.emoji.Category1;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


/**
 * @author Ankush Sachdeva (sankush@yahoo.co.in).
 */

public class EmojiconsPopup extends RelativeLayout implements ViewPager.OnPageChangeListener, EmojiconRecents {
	private int mEmojiTabLastSelectedIndex = -1;
	private TextView tvTitle;
	private View[] mEmojiTabs;
	private PagerAdapter mEmojisAdapter;
	private EmojiconRecentsManager mRecentsManager;
	private int keyBoardHeight = 0;
	private Boolean pendingOpen = false;
	private Boolean isOpened = false;
	OnEmojiconClickedListener onEmojiconClickedListener;
	OnEmojiconBackspaceClickedListener onEmojiconBackspaceClickedListener; 
	OnSoftKeyboardOpenCloseListener onSoftKeyboardOpenCloseListener;
	OnKeyboardCloseListener onKeyboardCloseListener;
	OnKeyboardSpaceListener onKeyboardSpaceListener;
	
//	View rootView;
	Context mContext;

	private ViewPager emojisPager;
	/**
	 * Constructor
	 */
/*	public EmojiconsPopup(View rootView, Context mContext){
		super(mContext);
		this.mContext = mContext;
//		this.rootView = rootView;
		
	
//		setContentView(customView);
//		setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		
		//default size 
		setSize((int) mContext.getResources().getDimension(R.dimen.keyboard_height), LayoutParams.MATCH_PARENT);
	}
	
	*/
	
	public EmojiconsPopup(final Context context) {
		super(context);

		init(context);
	}

	public EmojiconsPopup(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		init(context);
	}

	public EmojiconsPopup(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);

		init(context);
	}

	private void init(Context context) {

		mContext = context;
	}
	
	
	/**
	 * Set the listener for the event of keyboard opening or closing.
	 */
	public void setOnSoftKeyboardOpenCloseListener(OnSoftKeyboardOpenCloseListener listener){
		this.onSoftKeyboardOpenCloseListener = listener; 
	}

	/**
	 * Set the listener for the event when any of the emojicon is clicked
	 */
	public void setOnEmojiconClickedListener(OnEmojiconClickedListener listener){
		this.onEmojiconClickedListener = listener;
	}

	/**
	 * Set the listener for the event when backspace on emojicon popup is clicked
	 */
	public void setOnEmojiconBackspaceClickedListener(OnEmojiconBackspaceClickedListener listener){
		this.onEmojiconBackspaceClickedListener = listener;
	}

	public void setOnKeyboardCloseListener(OnKeyboardCloseListener listener){
		this.onKeyboardCloseListener = listener; 
	}

	public void setOnKeyboardSpaceClickedListener(OnKeyboardSpaceListener listener){
		this.onKeyboardSpaceListener = listener; 
	}

	/**
	 * Use this function when the soft keyboard has not been opened yet. This 
	 * will show the emoji popup after the keyboard is up next time.
	 * Generally, you will be calling InputMethodManager.showSoftInput function after 
	 * calling this function.
	 */ 
	public void showAtBottomPending(){
		if(isKeyBoardOpen()) {
			//showAtBottom();
		}
		else
			pendingOpen = true;
	}

	/**
	 * 
	 * @return Returns true if the soft keyboard is open, false otherwise.
	 */
	public Boolean isKeyBoardOpen(){
		return isOpened;
	}

	/**
	 * Dismiss the popup
	 */
	public void dismiss() {
		
		EmojiconRecentsManager.getInstance(mContext).saveRecents();
	}

	/**
	 * Call this function to resize the emoji popup according to your soft keyboard size
	 */
	public void setSizeForSoftKeyboard_(){
//		rootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
//			@Override
//			public void onGlobalLayout() {
//				Rect r = new Rect();
//				rootView.getWindowVisibleDisplayFrame(r);
//
//				int screenHeight = getUsableScreenHeight();
//				int heightDifference = screenHeight
//						- (r.bottom - r.top);
//				int resourceId = mContext.getResources()
//						.getIdentifier("status_bar_height",
//								"dimen", "android");
//				if (resourceId > 0) {
//					heightDifference -= mContext.getResources()
//							.getDimensionPixelSize(resourceId);
//				}
//				if (heightDifference > 100) {
//					keyBoardHeight = heightDifference;
//					setSize(LayoutParams.MATCH_PARENT, keyBoardHeight);
//					if(isOpened == false){
//						if(onSoftKeyboardOpenCloseListener!=null)
//							onSoftKeyboardOpenCloseListener.onKeyboardOpen(keyBoardHeight);
//					}
//					isOpened = true;
//					if(pendingOpen){
//						//showAtBottom();
//						pendingOpen = false;
//					}
//				}
//				else{
//					isOpened = false;
//					if(onSoftKeyboardOpenCloseListener!=null)
//						onSoftKeyboardOpenCloseListener.onKeyboardClose();
//				}
//			}
//		});
	}	

	private int getUsableScreenHeight() {
		return 0;
//	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//	    	DisplayMetrics metrics = new DisplayMetrics();
//	    	
//	    	WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
//	        windowManager.getDefaultDisplay().getMetrics(metrics);	 
//	        
//	        return metrics.heightPixels;
//	        
//	    } else {
//	    	return rootView.getRootView().getHeight();
//	    }
	}

	/**
	 * Manually set the popup window size
	 * @param width Width of the popup
	 * @param height Height of the popup
	 */
	public void setSize(int width, int height){
//		setWidth(width);
//		setHeight(height);
	}

//	@Override
//    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        // Round up a little
//        
//        setMeasuredDimension(widthMeasureSpec, (int) mContext.getResources().getDimension(R.dimen.keyboard_height));
//        
//    }
  
	public void setHeight(int height) {
		
		LinearLayout layout = (LinearLayout) this.findViewById(R.id.layout);
		
		android.view.ViewGroup.LayoutParams param = layout.getLayoutParams();
		param.height = height;
		requestLayout();			
	}
	
	public void setView(int theme) {
		//LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//		LayoutInflater inflater = LayoutInflater.from(mContext);		
//		View view = inflater.inflate(R.layout.emojicons, null, false);
		View view = this;

		tvTitle = findViewById(R.id.title);

		emojisPager = (ViewPager) view.findViewById(R.id.emojis_pager);
		emojisPager.setOnPageChangeListener(this);
		EmojiconRecents recents = this;
		mEmojisAdapter = new EmojisPagerAdapter(
				Arrays.asList(
						new EmojiconRecentsGridView(mContext, null, null, this),
						new EmojiconGridView(mContext, Category1.DATA, recents, this),
						new EmojiconGridView(mContext, Category2.DATA, recents, this),
						new EmojiconGridView(mContext, Category3.DATA, recents, this),
						new EmojiconGridView(mContext, Category4.DATA, recents, this),
						new EmojiconGridView(mContext, Category5.DATA, recents, this)
						)
				);
		emojisPager.setAdapter(mEmojisAdapter);
		mEmojiTabs = new View[6];
		mEmojiTabs[0] = view.findViewById(R.id.emojis_tab_0);
		mEmojiTabs[1] = view.findViewById(R.id.emojis_tab_1);
		mEmojiTabs[2] = view.findViewById(R.id.emojis_tab_2);
		mEmojiTabs[3] = view.findViewById(R.id.emojis_tab_3);
		mEmojiTabs[4] = view.findViewById(R.id.emojis_tab_4);
		mEmojiTabs[5] = view.findViewById(R.id.emojis_tab_5);
		for (int i = 0; i < mEmojiTabs.length; i++) {
			final int position = i;
			mEmojiTabs[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					emojisPager.setCurrentItem(position);
				}
			});
		}
		view.findViewById(R.id.emojis_backspace).setOnTouchListener(new RepeatListener(1000, 50, new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(onEmojiconBackspaceClickedListener != null)
					onEmojiconBackspaceClickedListener.onEmojiconBackspaceClicked(v);
			}
		}));
		

		// get last selected page
		mRecentsManager = EmojiconRecentsManager.getInstance(view.getContext());
		int page = mRecentsManager.getRecentPage();
		// last page was recents, check if there are recents to use
		// if none was found, go to page 1
		if (page == 0 && mRecentsManager.size() == 0) {
			page = 1;
		}

		if (page == 0) {
			onPageSelected(page);
		}
		else {
			emojisPager.setCurrentItem(page, false);
		}


	}

	@Override
	public void addRecentEmoji(Context context, Emojicon emojicon) {
		EmojiconRecentsGridView fragment = ((EmojisPagerAdapter)emojisPager.getAdapter()).getRecentFragment();
		fragment.addRecentEmoji(context, emojicon);
	}


	@Override
	public void onPageScrolled(int i, float v, int i2) {
	}

	@Override
	public void onPageSelected(int i) {
		if (mEmojiTabLastSelectedIndex == i) {
			return;
		}
		switch (i) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
			if (mEmojiTabLastSelectedIndex >= 0 && mEmojiTabLastSelectedIndex < mEmojiTabs.length) {
				mEmojiTabs[mEmojiTabLastSelectedIndex].setSelected(false);
			}
			mEmojiTabs[i].setSelected(true);
			mEmojiTabLastSelectedIndex = i;
			mRecentsManager.setRecentPage(i);
			break;
		}

		int resID = R.string.recent;
		switch (i) {
			case 0: resID = R.string.recent; break;
			case 1: resID = R.string.aunties; break;
			case 2: resID = R.string.activities; break;
			case 3: resID = R.string.food; break;
			case 4: resID = R.string.objects; break;
			case 5: resID = R.string.travel; break;
		}
		tvTitle.setText(mContext.getString(resID));
	}

	@Override
	public void onPageScrollStateChanged(int i) {
	}

	private static class EmojisPagerAdapter extends PagerAdapter {
		private List<EmojiconGridView> views;
		public EmojiconRecentsGridView getRecentFragment(){
			for (EmojiconGridView it : views) {
				if(it instanceof EmojiconRecentsGridView)
					return (EmojiconRecentsGridView)it;
				
			}
			return null;
		}
		public EmojisPagerAdapter(List<EmojiconGridView> views) {
			super();
			this.views = views;
		}

		@Override
		public int getCount() {
			return views.size();
		}


		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View v = views.get(position).rootView;
			((ViewPager)container).addView(v, 0);
			return v;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object view) {
			((ViewPager)container).removeView((View)view);
		}

		@Override
		public boolean isViewFromObject(View view, Object key) {
			return key == view;
		}
	}

	/**
	 * A class, that can be used as a TouchListener on any view (e.g. a Button).
	 * It cyclically runs a clickListener, emulating keyboard-like behaviour. First
	 * click is fired immediately, next before initialInterval, and subsequent before
	 * normalInterval.
	 * <p/>
	 * <p>Interval is scheduled before the onClick completes, so it has to run fast.
	 * If it runs slow, it does not generate skipped onClicks.
	 */
	public static class RepeatListener implements View.OnTouchListener {

		private Handler handler = new Handler();

		private int initialInterval;
		private final int normalInterval;
		private final View.OnClickListener clickListener;

		private Runnable handlerRunnable = new Runnable() {
			@Override
			public void run() {
				if (downView == null) {
					return;
				}
				handler.removeCallbacksAndMessages(downView);
				handler.postAtTime(this, downView, SystemClock.uptimeMillis() + normalInterval);
				clickListener.onClick(downView);
			}
		};

		private View downView;

		/**
		 * @param initialInterval The interval before first click event
		 * @param normalInterval  The interval before second and subsequent click
		 *                        events
		 * @param clickListener   The OnClickListener, that will be called
		 *                        periodically
		 */
		public RepeatListener(int initialInterval, int normalInterval, View.OnClickListener clickListener) {
			if (clickListener == null)
				throw new IllegalArgumentException("null runnable");
			if (initialInterval < 0 || normalInterval < 0)
				throw new IllegalArgumentException("negative interval");

			this.initialInterval = initialInterval;
			this.normalInterval = normalInterval;
			this.clickListener = clickListener;
		}

		public boolean onTouch(View view, MotionEvent motionEvent) {
			switch (motionEvent.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downView = view;
				handler.removeCallbacks(handlerRunnable);
				handler.postAtTime(handlerRunnable, downView, SystemClock.uptimeMillis() + initialInterval);
				clickListener.onClick(view);
				return true;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_OUTSIDE:
				handler.removeCallbacksAndMessages(downView);
				downView = null;
				return true;
			}
			return false;
		}
	}

	public interface OnEmojiconBackspaceClickedListener {
		void onEmojiconBackspaceClicked(View v);
	}

	public interface OnKeyboardCloseListener{
		void onKeyboardClose();
	}

	public interface OnKeyboardSpaceListener{
		void onKeyboardSpaceClicked();
	}

	public interface OnSoftKeyboardOpenCloseListener{
		void onKeyboardOpen(int keyBoardHeight);
		void onKeyboardClose();
	}
}
