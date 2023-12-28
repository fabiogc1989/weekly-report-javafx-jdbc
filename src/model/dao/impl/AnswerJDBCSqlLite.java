package model.dao.impl;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.AnswerDao;
import model.entity.Answer;
import model.entity.Question;
import model.entity.Report;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnswerJDBCSqlLite implements AnswerDao {

    private Connection conn;

    public AnswerJDBCSqlLite(Connection conn) {
        this.conn = conn;
    }
    
    @Override
	public List<Answer> findAll() {
    	List<Answer> list = new ArrayList<Answer>();
    	PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("""
                    SELECT id, text, question_id, Question.text AS question_text, Question.active AS question_active, report_id, Report.sent AS report_sent
                    FROM Answer
                        INNER JOIN Question ON Question.id = Answer.question_id
                        INNER JOIN Report ON Report.id = Answer.report_id
            """);
            rs = st.executeQuery();

            while (rs.next()){
                Answer obj = new Answer();
                obj.setId(rs.getInt("id"));
                obj.setText(new StringBuilder(rs.getString("text")));

                Report report = new Report();
                report.setId(rs.getInt("report_id"));
                report.setSent(rs.getDate("report_sent"));
                obj.setReport(report);

                Question question = new Question();
                question.setId(rs.getInt("question_id"));
                question.setText(rs.getString("question_text"));
                question.setActive(rs.getBoolean("question_active"));
                obj.setQuestion(question);

                list.add(obj);
            }
            return list;
        } catch(SQLException e){
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
	}

    @Override
    public Answer findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement("""
                    SELECT id, text, question_id, Question.text AS question_text, Question.active AS question_active, report_id, Report.sent AS report_sent
                    FROM Answer
                        INNER JOIN Question ON Question.id = Answer.question_id
                        INNER JOIN Report ON Report.id = Answer.report_id
                    WHERE id = ?
            """);
            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()){
                Answer obj = new Answer();
                obj.setId(rs.getInt("id"));
                obj.setText(new StringBuilder(rs.getString("text")));

                Report report = new Report();
                report.setId(rs.getInt("report_id"));
                report.setSent(rs.getDate("report_sent"));
                obj.setReport(report);

                Question question = new Question();
                question.setId(rs.getInt("question_id"));
                question.setText(rs.getString("question_text"));
                question.setActive(rs.getBoolean("question_active"));
                obj.setQuestion(question);

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
    public void insert(Answer obj) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("""
                INSERT INTO Answer(id, text, question_id, report_id)
                VALUES(?, ?, ?, ?)
            """, Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, obj.getId());
            st.setString(2, obj.getText().toString());
            st.setInt(3, obj.getQuestion().getId());
            st.setInt(4, obj.getReport().getId());

            int rowsAffected = st.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            }
        } catch(SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM Answer WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public void delete(Answer obj) {
        deleteById(obj.getId());
    }

    @Override
    public void update(Answer obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("""
                    UPDATE Answer
                    SET text = ?, question_id = ?, report_id = ?
                    WHERE id = ?
            """);
            st.setString(1, obj.getText().toString());
            st.setInt(2, obj.getQuestion().getId());
            st.setInt(3, obj.getReport().getId());
            st.setInt(4, obj.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
        }
    }

}
