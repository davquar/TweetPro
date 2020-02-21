package it.uniroma1.lcl.tweetpro;

import java.util.List;

/**
 * Abstract strategy to compute the top k hashtags of a corpus
 * @author Davide Quaranta
 *
 */
public interface TopHashtagsStrategy {

	/**
	 * Gets the k more used hashtags in the corpus
	 * @param tweetCorpus the corpus to analyze
	 * @param k the number to limit the hashtag list to
	 * @return the list containing the top k hashtags
	 */
	List<String> get(TweetCorpus tweetCorpus, int k);
	
}
