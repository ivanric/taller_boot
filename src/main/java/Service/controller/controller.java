package Service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Service.manager.ManejadorBeneficiarios;
import Service.models.Beneficiario;
import Service.models.Persona;

@RestController

public class controller {
	@Autowired
	ManejadorBeneficiarios manejadorBeneficiarios;

	
	@RequestMapping("/init")
	public ResponseEntity<Map<String, Object>> inicio() {
		Map<String, Object> m=new HashMap<>();
		m.put("Dato","data");
		m.put("Nombre","New");
		m.put("Unive","Catedra");
		m.put("Facultad","Informatica");
		m.put("Yiente","Escuela");
		m.put("Zapatos","Orcle");
		return new ResponseEntity<Map<String,Object>>(m,HttpStatus.OK);
	}
//	@RequestMapping(value="listar")
//	public ResponseEntity<Beneficiario> listar(HttpServletRequest req,HttpServletResponse res){	
//		System.out.println("Entro");
//		Beneficiario data=new Beneficiario();
//		data=manejadorBeneficiarios.obtenerBeneficiario(1);
//		return new ResponseEntity<Beneficiario>(data,HttpStatus.OK);
//	}
	@RequestMapping("hola")
	private ResponseEntity<List<?>> publ() {
		// TODO Auto-generated method stub
		List<?> dato=manejadorBeneficiarios.data();
		return new ResponseEntity<List<?>>(dato,HttpStatus.OK);
	}
}
