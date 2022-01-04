package edu.senla.service;

import edu.senla.dao.DishRepositoryInterface;
import edu.senla.dto.ContainerComponentsDTO;
import edu.senla.dto.DishDTO;
import edu.senla.entity.Dish;
import edu.senla.service.serviceinterface.DishServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class DishService implements DishServiceInterface {

    private final DishRepositoryInterface dishRepository;

    private final ModelMapper mapper;

    @Override
    public List<DishDTO> getAllDishes() {
        Page<Dish> dishes= dishRepository.findAll(PageRequest.of(0, 10, Sort.by("name").descending()));
        return dishes.stream().map(d -> mapper.map(d, DishDTO.class)).toList();
    }

    public void createDish(DishDTO newDishDTO) {
        dishRepository.save(mapper.map(newDishDTO, Dish.class));
    }

    public DishDTO getDish(long id) {
        try {
            return mapper.map(dishRepository.getById(id), DishDTO.class);
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public void updateDish(long id, DishDTO updatedDishDTO) {
        Dish updatedDish = mapper.map(updatedDishDTO, Dish.class);
        Dish dishToUpdate = dishRepository.getById(id);
        Dish dishWithNewParameters = updateDishesOptions(dishToUpdate, updatedDish);
        dishRepository.save(dishWithNewParameters);
    }

    public void deleteDish(long id) {
        dishRepository.deleteById(id);
    }

    public boolean isDishExists(Long id) {
        return dishRepository.existsById(id);
    }

    public boolean isDishExists(String name) {
        return dishRepository.getByName(name) != null;
    }

    public boolean isDishHasDishInformation(long id) {
        return dishRepository.getById(id).getDishInformation() != null;
    }

    public boolean isDishTypeCorrect(String dishType) {
        return dishType.equals("meat") || dishType.equals("garnish") || dishType.equals("salad") || dishType.equals("sauce");
    }

    public boolean isAllDishesHaveDishInformation(ContainerComponentsDTO containerComponentsDTO) {
        long meat = containerComponentsDTO.getMeat();
        long garnish = containerComponentsDTO.getGarnish();
        long salad = containerComponentsDTO.getSalad();
        long sauce = containerComponentsDTO.getSauce();
        return isDishHasDishInformation(meat) && isDishHasDishInformation(garnish) && isDishHasDishInformation(salad) && isDishHasDishInformation(sauce);
    }

    private Dish updateDishesOptions(Dish dish, Dish updatedDish) {
        dish.setDishType(updatedDish.getDishType());
        dish.setName(updatedDish.getName());
        return dish;
    }

}
