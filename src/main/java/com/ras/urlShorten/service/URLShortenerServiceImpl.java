package com.ras.urlShorten.service;

import com.ras.urlShorten.model.URL;
import com.ras.urlShorten.repository.URLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Iterator;
import java.util.Optional;

@Service
public class URLShortenerServiceImpl implements URLShortenerService {
	
    @Autowired
    private URLRepository repository;

    @Override
    public Iterable<URL> listURLs(String user) {
        return repository.findByUser(user);
    }

    @Override
    public URL addURL(String user, String url) {
    	if(!isValidURL(url)) return null;
    	Iterator<URL> iter = listURLs(user).iterator();
    	while (iter.hasNext()) {
    		URL urlInDb = iter.next();
    		if (cleanURL(urlInDb.getURL()).equals(cleanURL(url))) return urlInDb;
    	}
		URL newURL = new URL();
		newURL.setUser(user);
		newURL.setUrl(url);
		return repository.save(newURL);
    }
    
    private String cleanURL(String url) {
    	if (url.substring(0, 7).equals("http://")) url = url.substring(7);
    	if (url.substring(0, 8).equals("https://")) url = url.substring(8);
    	if (url.charAt(url.length() - 1) == '/') url = url.substring(0, url.length() - 1);
    	return url;
    }
    
    private static boolean isValidURL(String url) {
    	try {
    		java.net.URL newURL = new java.net.URL(url);
    		return true;
    	} catch (Exception e) {
			return false;
		}
    }
    
    @Override
    public Optional<URL> getURL(String user, String id) {
        return repository.findByIdAndUser(id, user);
    }

    @Override
    public Optional<URL> getURL(String id) {
        return repository.findById(id);
    }

    @Override
    public void deleteURL(String user, String id) {
        Optional<URL> url = repository.findByIdAndUser(id, user);
        if(url.isPresent()) {
            repository.delete(url.get());
        }
    }
}
