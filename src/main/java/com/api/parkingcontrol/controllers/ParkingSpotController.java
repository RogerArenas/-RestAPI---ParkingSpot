package com.api.parkingcontrol.controllers;

import com.api.parkingcontrol.dtos.ParkingSpotDto;
import com.api.parkingcontrol.models.ParkingSpotModel;
import com.api.parkingcontrol.services.ParkingSpotService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*",  maxAge = 3600)
@RequestMapping("/parking-spot")
public class ParkingSpotController {

	@Autowired
	private ParkingSpotService service;

	public ParkingSpotController( ParkingSpotService service ) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<Object> saveParking ( @RequestBody @Valid ParkingSpotDto spotDto ) {
		if(service.existsBYLicensePlateCar(spotDto.getLicensePlateCar())){
			return ResponseEntity.status( HttpStatus.CONFLICT).body( "Conflict : License Plate Car is already in use" );
		}
		if(service.existsByParkingSpotNumber (spotDto.getParkingSpotNumber())){
			return ResponseEntity.status( HttpStatus.CONFLICT ).body("Conflict: Parking spot is already in use");
		}
		if(service.existsByApartmentAndBlock(spotDto.getApartment(), spotDto.getBlock()  )){
			return ResponseEntity.status( HttpStatus.CONFLICT ).body("Conflict: Parking spot already registered other apartment");
		}

		 var parkingSpotModel = new ParkingSpotModel();
		BeanUtils.copyProperties(spotDto,  parkingSpotModel);
		parkingSpotModel.setRegistrationDate( LocalDateTime.now( ZoneId.of( "UTC" )) );
		return ResponseEntity.status( HttpStatus.CREATED ).body( service.save(parkingSpotModel) );
	}


	@GetMapping
	public ResponseEntity <List<ParkingSpotModel>> getAllParkingSpot() {
		return  ResponseEntity.status( HttpStatus.OK ).body(service.findAll() );
	}


	@GetMapping("/{id}")
	public ResponseEntity<Object> getOneParkingSpot(@PathVariable(value = "id") Long id){
		Optional<ParkingSpotModel> parkingSpotModelOptional = service.findById(id);
		if (!parkingSpotModelOptional.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Spot not found.");
		}
		return ResponseEntity.status(HttpStatus.OK).body(parkingSpotModelOptional.get());
	}

  @DeleteMapping("/{id}")
	public ResponseEntity <Object> deleteParkingSpot(@PathVariable (value = "id") Long id) {
		Optional<ParkingSpotModel> optional = service.findById( id );
		if (!optional.isPresent() ) {
			return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Parking Spot not found " );
		}
		service.delete(optional.get());
	    return ResponseEntity.status(HttpStatus.OK).body(optional.get());
  }

   @PutMapping("/{id}")
	public ResponseEntity<Object> UpdateParkingSpot ( @PathVariable (value = "id") Long id, @RequestBody @Valid ParkingSpotDto dto ) {
		Optional<ParkingSpotModel> optional = service.findById( id );
		if ( !optional.isPresent() ){
			return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "Not found" );
		}
		var parkingSpotModel = new ParkingSpotModel();
		BeanUtils.copyProperties( dto, parkingSpotModel );
		parkingSpotModel.setId( (optional.get( ).getId( ) ) );
		parkingSpotModel.setRegistrationDate( optional.get( ).getRegistrationDate( ) );
		return ResponseEntity.status( HttpStatus.OK ).body(service.save( parkingSpotModel ));
    }





}
