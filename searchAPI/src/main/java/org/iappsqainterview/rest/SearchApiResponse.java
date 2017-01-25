package org.iappsqainterview.rest;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SearchApiResponse {
	private int resultCount;
	private List<ResultLine> results;

}
