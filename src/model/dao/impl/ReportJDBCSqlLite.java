package model.dao.impl;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.ReportDao;
import model.entity.Answer;
import model.entity.Question;
import model.entity.Report;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportJDBCSqlLite implements ReportDao {

    private Connection conn;

    private AnswerJDBCSqlLite answerJDBC;

    public ReportJDBCSqlLite(Connection conn) {
        this.conn = conn;
        this.answerJDBC = new AnswerJDBCSqlLite(conn);
    }
    
    @Override
	public List<Report> findAll() {
    	List<Report> list = new ArrayList<>();
    	return list;
	}

    @Override
    public Report findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("SELECT id,text,active FROM Question WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();

            if(rs.next()){
                Report obj = new Report();
                obj.setId(rs.getInt("id"));
                obj.setSent(rs.getDate("sent"));

                DB.closeResultSet(rs);
                DB.closeStatement(st);
                st = conn.prepareStatement("""
                    SELECT id, text, question_id, Question.text AS question_text, Question.active AS question_active
                    FROM Answer
                        INNER JOIN Question ON Question.id = Answer.question_id
                        INNER JOIN Report ON Report.id = Answer.report_id
                    WHERE report_id = ?
                """);
                st.setInt(1, id);
                rs = st.executeQuery();
                Map<Integer, Question> questions_mapped = new HashMap<>();
                while (rs.next()) {
                    int answer_id = rs.getInt("id");
                    String answer_text = rs.getString("text");
                    int question_id = rs.getInt("question_id");
                    String question_text = rs.getString("question_text");
                    boolean question_active = rs.getBoolean("question_active");

                    if (questions_mapped.get(question_id) == null) {
                        questions_mapped.put(question_id, new Question(question_id, question_text, question_active));
                    }

                    Answer answer = new Answer(answer_id, new StringBuilder(answer_text), questions_mapped.get(question_id), obj);
                    obj.addAnswer(answer);
                }

                return obj;
            }
            return null;
        } catch(SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }

    @Override
    public void insert(Report obj) {
        PreparedStatement st = null;
        try {
            conn.setAutoCommit(false);
            st = conn.prepareStatement("""
                    INSERT INTO Report(id, sent)
                    VALUES(?, ?)
                """, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, obj.getId());
            st.setDate(2, new java.sql.Date(obj.getSent().getTime()));

            int rowsAffected = st.executeUpdate();
            if(rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    obj.setId(id);

                    // insert answers
                    for (Answer item : obj.getAnswers()) {
                        answerJDBC.insert(item);
                    }
                    conn.commit();
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch(SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            conn.setAutoCommit(false);

            Report obj = findById(id);
            // Delete answers
            for(Answer item : obj.getAnswers()){
                answerJDBC.delete(item);
            }

            // Delete report
            st = conn.prepareStatement("DELETE FROM Report WHERE id = ?");
            st.setInt(1, obj.getId());

            st.executeUpdate();
            conn.commit();
        } catch(SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void delete(Report obj) {
        deleteById(obj.getId());
    }

    @Override
    public void update(Report obj) {
        PreparedStatement st = null;
        try {
            conn.setAutoCommit(false);
            st = conn.prepareStatement("""
                    UPDATE Report
                    SET sent = ?
                    WHERE id = ?
                """);
            st.setDate(1, new java.sql.Date(obj.getSent().getTime()));
            st.setInt(2, obj.getId());
            st.executeUpdate();

            // Update answers
            for(Answer item : obj.getAnswers()){
                answerJDBC.update(item);
            }

            conn.commit();
        } catch(SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

}
