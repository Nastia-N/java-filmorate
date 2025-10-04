package ru.yandex.practicum.filmorate.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MPAMapper implements RowMapper<MpaRating> {
    @Override
    public MpaRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        MpaRating mr = new MpaRating();
        mr.setId(rs.getInt("id"));
        mr.setName(rs.getString("name"));
        return mr;
    }
}
