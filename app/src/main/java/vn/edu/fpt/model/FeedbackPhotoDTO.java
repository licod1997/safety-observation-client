package vn.edu.fpt.model;

public class FeedbackPhotoDTO {
    private String photoName;
    private String photoDirectory;

    public FeedbackPhotoDTO() {
    }

    public FeedbackPhotoDTO(String photoName, String photoDirectory, FeedbackDTO feedbackDTO) {
        this.photoName = photoName;
        this.photoDirectory = photoDirectory;
    }

    public String getPhotoName() {
        return photoName;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public String getPhotoDirectory() {
        return photoDirectory;
    }

    public void setPhotoDirectory(String photoDirectory) {
        this.photoDirectory = photoDirectory;
    }



    @Override
    public String toString() {
        return "FeedbackPhotoDTO{" +
                "photoName='" + photoName + '\'' +
                ", photoDirectory='" + photoDirectory + '\'' +
                '}';
    }
}
