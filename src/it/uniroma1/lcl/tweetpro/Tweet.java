package it.uniroma1.lcl.tweetpro;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A Tweet of Twitter
 * @author Davide Quaranta
 *
 */
public class Tweet {
	
	/** Unique numeric identifier **/
	private long id;
	
	/** The author **/
	private User user;
	
	/** The body **/
	private String text;
	
	/** The hashtags contained **/
	private List<String> hashtags;
	
	/** The number of likes received **/
	private int likeCount;
	
	/** The number of reweets **/
	private int rtCount;
	
	/** The URL of an optional media contained **/
	private URL mediaUrl;
	
	/** Reference to the original tweet, if this tweet is a retweet **/
	private Tweet originalTweet;
	
	/**
	 * Constructor for the Tweet class
	 * @param id the unique numeric identifier
	 * @param user the unique string identifier
	 * @param text the body of the tweet
	 * @param hashtags the list of hashtags
	 * @param likeCount the number of likes
	 * @param rtCount the number of retweets
	 * @param mediaUrl the url of the media content included in the tweet
	 * @param originalTweet the reference to the original tweet
	 */
	private Tweet(long id, User user, String text, List<String> hashtags, int likeCount, int rtCount, URL mediaUrl, Tweet originalTweet) {
		this.id = id;
		this.user = user;
		this.text = text;
		this.hashtags = new ArrayList<>(hashtags);
		this.likeCount = likeCount;
		this.rtCount = rtCount;
		this.mediaUrl = mediaUrl;
		this.originalTweet = originalTweet;
	}

	/**
	 * Gets the numeric unique identifier of the tweet
	 * @return the tweet id
	 */
	public long getID() { return id; }
	
	/**
	 * Gets the unique string identifier of the tweet
	 * @return the user name
	 */
	public User getUser() { return user; }
	
	/**
	 * Gets the tweet body
	 * @return the body of the tweet
	 */
	public String getText() { return text; }
	
	/**
	 * Gets the hashtags of the tweet
	 * @return the list of hashtags
	 */
	public List<String> getHashtags() { return hashtags; }
	
	/**
	 * Gets the likes count
	 * @return the number of likes
	 */
	public int getLikeCount() { return likeCount; }
	
	/**
	 * Gets the number of times this tweet has been retweeted
	 * @return the retweet count
	 */
	public int getRTCount() { return rtCount; }
	
	/**
	 * Checks if this tweet is a retweet
	 * @return true if the original tweet reference is null
	 */
	public boolean isRetweet() { return originalTweet != null; }
	
	/**
	 * Gets the original tweet
	 * @return the retweeted tweet
	 */
	public Tweet getOriginalTweet() { return originalTweet; }
	
	/**
	 * Gets the media URL
	 * @return an Optional containing the URL of the media content
	 */
	public Optional<URL> getMedia() {
		return Optional.ofNullable(mediaUrl);
	}
	
