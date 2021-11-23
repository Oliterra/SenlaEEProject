package edu.senla.service;

import edu.senla.dao.daointerface.ClientRepositoryInterface;
import edu.senla.dao.daointerface.DishRepositoryInterface;
import edu.senla.dto.DishDTO;
import edu.senla.entity.Client;
import edu.senla.entity.Dish;
import edu.senla.service.serviceinterface.DishServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class DishService implements DishServiceInterface {

    private final DishRepositoryInterface dishRepository;

    private final ModelMapper mapper;

    @Override
    public void createDish(DishDTO newDishDTO) {
        dishRepository.create(mapper.map(newDishDTO, Dish.class));
    }

    @Override
    public DishDTO readDish(int id) {
        Dish requestedDish = dishRepository.read(id);
        return mapper.map(requestedDish, DishDTO.class);
    }

    @Override
    public void updateDish(int id, DishDTO updatedDishDTO) {
        Dish updatedDish = mapper.map(updatedDishDTO, Dish.class);
        Dish dishToUpdate = mapper.map(readDish(id), Dish.class);
        Dish dishWithNewParameters = updateDishesOptions(dishToUpdate, updatedDish);
        dishRepository.update(dishWithNewParameters);
    }

    private Dish updateDishesOptions(Dish dish, Dish updatedDish)
    {
        dish.setDishType(updatedDish.getDishType());
        dish.setName(updatedDish.getName());
        return dish;
    }

    @Override
    public void deleteDish(int id) {
        dishRepository.delete(id);
    }

    @Override
    public int getDishIdByName(String dishName) {
        return dishRepository.getIdByName(dishName);
    }

}
