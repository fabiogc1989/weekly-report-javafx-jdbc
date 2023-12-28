package model.dto;

import model.entity.Answer;
import model.entity.Question;
import model.entity.Report;

public class AnswerDto implements DtoConvertible<Answer> {
    private Integer id;
    private StringBuilder text;
    private Question question;
    private Report report;

    public AnswerDto(){}

    public AnswerDto(Integer id, StringBuilder text, Question question, Report report){
        this.id = id;
        this.text = text;
        this.question = question;
        this.report = report;
    }

    public Integer getId() {
        return id;
    }

    public StringBuilder getText() {
        return text;
    }

    public void setText(StringBuilder text) {
        this.text = text;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    @Override
    public Answer toEntity() {
        return new Answer(getId(), getText(), getQuestion(), getReport());
    }
}
