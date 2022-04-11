package com.alejandro.app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.alejandro.app.entity.Cliente;

@Repository
public interface ClienteDao extends CrudRepository<Cliente, Long>{
	
}