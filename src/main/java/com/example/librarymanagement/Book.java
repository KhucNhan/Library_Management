
package com.example.librarymanagement;

public class Book {
    private String id;
    private String img;
    private String title;
    private String author;
    private String releaseYear;
    private String genre;
    private String status;

    public Book() {

    }

    public Book(String id, String img, String title, String author, String releaseYear, String genre, String status) {
        this.id = id;
        this.img = img;
        this.title = title;
        this.author = author;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.status = status;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(String releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}