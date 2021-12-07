package edu.senla.service;

import edu.senla.dao.daointerface.DishInformationRepositoryInterface;
import edu.senla.dao.daointerface.DishRepositoryInterface;
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
    public DishInformationDTO createDishInformation(int dishId, DishInformationDTO newDishInformationDTO) {
        DishInformation newDishInformation = mapper.map(newDishInformationDTO, DishInformation.class);
        Dish dish = dishRepository.read(dishId);
        newDishInformation.setDish(dish);
        DishInformation createdInformation = dishInformationRepository.create(newDishInformation);
        dish.setDishInformation(createdInformation);
        return mapper.map(createdInformation, DishInformationDTO.class);
    }

    @Override
    public DishInformationDTO readDishInformation(int id) {
        DishInformation requestedDishInformation = dishInformationRepository.read(id);
        return mapper.map(requestedDishInformation, DishInformationDTO.class);
    }

    @Override
    public DishInformationDTO updateDishInformation(int id, DishInformationDTO updatedDishInformationDTO) {
        DishInformation updatedDishInformation = mapper.map(updatedDishInformationDTO, DishInformation.class);
        DishInformation dishInformationToUpdate = mapper.map(readDishInformation(id), DishInformation.class);

        DishInformation dishInformationWithNewParameters = updateDishInformationOptions(dishInformationToUpdate, updatedDishInformation);
        DishInformation dishInformation = dishInformationRepository.update(dishInformationWithNewParameters);

        return mapper.map(dishInformation, DishInformationDTO.class);
    }

    @Override
    public void deleteDishInformation(int id) {
        dishInformationRepository.delete(id);
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
