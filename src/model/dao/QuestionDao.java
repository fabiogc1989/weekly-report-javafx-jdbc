/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;

import model.dto.SearchModel;
import model.entity.Question;
import model.util.Pager;

/**
 *
 * @author Fabio Coimbra
 */
public interface QuestionDao extends SharedDao<Integer, Question> {
	
	public Pager<Question> search(SearchModel<Question> obj, boolean pagination);
}
