package com.wingman.parsers;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.github.mhendred.face4j.DefaultFaceClient;
import com.github.mhendred.face4j.FaceClient;
import com.github.mhendred.face4j.exception.FaceClientException;
import com.github.mhendred.face4j.exception.FaceServerException;
import com.github.mhendred.face4j.model.Face;
import com.github.mhendred.face4j.model.Photo;
import com.github.mhendred.face4j.model.SavedTag;
import com.wingman.models.PersonModel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;

public class FaceComWrapper {

	private static final String IMAGE_PATH = "/sdcard/wingman/LatestWingman.jpg";
	private static final String IMAGE_PATH_DRAWN = "/sdcard/wingman/WingDraw.jpg";
	private static final String API_KEY = "04bab5fb51c5024f6e91640d23f49423";
  	private static final String API_SEC = "a832d0e110e1b7da57e3cb35897b91ca";
  	//private static final String URL_WITH_FACES = "http://seedmagazine.com/images/uploads/attractive_article.jpg";
  	private static final String NAMESPACE = "people";
  	private static final String RECOGNIZE_UID = "all@" + NAMESPACE;
  	private static final FaceClient faceClient = new DefaultFaceClient(API_KEY, API_SEC);
  	
  	public Photo detectAndDrawFaces() {
		Photo photo = null;
    	try{
    		// Find file
	       	File imageFile = new File(IMAGE_PATH);

	       	// Query Face.com for detection
	       	photo = faceClient.detect(imageFile);
        	if(photo.getFaceCount() > 0) {
        		// Faces were found, draw them on bitmap
        		photo = DrawFaces(photo);
        	}
        	
        	// HACK: Backup rectangle
        	Photo myPhot = photo;
        	
        	// Recognize people found
         	photo = faceClient.recognize(imageFile, "all@" + NAMESPACE);
	       	
         	for(int i=0; i<photo.getFaceCount(); i++)
         	{
         		photo.getFaces().get(i).setFaceRect(myPhot.getFaces().get(i).getRectangle());
         	}

       		/**
        	 * @see http://developers.face.com/docs/api/faces-detect/
        	 * @see http://developers.face.com/docs/api/faces-recognize/
        	 */
        	//Photo photo = faceClient.detect(URL_WITH_FACES).get(0);
        	
        	// Faces found, manipulate the image
        	}catch(Exception e) {
        		throw new RuntimeException(e);
        	}
    
		return photo;
	}

	 private Photo DrawFaces(Photo photo) throws IOException {
			// Load picture taken from cam
			Bitmap orgImage = BitmapFactory.decodeFile(IMAGE_PATH);
			
			// Create copy, which we can modify
			Bitmap newImage = orgImage.copy(orgImage.getConfig(), true);

			// Scale rectangles to the size of the image
			Photo myPhoto = photo;
			myPhoto.scaleFaceRects(photo.getWidth(), photo.getHeight());
			//photo.scaleFaceRects(newImage.getWidth(), newImage.getHeight());
			
			// Draw rectangle around faces
			for (Face face : myPhoto.getFaces())
	    	{
				float top = face.getRectangle().top;
				float bottom = face.getRectangle().bottom;
				float right = face.getRectangle().right;
				float left = face.getRectangle().left;
				
				// Draw the actual rectangle
				for(int i=(int) left ; i<right; i++)
				{
					newImage.setPixel(i, (int) top, Color.YELLOW);
				}
				for(int i=(int) left ; i<right; i++)
				{
					newImage.setPixel(i, (int) bottom, Color.YELLOW);
				}
				for(int i=(int) top ; i<bottom; i++)
				{
					newImage.setPixel((int) left, i, Color.YELLOW);
				}
				for(int i=(int) top ; i<bottom; i++)
				{
					newImage.setPixel((int) right, i, Color.YELLOW);
				}
	    	}
			
			// Save the new image
			FileOutputStream fileOutputStream = new FileOutputStream(IMAGE_PATH_DRAWN);
			BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

			newImage.compress(CompressFormat.JPEG, 100, bos);

			bos.flush();
			bos.close();
			return myPhoto;
	}
	 
	 public PersonModel addPerson(PersonModel pm)
	 {
		 Photo photo = pm.getPhoto();
		 int i = pm.getSelectedFace();
		 Face f = photo.getFaces().get(i);
		 //"a_user_id@" + NAMESPACE;
		 String uid = pm.getID_PERSON() + "@" + NAMESPACE;
		 try {			 
			List<SavedTag> tags = faceClient.saveTags(f.getTID(), uid, pm.getFirstName());
			faceClient.train(uid);
			String myTag = tags.get(0).getTID();
			pm.setIdTag(myTag);
		} catch (FaceClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FaceServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 //faceClient.saveTags(f.getTID(), pm.getID_PERSON(), label)
	 /*
 	 * Now we pull out the temporary tag and call save with the desired username and label.
 	 */
		 
 	
 	//faceClient.saveTags(f.getTID(), USER_ID, "a label");
 	
 	/*
 	 * Let get the training status for this user now. We should see training in progress TRUE
 	 * because we havent called train yet.
 	 */
 	//faceClient.status(USER_ID);

 	/*
 	 * IMPORTANT: Now we call train on our untrained user. This will commit our saved tag for this user to
 	 * the database so we can recognize them later with 'recognize' calls   
 	 */
 	//faceClient.train(USER_ID);
 	
 	/**
 	 * Now we can call recognize. Look for any user in our index (we only have one now)
 	 * We should see a guess now
 	 */
 	//photo = faceClient.recognize(URL_WITH_FACES, "all@" + NAMESPACE).get(0);
 	//photo = faceClient.recognize(imageFile, "all@" + NAMESPACE);
 	return pm;
	 }

	public static PersonModel recognizePersonModel(PersonModel m_pm) {
		File imageFile = new File(IMAGE_PATH);
		try{
			// Recognize selected face
			Photo photo = faceClient.recognize(imageFile, RECOGNIZE_UID);
			m_pm.setPhoto(photo);
			
			// Set fields for that face (id's)
			String photoID = photo.getFaces().get(m_pm.getSelectedFace()).getGuess().first; // Get person id of the current face selected
			String realId = photoID.replace("@people", "");
			m_pm.setID_PERSON(Integer.valueOf(realId.trim()).intValue());
			
		} catch(FaceClientException e) {
			e.printStackTrace();
		} catch(FaceServerException e) {
			e.printStackTrace();
		}
		
		return m_pm;
	}
}
