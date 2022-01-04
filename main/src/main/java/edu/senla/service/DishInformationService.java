package edu.senla.service;

import edu.senla.dao.DishInformationRepositoryInterface;
import edu.senla.dao.DishRepositoryInterface;
import edu.senla.dto.DishInformationDTO;
import edu.senla.entity.Dish;
import edu.senla.entity.DishInformation;
import edu.senla.service.serviceinterface.DishInformationServiceInterface;
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
public class DishInformationService implements DishInformationServiceInterface {

    private final DishInformationRepositoryInterface dishInformationRepository;

    private final DishRepositoryInterface dishRepository;

    private final ModelMapper mapper;

    public List<DishInformationDTO> getAllDishesInformation() {
        Page<DishInformation> dishesInformation = dishInformationRepository.findAll(PageRequest.of(0, 10, Sort.by("caloricContent").descending()));
        return dishesInformation.stream().map(d -> mapper.map(d, DishInformationDTO.class)).toList();
    }

    public void createDishInformation(DishInformationDTO newDishInformationDTO) {
        DishInformation newDishInformation = mapper.map(newDishInformationDTO, DishInformation.class);
        Dish dish = dishRepository.getById(newDishInformationDTO.getDishId());
        dish.setDishInformation(dishInformationRepository.saveAndFlush(newDishInformation));
        dishRepository.save(dish);
    }

    public DishInformationDTO getDishInformation(long id) {
        try {
            return mapper.map(dishInformationRepository.getById(id), DishInformationDTO.class);
        } catch (RuntimeException exception) {
            return null;
        }
    }

    public void updateDishInformation(long id, DishInformationDTO updatedDishInformationDTO) {
        DishInformation updatedDishInformation = mapper.map(updatedDishInformationDTO, DishInformation.class);
        DishInformation dishInformationToUpdate = dishInformationRepository.getById(id);
        DishInformation dishInformationWithNewParameters = updateDishInformationOptions(dishInformationToUpdate, updatedDishInformation);
        dishInformationRepository.save(dishInformationWithNewParameters);
    }

    public void deleteDishInformation(long id) {
        dishInformationRepository.deleteById(id);
    }

    public boolean isDishInformationExists(long id) {
        return dishInformationRepository.existsById(id);
    }

    private DishInformation updateDishInformationOptions(DishInformation dishInformation, DishInformation updatedDishInformation) {
        dishInformation.setDescription(updatedDishInformation.getDescription());
        dishInformation.setProteins(updatedDishInformation.getProteins());
        dishInformation.setFats(updatedDishInformation.getFats());
        dishInformation.setCarbohydrates(updatedDishInformation.getCarbohydrates());
        dishInformation.setCaloricContent(updatedDishInformation.getCaloricContent());
        return dishInformation;
    }

}
