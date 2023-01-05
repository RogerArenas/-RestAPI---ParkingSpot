package com.api.parkingcontrol.services;

import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.repositories.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ParkingSpotService {

	@Autowired
	ParkingSpotRepository repository;

	public ParkingSpotService ( ParkingSpotRepository parkingSpotRepository) {
		this.repository = parkingSpotRepository;
	}

	public  boolean existsBYLicensePlateCar( String licensePlateCar ) {
		return repository.existsByLicensePlateCar( licensePlateCar );
	}

	@Transactional
	public ParkingSpotModel save( ParkingSpotModel parkingSpotModel ) {
		return repository.save( parkingSpotModel );
	}

	public boolean existsByParkingSpotNumber( String parkingSpotNumber ) {
		return repository.existsByParkingSpotNumber(parkingSpotNumber )
;	}

	public boolean existsByApartmentAndBlock( String apartment, String block ) {
		return repository.existsByApartmentAndBlock( apartment, block);
	}

	public List< ParkingSpotModel> findAll( ) {
		return repository.findAll();
	}

	public Optional< ParkingSpotModel> findById( Long id ) {
		return repository.findById( id );
	}
   @Transactional
	public void delete( ParkingSpotModel parkingSpotModel ) {
		 repository.delete(parkingSpotModel);
	}
}
