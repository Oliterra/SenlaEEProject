package edu.senla.service;

import edu.senla.dao.daointerface.CourierRepositoryInterface;
import edu.senla.dto.ClientDTO;
import edu.senla.dto.CourierDTO;
import edu.senla.entity.Courier;
import edu.senla.service.serviceinterface.CourierServiceInterface;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class CourierService implements CourierServiceInterface{

    private final CourierRepositoryInterface courierRepository;

    private final ModelMapper mapper;

    @Override
    public void createCourier(CourierDTO newCourierDTO) {
        courierRepository.create(mapper.map(newCourierDTO, Courier.class));
    }

    @Override
    public CourierDTO readCourier(int id) {
        Courier requestedCourier = courierRepository.read(id);
        return mapper.map(requestedCourier, CourierDTO.class);
    }

    @Override
    public void updateCourier(int id, CourierDTO updatedCourierDTO) {
        Courier updatedCourier = mapper.map(updatedCourierDTO, Courier.class);
        Courier courierToUpdate = mapper.map(readCourier(id), Courier.class);
        Courier courierWithNewParameters = updateCouriersOptions(courierToUpdate, updatedCourier);
        courierRepository.update(courierWithNewParameters);
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
    public int getCourierIdByPhone(String courierPhone) {
        return courierRepository.getIdByPhone(courierPhone);
    }

    @Override
    public String getByIdWithOrders(int courierId) {
        System.out.println(courierRepository.getByIdWithOrders(courierId));
        final Courier courier = courierRepository.getByIdWithOrders(courierId);
        System.out.println(courier);
        return courier.toString();
    }

    @Override
    public CourierDTO getByIdWithOrdersJPQL(int courierId) {
        return mapper.map(courierRepository.getByIdWithOrdersJPQL(courierId), CourierDTO.class);
    }

}