	@Override
	public String toString() {
		return getText();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id, user, text, hashtags, likeCount, rtCount, mediaUrl, originalTweet);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)	return true;
		if (obj == null)	return false;
		if (!(obj instanceof Tweet)) return false;
		Tweet other = (Tweet) obj;
		return 
			id == other.id &&
			user.equals(other.user) &&
			text.equals(other.text) &&
			hashtags.equals(other.hashtags) &&
			likeCount == other.likeCount &&
			rtCount == other.rtCount &&
			mediaUrl.equals(other.mediaUrl) &&
			originalTweet.equals(other.originalTweet);
	}

	/**
	 * Builder class for Tweet
	 * @author Davide Quaranta
	 *
	 */
	public static class Builder {
		
		private long id;
		private User user;
		private String text;
		private List<String> hashtags;
		private int likeCount;
		private int rtCount;
		private URL mediaUrl;
		private Tweet originalTweet;
		
		public Builder() {
			hashtags = new LinkedList<>();
		}
		
		/**
		 * Sets the tweet id
		 * @param id the long id
		 * @return the builder instance
		 */
		public Builder setId(long id) {
			this.id = id;
			return this;
		}
		
		/**
		 * Sets the tweet id
		 * @param id the string containing the id
		 * @return the builder instance
		 */
		public Builder setId(String id) {
			return setId(Long.valueOf(id));
		}
		
		/**
		 * Sets the tweet author
		 * @param user the author of the tweet
		 * @return the builder instance
		 */
		public Builder setUser(User user) {
			this.user = user;
			return this;
		}
		
		/**
		 * Sets the tweet body
		 * @param text the tweet body
		 * @return the builder instance
		 */
		public Builder setText(String text) {
			this.text = text;
			return this;
		}
		
		/**
		 * Sets the hashtags contained in the tweet
		 * @param hashtags the list of hashtags
		 * @return the builder instance
		 */
		public Builder setHashtags(List<String> hashtags) {
			this.hashtags = new ArrayList<>(hashtags);
			return this;
		}
		
		/**
		 * Adds the given hashtag to the hashtag list
		 * @param hashtag the hashtag to add
		 * @return the builder instance
		 */
		public Builder addHashtag(String hashtag) {
			this.hashtags.add(hashtag);
			return this;
		}
		
		/**
		 * Adds all the given hashtags to the hashtag list
		 * @param hashtags the list of hashtags to add
		 * @return the builder instance
		 */
		public Builder addHashtags(List<String> hashtags) {
			this.hashtags.addAll(hashtags);
			return this;
		}
		
		/**
		 * Sets the number of likes to this tweet
		 * @param likeCount the number of likes of this tweet
		 * @return the builder instance
		 */
		public Builder setLikeCount(int likeCount) {
			this.likeCount = likeCount;
			return this;
		}
		
		/**
		 * Sets the number of like to this tweet
		 * @param likeCount the string containing the integer number of likes
		 * @return the builder instance
		 */
		public Builder setLikeCount(String likeCount) {
			return setLikeCount(Integer.valueOf(likeCount));
		}
		
		/**
		 * Sets the number of retweets to this tweet
		 * @param rtCount the number of retweets
		 * @return the builder instance
		 */
		public Builder setRtCount(int rtCount) {
			this.rtCount = rtCount;
			return this;
		}
		
		/**
		 * Sets the number of retweets to this tweet
		 * @param rtCount the string containing the integer number of retweets
		 * @return the builder instance
		 */
		public Builder setRtCount(String rtCount) {
			return setRtCount(Integer.valueOf(rtCount));
		}
		
		/**
		 * Sets the URL of the media file contained in this tweet
		 * @param mediaUrl the media URL
		 * @return the builder instance
		 */
		private Builder setMediaUrl(URL mediaUrl) {
			this.mediaUrl = mediaUrl;
			return this;
		}
		
		/**
		 * Sets the URL of the media file contained in this tweet, given a string that contains a valid URL
		 * @param mediaUrl the string containing the URL
		 * @return the builder instance
		 */
		private Builder setMediaUrl(String mediaUrl) {
			try {
				return setMediaUrl(new URL(mediaUrl));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return this;
		}
		
		/**
		 * Sets the URL of the media file contained in this tweet, given an Optional that contains a valid String URL
		 * @param mediaUrl the optional containing the String URL
		 * @return the builder instance
		 */
		public Builder setMediaUrl(Optional<String> mediaUrl) {
			if (mediaUrl.isPresent())	return setMediaUrl(mediaUrl.get());
			return this;
		}
		
		/**
		 * Sets the original tweet that this tweet is retweeting
		 * @param originalTweet the retweeted tweet
		 * @return the builder instance
		 */
		public Builder setOriginalTweet(Tweet originalTweet) {
			this.originalTweet = originalTweet;
			return this;
		}
		
		/**
		 * Creates a Tweet object, as configured through the builder methods
		 * @return the Tweet object
		 */
		public Tweet build() {
			return new Tweet(id, user, text, hashtags, likeCount, rtCount, mediaUrl, originalTweet);
		}
	}

}
