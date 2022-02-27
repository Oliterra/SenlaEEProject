package edu.senla.dao.impl;

import edu.senla.dao.DishMapper;
import edu.senla.dao.DishRepository;
import edu.senla.model.entity.Dish;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Locale;

@Repository
@AllArgsConstructor
public class DishRepositoryImpl implements DishRepository {

    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Dish> findAll() {
        String sql = "SELECT * FROM dishes";
        List<Dish> dishes = jdbcTemplate.query(sql, new DishMapper());
        return dishes;
    }

    @Override
    public Dish save(Dish dishEntity) {
        String sql = "INSERT INTO dishes (name, type) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(d -> {
            PreparedStatement ps = d.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, dishEntity.getName());
            ps.setString(2, dishEntity.getType().toString().toLowerCase(Locale.ROOT));
            return ps;
        }, keyHolder);
        return getById(keyHolder.getKey().longValue());
    }

    @Override
    public Dish getById(long id) {
        String sql = "SELECT * FROM dishes WHERE id=?";
        return jdbcTemplate.queryForObject(sql, new DishMapper(), id);
    }

    @Override
    public void update(long id, Dish updatedDishEntity) {
        String sql = "UPDATE dishes SET name=?, type=? WHERE id=?";
        jdbcTemplate.update(sql, updatedDishEntity.getName(), updatedDishEntity.getType().toString().toLowerCase(Locale.ROOT), id);
    }

    @Override
    public void deleteById(long id) {
        String sql = "DELETE FROM dishes WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public boolean existsById(long id) {
        String sql = "SELECT count(*) FROM dishes WHERE id=?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) > 0;
    }

    @Override
    public Dish getByName(String name) {
        String sql = "SELECT * FROM dishes WHERE name=?";
        return jdbcTemplate.queryForObject(sql, new DishMapper(), name);
    }

    @Override
    public String getNameById(long id) {
        String sql = "SELECT name FROM dishes WHERE id=?";
        return jdbcTemplate.queryForObject(sql, String.class, id);
    }
}

