package aic.project3.analysis.core.classifier;

import java.util.HashMap;
import java.util.Map;

/**
 * class representing a tweet in weighted majority algorithm
 */
public class Item {
	
	private String _text;
	private int _polarity;
	private int _target = -1;
	private Map<Integer, Integer> _cl2pol;
	
	/**
	 * @param text the tweet's text
	 */
	public Item(String text) {
		this._text = text;
		_cl2pol = new HashMap<Integer, Integer>();
	}
	
	/**
	 * gets the tweet's text 
	 * @return the tweet's text
	 */
	public String getText() {
		return _text;
	}
	
	/**
	 * sets a given text
	 * @param text the tweet's text
	 */
	public void setText(String text) {
		this._text = text;
	}
	
	/**
	 * gets the tweet's polarity
	 * @return the tweet's polarity
	 */
	public int getPolarity() {
		return _polarity;
	}
	
	/**
	 * sets the tweet's polarity
	 * @param polarity the tweet's polarity
	 */
	public void setPolarity(int polarity) {
		this._polarity = polarity;
	}
	
	/**
	 * gets a map that associates classifiers' id to polarities
	 * @return a map that associates classifiers' id to polarities
	 */
	public Map<Integer, Integer> getCl2pol() {
		return _cl2pol;
	}
	
	/**
	 * sets a given map
	 * @param cl2pol a map that associates classifiers' id to polarities
	 */
	public void setCl2pol(Map<Integer, Integer> cl2pol) {
		this._cl2pol = cl2pol;
	}
	
	/**
	 * gets the tweet's target polarity
	 * @return tweet's target polarity
	 */
	public int getTarget() {
		return _target;
	}
	
	/**
	 * sets a given target polarity
	 * @param target the tweet's target polarity
	 */
	public void setTarget(int target) {
		this._target = target;
	}
	
	
}
