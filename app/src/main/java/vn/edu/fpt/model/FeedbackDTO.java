package vn.edu.fpt.model;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FeedbackDTO {
    private String feedbackDescription;
    private Long time;
    private FeedbackPhotoDTO[] feedbackPhotoList;

    public FeedbackDTO() {
    }

    @Override
    public String toString() {
        return "FeedbackDTO{" +
                "feedbackDescription='" + feedbackDescription + '\'' +
                ", time=" + time +
                ", feedbackPhotoList=" + Arrays.toString(feedbackPhotoList) +
                '}';
    }

    public String getFeedbackDescription() {
        return feedbackDescription;
    }

    public void setFeedbackDescription(String feedbackDescription) {
        this.feedbackDescription = feedbackDescription;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public FeedbackPhotoDTO[] getFeedbackPhotoList() {
        return feedbackPhotoList;
    }

    public void setFeedbackPhotoList(FeedbackPhotoDTO[] feedbackPhotoList) {
        this.feedbackPhotoList = feedbackPhotoList;
    }

    public FeedbackDTO(String feedbackDescription, Long time, FeedbackPhotoDTO[] feedbackPhotoList) {

        this.feedbackDescription = feedbackDescription;
        this.time = time;
        this.feedbackPhotoList = feedbackPhotoList;
    }
}
