package edu.senla.service;

import edu.senla.dao.daointerface.CourierRepositoryInterface;
import edu.senla.dto.CourierDTO;
import edu.senla.entity.Courier;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CourierService implements CourierServiceInterface{

    private final CourierRepositoryInterface courierRepository;

    private final ModelMapper mapper;

    @Override
    public CourierDTO createCourier(CourierDTO newCourierDTO) {
        Courier newCourier = courierRepository.create(mapper.map(newCourierDTO, Courier.class));
        return mapper.map(newCourier, CourierDTO.class);
    }

    @Override
    public CourierDTO readCourier(int id) {
        Courier requestedCourier = courierRepository.read(id);
        return mapper.map(requestedCourier, CourierDTO.class);
    }

    @Override
    public CourierDTO updateCourier(int id, CourierDTO courierDTO) {
        Courier updatedCourier = mapper.map(courierDTO, Courier.class);
        Courier courierToUpdate = mapper.map(readCourier(id), Courier.class);

        Courier courierWithNewParameters = updateCouriersOptions(courierToUpdate, updatedCourier);
        Courier courier = courierRepository.update(courierWithNewParameters);

        return mapper.map(courier, CourierDTO.class);
    }

    private Courier updateCouriersOptions(Courier courier, Courier updatedCourier)
    {
        courier.setFirstName(updatedCourier.getFirstName());
        courier.setLastName(updatedCourier.getLastName());
        courier.setPhone(updatedCourier.getPhone());
        return courier;
    }

    @Override
    public void deleteCourier(int id) {
        courierRepository.delete(id);
    }

    @Override
    public CourierDTO getByIdWithOrders(int courierId) {
        return mapper.map(courierRepository.getByIdWithOrders(courierId), CourierDTO.class);
    }

    @Override
    public boolean isCourierExists(CourierDTO courier) {
        try {
            return courierRepository.getCourierByPhone(courier.getPhone()) != null;
        }
        catch (NoResultException e){
            return false;
        }
    }

}
