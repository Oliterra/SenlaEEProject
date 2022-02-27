package edu.senla.dao;

import edu.senla.exeption.UnexpectedInternalError;
import edu.senla.model.entity.Dish;
import edu.senla.model.enums.DishType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.Locale;

public class DishMapper implements RowMapper<Dish> {

    @Override
    public Dish mapRow(ResultSet rs, int rowNum) {
        Dish dish = new Dish();
        try {
            dish.setId(rs.getLong("id"));
            dish.setName(rs.getString("name"));

            dish.setType(DishType.valueOf(rs.getString("type").toUpperCase(Locale.ROOT)));
            return dish;
        } catch (Exception throwables) {
            throw new UnexpectedInternalError("Sorry, an internal error has occurred. Try again later");
        }
    }
}
