package com.wingman.parsers;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import com.wingman.models.PersonModel;


import android.util.Xml;

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



public class AppharborWrapper extends BaseFeedParser {
	private static final String BASE_URL = "http://wingapp.apphb.com";
    public String errorCode = "";
    
	public AppharborWrapper(PersonModel pm) {
        super();
    }
    
	// Make a PersonModel instance persistant on Central DB
	// return it's id
    public PersonModel addPerson(PersonModel pm) throws MalformedURLException
    {    	    	
        //DataOutputStream out = this.getOutputStream(myUrl);
        //InputStream in = this.getInputStream(myUrl);
        PersonModel result = readResultPersonInsert(pm);
    	return result;
        /*
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter("http.socket.timeout", 90000); // 90 second 
        try {
			HttpPost post = new HttpPost(new URI(insertUrl));
			
			HttpResponse response = client.execute(post); 
	        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
	        { 
	        	errorCode = "--------Error--------Response Status line code:"+response.getStatusLine();
	        	Log.e(TAG,errorCode); 
	        }else { 
	                // everything went better than expected.
	        	int idPerson = readResultPersonInsert(in);
	        	pm.setID_PERSON(idPerson);
	        }
	        
	        HttpEntity resEntity = response.getEntity(); 
            if (resEntity == null) { 
            		errorCode = "---------Error No Response !!!-----";
                    Log.e(TAG,errorCode); 
            }
            
            
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        
		
     finally { 
        client.getConnectionManager().shutdown(); 
     }
     */
		
		
        /*
        // Write out the bytes of the content string to the stream.
    	String content = this.writeXml(pm);
        try {
			out.writeBytes(content);
			out.flush();
	        out.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
        */
    }
    
    private PersonModel readResultPersonInsert(PersonModel pm) throws MalformedURLException {
    	//List<PersonModel> messages = null;
    	StringBuilder builder = new StringBuilder(BASE_URL + "/Person/Person");
    	PersonModel myPm = pm;
        //POSTText = "mydata=" + URLEncoder.encode("HELLO, ANDROID HTTPPostExample - by anddev.org", "UTF-8");

    	
    	
        builder.append("?firstName=");
        builder.append(URLEncoder.encode(myPm.getFirstName()));
        
        builder.append("&lastName=");
        builder.append(URLEncoder.encode(myPm.getLastName()));
        
        builder.append("&comment=");
        builder.append(URLEncoder.encode(myPm.getComment()));
        
        builder.append("&single=");
        builder.append(URLEncoder.encode(String.valueOf(myPm.getIsSingle())));
        
        builder.append("&rating=");
        builder.append(URLEncoder.encode(String.valueOf(myPm.getRating())));
        
        /*
        builder.append("&TID=");
        String i = pm.getIdTag();
        builder.append(URLEncoder.encode(pm.getIdTag()));
        */
        
        String insertUrl = builder.toString();
        URL myUrl = new URL(insertUrl);
        
        XmlPullParser parser = Xml.newPullParser();
        try {
            // auto-detect the encoding from the stream
            //parser.setInput(this.getInputStream(feedUrl), null);
        	InputStream in = this.getInputStream(myUrl);
        	parser.setInput(in, null);
        	
            int eventType = parser.getEventType();
            //PersonModel currentPersonModel = null;
            boolean done = false;
            
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(ID_PERSON)){
                                //currentPersonModel.setID_PERSON(Integer.valueOf(parser.nextText().trim()).intValue());
                        	int idPersonResult = Integer.valueOf(parser.nextText().trim()).intValue();
                        	myPm.setID_PERSON(idPersonResult);
                            }
                        else if(name.equalsIgnoreCase(ID_DESCRIPTION)){
                        	int idDescResult = Integer.valueOf(parser.nextText().trim()).intValue();
                        	myPm.setID_DESCRIPTION(idDescResult);
                        }
                        else if(name.equals(RESULT)) {
                        	parser.next();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(RESULT)){
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return myPm;
	}

    // Not currently in use.... maybe useful in future?
	@SuppressWarnings("unused")
	private String writeXml(PersonModel pm){
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            
            //Root node, DESCRIPTION
            serializer.startTag("", DESCRIPTION);
            
            serializer.startTag("", FIRST_NAME);
            serializer.text(pm.getFirstName());
            serializer.endTag("", FIRST_NAME);
            
            serializer.startTag("", LAST_NAME);
            serializer.text(pm.getLastName());
            serializer.endTag("", LAST_NAME);
            
            serializer.startTag("", DATE);
            serializer.text(pm.getDate());
            serializer.endTag("", DATE);
            
            serializer.startTag("", COMMENT);
            serializer.text(pm.getComment());
            serializer.endTag("", COMMENT);
            
            serializer.startTag("", RATING);
            serializer.text(Float.toString(pm.getRating()));
            serializer.endTag("", RATING);
            
            serializer.endTag("", DESCRIPTION);
            serializer.endDocument();
            return writer.toString();
            
        } catch (Exception e) {
            throw new RuntimeException(e);
        } 
    }
    
