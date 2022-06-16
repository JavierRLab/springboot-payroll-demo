package com.example.demo.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entities.Order;
import com.example.demo.entities.OrderModelAssembler;
import com.example.demo.exceptions.OrderNotFoundException;
import com.example.demo.repositories.OrderRepository;

@RestController
public class OrderController {

	private final OrderRepository repository;

	private final OrderModelAssembler assembler;

	public OrderController(OrderRepository repository, OrderModelAssembler assembler) {

		this.repository = repository;
		this.assembler = assembler;
	}

	@GetMapping("/orders")
	public CollectionModel<EntityModel<Order>> all() {

		List<EntityModel<Order>> orders = repository.findAll().stream()
				.map(assembler::toModel)
				.collect(Collectors.toList());

		return CollectionModel.of(orders,
				linkTo(methodOn(OrderController.class).all()).withSelfRel());
	}

	@PostMapping("/orders")
	ResponseEntity<?> newOrder(@RequestBody Order newOrder) {

		EntityModel<Order> entityModel = assembler.toModel(repository.save(newOrder));

		return ResponseEntity
				.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
				.body(entityModel);
	}

	@GetMapping("/orders/{id}")
	public EntityModel<Order> one(@PathVariable Long id) {

		Order Order = repository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException(id));

		return assembler.toModel(Order);
	}

	// @PutMapping("/orders/{id}")
	// Order replaceOrder(@RequestBody Order newOrder, @PathVariable Long id) {
	//
	// return repository.findById(id)
	// .map(Order -> {
	// Order.setName(newOrder.getName());
	// Order.setRole(newOrder.getRole());
	// return repository.save(Order);
	// })
	// .orElseGet(() -> {
	// newOrder.setId(id);
	// return repository.save(newOrder);
	// });
	// }

	// @DeleteMapping("/orders/{id}")
	// ResponseEntity<?> deleteOrder(@PathVariable Long id) {
	// repository.deleteById(id);
	// return ResponseEntity.noContent().build();
	// }
}
