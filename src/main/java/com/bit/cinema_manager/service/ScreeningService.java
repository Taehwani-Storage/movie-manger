package com.bit.cinema_manager.service;

import com.bit.cinema_manager.model.Screening;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ScreeningService {
    private final String NAMESPACE = "mappers.ScreeningMapper";
    private final SqlSession sqlSession;

    public List<Screening> getScreeningsByTheater(int theaterId) {
        return sqlSession.selectList(NAMESPACE + ".getScreeningsByTheater", theaterId);
    }

    public void addScreening(Screening screening) {
        sqlSession.insert(NAMESPACE + ".addScreening", screening);
    }

    public void updateScreening(Screening screening) {
        sqlSession.update(NAMESPACE + ".updateScreening", screening);
    }

    public void deleteScreening(int id) {
        sqlSession.delete(NAMESPACE + ".deleteScreening", id);
    }
}