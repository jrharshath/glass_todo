package com.jugaad.glasstodo.view;

public interface ReorderListener {
	void move(int index, boolean up); // return new index

	void selectItem(int index);

	void startDragging(int index);
	
	void stopDragging(int index);
}
