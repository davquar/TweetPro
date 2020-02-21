package it.uniroma1.lcl.tweetpro;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * Object to parse a text file representing a tweet corpus.
 * A tweet corpus file has this characteristics:
 * <ul>
 * 	<li>Every line is a tweet;</li>
 * 	<li>The line format is standard Twitter JSON, without line breaks.</li>
 * </ul>
 * @author Davide Quaranta
 *
 */
public class JsonParser {

	private static final char QUOTE_MARK = '"';
	private static final char BACKSLASH = '\\';
	private static final String COLON = ":";
	private static final String COMMA = ",";
	
	/**
	 * Strings used to determine key blocks of the document
	 * @author Davide Quaranta
	 *
	 */
	private enum JsonKey {
		ID("id:"),
		TWEET_TEXT_CLEAN("text"),
		TEXT("text:"),
		ID_STR("id_str:"),
		NAME("name:"),
		SCREEN_NAME("screen_name:"),
		FOLLOWERS_COUNT("followers_count:"),
		FRIENDS_COUNT("friends_count:"),
		FAVOURITES_COUNT("favourites_count:"),
		VERIFIED("verified:"),
		STATUSES_COUNT("statuses_count:"),
		RETWEETED_STATUS("retweeted_status:"),
		RETWEETED_STATUS_CLEAN("retweeted_status"),
		RETWEET_COUNT("retweet_count:"),
		FAVORITE_COUNT("favorite_count:"),
		ENTITIES("entities:"),
		HASHTAGS("hashtags:"),
		SYMBOLS("symbols:"),
		MEDIA("media:"),
		MEDIA_URL("media_url:"),
		TWEET_END("favorited:");
		
		private String key;
		
		JsonKey(String key) {
			this.key = key;
		}

		@Override
		public java.lang.String toString() {
			return key;
		}
		
		/**
		 * Makes the key look ugly.
		 * </br>Appends quote marks and colon to make it look like "key":
		 * </br>This method is used to uglify the TWEET_TEXT_CLEAN key, to easily extract the tweet text from the pure JSON string
		 * @return
		 */
		public String uglify() {
			return new StringBuilder()
					.append(QUOTE_MARK)
					.append(key)
					.append(QUOTE_MARK)
					.append(COLON)
					.toString();
		}
	}
	
	/** The lines of the document **/
	private List<String> lines;
	
	/** The list of tweets **/
	List<Tweet> tweetList;
	
	/** The map from userId to User **/
	Map<Long, User> userMap;
	
	/**
	 * Constructor for class JsonParser
	 * @param lines the list of JSON lines that represent tweets
	 */
	public JsonParser(List<String> lines) {
		this.lines = lines;
		tweetList = new LinkedList<>();
		userMap = new TreeMap<>();
	}
	
