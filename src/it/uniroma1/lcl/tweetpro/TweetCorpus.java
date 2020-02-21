package it.uniroma1.lcl.tweetpro;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An iterable collection of Tweets.
 * @author Davide Quaranta
 *
 */
public class TweetCorpus implements Iterable<Tweet> {
	
	/** List of Tweets */
	private List<Tweet> tweetList;
	
	/** Map from userId to User object */
	private Map<Long, User> userMap;
	
	/** Strategy to get the top k hashtags in this corpus */
	private TopHashtagsStrategy topHashtagsStrategy;
	
	/** Strategy to get the number of unique users in this corpus */
	private UniqueUsersCountStrategy uniqueUsersCountStrategy;

	/**
	 * Private constructor, because a TweetCorpus can only be created after parsing a JSON file.
	 * <br>Initializes the fields, and sets the default counting strategies.
	 * @param tweetList the list of tweets to put in the corpus
	 * @param userMap the map (userId, User) to put in the corpus
	 */
	private TweetCorpus(List<Tweet> tweetList, Map<Long, User> userMap) {
		this.tweetList = tweetList;
		this.userMap = userMap;
		setTopHashtagsStrategy(new CountMinTopHashtagsStrategy());
		setUniqueUsersCountStrategy(new StandardUniqueUsersCountStrategy());
	}
	
	/**
	 * Gets the Tweet list
	 * @return the list of Tweet object in this corpus
	 */
	public List<Tweet> getTweetList() { return tweetList; }
	
	/**
	 * Gets the Users
	 * @return the set of User objects in this corpus
	 */
	public Set<User> getUsers() { return userMap.values().stream().collect(Collectors.toSet()); }
	
	/**
	 * Given a valid Twitter JSON parseable file, parses its content and creates a corpus.
	 * @param file the JSON file to parse
	 * @return the TweetCorpus object representing the given file
	 */
	public static TweetCorpus parseFile(File file) {
		BufferedReader br = null;
		List<String> lines = new LinkedList<>();
		
		try {
			br = Files.newBufferedReader(Paths.get(file.toString()));
			lines = br.lines().collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
				
		JsonParser jsonParser = new JsonParser(lines);
		Pair<List<Tweet>, Map<Long, User>> pair = jsonParser.parse();

		return new TweetCorpus(pair.getFirst(), pair.getSecond());
	}
		
	/**
	 * Gets the tweet count
	 * @return the number of tweets in this corpus
	 */
	public int getTweetCount() { return tweetList.size(); }
	
	/**
	 * Gets the tweets of the given user
	 * @param user the user whom tweets are to retrieve
	 * @return the list of tweets of the given user
	 */
	public List<Tweet> getTweets(User user) {
		return tweetList.stream().filter(x -> x.getUser().equals(user)).collect(Collectors.toList());
	}
	
	/**
	 * Sets the strategy to compute the top k hashtags in the corpus
	 * @param topHashtagsStrategy the strategy to use
	 */
	public void setTopHashtagsStrategy(TopHashtagsStrategy topHashtagsStrategy) {
		this.topHashtagsStrategy = topHashtagsStrategy;
	}
	
	/**
	 * Gets the top k hashtags in this corpus
	 * @param k the number to limit the ranking length to
	 * @return the list of the top k hashtags
	 */
	public List<String> getTopHashtags(int k) {
		return topHashtagsStrategy.get(this, k);
	}
	
	/**
	 * Sets the strategy to compute the unique users in the corpus
	 * @param uniqueUsersCountStrategy the strategy to use
	 */
	public void setUniqueUsersCountStrategy(UniqueUsersCountStrategy uniqueUsersCountStrategy) {
		this.uniqueUsersCountStrategy = uniqueUsersCountStrategy;
	}
	
	/**
	 * Gets the unique users in this corpus
	 * @return the number of unique users
	 */
	public int getUniqueUsersCount() {
		return uniqueUsersCountStrategy.get(this);
	}

	@Override
	public Iterator<Tweet> iterator() {
		return tweetList.iterator();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(tweetList, userMap);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)	return true;
		if (obj == null)	return false;
		if (!(obj instanceof TweetCorpus))	return false;
		TweetCorpus other = (TweetCorpus) obj;
		return tweetList.equals(other.tweetList) && userMap.equals(other.userMap);
	}
}