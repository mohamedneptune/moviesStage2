package com.udacity.moviestage2.data.database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "review")
public class ReviewEntry {

    @PrimaryKey
    private String id;
    private String author;
    private String content;
    private String url;

    public ReviewEntry() {
    }

    public ReviewEntry(String author, String content, String id, String url) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.url = url;
    }

    @Ignore
    public ReviewEntry(String author, String content, String url) {
        this.author = author;
        this.content = content;
        this.url = url;
    }



    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