	/**
	 * Parses the document.
	 * For each non-empty line:
	 * <ol>
	 * 	<li>Initializes the builders for tweet and an eventual retweet;</li>
	 * 	<li>Extracts the text of the tweet;
	 * 		<br>If it is a retweet, extracts its text too;</li>
	 * 	<li>Cleans the line by removing the characters <code>"[]{}</code>;</li>
	 * 	<li>Splits on commas in a new list;</li>
	 * 	<li>Calls parseTweet() on the splitted list.</li>
	 * </ol>
	 * @return a list of tweets and a map of users
	 */
	public Pair<List<Tweet>, Map<Long, User>> parse() {
		lines.forEach(line -> {
			if (line.isEmpty())	return;
			Tweet.Builder tweetBuilder = new Tweet.Builder();
			Tweet.Builder originalTweetBuilder = new Tweet.Builder();
			
			// find the TEXT key and value
			String textKey = JsonKey.TWEET_TEXT_CLEAN.uglify();
	
			int startQuote = line.indexOf(textKey) + textKey.length() + 1;

			for (int i=startQuote; i<line.length(); i++) {
				// if the quote char is closing a key/value
				if (line.charAt(i) == QUOTE_MARK && line.charAt(i-1) != BACKSLASH) {
					tweetBuilder.setText(line.substring(startQuote, i));
					break;
				}
			}
			
			// if it is a retweet, find TEXT key and value too
			String retweetKey = JsonKey.RETWEETED_STATUS_CLEAN.uglify();
			int startRetweet = line.indexOf(retweetKey);
			if (startRetweet > -1) {
				originalTweetBuilder = new Tweet.Builder();
				startRetweet += retweetKey.length() + 1;
				startQuote = line.indexOf(textKey, startRetweet) + textKey.length() + 1;
				
				for (int i=startQuote; i<line.length(); i++) {
					// if the quote char is closing a key/value
					if (line.charAt(i) == QUOTE_MARK && line.charAt(i-1) != BACKSLASH) {
						originalTweetBuilder.setText(line.substring(startQuote, i));
						break;
					}
				}
			}
			
			// Regex to remove characters "[]{}
			line = line.replaceAll("\"|\\[|\\]|\\{|\\}", "");
			List<String> splitted = Arrays.asList(line.split(COMMA));
			
			parseTweet(splitted, tweetBuilder, originalTweetBuilder, false);			
		});
		
		return new Pair<List<Tweet>, Map<Long, User>>(tweetList, userMap);
	}
	
	/**
	 * Parse a specific tweet, given a list where each item is the result of a split on commas.
	 * @param splitted the list splitted by comma
	 * @param tweetBuilder the buider for the Tweet
	 * @param originalTweetBuilder the builder for the eventual original tweet
	 * @param isOriginalTweet true if this tweet is an original tweet, nested in a tweet
	 * @return the parsed Tweet
	 */
	private Tweet parseTweet(List<String> splitted, Tweet.Builder tweetBuilder, Tweet.Builder originalTweetBuilder, boolean isOriginalTweet) {		
		/* tweet id */
		String tweetId = getValue(splitted, JsonKey.ID);
		tweetBuilder.setId(tweetId);

		splitted = cutList(splitted, JsonKey.TEXT.toString());

		/* user things */
		User user = null;
		String userId = getValue(splitted, JsonKey.ID_STR);

		if (!userMap.containsKey(Long.valueOf(userId))) {
			User.Builder userBuilder = new User.Builder()
				.setId(userId)
				.setScreenName(getValue(splitted, JsonKey.SCREEN_NAME))
				.setFollowersCount(getValue(splitted, JsonKey.FOLLOWERS_COUNT))
				.setFriendsCount(getValue(splitted, JsonKey.FRIENDS_COUNT))
				.setFavsCount(getValue(splitted, JsonKey.FAVOURITES_COUNT))
				.setVerified(getValue(splitted, JsonKey.VERIFIED))
				.setTweetCount(getValue(splitted, JsonKey.STATUSES_COUNT));
				
			/* handle empty public names */
			try {
				userBuilder.setName(getValue(splitted, JsonKey.NAME));
			} catch (ArrayIndexOutOfBoundsException e) {
				// it means that the user has an empty public name, because .split(":")[1] in getValue fails
				userBuilder.setName("");
			}
			user = userBuilder.build();
			tweetBuilder.setUser(user);
			if (!isOriginalTweet)	userMap.put(user.getId(), user);
		} else {
			tweetBuilder.setUser(userMap.get(Long.valueOf(userId)));
		}
		
		splitted = cutList(splitted, JsonKey.STATUSES_COUNT.toString());
		
		/* retweet things */ 
		if (isRetweet(splitted)) {
			long originalTweetId = Long.parseLong(getValue(splitted, JsonKey.ID));
			Tweet originalTweet = tweetList.stream().filter(x -> x.getID() == originalTweetId).findFirst().orElse(null);
			// if the originalTweet doesn't yet exist, create it right now
			if (originalTweet == null) {
				int start = splitted.indexOf(splitted.stream().filter(x -> x.startsWith(JsonKey.RETWEETED_STATUS.toString())).findFirst().get());
				int end = splitted.indexOf(splitted.stream().filter(x -> x.startsWith(JsonKey.TWEET_END.toString())).findFirst().get());
				// we pass retweetBuilder, so parseTweet() will parse the original tweet, and put its data in retweetBuilder, that will be returned.
				// it is irrelevant what we pass as second builder parameter, so we can put null.
				originalTweet = parseTweet(splitted.subList(start, end), originalTweetBuilder, null, true);				
			}
			
			tweetBuilder.setOriginalTweet(originalTweet);
		}
		
		/* retweeted count */
		tweetBuilder
			.setRtCount(getValue(splitted, JsonKey.RETWEET_COUNT))
			.setLikeCount(getValue(splitted, JsonKey.FAVORITE_COUNT));

		// cut at ENTITIES and not HASHTAGS, because the list item to stop cutting at would be in the form "entities:hashtags:", so .startsWith("hashtags") --> false.
		splitted = cutList(splitted, JsonKey.ENTITIES.toString());
		
		/* hashtag things */
		if (hasHashtags(splitted)) {
			// special treatment to get the 1st hashtag.
			// [entities:hashtags:text:cooltag][3] --> "cooltag".
			tweetBuilder.addHashtag(splitted.get(0).split(COLON)[3]);
			// now every hashtag starts with text:
			tweetBuilder.addHashtags(splitted.stream().filter(x -> x.startsWith(JsonKey.TEXT.toString())).map(x -> x.split(COLON)[1]).collect(Collectors.toList()));
		}
		
		/* media things */
		tweetBuilder.setMediaUrl(splitted.stream().filter(x -> x.startsWith(JsonKey.MEDIA_URL.toString())).map(x -> "http:" + x.split(COLON)[2].replace("\\", "")).findFirst());
		
		/* build and save this tweet */
		Tweet tweet = tweetBuilder.build();
		if (!isOriginalTweet)	tweetList.add(tweet);
		
		return tweet;
	}
	
