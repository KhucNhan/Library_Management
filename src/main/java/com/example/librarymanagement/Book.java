package com.example.librarymanagement;

public class Book {
    private String id;
    private String title;
    private String author;
    private String releaseYear;
    private String genre;

    public Book() {

    }

    public Book(String id, String title, String author, String releaseYear, String genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.releaseYear = releaseYear;
        this.genre = genre;
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

    public int getReleaseYear() {
        return Integer.parseInt(releaseYear);
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

    public String toString() {
        return "ID: " + getId()
                + "  ||  Author: " + getAuthor()
                + "  ||  Title: " + getTitle()
                + "  ||  Genre: " + getGenre()
                + "  ||  Release year: " + getReleaseYear();
    }
}
