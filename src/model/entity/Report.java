/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

import model.dto.ReportDto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Fabio Coimbra
 */
public class Report implements EntityConvertible<ReportDto> {

    private Integer id;
    private Date sent;
    private List<Answer> answers = new ArrayList<>();

    public Report() {
    }

    public Report(Integer id, Date sent) {
        this.id = id;
        this.sent = sent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public List<Answer> getAnswers(){
        return answers;
    }

    public void addAnswer(Answer item) {
        this.answers.add(item);
    }

    public void removeAnswer(Answer item) {
        this.answers.remove(item);
    }

    @Override
    public ReportDto toDto() {
        ReportDto obj = new ReportDto(getId(), getSent());
        getAnswers()
                .stream()
                .map(a -> a.toDto())
                .forEach(x -> {obj.addAnswer(x);});
        return obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Report report = (Report) o;

        if (id != report.id) return false;
        return Objects.equals(sent, report.sent);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (sent != null ? sent.hashCode() : 0);
        return result;
    }
}
