package it.uniroma1.lcl.tweetpro;

import java.util.Objects;

/**
 * Utility class to represent a pair of objects
 * @author Davide Quaranta
 *
 * @param <T> the type of the first object
 * @param <S> the type of the second object
 */
public class Pair<T, S> {
	
	private T first;
	private S second;
	
	/**
	 * Constructor for class Pair
	 * @param first the first object
	 * @param second the second object
	 */
	public Pair(T first, S second) {
		this.first = first;
		this.second = second;
	}
	
	/**
	 * Gets the first object
	 * @return the first object
	 */
	public T getFirst() { return first; }
	
	/**
	 * Gets the second object
	 * @return the second object
	 */
	public S getSecond() { return second; }
	
	@Override
	public int hashCode() {
		return Objects.hash(first, second);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)			return true;
		if (obj == null)			return false;
		if (!(obj instanceof Pair))	return false;
		Pair<T, S> other = (Pair<T, S>) obj;
		return first.equals(other.first) && second.equals(other.second);
	}
	
}
