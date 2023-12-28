package model.dto;

import model.entity.Report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReportDto implements DtoConvertible<Report> {
    private Integer id;
    private Date sent;
    private List<AnswerDto> answers = new ArrayList<>();

    public ReportDto() {}

    public ReportDto(Integer id, Date sent) {
        this.id = id;
        this.sent = sent;
    }

    public Integer getId() {
        return id;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public List<AnswerDto> getAnswers(){
        return this.answers;
    }

    public void addAnswer(AnswerDto item){
        getAnswers().add(item);
    }

    public void removeAnswer(AnswerDto item){
        getAnswers().remove(item);
    }

    @Override
    public Report toEntity() {
        Report obj = new Report(getId(), getSent());
        getAnswers()
                .stream()
                .map(a -> a.toEntity())
                .forEach(x -> {obj.addAnswer(x);});
        return null;
    }
}
