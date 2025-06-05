package com.example.gunmedia.publications;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.PropertyName;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Publication {
    private String id;
    private String title;

    @PropertyName("short_description")
    private String shortDescription;

    @PropertyName("full_content")
    private String fullContent;

    private String category;
    private Map<String, Boolean> tags;

    @PropertyName("comment_count")
    private int commentCount;

    @PropertyName("image_url")
    private String imageUrl;

    @PropertyName("timestamp")
    private long timestamp;

    @PropertyName("author")
    private Author author;

    private boolean isFullContentLoaded = false;

    public boolean isFullContentLoaded() {
        return isFullContentLoaded;
    }

    public void setFullContentLoaded(boolean fullContentLoaded) {
        isFullContentLoaded = fullContentLoaded;
    }

    public void loadFullContent(DatabaseReference database, OnFullContentLoadedListener listener) {
        if (isFullContentLoaded) {
            listener.onLoaded();
            return;
        }

        database.child("publications").child(id).child("full_content")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        fullContent = snapshot.getValue(String.class);
                        isFullContentLoaded = true;
                        listener.onLoaded();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onError(error.toException());
                    }
                });
    }

    public interface OnFullContentLoadedListener {
        void onLoaded();
        void onError(Exception e);
    }

    // Конструкторы
    public Publication() {
        // Пустой конструктор для Firebase
    }

    public Publication(String title, String shortDescription, String fullContent,
                       String category, int commentCount, long timestamp) {
        this.title = title;
        this.shortDescription = shortDescription;
        this.fullContent = fullContent;
        this.category = category;
        this.commentCount = commentCount;
        this.timestamp = timestamp;
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @PropertyName("short_description")
    public String getShortDescription() {
        return shortDescription;
    }

    @PropertyName("short_description")
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @PropertyName("full_content")
    public String getFullContent() {
        return fullContent;
    }

    @PropertyName("full_content")
    public void setFullContent(String fullContent) {
        this.fullContent = fullContent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Map<String, Boolean> getTags() {
        return tags;
    }

    public void setTags(Map<String, Boolean> tags) {
        this.tags = tags;
    }

    @PropertyName("comment_count")
    public int getCommentCount() {
        return commentCount;
    }

    @PropertyName("comment_count")
    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @PropertyName("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @PropertyName("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @PropertyName("timestamp")
    public long getTimestamp() {
        return timestamp;
    }

    @PropertyName("timestamp")
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @PropertyName("author")
    public Author getAuthor() {
        return author;
    }

    @PropertyName("author")
    public void setAuthor(Author author) {
        this.author = author;
    }

    public static class Author {
        private String id;
        private String name;

        public Author() {
        }

        public Author(String id, String name) {
            this.id = id;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}