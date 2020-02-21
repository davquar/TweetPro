package it.uniroma1.lcl.tweetpro;

/**
 * Abstract strategy to compute the number of unique Users in a TweetCorpus object.
 * @author Davide Quaranta
 *
 */
public interface UniqueUsersCountStrategy {

	/**
	 * Gets the number of unique Users in the given TweetCorpus.
	 * @param tweetCorpus the TweetCorpus to analyze
	 * @return the number of unique users
	 */
	int get(TweetCorpus tweetCorpus);
	
}
