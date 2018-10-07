package model;

public class DetectionResult {
    private String tag;
    private Double score;

    public DetectionResult() {
    }

    public String getTag() {
        return tag;
    }

    public void setTag( String tag ) {
        this.tag = tag;
    }

    public Double getScore() {
        return score;
    }

    public void setScore( Double score ) {
        this.score = score;
    }
}
