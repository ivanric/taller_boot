package app;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.manager.ManejadorRoles;
import app.manager.ManejadorUsuarios;
import app.models.Persona;
import app.models.Rol;
import app.models.respuesta;

@RequestMapping(value="/RestLogin/")
@RestController
public class RestLogin {
	@Autowired 
	ManejadorUsuarios manejadorUsuarios;
	@Autowired 
	ManejadorRoles manejadorRoles;
	
	
	@RequestMapping(value="/log")
	public Map<String, Object> log() {
		Map<String, Object> d= new HashMap<>();
		d.put("Helo", "aqui");
		return d;
	}
	@Transactional
	@RequestMapping({"/Aut"})	
	public ResponseEntity<respuesta> validar(HttpServletRequest request,HttpSession session, Model model)  throws IOException  {			
		System.out.println("entro AuT");
		String xlogin=request.getParameter("xlogin");
		String xpassword=request.getParameter("xpassword");
		respuesta resp=new respuesta();
		try {
			Persona xusuario=this.manejadorUsuarios.iniciarSession(xlogin,xpassword);
			System.out.println(xusuario);
			List<Rol> ListaRoles=this.manejadorRoles.ControlRoles(xusuario.getIdper());
			System.out.println("RolesFuera: "+ListaRoles.toString());
			if(xusuario!=null){
				if(xusuario.getEstado()!=1){
					resp.setMsg("Usuario no esta activo");
					resp.setStatus(false);
				}
				else if(ListaRoles.size()==0){
					resp.setMsg("Este usuario no tiene Roles");
					resp.setStatus(false);
				}
				else{
					System.out.println("sessionooo");
					session.setAttribute("xusuario",xusuario);
					resp.setMsg("Usuario registrado con Exito");
					resp.setStatus(true);
				}
			}else{	
				resp.setMsg("Ocurrio un Error con el Servidor");
				resp.setStatus(false);
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			System.out.println(e.getMessage());
			resp.setMsg("Usuario incorrecto, por favor verifique login y clave.");
			resp.setStatus(false);

		}
		System.out.println("En Session Abierta: "+request.getSession().getAttribute("xusuario"));
		System.out.println("objeto:"+resp.toString());

		return new ResponseEntity<respuesta>(resp,HttpStatus.OK);
	} 
	
}
