package edu.senla.service;

import edu.senla.dao.DAO;
import edu.senla.dto.DishInformationDTO;
import edu.senla.entity.DishInformation;
import edu.senla.service.serviceinterface.DishInformationServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishInformationService implements DishInformationServiceInterface {

    private final DAO<DishInformation> dishInformationDAO;

    private final ModelMapper mapper;

    @Override
    public void createDishInformation(DishInformationDTO newDishInformationDTO) {
        dishInformationDAO.create(mapper.map(newDishInformationDTO, DishInformation.class));
    }

    @Override
    public DishInformationDTO read(int id) {
        DishInformation requestedDishInformation = dishInformationDAO.read(id);
        return mapper.map(requestedDishInformation, DishInformationDTO.class);
    }

    @Override
    public DishInformation update(DishInformationDTO dishInformationToUpdateDTO, DishInformationDTO updatedDishInformationDTO) {
        DishInformation updatedDishInformation = mapper.map(updatedDishInformationDTO, DishInformation.class);
        return updateDishInformationOptions(dishInformationDAO.read(dishInformationToUpdateDTO.getId()), updatedDishInformation);
    }

    @Override
    public void delete(int id) {
        dishInformationDAO.delete(id);
    }

    private DishInformation updateDishInformationOptions(DishInformation dishInformation, DishInformation updatedDishInformation)
    {
        dishInformation.setId(updatedDishInformation.getId());
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
