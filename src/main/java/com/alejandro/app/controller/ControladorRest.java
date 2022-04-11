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
import com.alejandro.app.service.ClienteService;
import com.alejandro.app.service.ClienteServiceImpl;

@RestController
@RequestMapping("/api")
public class ControladorRest {
	
	@Autowired
	private ClienteService servicio;
	
	@GetMapping("/clientes")
	public List<Cliente> getAllClientes() {
		return servicio.mostrarTodo();
	}
	
	@GetMapping("/clientes/{id}")
	public ResponseEntity<?> buscarCliente(@PathVariable long id) {
		Cliente cliente= null;
		Map<String, Object> response= new HashMap<>();
		try {
			cliente=servicio.buscarPorId(id);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al realizar consulta a base de datos");
			response.put("error", e.getMessage().concat(":").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if (cliente==null) {
			response.put("mensaje", "El cliente con ID: "+id+" no existe");
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(cliente,HttpStatus.OK);
	}
	
	@PostMapping("/clientes")
	public ResponseEntity<?> guardarCliente(@RequestBody Cliente cliente) {
		Cliente nuevoCliente = null;
		Map<String,Object> response = new HashMap<>();
		try {
			nuevoCliente = servicio.guardar(cliente);
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al insertar en base de datos");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje","El cliente ha sido creado con éxito");
		response.put("cliente",nuevoCliente);
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}

	@PutMapping("/clientes/{id}")
	public ResponseEntity<?> actualizarCliente(@RequestBody Cliente cliente, @PathVariable long id) {
		
		Cliente clienteUpdate=null;
		Map<String,Object> response = new HashMap<>();
		
		clienteUpdate = servicio.buscarPorId(id);
		
		if(clienteUpdate == null) {
			response.put("mensaje","Error: no se pudo editar, el cliente con ID: "+id+" no existe en la base de datos");
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		try {
				
			clienteUpdate.setNombre(cliente.getNombre());
			clienteUpdate.setApellido(cliente.getApellido());
			clienteUpdate.setEmail(cliente.getEmail());
			clienteUpdate.setTelefono(cliente.getTelefono());
			clienteUpdate.setCreadAt(cliente.getCreadAt());
			servicio.guardar(clienteUpdate);
			
		} catch (DataAccessException e) {
			response.put("mensaje", "Error al actualizar en base de datos");
			response.put("error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
		}
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/clientes/{id}")
	public void deleteCliente(@PathVariable long id) {
		servicio.deleteCliente(id);
	}
	
}
