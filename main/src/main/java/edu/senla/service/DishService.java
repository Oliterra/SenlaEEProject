package edu.senla.service;

import edu.senla.dao.DAO;
import edu.senla.dto.DishDTO;
import edu.senla.entity.Dish;
import edu.senla.service.serviceinterface.DishServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishService implements DishServiceInterface {

    private final DAO<Dish> dishDAO;

    private final ModelMapper mapper;

    @Override
    public void createDish(DishDTO newDishDTO) {
        dishDAO.create(mapper.map(newDishDTO, Dish.class));
    }

    @Override
    public DishDTO read(int id) {
        Dish requestedDish = dishDAO.read(id);
        return mapper.map(requestedDish, DishDTO.class);
    }

    @Override
    public Dish update(DishDTO dishToUpdateDTO, DishDTO updatedDishDTO) {
        Dish updatedDish = mapper.map(updatedDishDTO, Dish.class);
        return updateDishesOptions(dishDAO.read(dishToUpdateDTO.getId()), updatedDish);
    }

    @Override
    public void delete(int id) {
        dishDAO.delete(id);
    }

    private Dish updateDishesOptions(Dish dish, Dish updatedDish)
    {
        dish.setId(updatedDish.getId());
        dish.setDishType(updatedDish.getDishType());
        dish.setName(updatedDish.getName());
        return dish;
    }

}
