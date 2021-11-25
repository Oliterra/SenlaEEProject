package edu.senla.service;

import edu.senla.dao.daointerface.DishInformationRepositoryInterface;
import edu.senla.dao.daointerface.DishRepositoryInterface;
import edu.senla.dto.DishInformationDTO;
import edu.senla.entity.Dish;
import edu.senla.entity.DishInformation;
import edu.senla.entity.Order;
import edu.senla.service.serviceinterface.DishInformationServiceInterface;
import edu.senla.service.serviceinterface.DishServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class DishInformationService implements DishInformationServiceInterface {

    private final DishInformationRepositoryInterface dishInformationRepository;

    private final DishServiceInterface dishService;

    private final ModelMapper mapper;

    @Override
    public void createDishInformation(DishInformationDTO newDishInformationDTO) {
        DishInformation newDishInformation = mapper.map(newDishInformationDTO, DishInformation.class);
        DishInformation newDishInformationEntity = dishInformationRepository.create(mapper.map(newDishInformationDTO, DishInformation.class));
        System.out.println("bla" + newDishInformationEntity);
        //dishService.setDishInformation(dishId, newDishInformationEntity);
    }

    @Override
    public DishInformationDTO readDishInformation(int id) {
        DishInformation requestedDishInformation = dishInformationRepository.read(id);
        return mapper.map(requestedDishInformation, DishInformationDTO.class);
    }

    @Override
    public void updateDishInformation(int id, DishInformationDTO updatedDishInformationDTO) {
        DishInformation updatedDishInformation = mapper.map(updatedDishInformationDTO, DishInformation.class);
        DishInformation dishInformationToUpdate = mapper.map(readDishInformation(id), DishInformation.class);
        DishInformation dishInformationWithNewParameters = updateDishInformationOptions(dishInformationToUpdate, updatedDishInformation);
        dishInformationRepository.update(dishInformationWithNewParameters);
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
