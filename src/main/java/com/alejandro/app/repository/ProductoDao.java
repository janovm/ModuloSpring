package com.alejandro.app.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.alejandro.app.entity.Producto;

@Repository
public interface ProductoDao extends CrudRepository<Producto, Long>{
	
}