package com.alejandro.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alejandro.app.entity.Cliente;
import com.alejandro.app.entity.Producto;
import com.alejandro.app.service.ClienteService;
import com.alejandro.app.service.ClienteServiceImpl;
import com.alejandro.app.service.ProductoService;

@RestController
@RequestMapping("/api")
public class ControladorRestProducto {
	
	@Autowired
	private ProductoService servicio;
	
	@GetMapping("/productos")
	public List<Producto> getAllProductos() {
		return servicio.mostrarTodo();
	}
	
	@GetMapping("/productos/{id}")
	public ResponseEntity<?> buscarProducto(@PathVariable long id) {
		Producto producto= null;
		Map<String, Object> response= new HashMap<>();
		try {
			producto=servicio.buscarPorId(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar consulta a base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (producto==null) {
			response.put("mensaje", "El producto con ID: "+id+" no existe");
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Producto>(producto,HttpStatus.OK);
	}
	
	@PostMapping("/productos")
	public ResponseEntity<?> guardarProducto(@RequestBody Producto producto) {
		Producto nuevoProducto = null;
		Map<String,Object> response = new HashMap<>();
		try {
			nuevoProducto = servicio.guardar(producto);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar en base de datos");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje","El producto ha sido creado con éxito");
		response.put("cliente",nuevoProducto);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}

	@PutMapping("/productos/{id}")
	public ResponseEntity<?> actualizarCliente(@RequestBody Producto producto, @PathVariable long id) {
		
		Producto productoUpdate=null;
		Map<String,Object> response = new HashMap<>();
		
		productoUpdate = servicio.buscarPorId(id);
		
		if(productoUpdate == null) {
			response.put("mensaje","Error: no se pudo editar, el producto con ID: "+id+" no existe en la base de datos");
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
				
			productoUpdate.setNombre(producto.getNombre());
			productoUpdate.setCliente(producto.getCliente());
			productoUpdate.setCantidad(producto.getCantidad());
			productoUpdate.setFecha_venta(producto.getFecha_venta());
			productoUpdate.setTipo(producto.getTipo());
	
			servicio.guardar(productoUpdate);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar en base de datos");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/productos/{id}")
	public ResponseEntity<?> deleteProducto(@PathVariable long id) {
		Map<String,Object> response = new HashMap<>();
		Producto productoUpdate = servicio.buscarPorId(id);
		
		if(productoUpdate == null) {
			response.put("mensaje","Error: no se pudo eliminar, el producto con ID: "+id+" no existe en la base de datos");
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
			servicio.deleteProducto(id);
			response.put("mensaje", "Producto añadido");
		} catch (Exception e) {
			response.put("mensaje", "Error al eliminar");
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
		
	}
	
}
