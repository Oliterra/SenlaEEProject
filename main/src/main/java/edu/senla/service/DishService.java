package edu.senla.service;

import edu.senla.dao.daointerface.DishRepositoryInterface;
import edu.senla.dto.DishDTO;
import edu.senla.entity.Dish;
import edu.senla.service.serviceinterface.DishServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class DishService implements DishServiceInterface {

    private final DishRepositoryInterface dishRepository;

    private final ModelMapper mapper;

    @Override
    public DishDTO createDish(DishDTO newDishDTO) {
        Dish newDish = dishRepository.create(mapper.map(newDishDTO, Dish.class));
        return mapper.map(newDish, DishDTO.class);
    }

    @Override
    public DishDTO readDish(int id) {
        Dish requestedDish = dishRepository.read(id);
        return mapper.map(requestedDish, DishDTO.class);
    }

    @Override
    public DishDTO updateDish(int id, DishDTO updatedDishDTO) {
        Dish updatedDish = mapper.map(updatedDishDTO, Dish.class);
        Dish dishToUpdate = mapper.map(readDish(id), Dish.class);

        Dish dishWithNewParameters = updateDishesOptions(dishToUpdate, updatedDish);
        Dish dish = dishRepository.update(dishWithNewParameters);

        return mapper.map(dish, DishDTO.class);
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
    public DishDTO getByIdWithFullInformation(int dishId) {
        return mapper.map(dishRepository.getByIdWithFullInformation(dishId), DishDTO.class);
    }

    @Override
    public boolean isDishExists(DishDTO dish) {
        try {
            return dishRepository.getDishByName(dish.getName()) != null;
        }
        catch (NoResultException e){
            return false;
        }
    }

}
