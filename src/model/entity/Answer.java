/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

import model.dto.AnswerDto;

import java.util.Objects;

/**
 *
 * @author Fabio Coimbra
 */
public class Answer implements EntityConvertible<AnswerDto> {

    private Integer id;
    StringBuilder text;
    Question question;
    Report report;

    public Answer() {
    }

    public Answer(Integer id, StringBuilder text, Question question, Report report) {
        this.id = id;
        this.text = text;
        this.question = question;
        this.report = report;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    public AnswerDto toDto() {
        return new AnswerDto(getId(), getText(), getQuestion(), getReport());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Answer answer = (Answer) o;

        if (id != answer.id) return false;
        if (!Objects.equals(text, answer.text)) return false;
        if (!Objects.equals(question, answer.question)) return false;
        return Objects.equals(report, answer.report);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + (question != null ? question.hashCode() : 0);
        result = 31 * result + (report != null ? report.hashCode() : 0);
        return result;
    }
}
