package com.examenSBDO.roomservice.service.impl;

import com.examenSBDO.roomservice.dao.IRoomRepository;
import com.examenSBDO.roomservice.dto.Room;
import com.examenSBDO.roomservice.entities.RoomEntity;
import com.examenSBDO.roomservice.mapping.RoomMapper;
import com.examenSBDO.roomservice.service.IRoomService;
import com.examenSBDO.roomservice.exception.RequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;


import jakarta.persistence.EntityNotFoundException;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Locale;

@Service
@RequiredArgsConstructor
@Slf4j
@CacheConfig(cacheNames = "rooms")
public class RoomService implements IRoomService {

    private final  IRoomRepository iRoomRepository;
    private final RoomMapper roomMapper;
    private final MessageSource messageSource;



    @Override
    @Transactional(readOnly = true)
    public List<Room> getRooms() {

        return iRoomRepository.findAll().stream().map(roomMapper::toRoom).collect(Collectors.toList());
    }

    @Override
    @Cacheable(key = "#id")
    @Transactional(readOnly = true)
    public Room getRoom(Long id) {
        log.info("Recherche chambre id={}", id);

        Room room = roomMapper.toRoom(
                iRoomRepository.findById(id).orElseThrow(() -> {
                    log.error(" Chambre introuvable id={}", id);
                    return new EntityNotFoundException(
                            messageSource.getMessage("room.notfound", new Object[]{id},
                                    Locale.getDefault()));
                })
        );

        log.debug("✅ Chambre trouvée depuis Redis ou BDD : {}", room);
        return room;
    }

    @Override
    public Room createRoom(Room room) {
        Optional<RoomEntity> existing = iRoomRepository.findByRoomNumber(room.getRoomNumber());
        if (existing.isPresent()) {
            throw new RequestException(
                    messageSource.getMessage("produit.exists",
                            new Object[]{room.getRoomNumber()}, Locale.getDefault()),
                    HttpStatus.CONFLICT);
        }

        log.info("Création chambre : {}", room.getRoomNumber());
        return roomMapper.toRoom(iRoomRepository.save(roomMapper.fromRoom(room)));
    }

    @Override
    @CachePut(key = "#id")
    @Transactional
    public Room updateRoom(Long id, Room room) {
        return iRoomRepository.findById(id).
                map(entity -> {
                    room.setId(id);
                    entity.setRoomNumber(room.getRoomNumber());
                    entity.setType(room.getType());
                    entity.setAvailable(room.getAvailable());
                    entity.setPrice(room.getPrice());
                    return roomMapper.toRoom(iRoomRepository.save(entity));
                }).orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("room.notfound", new Object[] { id },
                                Locale.getDefault())));
    }

    @Override
    @CacheEvict(key = "#id")
    @Transactional
    public void deleteRoom(Long id) {
        RoomEntity entity= iRoomRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                messageSource.getMessage("produit.notfound", new Object[] { id },
                        Locale.getDefault())));
        try{
            iRoomRepository.delete(entity);
        }catch (Exception e) {
            throw new RequestException(messageSource.getMessage("room.errordeletion", new Object[] { id },
                    Locale.getDefault()),
                    HttpStatus.CONFLICT);
        }

    }

    @Override
    public Page<Room> findAll(Pageable pageable) {
        Page<RoomEntity>  roomPage = iRoomRepository.findAll(pageable);
        return roomPage.map(roomMapper::toRoom);
    }
}
