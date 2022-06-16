package com.example.demo.entities;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.example.demo.controllers.OrderController;

@Component
public class EmployeeModelAssembler implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {

	@Override
	public EntityModel<Employee> toModel(Employee employee) {

		return EntityModel.of(employee,
				linkTo(methodOn(OrderController.class).one(employee.getId())).withSelfRel(),
				linkTo(methodOn(OrderController.class).all()).withRel("employees"));
	}

}
