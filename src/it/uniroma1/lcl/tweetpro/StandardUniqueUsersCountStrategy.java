package it.uniroma1.lcl.tweetpro;

/**
 * Concrete strategy to compute the number of unique users in a corpus
 * @author Davide Quaranta
 *
 */
public class StandardUniqueUsersCountStrategy implements UniqueUsersCountStrategy {

	@Override
	public int get(TweetCorpus tweetCorpus) {
		return tweetCorpus.getUsers().size();
	}

}
