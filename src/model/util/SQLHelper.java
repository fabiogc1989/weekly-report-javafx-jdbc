package model.util;

import application.Contants;
import db.DB;
import db.DbException;
import java.sql.*;
import java.util.Map;

public class SQLHelper {
    public static StringBuilder buildSQLQueryText(String query, Map<String, Object> parameters) {
        StringBuilder text_builder = new StringBuilder(query);
        for(Map.Entry<String, Object> entry : parameters.entrySet()){
            String field = entry.getKey();
            Object value = entry.getValue();
            if(value instanceof String){
                text_builder.append(text_builder.toString().contains("WHERE") ? " AND " : " WHERE ").append(field).append(" Like ?");
            } else {
                text_builder.append(text_builder.toString().contains("WHERE") ? " AND " : " WHERE ").append(field).append(" = ?");
            }
        }
        
        return text_builder;
    }
    
    public static StringBuilder buildSQLQueryText(String query, Map<String, Object> parameters, int page) {
        StringBuilder text_builder = SQLHelper.buildSQLQueryText(query, parameters);
        text_builder.append(" LIMIT ").append(Contants.ITEMS_PER_PAGE).append(" OFFSET ").append(Contants.ITEMS_PER_PAGE * page);
        return text_builder;
    }
    
    public static int countRows(String table, Map<String, Object> parameters, Connection conn){
        PreparedStatement st_total = null;
        ResultSet rs_total = null;
        try {
            st_total = conn.prepareStatement(SQLHelper.buildSQLQueryText("SELECT count(*) FROM " + table, parameters).toString());
            int i = 1;
            for (Object item : parameters.values()) {
                st_total.setObject(i++, item);
            }
            rs_total = st_total.executeQuery();
            if (rs_total.next()){
                return rs_total.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs_total);
            DB.closeStatement(st_total);
        }
    }
}
