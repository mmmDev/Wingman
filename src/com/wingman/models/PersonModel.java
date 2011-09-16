package com.wingman.models;

import com.github.mhendred.face4j.model.Photo;

import android.os.Parcel;
import android.os.Parcelable;

public class PersonModel implements Parcelable {
	private Photo photo;
	private int selectedFace;

	private String firstName = null;
	private String lastName = null;
	private String comment = null;
	private String idTag = null;
	private String date = null;

	private float rating = 0;
	private int isSingle = 0;
	private int idPerson = 0;
	private int idDescription = 0;

	public int getIsSingle() {
		return isSingle;
	}

	public void setIsSingle(int isSingle) {
		this.isSingle = isSingle;
	}

	public int isSingle() {
		return isSingle;
	}

	public void setSingle(int isSingle) {
		this.isSingle = isSingle;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	// Where do i use these? lol.... not for xml parser, that uses
	// BaseFeedParser
	//private String DATE = "date";

	// private String COMMENT = "comment";

	// private String NAME = "name";

	public String getComment() {
		return comment;
	}

	public void setComment(String desc) {
		comment = desc;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}

	public int getSelectedFace() {
		return selectedFace;
	}

	public void setSelectedFace(int selectedFace) {
		
		this.selectedFace = selectedFace;
	}


	public int getID_DESCRIPTION() {
		return idDescription;
	}

	public void setID_DESCRIPTION(int iD_DESCRIPTION) {
		idDescription = iD_DESCRIPTION;
	}

	public int getID_PERSON() {

		return idPerson;
	}

	public void setID_PERSON(int id) {
		idPerson = id;
	}

	// 99.9% of the time you can just ignore this
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	// write your object's data to the passed-in Parcel
	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub

		out.writeValue(photo);
		out.writeInt(selectedFace);
		out.writeString(firstName);
		out.writeString(lastName);
		out.writeString(comment);
		out.writeString(date);
		out.writeInt(isSingle);
		out.writeFloat(rating);
		out.writeString(idTag);
		out.writeInt(idPerson);
		out.writeInt(idDescription);

		// out.writeBooleanArray(isSingle);
	}

	// this is used to regenerate your object. All Parcelables must have a
	// CREATOR that implements these two methods
	public static final Parcelable.Creator<PersonModel> CREATOR = new Parcelable.Creator<PersonModel>() {
		public PersonModel createFromParcel(Parcel in) {
			return new PersonModel(in);
		}

		public PersonModel[] newArray(int size) {
			return new PersonModel[size];
		}
	};

	// example constructor that takes a Parcel and gives you an object populated
	// with it's values
	private PersonModel(Parcel in) {
		photo = (Photo) in.readValue(PersonModel.class.getClassLoader());
		selectedFace = in.readInt();
		firstName = in.readString();
		lastName = in.readString();
		comment = in.readString();
		date = in.readString();
		isSingle = in.readInt();
		rating = in.readFloat();
		idTag = in.readString();
		idPerson = in.readInt();
		idDescription = in.readInt();

		// setIdTag(in.readString());
		// isSingle = in.read
	}

	public PersonModel(Photo p, int face) {
		// TODO Auto-generated constructor stub
		photo = p;
		selectedFace = face;
	}

	public PersonModel() {
		// TODO Auto-generated constructor stub
	}

	public void setFirstName(String nextText) {
		// TODO Auto-generated method stub
		firstName = nextText;
	}

	public void setLastName(String name) {
		lastName = name;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setIdTag(String idTag) {
		this.idTag = idTag;
	}

	public String getIdTag() {
		return idTag;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}
}
