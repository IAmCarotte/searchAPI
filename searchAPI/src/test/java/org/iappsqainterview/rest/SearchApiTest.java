package org.iappsqainterview.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SearchApiTest {

	private final String BASE_URL = "https://itunes.apple.com/search?";

	public <T> T retrieveResourceFromResponse(final HttpResponse response, final Class<T> clazz) throws IOException {
		final String jsonFromResponse = EntityUtils.toString(response.getEntity());
		final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(jsonFromResponse, clazz);
	}

	@Test
	public void verifyCountry() throws ClientProtocolException, IOException {
		// Given
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("term", "apple watch"));
		params.add(new BasicNameValuePair("country", "GB"));
		final HttpUriRequest request = new HttpGet(BASE_URL + URLEncodedUtils.format(params, Consts.UTF_8));
		// When
		final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

		SearchApiResponse resp = retrieveResourceFromResponse(httpResponse, SearchApiResponse.class);

		// Then
		assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
		assertNotNull(resp);
		assertEquals(50, resp.getResultCount());
		assertNotNull(resp.getResults());
		assertEquals(50, resp.getResults().size());
		
		int countryCount = 0;
		for (ResultLine line:resp.getResults()) {
			if (line.getCountry().equals("GBR")) {
				countryCount++;
			}
		}
		
		assertEquals(50, countryCount);
		log.info("verifyCountry:{}", resp);
	}

	@Test
	public void testCase1() throws ClientProtocolException, IOException {

		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("term", "Star"));
		params.add(new BasicNameValuePair("country", "US"));
		params.add(new BasicNameValuePair("media", "movie"));
		params.add(new BasicNameValuePair("limit", "25"));
		final HttpUriRequest request = new HttpGet(BASE_URL + URLEncodedUtils.format(params, Consts.UTF_8));
		final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		SearchApiResponse resp = retrieveResourceFromResponse(httpResponse, SearchApiResponse.class);

		assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
		assertNotNull(resp);
		assertEquals(25, resp.getResultCount());
		assertNotNull(resp.getResults());
		assertEquals(25, resp.getResults().size());
		
		int countryCount = 0;
		Set<String> mediaTypes = new HashSet<>();
		for (ResultLine line:resp.getResults()) {
			if (line.getCountry().equals("USA")) {
				countryCount++;
			}
			mediaTypes.add(line.getKind());
		}
		
		assertEquals(25, countryCount);
		assertEquals(1, mediaTypes.size());
		assertTrue(mediaTypes.contains("feature-movie"));
	}

	@Test
	public void testCase2() throws ClientProtocolException, IOException {

		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("term", "jack+Johnson"));
		final HttpUriRequest request = new HttpGet(BASE_URL + URLEncodedUtils.format(params, Consts.UTF_8));
		final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		SearchApiResponse resp = retrieveResourceFromResponse(httpResponse, SearchApiResponse.class);

		assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
		assertNotNull(resp);
		assertEquals(50, resp.getResultCount());
		assertNotNull(resp.getResults());
		assertEquals(50, resp.getResults().size());
		
		int countryCount = 0;
		Set<String> mediaTypes = new HashSet<>();
		for (ResultLine line:resp.getResults()) {
			if (line.getCountry().equals("USA")) {
				countryCount++;
			}
			mediaTypes.add(line.getKind());
		}
		
		assertEquals(50, countryCount);
		assertTrue(mediaTypes.size()>0);
	}

	@Test
	public void testCase3() throws ClientProtocolException, IOException {

		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("term", "Monocle: Culture"));
		params.add(new BasicNameValuePair("country", "GB"));
		params.add(new BasicNameValuePair("media", "podcast"));
		params.add(new BasicNameValuePair("limit", "1"));
		final HttpUriRequest request = new HttpGet(BASE_URL + URLEncodedUtils.format(params, Consts.UTF_8));
		final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		SearchApiResponse resp = retrieveResourceFromResponse(httpResponse, SearchApiResponse.class);

		assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
		assertNotNull(resp);
		assertEquals(1, resp.getResultCount());
		assertNotNull(resp.getResults());
		assertEquals(1, resp.getResults().size());
		
		int countryCount = 0;
		Set<String> mediaTypes = new HashSet<>();
		for (ResultLine line:resp.getResults()) {
			if (line.getCountry().equals("GBR")) {
				countryCount++;
			}
			mediaTypes.add(line.getKind());
		}
		
		assertEquals(1, countryCount);
		assertEquals(1, mediaTypes.size());
	}

	@Test
	public void testCase4() throws ClientProtocolException, IOException {
		
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("term", "Red.Hot Chili Peppers"));
		params.add(new BasicNameValuePair("country", "US"));
		params.add(new BasicNameValuePair("media", "music"));
		params.add(new BasicNameValuePair("limit", "200"));
		final HttpUriRequest request = new HttpGet(BASE_URL + URLEncodedUtils.format(params, Consts.UTF_8));
		final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		SearchApiResponse resp = retrieveResourceFromResponse(httpResponse, SearchApiResponse.class);
		
		assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
		assertNotNull(resp);
		assertEquals(200, resp.getResultCount());
		assertNotNull(resp.getResults());
		assertEquals(200, resp.getResults().size());
		
		int countryCount = 0;
		Set<String> mediaTypes = new HashSet<>();
		for (ResultLine line:resp.getResults()) {
			if (line.getCountry().equals("USA")) {
				countryCount++;
			}
			mediaTypes.add(line.getKind());
		}
		
		assertEquals(200, countryCount);
		assertTrue(mediaTypes.size() >=1 || 2 <= mediaTypes.size());
	}

	@Test
	public void testCase5() throws ClientProtocolException, IOException {
		
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("term", "Christmas"));
		params.add(new BasicNameValuePair("country", "US"));
		params.add(new BasicNameValuePair("media", "musicVideo"));
		params.add(new BasicNameValuePair("limit", "199"));
		final HttpUriRequest request = new HttpGet(BASE_URL + URLEncodedUtils.format(params, Consts.UTF_8));
		final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		SearchApiResponse resp = retrieveResourceFromResponse(httpResponse, SearchApiResponse.class);
		
		assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
		assertNotNull(resp);
		assertEquals(199, resp.getResultCount());
		assertNotNull(resp.getResults());
		assertEquals(199, resp.getResults().size());
		
		int countryCount = 0;
		Set<String> mediaTypes = new HashSet<>();
		for (ResultLine line:resp.getResults()) {
			if (line.getCountry().equals("USA")) {
				countryCount++;
			}
			mediaTypes.add(line.getKind());
		}
		
		assertEquals(199, countryCount);
		assertTrue(mediaTypes.size()==1);
		assertTrue(mediaTypes.contains("music-video"));
	}
	
	@Test
	public void testCase6() throws ClientProtocolException, IOException {
		
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("term", "Stephen_King"));
		params.add(new BasicNameValuePair("country", "US"));
		params.add(new BasicNameValuePair("media", "audiobook"));
		params.add(new BasicNameValuePair("limit", "100"));
		final HttpUriRequest request = new HttpGet(BASE_URL + URLEncodedUtils.format(params, Consts.UTF_8));
		final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		SearchApiResponse resp = retrieveResourceFromResponse(httpResponse, SearchApiResponse.class);
		
		assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
		assertNotNull(resp);
		assertTrue(100 >= resp.getResultCount());
		assertNotNull(resp.getResults());
		assertTrue(100 >= resp.getResults().size());
		
		int countryCount = 0;
		Set<String> mediaTypes = new HashSet<>();
		for (ResultLine line:resp.getResults()) {
			if (line.getCountry().equals("USA")) {
				countryCount++;
			}
			mediaTypes.add(line.getWrapperType());
		}
		
		assertTrue(100 >= countryCount);
		assertTrue(mediaTypes.size()==1);
		assertTrue(mediaTypes.contains("audiobook"));
	}
	
	@Test
	public void testCase7() throws ClientProtocolException, IOException {
		
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("term", "little"));
		params.add(new BasicNameValuePair("country", "US"));
		params.add(new BasicNameValuePair("media", "shortFilm"));
		params.add(new BasicNameValuePair("limit", "2"));
		final HttpUriRequest request = new HttpGet(BASE_URL + URLEncodedUtils.format(params, Consts.UTF_8));
		final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		SearchApiResponse resp = retrieveResourceFromResponse(httpResponse, SearchApiResponse.class);
		
		assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
		assertNotNull(resp);
		assertEquals(2, resp.getResultCount());
		assertNotNull(resp.getResults());
		assertEquals(2, resp.getResults().size());
		
		int countryCount = 0;
		Set<String> mediaTypes = new HashSet<>();
		for (ResultLine line:resp.getResults()) {
			if (line.getCountry().equals("USA")) {
				countryCount++;
			}
			mediaTypes.add(line.getKind());
		}
		
		assertEquals(2, countryCount);
		assertTrue(mediaTypes.size() ==1 );
		assertTrue(mediaTypes.contains("feature-movie"));
		
	}
	
	@Test
	public void testCase8() throws ClientProtocolException, IOException {
		
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("term", "Big Bang-Theory 1"));
		params.add(new BasicNameValuePair("country", "US"));
		params.add(new BasicNameValuePair("media", "tvShow"));
		params.add(new BasicNameValuePair("limit", "20"));
		final HttpUriRequest request = new HttpGet(BASE_URL + URLEncodedUtils.format(params, Consts.UTF_8));
		final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		SearchApiResponse resp = retrieveResourceFromResponse(httpResponse, SearchApiResponse.class);
		
		assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
		assertNotNull(resp);
		assertTrue(20 >= resp.getResultCount());
		assertNotNull(resp.getResults());
		assertTrue(20 >= resp.getResults().size());
		
		int countryCount = 0;
		Set<String> mediaTypes = new HashSet<>();
		for (ResultLine line:resp.getResults()) {
			if (line.getCountry().equals("USA")) {
				countryCount++;
			}
			mediaTypes.add(line.getKind());
		}
		
		assertTrue(20 >= countryCount);
		assertTrue(mediaTypes.size() ==1 );
		assertTrue(mediaTypes.contains("tv-episode"));
	}
	
	@Test
	public void testCase9() throws ClientProtocolException, IOException {
		
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("term", "SQL"));
		params.add(new BasicNameValuePair("country", "US"));
		params.add(new BasicNameValuePair("media", "software"));
		params.add(new BasicNameValuePair("limit", "5"));
		final HttpUriRequest request = new HttpGet(BASE_URL + URLEncodedUtils.format(params, Consts.UTF_8));
		final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		SearchApiResponse resp = retrieveResourceFromResponse(httpResponse, SearchApiResponse.class);
		
		assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
		assertNotNull(resp);
		assertEquals(5, resp.getResultCount());
		assertNotNull(resp.getResults());
		assertEquals(5, resp.getResults().size());
		
		Set<String> mediaTypes = new HashSet<>();
		for (ResultLine line:resp.getResults()) {
			mediaTypes.add(line.getKind());
		}
		
		assertTrue(mediaTypes.size() ==1 );
	}
	
	@Test
	public void testCase10() throws ClientProtocolException, IOException {
		
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("term", "King"));
		params.add(new BasicNameValuePair("country", "US"));
		params.add(new BasicNameValuePair("media", "ebook"));
		params.add(new BasicNameValuePair("limit", "3"));
		final HttpUriRequest request = new HttpGet(BASE_URL + URLEncodedUtils.format(params, Consts.UTF_8));
		final HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
		SearchApiResponse resp = retrieveResourceFromResponse(httpResponse, SearchApiResponse.class);
		
		assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
		assertNotNull(resp);
		assertEquals(3, resp.getResultCount());
		assertNotNull(resp.getResults());
		assertEquals(3, resp.getResults().size());
		
		Set<String> mediaTypes = new HashSet<>();
		for (ResultLine line:resp.getResults()) {
			mediaTypes.add(line.getKind());
		}
		
		assertTrue(mediaTypes.size() ==1 );
	}
}
