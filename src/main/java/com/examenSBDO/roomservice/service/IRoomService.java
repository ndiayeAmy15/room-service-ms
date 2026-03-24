package com.examenSBDO.roomservice.service;

import com.examenSBDO.roomservice.dto.Room;
import com.examenSBDO.roomservice.entities.RoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRoomService {
    List<Room> getRooms();
    Room getRoom(Long id);
    Room createRoom(Room room);
    Room updateRoom(Long id,Room room);
    void deleteRoom(Long id);
    Page<Room> findAll(Pageable pageable);

}
