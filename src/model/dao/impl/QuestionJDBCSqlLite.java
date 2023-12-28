package model.dao.impl;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.QuestionDao;
import model.entity.Answer;
import model.entity.Question;
import model.entity.Report;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.dto.SearchModel;
import model.util.Pager;
import model.util.SQLHelper;

public class QuestionJDBCSqlLite implements QuestionDao {

    private final Connection conn;

    public QuestionJDBCSqlLite(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Pager<Question> search(SearchModel<Question> searchedObj, boolean pagination) {
        List<Question> list = new ArrayList<>();
        Map<String, Object> parameters = new HashMap<>();
        
        if(searchedObj != null && searchedObj.getModel() != null){
            if (searchedObj.getModel().getId() != null && searchedObj.getModel().getId() > 0) {
                parameters.put("id", searchedObj.getModel().getId());
            }
            if (searchedObj.getModel().getText() != null && !searchedObj.getModel().getText().isEmpty()) {
                parameters.put("text", "%" + searchedObj.getModel().getText() + "%");
            }
            if (searchedObj.getModel().getActive() != null) {
                parameters.put("active" , searchedObj.getModel().getActive());
            }
        }
        
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            if(pagination == true){
                st = conn.prepareStatement(SQLHelper.buildSQLQueryText("SELECT id,text,active FROM Question", parameters, searchedObj != null ? searchedObj.getPage(): 0).toString());
            } else {
                st = conn.prepareStatement(SQLHelper.buildSQLQueryText("SELECT id,text,active FROM Question", parameters).toString());
            }
            
            int i = 1;
            for (Map.Entry<String, Object> item : parameters.entrySet()) {
                st.setObject(i++, item.getValue());
            }
            rs = st.executeQuery();
            while (rs.next()) {
                Question obj = new Question();
                obj.setId(rs.getInt("id"));
                obj.setText(rs.getString("text"));
                obj.setActive(rs.getBoolean("active"));
                buildAnswers(obj);
                
                list.add(obj);
            }
            
            int total_rows = SQLHelper.countRows("Question", parameters, conn);
            Pager<Question> pager = new Pager<>(searchedObj != null ? searchedObj.getPage() : 0, total_rows);
            pager.setItems(list);

            return pager;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }
    
    private void buildAnswers(Question obj) {
        PreparedStatement st_answers = null;
        ResultSet rs_answers = null;
        try {
            st_answers = conn.prepareStatement("""
                    SELECT Answer.id, Answer.text, reportId, sent
                    FROM Answer
                        INNER JOIN Question q ON q.id = questionId
                        INNER JOIN Report r ON r.id = reportId
                    WHERE questionId = ?
                """);
            st_answers.setInt(1, obj.getId());
            rs_answers = st_answers.executeQuery();
            Map<Integer, Report> reports_mapped = new HashMap<>();
            while (rs_answers.next()) {
                int answer_id = rs_answers.getInt("id");
                String answer_text = rs_answers.getString("text");
                int report_id = rs_answers.getInt("reportId");
                Date report_sent = rs_answers.getDate("sent");

                if (reports_mapped.get(report_id) == null) {
                    reports_mapped.put(report_id, new Report(report_id, report_sent));
                }

                Answer answer = new Answer(answer_id, new StringBuilder(answer_text), obj, reports_mapped.get(report_id));
                obj.addAnswer(answer);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs_answers);
            DB.closeStatement(st_answers);
        }
    }

    @Override
    public List<Question> findAll() {
        SearchModel<Question> obj = new SearchModel<>();
        Pager<Question> pager = search(obj, false);
        return pager.getItems();
    }

    @Override
    public Question findById(Integer id) {
        Question obj = new Question();
        obj.setId(id);
        SearchModel<Question> searchObj = new SearchModel<>(obj);
        Pager<Question> pager = search(searchObj, false);
        if (!pager.getItems().isEmpty()) {
            obj.setText(pager.getItems().get(0).getText());
            obj.setActive(pager.getItems().get(0).getActive());
            return obj;
        }
        return null;
    }

    @Override
    public void insert(Question obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("""
                    INSERT INTO Question(text, active)
                    VALUES(?, ?)
                """, Statement.RETURN_GENERATED_KEYS);
            st.setString(1, obj.getText());
            st.setBoolean(2, obj.getActive());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM Question WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void delete(Question obj) {
        deleteById(obj.getId());
    }

    @Override
    public void update(Question obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("""
                    UPDATE Question
                    SET sent = ?
                    WHERE id = ?
                """);
            st.setString(1, obj.getText());
            st.setBoolean(2, obj.getActive());
            st.setInt(3, obj.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

}
