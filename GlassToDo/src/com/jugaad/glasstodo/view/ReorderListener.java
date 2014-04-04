package com.jugaad.glasstodo.view;

public interface ReorderListener {
	void move(int index, boolean up);

	void doneDragging();

	void startDragging();
}
