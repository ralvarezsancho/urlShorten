package com.ras.urlShorten.service;

import java.util.concurrent.ConcurrentMap;
import javax.servlet.http.HttpServletRequest;
import com.ras.urlShorten.service.MetricServiceImpl.Metric;

public interface MetricService {
	
	/**
     * Add a new metric.
     * @param id
     * @param httpRequest
     */
    void addMetric(String id, HttpServletRequest httpServletRequest);
    
    /**
     * Get the metrics.
     *
     * @return a map with the full metric 
     */
    ConcurrentMap<String, Metric> getMetrics();

}
