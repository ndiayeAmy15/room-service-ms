package com.examenSBDO.roomservice.mapping;

import com.examenSBDO.roomservice.dto.Room;
import com.examenSBDO.roomservice.entities.RoomEntity;
import org.mapstruct.Mapper;

@Mapper
public interface RoomMapper {
    Room toRoom(RoomEntity roomEntity);
    RoomEntity fromRoom(Room room);
}