	/*
	 *     <DESCRIPTION>
        <ID_DESCRIPTION>@elem.idDescription</ID_DESCRIPTION>
        <ID_PERSON>@elem.idPerson</ID_PERSON>
       240 <FIRSTNAME>@elem.firstName</FIRSTNAME>
        <LASTNAME>@elem.lastName</LASTNAME>
        <DATE>@elem.date</DATE>
        <COMMENT>@elem.comment</COMMENT>
        <RATING>@elem.rating</RATING>
    </DESCRIPTION>
	 */
     public List<PersonModel> FindAllDescriptions(PersonModel pm) throws MalformedURLException {
    	 URL url = new URL(BASE_URL + "/Person/Show?id=" + pm.getID_PERSON());
        List<PersonModel> messages = null;
        XmlPullParser parser = Xml.newPullParser();
        try {
            //auto-detect the encoding from the stream
            parser.setInput(this.getInputStream(url), null);
            int eventType = parser.getEventType();
            PersonModel currentPersonModel = null;
            boolean done = false;
            
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        messages = new ArrayList<PersonModel>();
                        break;
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(DESCRIPTION)){
                            currentPersonModel = new PersonModel(pm.getPhoto(), pm.getSelectedFace());
                        } else if (currentPersonModel != null){
                            if (name.equalsIgnoreCase(ID_DESCRIPTION)){
                                currentPersonModel.setID_DESCRIPTION(Integer.valueOf(parser.nextText().trim()).intValue());
                            } else if (name.equalsIgnoreCase(ID_PERSON)){
                                currentPersonModel.setID_PERSON(Integer.valueOf(parser.nextText().trim()).intValue());
                            } else if (name.equalsIgnoreCase(DATE)){
                                currentPersonModel.setDate(parser.nextText());
                            } else if (name.equalsIgnoreCase(FIRST_NAME)){
                                currentPersonModel.setFirstName(parser.nextText());
                            } else if (name.equalsIgnoreCase(LAST_NAME)){
                            	currentPersonModel.setLastName(parser.nextText());
                            } else if (name.equalsIgnoreCase(COMMENT)){
                                currentPersonModel.setComment(parser.nextText());
                            } else if (name.equalsIgnoreCase(SINGLE)){
                            	currentPersonModel.setSingle(Integer.valueOf(parser.nextText().trim()).intValue());
                            } else if (name.equalsIgnoreCase(RATING)){
                                currentPersonModel.setRating(Float.valueOf(parser.nextText().trim()).floatValue());
                            } else if (name.equalsIgnoreCase(ID_TAG)) {
                            	currentPersonModel.setIdTag(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(DESCRIPTION) && currentPersonModel != null){
                            messages.add(currentPersonModel);
                        } else if (name.equalsIgnoreCase(RESULT)) {
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return messages;
    }

	public void attachTag(int idDesc, String idTag) throws MalformedURLException {
		StringBuilder builder = new StringBuilder(BASE_URL + "/Person/Attach");
    	//PersonModel myPm = pm;
		builder.append("?idDesc=");
		String desc = String.valueOf(idDesc);
        builder.append(URLEncoder.encode(desc));
        
        builder.append("&idTag=");
        builder.append(URLEncoder.encode(idTag));
        

        String insertUrl = builder.toString();
        URL myUrl = new URL(insertUrl);
        
        XmlPullParser parser = Xml.newPullParser();
        try {
        	InputStream in = this.getInputStream(myUrl);
        	parser.setInput(in, null);
        	
            int eventType = parser.getEventType();
            //PersonModel currentPersonModel = null;
            boolean done = false;
            
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if(name.equals(RESULT)) {
                        	parser.next();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(RESULT)){
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

		
	

	// 
	public PersonModel addPersonTag(PersonModel pm) throws MalformedURLException {
		// TODO Auto-generated method stub
		PersonModel myPm = pm;
		StringBuilder builder = new StringBuilder(BASE_URL + "/Person/SubmitTag");
		
		/*builder.append("?idTag=");
		String idTag = pm.getIdTag();
	    builder.append(URLEncoder.encode(idTag));
	    */
		
	    builder.append("?idPerson=");
	    String pID = String.valueOf(pm.getID_PERSON()).trim();
	    builder.append(URLEncoder.encode(pID));
		
        builder.append("&firstName=");
        builder.append(URLEncoder.encode(myPm.getFirstName()));
        
        builder.append("&lastName=");
        builder.append(URLEncoder.encode(myPm.getLastName()));
        
        builder.append("&comment=");
        builder.append(URLEncoder.encode(myPm.getComment()));
        
        builder.append("&single=");
        builder.append(URLEncoder.encode(String.valueOf(myPm.getIsSingle())));
        
        builder.append("&rating=");
        builder.append(URLEncoder.encode(String.valueOf(myPm.getRating())));
	    
	    String insertUrl = builder.toString();
        URL myUrl = new URL(insertUrl);
	    
        XmlPullParser parser = Xml.newPullParser();
        try {
        	InputStream in = this.getInputStream(myUrl);
        	parser.setInput(in, null);
        	
            int eventType = parser.getEventType();
            //PersonModel currentPersonModel = null;
            boolean done = false;
            
            while (eventType != XmlPullParser.END_DOCUMENT && !done){
                String name = null;
                switch (eventType){
                    case XmlPullParser.START_TAG:
                        name = parser.getName();
                        if(name.equalsIgnoreCase(RESULT)) {
                        	parser.next();
                        } else if(name.equalsIgnoreCase(ID_DESCRIPTION)){
                        	myPm.setID_DESCRIPTION(Integer.valueOf(parser.nextText().trim()).intValue());
                        } else if(name.equalsIgnoreCase(ID_PERSON)) {
                        	myPm.setID_PERSON(Integer.valueOf(parser.nextText().trim()).intValue());
                        } 
                        break;
                    case XmlPullParser.END_TAG:
                        name = parser.getName();
                        if (name.equalsIgnoreCase(RESULT)){
                            done = true;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
		return myPm;
	}
    
}