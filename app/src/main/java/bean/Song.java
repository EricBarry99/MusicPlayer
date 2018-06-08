package bean;

import android.graphics.Bitmap;

public class Song {

    public String title;
    public String artist;
    public String album;
    public byte[] albumArt;
    public String duration;
    public String path;

    public Song() {
    }

    public Song(String title, String artist, String album, byte[] albumArt, String duration, String path) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.albumArt = albumArt;
        this.duration = duration;
        this.path = path;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public byte[] getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(byte[] albumArt) {
        this.albumArt = albumArt;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
