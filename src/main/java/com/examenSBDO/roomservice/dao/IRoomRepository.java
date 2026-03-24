package com.examenSBDO.roomservice.dao;

import com.examenSBDO.roomservice.entities.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoomRepository extends JpaRepository<RoomEntity,Long> {
    Optional<RoomEntity> findByRoomNumber(String roomNumber);
    //List<RoomEntity> findByAvailableTrue();
}
