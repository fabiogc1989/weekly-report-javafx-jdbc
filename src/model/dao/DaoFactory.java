package model.dao;

import db.DB;
import model.dao.impl.AnswerJDBCSqlLite;
import model.dao.impl.QuestionJDBCSqlLite;
import model.dao.impl.ReportJDBCSqlLite;

public class DaoFactory {
    public static QuestionDao createQuestionDao () {
        return new QuestionJDBCSqlLite(DB.getConnection());
    }
    public static AnswerDao createAnswerDao () {
        return new AnswerJDBCSqlLite(DB.getConnection());
    }
    public static ReportDao createReportDao () {
        return new ReportJDBCSqlLite(DB.getConnection());
    }
}
