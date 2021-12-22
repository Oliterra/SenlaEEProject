package edu.senla.service;

import edu.senla.dao.DishInformationRepositoryInterface;
import edu.senla.dao.DishRepositoryInterface;
import edu.senla.dto.DishInformationDTO;
import edu.senla.entity.Dish;
import edu.senla.entity.DishInformation;
import edu.senla.service.serviceinterface.DishInformationServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class DishInformationService implements DishInformationServiceInterface {

    private final DishInformationRepositoryInterface dishInformationRepository;

    private final DishRepositoryInterface dishRepository;

    private final ModelMapper mapper;

    @Override
    public DishInformationDTO createDishInformation(long dishId, DishInformationDTO newDishInformationDTO) {
        DishInformation newDishInformation = mapper.map(newDishInformationDTO, DishInformation.class);
        Dish dish = dishRepository.getById(dishId);
        newDishInformation.setDish(dish);
        DishInformation createdInformation = dishInformationRepository.save(newDishInformation);
        dish.setDishInformation(createdInformation);
        return mapper.map(createdInformation, DishInformationDTO.class);
    }

    @Override
    public DishInformationDTO readDishInformation(long id) {
        return mapper.map(dishInformationRepository.getById(id), DishInformationDTO.class);
    }

    @Override
    public DishInformationDTO updateDishInformation(long id, DishInformationDTO updatedDishInformationDTO) {
        DishInformation updatedDishInformation = mapper.map(updatedDishInformationDTO, DishInformation.class);
        DishInformation dishInformationToUpdate = dishInformationRepository.getById(id);

        DishInformation dishInformationWithNewParameters = updateDishInformationOptions(dishInformationToUpdate, updatedDishInformation);
        DishInformation dishInformation = dishInformationRepository.save(dishInformationWithNewParameters);

        return mapper.map(dishInformation, DishInformationDTO.class);
    }

    @Override
    public void deleteDishInformation(long id) {
        dishInformationRepository.deleteById(id);
    }

    private DishInformation updateDishInformationOptions(DishInformation dishInformation, DishInformation updatedDishInformation)
    {
        dishInformation.setCarbohydrates(updatedDishInformation.getCarbohydrates());
        dishInformation.setCaloricContent(updatedDishInformation.getCaloricContent());
        dishInformation.setDescription(updatedDishInformation.getDescription());
        dishInformation.setCookingDate(updatedDishInformation.getCookingDate());
        dishInformation.setExpirationDate(updatedDishInformation.getExpirationDate());
        dishInformation.setFats(updatedDishInformation.getFats());
        dishInformation.setProteins(updatedDishInformation.getProteins());
        return dishInformation;
    }

}
