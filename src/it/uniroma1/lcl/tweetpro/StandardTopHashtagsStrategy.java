package it.uniroma1.lcl.tweetpro;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Standard concrete strategy to compute the k top hashtags in a corpus.
 * Uses an HashMap to store the frequency of use for each hashtag, then reverse orders the map by value, and returns the top k hashtags.
 * @author Davide Quaranta
 *
 */
public class StandardTopHashtagsStrategy implements TopHashtagsStrategy {
	
	@Override
	public List<String> get(TweetCorpus tweetCorpus, int limit) {
		Map<String, Integer> hashtagMap = new HashMap<>();
		
		tweetCorpus.forEach(tweet -> {
			tweet.getHashtags().forEach(hashtag -> {
				hashtag = hashtag.toLowerCase();
				hashtagMap.putIfAbsent(hashtag, 1);
				hashtagMap.computeIfPresent(hashtag, (k, v) -> v + 1);
			});
		});
				
		return hashtagMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.limit(limit)
				.map(x -> x.getKey())
				.collect(Collectors.toList());
	}

}
