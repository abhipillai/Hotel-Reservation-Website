package com.abhishek.business.service;

import org.springframework.stereotype.Service;

import com.abhishek.data.entity.Guest;
import com.abhishek.data.entity.Reservation;
import com.abhishek.data.entity.Room;
import com.abhishek.data.repository.*;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import com.abhishek.business.domain.RoomReservation;;

@Service
public class ReservationService {
	private GuestRepository guestRepository;
	private ReservationRepository reservationRepository;
	private RoomRepository roomRepository;
	
	@Autowired
	public ReservationService(GuestRepository guestRepository, ReservationRepository reservationRepository,
			RoomRepository roomRepository) {
		super();
		this.guestRepository = guestRepository;
		this.reservationRepository = reservationRepository;
		this.roomRepository = roomRepository;
	}
	
	public List<RoomReservation> getRoomReservationsForDate(Date date){
		Iterable<Room> rooms = this.roomRepository.findAll();
		Map<Long, RoomReservation> roomReservationMap = new HashMap<>();
		rooms.forEach(room->{
			RoomReservation roomReservation = new RoomReservation();
			roomReservation.setRoomId(room.getId());
			roomReservation.setRoomName(room.getName());
			roomReservation.setRoomNumber(room.getNumber());
			roomReservationMap.put(room.getId(), roomReservation);
		});
		Iterable<Reservation> reservations = this.reservationRepository.
				findByDate(new java.sql.Date(date.getTime()));
		if(reservations!=null){
			reservations.forEach(reservation ->{
				Optional<Guest> ar= this.guestRepository.findById(reservation.getGuestId());				
				if(ar!=null){
					Guest guest = ar.get();
					RoomReservation roomReservation = roomReservationMap
							.get(reservation.getId());
					roomReservation.setDate(date);
					roomReservation.setFirstName(guest.getFirstName());
					roomReservation.setLastName(guest.getLastName());
					roomReservation.setGuestId(guest.getId());
				}
			});
		}
		List<RoomReservation> roomReservations = new ArrayList<>();
		for(Long roomId:roomReservationMap.keySet()){
			roomReservations.add(roomReservationMap.get(roomId));
		}
		return roomReservations;
	}
}
