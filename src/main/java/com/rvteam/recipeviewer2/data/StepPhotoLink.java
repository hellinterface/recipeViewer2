package com.rvteam.recipeviewer2.data;

public class StepPhotoLink implements IEntity {
    private Integer id;
    private Integer stepId;
    private Integer photoId;

    public StepPhotoLink(int _id, int _stepId, int _photoId) {
        id = _id;
        stepId = _stepId;
        photoId = _photoId;
    }

    public Integer getID() {
        return this.id;
    }
    public void setID(int n) {
        this.id = n;
    }

    public Integer getStepID() {
        return stepId;
    }

    public Integer getPhotoID() {
        return photoId;
    }
}
