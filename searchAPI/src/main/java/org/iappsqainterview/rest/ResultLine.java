package org.iappsqainterview.rest;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResultLine {
	private String wrapperType;
	private String kind;
	private String artistId;
	private String collectionId;
	private String trackId;
	private String artistName;
	private String collectionName;
	private String trackName;
	private String collectionCensoredName;
	private String trackCensoredName;
	private String artistViewUrl;
	private String collectionViewUrl;
	private String feedUrl;
	private String trackViewUrl;
	private String artworkUrl30;
	private String artworkUrl60;
	private String artworkUrl100;
	private String collectionPrice;
	private String trackPrice;
	private String trackRentalPrice;
	private String collectionHdPrice;
	private String trackHdPrice;
	private String trackHdRentalPrice;
	private String releaseDate;
	private String collectionExplicitness;
	private String trackExplicitness;
	private String trackCount;
	private String country;
	private String currency;
	private String primaryGenreName;
	private String contentAdvisoryRating;
	private String artworkUrl600;
	private List<Long> genreIds;
	private List<String> genres;

}
