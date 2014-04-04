package com.jugaad.glasstodo.view;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class ReorderableListView extends ListView {

	private static String TAG = "ReorderableListView";

	private GestureDetector mGestureDetector;
	private boolean dragging = false;

	public ReorderableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ReorderableListView(Context context) {
		super(context);
		init(context);
	}

	public ReorderableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private float accumulatedDelta = 0;

	private ReorderListener mListener;

	private void init(Context context) {
		mGestureDetector = new GestureDetector(context);

		final float minDelta = 100;

		final ListView list = this;
		mGestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
			@Override
			public boolean onScroll(float displacement, float delta, float velocity) {
				if(!dragging) {
					return false;
				}

				accumulatedDelta += delta;
				Log.i(TAG, "Delta = " +delta + ", accumulated = " + accumulatedDelta);

				if(accumulatedDelta >= minDelta || accumulatedDelta <= -minDelta) {

					boolean moveUp = accumulatedDelta < 0;
					accumulatedDelta = 0;

					// move the current selection
					int selIndex = list.getSelectedItemPosition();

					if(mListener != null) {
						mListener.move(selIndex, moveUp);
					}

				}
				return true;
			}
		});

		mGestureDetector.setBaseListener(new GestureDetector.BaseListener() {
			@Override
			public boolean onGesture(Gesture gesture) {

				if(dragging) {
					if (gesture == Gesture.TAP || gesture == Gesture.LONG_PRESS) {
						Log.i(TAG, "Stop drag mode");
						dragging = false;
						accumulatedDelta = 0;
						mListener.doneDragging();
					}
					return true;
				}

				// if not dragging
				if (gesture == Gesture.LONG_PRESS) {
					Log.i(TAG, "Start drag mode");
					dragging = true;
					mListener.startDragging();
					return true;
				} 

				return false;
			}
		});
	}	

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		if (mGestureDetector != null) {
			return mGestureDetector.onMotionEvent(event);
		}
		return false;
	}

	public void addReorderListener(ReorderListener listener) {
		mListener = listener;		
	}
}
