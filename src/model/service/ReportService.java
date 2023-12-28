package model.service;

import model.dao.DaoFactory;
import model.dao.ReportDao;
import model.dto.ReportDto;
import model.entity.Report;

public class ReportService {
    private ReportDao dao = DaoFactory.createReportDao();

    public ReportService(){}

    public ReportDto findById(Integer id){
        Report report = dao.findById(id);
        return report.toDto();
    }

    public void delete(ReportDto obj){
        dao.delete(obj.toEntity());
    }

    public void insert(ReportDto obj){
        dao.insert(obj.toEntity());
    }

    public void update(ReportDto obj){
        dao.update(obj.toEntity());
    }
}
