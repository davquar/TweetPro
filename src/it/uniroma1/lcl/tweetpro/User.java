package it.uniroma1.lcl.tweetpro;

import java.util.Objects;

/**
 * A Twitter user
 * @author Davide Quaranta
 *
 */
public class User {
	
	/** Unique numeric identifier */
	private long id;
	
	/** Unique string identifier */
	private String name;
	
	/** Displayed name */
	private String screenName;
	
	/** Number of tweets of this user */
	private long tweetsCount;
	
	/** Number of Tweets that this user likes */
	private long favsCount;
	
	/** Number of Users following this user */
	private long followersCount;
	
	/** Number of Users that this user follows */
	private long friendsCount;
	
	/** Verification mark for this user */
	private boolean verified;
	
	/**
	 * Constructor for User class
	 * @param id the unique user id
	 * @param name the unique user name
	 * @param screenName the displayed name
	 * @param tweetsCount the number of tweets
	 * @param favsCount the number of liked tweets
	 * @param followersCount the number of followers
	 * @param friendsCount the number of followed users
	 * @param verified the verification mark
	 */
	private User(long id, String name, String screenName, long tweetsCount, long favsCount, long followersCount, long friendsCount, boolean verified) {
		this.id = id;
		this.name = name;
		this.screenName = screenName;
		this.tweetsCount = tweetsCount;
		this.favsCount = favsCount;
		this.followersCount = followersCount;
		this.friendsCount = friendsCount;
		this.verified = verified;
	}
	
	/**
	 * Gets the user id
	 * @return the unique user id
	 */
	public long getId() { return id; }
	
	/**
	 * Gets the user's displayed name
	 * @return the displayed name
	 */
	public String getName() { return name; }
	
	/**
	 * Gets the user's unique name
	 * @return the unique name
	 */
	public String getScreenName() { return screenName; }
	
	/**
	 * Gets the tweets count
	 * @return the number of tweets
	 */
	public long getTweetsCount() { return tweetsCount; }
	
	/**
	 * Gets the favourites count
	 * @return the number of liked tweets
	 */
	public long getFavsCount() { return favsCount; }
	
	/**
	 * Gets the followers count
	 * @return the number of followers
	 */
	public long getFollowersCount() { return followersCount; }
	
	/**
	 * Gets the followed count
	 * @return the number of followed users
	 */
	public long getFriendsCount() { return friendsCount; }
	
	/**
	 * Checks if the user is verified (blue mark near his name)
	 * @return true if the user is verified
	 */
	public boolean isVerified() { return verified; }


	@Override
	public int hashCode() {
		return Objects.hash(id, name, screenName, favsCount, followersCount, friendsCount, verified);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)			return true;
		if (obj == null)			return false;
		if (!(obj instanceof User))	return false; 
		User other = (User) obj;
		return 
			id == other.id && 
			name.equals(other.name) && 
			screenName.equals(other.screenName) && 
			favsCount == other.favsCount && 
			followersCount == other.followersCount && 
			friendsCount == other.friendsCount && 
			verified == other.verified;
	}

	/**
	 * Builder class for User
	 * @author Davide Quaranta
	 *
	 */
	public static class Builder {
		
		private long id;
		private String name;
		private String screenName;
		private long tweetsCount;
		private long favsCount;
		private long followersCount;
		private long friendsCount;
		private boolean verified;
		
		/**
		 * Sets the user id
		 * @param id the id of the user
		 * @return the builder instance
		 */
		public Builder setId(long id) {
			this.id = id;
			return this;
		}
		
		/**
		 * Sets the user id
		 * @param id the string containing the long user id
		 * @return the builder instance
		 */
		public Builder setId(String id) {
			return setId(Long.parseLong(id));
		}
		
		/**
		 * Sets the user name
		 * @param name the name of the user
		 * @return the builder instance
		 */
		public Builder setName(String name) {
			this.name = name;
			return this;
		}
		
		/**
		 * Sets the unique screen name
		 * @param screenName the unique user name
		 * @return the builder instance
		 */
		public Builder setScreenName(String screenName) {
			this.screenName = screenName;
			return this;
		}
		
		/**
		 * Sets the number of tweets of this user
		 * @param tweetCount the number of tweets
		 * @return the builder instance
		 */
		public Builder setTweetCount(long tweetCount) {
			this.tweetsCount = tweetCount;
			return this;
		}
		
		/**
		 * Sets the number of tweets of this user
		 * @param tweetsCount the string containing a long number of tweets
		 * @return the builder instance
		 */
		public Builder setTweetCount(String tweetsCount) {
			return setTweetCount(Long.parseLong(tweetsCount));
		}
		
		/**
		 * Sets the number of tweets that this user likes
		 * @param favsCount the number of liked tweets
		 * @return the builder instance
		 */
		public Builder setFavsCount(long favsCount) {
			this.favsCount = favsCount;
			return this;
		}
		
		/**
		 * Sets the number of tweets that this user likes
		 * @param favsCount the string containing the number of liked tweets
		 * @return the builder instance
		 */
		public Builder setFavsCount(String favsCount) {
			return setFavsCount(Long.parseLong(favsCount));
		}
		
		/**
		 * Sets the number of followers of this user
		 * @param followersCount the number of followers
		 * @return the builder instance
		 */
		public Builder setFollowersCount(long followersCount) {
			this.followersCount = followersCount;
			return this;
		}
		
		/**
		 * Sets the number of followers of this user
		 * @param followersCount the string containing the number of followers
		 * @return the builder instance
		 */
		public Builder setFollowersCount(String followersCount) {
			return setFollowersCount(Long.valueOf(followersCount));
		}
		
		/**
		 * Sets the number of users that this user follows
		 * @param friendsCount the number of followed users
		 * @return the builder instance
		 */
		public Builder setFriendsCount(long friendsCount) {
			this.friendsCount = friendsCount;
			return this;
		}
		
		/**
		 * Sets the number of users that this user follows
		 * @param friendsCount the string containing the number of followed users
		 * @return the builder instance
		 */
		public Builder setFriendsCount(String friendsCount) {
			return setFriendsCount(Long.parseLong(friendsCount));
		}
		
		/**
		 * Sets the verification mark for this user
		 * @param verified true if this user is verified
		 * @return the builder instance
		 */
		public Builder setVerified(boolean verified) {
			this.verified = verified;
			return this;
		}
		
		/**
		 * Sets the verification mark for this user
		 * @param verified the string "true"
		 * @return the builder instance
		 */
		public Builder setVerified(String verified) {
			return setVerified(verified.equals("true"));
		}
		
		/**
		 * Creates an User object, as configured through the builder methods
		 * @return the User object
		 */
		public User build() {
			return new User(id, name, screenName, tweetsCount, favsCount, followersCount, friendsCount, verified);
		}
	}
	
}