	/**
	 * Checks if the given slice of list contains hashtags, assuming that the first element is entities:hashtags:
	 * <br>If the 2nd item starts with "indices", it means that there is at least one hashtag.
	 * @param list the list starting with "entities".
	 * @return true if the 2nd item starts with "indices"
	 */
	private static boolean hasHashtags(List<String> list) {
		return list.get(1).startsWith("indices");
	}
	
	/**
	 * Checks if the given slice of list contains an original tweet, by checking the presence of RETWEETED_STATUS.
	 * @param list the list to check
	 * @return true if RETWEETED_STATUS is present
	 */
	private static boolean isRetweet(List<String> list) {
		return list.stream().anyMatch(x -> x.startsWith(JsonKey.RETWEETED_STATUS.toString()));
	}
	
	/**
	 * Cuts the given list, from 0 to indexOf(key), if key is present; otherwise returns the untouched list.
	 * @param list the list to cut
	 * @param key the string at the position to cut to
	 * @return the sublist starting from 0 to indexOf(key)
	 */
	private static List<String> cutList(List<String> list, String key) {
		int size = list.size();
		for (int i=0; i<size; i++) {
			if (list.get(i).startsWith(key))	return list.subList(i, size);
		}
		return list;
	}
	
	/**
	 * Gets the value associated to the given key in the given splitted list.
	 * <br>The pairs (key, value) that we need to retrieve using this method, are in the form key:value.
	 * <br>For this reason, this method finds the first list item that starts with the given key, and returns the value after the COLON.
	 * @param list the splitted list to get the value from
	 * @param key the string to find
	 * @return the value associated to the given key
	 */
	private static String getValue(List<String> list, JsonKey key) {
		return list.stream().filter(x -> x.startsWith(key.toString())).findFirst().orElse(null).split(COLON)[1];
	}
	
}
