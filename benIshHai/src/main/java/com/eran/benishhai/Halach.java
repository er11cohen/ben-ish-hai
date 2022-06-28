package com.eran.benishhai;

import android.os.Parcel;
import android.os.Parcelable;

public class Halach extends Location implements Parcelable {
    private String text;
    private String textToSearch;
    private int parshIndex;

    public Halach(int parshIndex, String yearHe, String yearEn, String parshHe, String parshEn, String humashHe, String humashEn, String text, String textToSearch) {
        super(null, yearEn, yearHe, humashHe, humashEn, parshHe, parshEn, 0);
        this.parshIndex = parshIndex;
        this.text = text;
        this.textToSearch = textToSearch;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return this.text; //what you want displayed for each row in the listview
    }

    public int getParshIndex() {
        return parshIndex;
    }

    public void setParshIndex(int parshIndex) {
        this.parshIndex = parshIndex;
    }

    public String getTextToSearch() {
        return text;
    }

    public void setTextToSearch(String textToSearch) {
        this.textToSearch = textToSearch;
    }


    protected Halach(Parcel in) {
        super(in);
        text = in.readString();
        textToSearch = in.readString();
        parshIndex = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(text);
        dest.writeString(textToSearch);
        dest.writeInt(parshIndex);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Halach> CREATOR = new Parcelable.Creator<Halach>() {
        @Override
        public Halach createFromParcel(Parcel in) {
            return new Halach(in);
        }

        @Override
        public Halach[] newArray(int size) {
            return new Halach[size];
        }

    };
}