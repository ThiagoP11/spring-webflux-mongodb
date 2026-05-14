package com.devsuperior.workshopmongo.services;

import com.devsuperior.workshopmongo.dto.UserDTO;
import com.devsuperior.workshopmongo.entities.User;
import com.devsuperior.workshopmongo.repositories.UserRepository;
import com.devsuperior.workshopmongo.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;


	public Flux<UserDTO> findAll() {
		return repository.findAll().map(UserDTO::new);
	}

	public Mono<UserDTO> findById(String id) {
		return repository.findById(id).map(UserDTO::new)
				.switchIfEmpty(Mono.error(new ResourceNotFoundException("Recurso não encontrado: " + id)));
	}

	public Mono<UserDTO> insert(UserDTO userDTO) {
		User user = new User();
		copyDtoToEntity(userDTO, user);
		return repository.save(user).map(UserDTO::new);
	}

	public Mono<UserDTO> update(String id, UserDTO userDTO) {
		return repository.findById(id)
				.flatMap(existingUser -> {
					existingUser.setName(userDTO.getName());
					existingUser.setEmail(userDTO.getEmail());
					return repository.save(existingUser);
				}).map(UserDTO::new).switchIfEmpty(Mono.error(new ResourceNotFoundException("Recurso não encontrado: " + id)));
	}

	private void copyDtoToEntity(UserDTO userDTO, User user) {
		user.setName(userDTO.getName());
		user.setEmail(userDTO.getEmail());
	}
}
