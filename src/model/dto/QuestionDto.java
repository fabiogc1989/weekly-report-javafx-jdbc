package model.dto;

import model.entity.Question;

public class QuestionDto implements DtoConvertible<Question> {
    private Integer id;
    private String text;
    private Boolean isActive;

    public QuestionDto() {}

    public QuestionDto(Integer id, String text, Boolean isActive) {
        this.id = id;
        this.text = text;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Question toEntity(){
        return new Question(getId(),getText(),getIsActive());
    }
}
