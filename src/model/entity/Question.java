/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.entity;

import model.dto.QuestionDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Fabio Coimbra
 */
public class Question implements EntityConvertible<QuestionDto> {

    private Integer id;
    private String text;
    private Boolean active;
    private List<Answer> answers = new ArrayList<>();

    public Question() {
    }

    public Question(Integer id, String text, Boolean active) {
        this.id = id;
        this.text = text;
        this.active = active;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void addAnswer(Answer item) {
        this.answers.add(item);
    }

    public void removeAnswer(Answer item) {
        this.answers.remove(item);
    }

    @Override
    public QuestionDto toDto() {
        return new QuestionDto(getId(), getText(), getActive());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.id;
        hash = 71 * hash + Objects.hashCode(this.text);
        hash = 71 * hash + (this.active ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Question other = (Question) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.active != other.active) {
            return false;
        }
        return Objects.equals(this.text, other.text);
    }
}
