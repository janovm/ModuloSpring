package com.alejandro.app.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alejandro.app.entity.Cliente;
import com.alejandro.app.entity.Producto;
import com.alejandro.app.repository.ClienteDao;
import com.alejandro.app.repository.ProductoDao;

@Service
public class ProductoServiceImpl implements ProductoService{

	@Autowired
	private ProductoDao repositorioProducto;
	
	@Override
	@Transactional( readOnly = true)
	public List<Producto> mostrarTodo() {
		return (List<Producto>) repositorioProducto.findAll();
	}

	@Override
	@Transactional( readOnly = true)
	public Producto buscarPorId(long id) {
		return repositorioProducto.findById(id).orElse(null);
	}
	
	@Override
	@Transactional
	public Producto guardar(Producto producto) {
		return repositorioProducto.save(producto);
	}

	@Override
	@Transactional
	public void deleteProducto(long id) {
		// TODO Auto-generated method stub
		repositorioProducto.deleteById(id);
	}
	
}
