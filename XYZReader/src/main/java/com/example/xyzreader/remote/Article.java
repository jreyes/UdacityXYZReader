package com.example.xyzreader.remote;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Article implements Parcelable {
// ------------------------------ FIELDS ------------------------------

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

    public String author;
    public String body;
    public String id;
    public String photo;
    @SerializedName("published_date")
    public Date publishedDate;
    public String title;

// --------------------------- CONSTRUCTORS ---------------------------

    public Article() {
    }

    protected Article(Parcel in) {
        this.author = in.readString();
        this.body = in.readString();
        this.id = in.readString();
        this.photo = in.readString();
        this.publishedDate = new Date(in.readLong());
        this.title = in.readString();
    }

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Parcelable ---------------------

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.body);
        dest.writeString(this.id);
        dest.writeString(this.photo);
        dest.writeLong(this.publishedDate.getTime());
        dest.writeString(this.title);
    }
}
