package com.alejandro.app.service;

import java.util.List;

import com.alejandro.app.entity.Cliente;
import com.alejandro.app.entity.Producto;

public interface ProductoService {
	public List<Producto> mostrarTodo();
	public Producto buscarPorId(long id);
	public Producto guardar(Producto producto);
	public void deleteProducto(long id);
}