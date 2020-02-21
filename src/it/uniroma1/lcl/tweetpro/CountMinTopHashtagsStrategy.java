package it.uniroma1.lcl.tweetpro;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Concrete strategy that uses Count-Min Sketch algorithm to compute the k top hashtags in a corpus.
 * @author Davide Quaranta
 *
 */
public class CountMinTopHashtagsStrategy implements TopHashtagsStrategy {
	
	@Override
	public List<String> get(TweetCorpus tweetCorpus, int limit) {
		Map<String, Integer> hashtagMap = new HashMap<>();
		int maxLength = limit*10;
		
		tweetCorpus.forEach(tweet -> {
			tweet.getHashtags().forEach(hashtag -> {
				hashtag = hashtag.toLowerCase();
				if (hashtagMap.containsKey(hashtag) || hashtagMap.size() < maxLength) {
					hashtagMap.putIfAbsent(hashtag, 1);
					hashtagMap.computeIfPresent(hashtag, (k, v) -> v + 1);
				} else {
					Map.Entry<String, Integer> min = Collections.min(hashtagMap.entrySet(), Comparator.comparing(Map.Entry<String, Integer>::getValue));
					hashtagMap.remove(min.getKey());
					hashtagMap.put(hashtag, min.getValue() + 1);
				}
			});
		});
		
		return hashtagMap.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.limit(limit)
				.map(x -> x.getKey())
				.collect(Collectors.toList());
	}

}
