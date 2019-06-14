package com.hasckel.techassist.controller;

import com.hasckel.techassist.exception.ResourceNotFoundException;
import com.hasckel.techassist.model.Client;
import com.hasckel.techassist.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@Secured({"ROLE_ADMIN", "ROLE_RECEPCIONIST"})
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    @GetMapping
    public Page<Client> all(@RequestParam(value = "page", defaultValue = "0") int page,
                            @RequestParam(value = "size", defaultValue = "15") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "name");

        return clientRepository.findAll(pageable);
    }

    @PostMapping
    public ResponseEntity<Client> save(@RequestBody Client client) {
        Client saved = clientRepository.save(client);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{id}")
                .buildAndExpand(saved.getId()).toUri();

        return ResponseEntity
                .created(location)
                .body(saved);
    }

    @PutMapping
    public ResponseEntity<Client> edit(@RequestBody Client client) {
        Client saved = clientRepository.save(client);

        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        Client client = clientRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));

        clientRepository.delete(client);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> get(@PathVariable Long id) {
        Client client = clientRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", "id", id));

        return ResponseEntity.ok(client);
    }

}
