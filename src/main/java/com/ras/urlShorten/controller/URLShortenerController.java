package com.ras.urlShorten.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.ras.urlShorten.model.URL;
import com.ras.urlShorten.service.MetricServiceImpl;
import com.ras.urlShorten.service.MetricServiceImpl.Metric;
import com.ras.urlShorten.service.URLShortenerServiceImpl;

@RestController
public class URLShortenerController {
	
	@Autowired
	URLShortenerServiceImpl URLShortenerService;
	
	@Autowired
	MetricServiceImpl metricServiceImpl;
	
	@PostMapping("/addUrl")
	public URL addURL(@RequestBody URL url) {
		return URLShortenerService.addURL(url.getUser(), url.getURL());
	}
	
	@GetMapping("/getUrl/{id}")
	public Optional<URL> getURL(@PathVariable("id") final String id) {
		return URLShortenerService.getURL(id);
	}
	
	@GetMapping("/deleteUrl/{user}/{id}")
	public void delelteURL(@PathVariable("user") final String user, @PathVariable("id") final String id) {
		URLShortenerService.deleteURL(user, id);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Object> redirectURLShortener(@PathVariable("id") final String id, HttpServletRequest httpServletRequest) throws URISyntaxException {	
		Optional<URL> url = URLShortenerService.getURL(id);
		if (url.isEmpty()) return null;
		metricServiceImpl.addMetric(id, httpServletRequest);
		URI uri = new URI(url.get().getURL());
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setLocation(uri);
		return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
	}
	
	@GetMapping("/metrics")
	public ConcurrentMap<String, Metric> getMetrics() {
		return metricServiceImpl.getMetrics();
	}

}
