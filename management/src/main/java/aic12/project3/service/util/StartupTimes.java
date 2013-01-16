package aic12.project3.service.util;

import java.util.LinkedList;
import java.util.List;

public class StartupTimes {

	private List<Long> list = new LinkedList<Long>();
	private int _size;
	private int _curSize;

	public StartupTimes(int size) {
		_size = size;
		_curSize = 0;
	}

	public synchronized void add(long timeToStartup) {
		list.add(0, timeToStartup);
		_curSize++;
		while(_curSize > _size) {
			_curSize--;
			list.remove(_size);
		}
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public synchronized int calculateAverageStartupTime() {
		double totalTime = 0;
		for(Long time : list) {
			totalTime += time;
		}
		return (int) (totalTime/_curSize);
	}

}
