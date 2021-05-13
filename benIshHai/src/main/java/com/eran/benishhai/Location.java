package com.eran.benishhai;

import android.os.Parcel;
import android.os.Parcelable;


public class Location implements Parcelable {

	 private String time;
	 private String yearEn;
	 private String yearHe;
	 private String humashEn;
	 private String humashHe;
	 private String parshHe;
	 private String parshEn;
	 private int scrollY;
	 private String textToSearch;
	 
	 public Location(String time,String yearEn,String yearHe,String humashHe, String humashEn,String parshHe,String parshEn,int scrollY)
	 {
		this.time=time;
	 	this.yearEn=yearEn;
		this.yearHe=yearHe;
		this.humashHe = humashHe;
		this.humashEn=humashEn;
		this.parshHe=parshHe;
		this.parshEn=parshEn;
		this.scrollY=scrollY;
	 }
	 
	 public Location(String yearEn,String yearHe,String humashHe, String humashEn,String parshHe,String parshEn, String textToSearch)
	 {
	 	this.yearEn=yearEn;
		this.yearHe=yearHe;
		this.humashHe = humashHe;
		this.humashEn=humashEn;
		this.parshHe=parshHe;
		this.parshEn=parshEn;
		this.textToSearch=textToSearch;
	 }
	
	public String getTime() {
			return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getYearEn() {
			return yearEn;
	}
	public void setYearEn(String yearEn) {
		this.yearEn = yearEn;
	}
	
	public String getYearHe() {
		return yearHe;
	}
	public void setYearHe(String yearHe) {
		this.yearHe = yearHe;
	}
	
	public String getHumashEn() {
		return humashEn;
	}
	public void setHumashEn(String humashEn) {
		this.humashEn = humashEn;
	}
	
	public String getHumashHe() {
		return humashHe;
	}
	public void setHumashHe(String humashHe) {
		this.humashHe = humashHe;
	}

	public String getParshHe() {
		return parshHe;
	}
	public void setParshEn(String parshEn) {
		this.parshEn = parshEn;
	}
	
	public String getParshEn() {
		return parshEn;
	}
	public void setParshHe(String parshHe) {
		this.parshHe = parshHe;
	}
	
	public int getScrollY() {
		return scrollY;
	}
	public void setScrollY(int scrollY) {
		this.scrollY = scrollY;
	}
	
	public String getTextToSearch() {
		return textToSearch;
	}

	public void setTextToSearch(String textToSearch) {
		this.textToSearch = textToSearch;
	}

    protected Location(Parcel in) {
        time = in.readString();
        yearEn = in.readString();
        yearHe = in.readString();
        humashEn = in.readString();
        humashHe = in.readString();
        parshHe = in.readString();
        parshEn = in.readString();
        scrollY = in.readInt();
        textToSearch = in.readString();
    }


	@Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(yearEn);
        dest.writeString(yearHe);
        dest.writeString(humashEn);
        dest.writeString(humashHe);
        dest.writeString(parshHe);
        dest.writeString(parshEn);
        dest.writeInt(scrollY);
        dest.writeString(textToSearch);
    }

	@SuppressWarnings("unused")
    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };
}