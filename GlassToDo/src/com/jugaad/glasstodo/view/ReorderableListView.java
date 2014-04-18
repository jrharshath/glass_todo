package com.jugaad.glasstodo.view;

import com.google.android.glass.media.Sounds;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;

import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

public class ReorderableListView extends ListView {

	private static String TAG = "ReorderableListView";

	private GestureDetector mGestureDetector;
	private boolean dragging = false;
	private int selectionIndex;

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

	private AudioManager mAudio;
	
	

	private void init(Context context) {
		mGestureDetector = new GestureDetector(context);

		final float minDelta = 100;

		final ListView list = this;
		mGestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
			@Override
			public boolean onScroll(float displacement, float delta, float velocity) {
				accumulatedDelta += delta;
				Log.d(TAG, "Delta = " +delta + ", accumulated = " + accumulatedDelta);

				if(accumulatedDelta >= minDelta || accumulatedDelta <= -minDelta) {
					boolean moveUp = accumulatedDelta < 0;
					accumulatedDelta = 0;
					
					if(selectionIndex<=0 && moveUp) { return true; }
					if(selectionIndex>=getAdapter().getCount()-1 && !moveUp) { return true; }
					
					int newIndex = selectionIndex + (moveUp ? -1 : 1);
					
					mAudio.playSoundEffect(Sounds.SELECTED);

					if(dragging) {
						if(mListener != null) {
							mListener.move(selectionIndex, moveUp);
							selectionIndex = newIndex;
						}
					} else {
						selectionIndex = newIndex;
					}
					setSelection(newIndex);
				}
				return true;
			}
		});

		mGestureDetector.setBaseListener(new GestureDetector.BaseListener() {
			@Override
			public boolean onGesture(Gesture gesture) {

				if(dragging) {
					if (gesture == Gesture.TAP || gesture == Gesture.LONG_PRESS) {
						Log.d(TAG, "Stop drag mode");
						dragging = false;
						accumulatedDelta = 0;
						mAudio.playSoundEffect(Sounds.TAP);
						mListener.stopDragging(selectionIndex);
						return false;
					}
					return true;
				}

				// start dragging on long press
				if (gesture == Gesture.LONG_PRESS && mListener != null) {
					Log.d(TAG, "Start drag mode");
					dragging = true;
					mAudio.playSoundEffect(Sounds.TAP);
					mListener.startDragging(selectionIndex);
					return true;
				} 
				
				// signal selection on tap
				if(gesture == Gesture.TAP && mListener != null) {
					Log.d(TAG, "Select item");
					mListener.selectItem(selectionIndex);
					mAudio.playSoundEffect(Sounds.TAP);
					return true;
				}

				return false;
			}
		});
		
		selectionIndex = 0;
		setSelection(selectionIndex);
	}	

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		if (mGestureDetector != null) {
			boolean handled = mGestureDetector.onMotionEvent(event);
			if(handled) return false;
			else return super.onGenericMotionEvent(event);
		}
		return super.onGenericMotionEvent(event);
	}

	public void addReorderListener(ReorderListener listener) {
		mListener = listener;		
	}

	public void setAudio(AudioManager audio) {
		this.mAudio = audio;		
	}

	public int getSelectionIndex() {
		return selectionIndex;
	}
}
