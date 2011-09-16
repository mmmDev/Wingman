package com.wingman.parsers;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public abstract class BaseFeedParser {

	/*
	 * <DESCRIPTION>
	 * 	<ID_DESCRIPTION>123</ID_DESCRIPTION>
	 * 	<ID_PERSON>456</ID_PERSON>
	 * 	<FIRSTNAME>Jane<FIRSTNAME>
	 * 	<LASTNAME>Smith<LASTNAME>
	 * 	<DATE>12-12-2012</DATE>
	 * 	<COMMENT>This is my amazing comment</COMMENT>
	 * 	<RATING>4.0</RATING>
	 * </DESCRIPTION>
	 */
	
    // names of the XML tags
	static final String DESCRIPTION = "DESCRIPTION";
	static final String ID_DESCRIPTION = "ID_DESCRIPTION";
	static final String ID_PERSON = "ID_PERSON";
	static final String ID_TAG = "ID_TAG";
	static final String FIRST_NAME = "FIRSTNAME";
    static final String LAST_NAME = "LASTNAME";
	static final String DATE = "date";
    static final  String COMMENT = "COMMENT";
    static final  String RATING = "RATING";
    static final String SINGLE = "SINGLE";
    static final String RESULT = "RESULT";
    static final String BASE_URL = "http://wingapp.apphb.com";
    
   // final URL feedUrl;

    /*
    protected BaseFeedParser(String feedUrl){
        try {
            this.feedUrl = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
*/
	protected DataOutputStream getOutputStream(URL url){
    	try {
    		URLConnection conn = url.openConnection();
    		// Set connection parameters.
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            
            // Make server believe we are form data...
            conn.setRequestProperty("Content-Type", "application/xml");
            return new DataOutputStream(conn.getOutputStream());
 
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}
    }
    
    protected InputStream getInputStream(URL url) {
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
}