package com.examenSBDO.roomservice.controller;

import com.examenSBDO.roomservice.dto.Room;
import com.examenSBDO.roomservice.service.impl.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<List<Room>> getRooms(){return new ResponseEntity<>(roomService.getRooms(), HttpStatus.OK);}

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable Long id){return new ResponseEntity<>(roomService.getRoom(id),HttpStatus.OK);}
    @PostMapping
    public ResponseEntity<Room> createRoom(@Valid @RequestBody  Room room){return  new ResponseEntity<>(roomService.createRoom(room),HttpStatus.CREATED);}
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @Valid @RequestBody Room room){return new ResponseEntity<>(roomService.updateRoom(id,room),HttpStatus.OK);}
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id){
        roomService.deleteRoom(id);
        return new ResponseEntity<>("La chambre avec l'id : " + id + "  est supprimée avec succès",
                HttpStatus.OK);
    }

    @GetMapping("/paginated")
    public Page<Room> getProduits(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size); // Création de Pageable avec page et size
        return roomService.findAll(pageable);
    }

}
