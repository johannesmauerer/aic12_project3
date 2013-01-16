package aic12.project3.service.util;

import java.util.LinkedList;
import java.util.List;

public class FifoWithAverageCalculation {

	private List<Long> list = new LinkedList<Long>();
	private int _size;
	private int _curSize;

	public FifoWithAverageCalculation(int size) {
		_size = size;
		_curSize = 0;
	}

	public synchronized void add(long measurement) {
		list.add(0, measurement);
		_curSize++;
		while(_curSize > _size) {
			_curSize--;
			list.remove(_size);
		}
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public synchronized int calculateAverage() {
		double totalTime = 0;
		for(Long time : list) {
			totalTime += time;
		}
		return (int) (totalTime/_curSize);
	}

}
