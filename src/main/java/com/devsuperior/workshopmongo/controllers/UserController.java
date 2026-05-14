package com.devsuperior.workshopmongo.controllers;

import com.devsuperior.workshopmongo.dto.UserDTO;
import com.devsuperior.workshopmongo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService service;

	@GetMapping
	public Flux<UserDTO> findAll() {
		return service.findAll();
	}

	@GetMapping(value = "/{id}")
	public Mono<ResponseEntity<UserDTO>> findById(@PathVariable String id) {
		return service.findById(id).map(userDTO ->  ResponseEntity.ok().body(userDTO));
	}

	@PostMapping
	public Mono<ResponseEntity<UserDTO>> insert(@RequestBody UserDTO userDTO, UriComponentsBuilder builder) {
		return service.insert(userDTO)
				.map(userDTO1 ->  ResponseEntity.created(builder.path("/users/{id}")
						.buildAndExpand(userDTO1.getId()).toUri()).body(userDTO1));
	}

	@PutMapping(value = "/{id}")
	public Mono<ResponseEntity<UserDTO>> update(@PathVariable String id, @RequestBody UserDTO userDTO) {
		return service.update(id, userDTO).map(userDTO1 ->  ResponseEntity.ok().body(userDTO1));
	}

	@DeleteMapping(value = "/{id}")
	public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
		return service.delete(id).then(Mono.just(ResponseEntity.noContent().<Void>build()));
	}

	/*

	@PostMapping
	public ResponseEntity<UserDTO> insert(@RequestBody UserDTO dto) {
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable String id, @RequestBody UserDTO dto) {
		dto = service.update(id, dto);
		return ResponseEntity.ok(dto);
	}
	
	@DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }*/
}
