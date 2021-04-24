package com.ras.urlShorten.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class MetricServiceImpl implements MetricService {
	
	private ConcurrentMap<String, Metric> metricMap;	
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Override
	public ConcurrentMap<String, Metric> getMetrics() {
		return metricMap;
	}
	
	@Override
	public void addMetric(String id, HttpServletRequest httpServletRequest) {
		Metric metricStored;
		Metric metric = new Metric();
		Info info = new Info();
		info.setDateTime(dateFormat.format(new Date()));
		info.setUserAgent(httpServletRequest.getHeader("User-Agent"));
		metric.getInfoList().add(info);
		if (metricMap ==  null) {
			metricMap = new ConcurrentHashMap<String, Metric>();
			metricMap.put(id, metric);
		} else if ((metricStored = metricMap.get(id)) == null) {
			metricMap.put(id, metric);
		} else {
			metricStored.getInfoList().add(info);
			metricStored.incCount();
		}
	}
	
	public class Metric {
		private Integer count;
		private List<Info> infoList;
		public Metric() {
			infoList = Collections.synchronizedList(new ArrayList<Info>());
			count = 1;
		}
		public Integer getCount() { return this.count; }
		public void setCount(Integer count) { this.count = count; }
		public List<Info> getInfoList() { return this.infoList; }
		public void setInfoList(List<Info> infoList) { this.infoList = infoList; }
		public void incCount() { this.count++; }	
	}
	
	public class Info {
		private String dateTime;
		private String userAgent;
		public String getDateTime() { return this.dateTime; }
		public String getUserAgent() { return this.userAgent; }
		public void setDateTime(String dateTime) { this.dateTime = dateTime; }
		public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
	}
	
}
