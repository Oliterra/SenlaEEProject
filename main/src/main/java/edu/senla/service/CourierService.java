package edu.senla.service;

import edu.senla.dao.DAO;
import edu.senla.dto.CourierDTO;
import edu.senla.entity.Courier;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourierService implements CourierServiceInterface {

    private final DAO<Courier> courierDAO;

    private final ModelMapper mapper;

    @Override
    public void createCourier(CourierDTO newCourierDTO) {
        courierDAO.create(mapper.map(newCourierDTO, Courier.class));
    }

    @Override
    public CourierDTO read(int id) {
        Courier requestedСourier = courierDAO.read(id);
        return mapper.map(requestedСourier, CourierDTO.class);
    }

    @Override
    public Courier update(CourierDTO courierToUpdateDTO, CourierDTO updatedCourierDTO) {
        Courier updatedCourier = mapper.map(updatedCourierDTO, Courier.class);
        return updateCouriersOptions(courierDAO.read(courierToUpdateDTO.getId()), updatedCourier);
    }

    @Override
    public void delete(int id) {
        courierDAO.delete(id);
    }

    private Courier updateCouriersOptions(Courier courier, Courier updatedCourier)
    {
        courier.setId(updatedCourier.getId());
        courier.setFirstName(updatedCourier.getFirstName());
        courier.setLastName(updatedCourier.getLastName());
        courier.setPhone(updatedCourier.getPhone());
        return courier;
    }

}
