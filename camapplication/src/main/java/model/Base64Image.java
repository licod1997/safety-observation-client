package model;

public class Base64Image {
    private String file;

    public Base64Image() {
    }

    public Base64Image( String file ) {
        this.file = file;
    }

    public String getFile() {
        return file;
    }

    public void setFile( String file ) {
        this.file = file;
    }
}
