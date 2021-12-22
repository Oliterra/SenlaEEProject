package edu.senla.service;

import edu.senla.dao.CourierRepositoryInterface;
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
        Courier newCourier = courierRepository.save(mapper.map(newCourierDTO, Courier.class));
        return mapper.map(newCourier, CourierDTO.class);
    }

    @Override
    public CourierDTO readCourier(long id) {
        return mapper.map(courierRepository.getById(id), CourierDTO.class);
    }

    @Override
    public CourierDTO updateCourier(long id, CourierDTO courierDTO) {
        Courier updatedCourier = mapper.map(courierDTO, Courier.class);
        Courier courierToUpdate = courierRepository.getById(id);

        Courier courierWithNewParameters = updateCouriersOptions(courierToUpdate, updatedCourier);
        Courier courier = courierRepository.save(courierWithNewParameters);

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
    public void deleteCourier(long id) {
        courierRepository.deleteById(id);
    }

    @Override
    public CourierDTO getByIdWithOrders(long courierId) {
        //return mapper.map(courierRepository.getByIdWithOrders(courierId), CourierDTO.class);
        return null;
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
