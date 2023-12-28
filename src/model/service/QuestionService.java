package model.service;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.QuestionDao;
import model.dto.QuestionDto;
import model.dto.SearchModel;
import model.entity.Question;
import model.util.Pager;

public class QuestionService {
    private QuestionDao dao = DaoFactory.createQuestionDao();

    public QuestionService () {}
    
    public Pager<QuestionDto> search(SearchModel<QuestionDto> searchedObj){
        SearchModel<Question> obj = new SearchModel<>(searchedObj.getModel().toEntity(), searchedObj.getPage());
    	Pager<Question> searchedList = dao.search(obj, true);
    	
        List<QuestionDto> list = new ArrayList<>();
    	for(Question item : searchedList.getItems()) {
    		list.add(item.toDto());
    	}
        
    	Pager<QuestionDto> output = new Pager<>(searchedList.getPage(), searchedList.getTotalItems());
        output.setItems(list);
        return output;
    }
    
    public List<QuestionDto> findAll() {
    	List<QuestionDto> list_dto = new ArrayList<>();
    	List<Question> list = dao.findAll();
    	
    	for(Question obj : list) {
    		list_dto.add(obj.toDto());
    	}
    	return list_dto;
    }

    public QuestionDto findById(Integer id) {
        Question question = dao.findById(id);
        return question.toDto();
    }

    public void delete(QuestionDto obj) {
        dao.delete(obj.toEntity());
    }

    public void insert(QuestionDto obj) {
        dao.insert(obj.toEntity());
    }

    public void update(QuestionDto obj) {
        dao.update(obj.toEntity());
    }

}
