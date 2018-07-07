package Service.controlador;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import Service.manager.ManejadorModulos;
import Service.manager.ManejadorOpciones;
import Service.manager.ManejadorProcesos;
import Service.manager.ManejadorRoles;
import Service.manager.ManejadorUsuarios;
import Service.models.Modulo;
import Service.models.Persona;
import Service.models.Proceso;
import Service.models.Rol;
import Service.models.Usuario;
import Service.models.respuesta;
//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/RestUsuario/")
@RestController
public class RestUsuario {
	@Autowired 
	ManejadorUsuarios manejadorUsuarios;
	@Autowired 
	ManejadorRoles manejadorRoles;
	@Autowired 
	ManejadorModulos manejadorModulos;
	@Autowired 
	ManejadorProcesos manejadorProcesos;
	@Autowired
	ManejadorOpciones manejadorOpciones;
	
	@Transactional
	@ResponseBody 
	@RequestMapping({"/validar"})	
	public  ResponseEntity<Map<String, Object>> validar(HttpServletRequest request,HttpSession session,Principal principal)  throws IOException  {			
		String xlogin=request.getParameter("login");
		String xpassword=request.getParameter("password");
		Map<String, Object> Data=new HashMap<>();
//		respuesta resp=new respuesta();
		try {
			Persona xusuario=this.manejadorUsuarios.iniciarSession(xlogin,xpassword);
			
			
			System.out.println(xusuario);
			List<Rol> ListaRoles=this.manejadorRoles.ControlRoles(xusuario.getIdper());
			System.out.println("RolesFuera: "+ListaRoles.toString());
			if(xusuario!=null){
				if(xusuario.getEstado()!=1){
//					resp.setMsg("Usuario no esta activo");
//					resp.setStatus(false);
				}
				else if(ListaRoles.size()==0){
//					resp.setMsg("Este usuario no tiene Roles");
//					resp.setStatus(false);
				}
				else{
					System.out.println("sessionooo");
//					HttpSession sesion=request.getSession(true);
//					HttpSession sesion=request.getSession(true);
//					session.setAttribute("user","datito");
					session.setAttribute("xusuario",xusuario);
//					resp.setMsg("Usuario registrado con Exito");
//					resp.setStatus(true);
//					Data.put("session",session.getAttribute("user"));
					Data.put("msg","Usuario registrado con Exito");
					Data.put("status",true);
				}
			}else{	
//				resp.setMsg("Ocurrio un Error con el Servidor");
//				resp.setStatus(false);
				
				Data.put("msg","error serv");
				Data.put("status",false);
			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			System.out.println(e.getMessage());
//			resp.setMsg("Usuario incorrecto, por favor verifique login y clave.");
//			resp.setStatus(false);
			Data.put("msg","Usuario incorrecto, por favor verifique login y clave.");
			Data.put("status",false);

		}
//		System.out.println("En Session Abierta: "+request.getSession().getAttribute("xusuario"));
//		System.out.println("En Session Abierta: "+session.getAttribute("xusuario"));
//		System.out.println("En Session Abierta: "+session.getAttribute("user"));
//		System.out.println("objeto:"+resp.toString());

		return new ResponseEntity<Map<String,Object>>(Data,HttpStatus.OK);
	} 
	
	
    @RequestMapping(value={"/login"})
    public String login(){
        return "login";
    }
   
	
	@RequestMapping(value = "/demo", produces = "application/json")
	public Persona helloUser(Principal principal,HttpServletRequest request,Usuario u) {
		System.out.println("UserRecuperado: "+ u.toString());
		HttpSession session=request.getSession(true);
		Persona xusuario=this.manejadorUsuarios.iniciarSession(u.getLogin(),u.getPassword());

//		List<?> l=new ArrayList<>();
		session.setAttribute("username",xusuario);
		Persona p=(Persona) session.getAttribute("username");


		return p;
	}
	
//	@RequestMapping(value="/salir",produces = "application/json",consumes="application/json")
	@RequestMapping(value="/salir",produces = "application/json")
//	@ResponseStatus(HttpStatus.NO_CONTENT)
//	public String salir(HttpSession session,Principal p) {
	public Map<String, Object> salir(HttpSession session) {
		Persona person=(Persona) session.getAttribute("username");
		System.out.println("RECUPERANDOOOOO: "+person.toString());
//		session.invalidate();
		String Salio="nada sin session";
		if(person!=null) {
			Salio="si salio:";
		}
		Map<String, Object> m=new HashMap<>();
		m.put("cod:", Salio);
		return m;
		
	}
	@RequestMapping(value="/Jwt")
	public Map<String, Object> d(HttpServletRequest req){
		HttpSession session=req.getSession();
//		System.out.println("YAUSER: "+session.getAttribute("username"));
//		System.out.println("YAUSER1: "+session.getAttribute("username"));
		Map<String, Object> m=new HashMap<>();
		m.put("1", "Course1");
		m.put("2", "Course2");
		return m;
	}

//	@RequestMapping(value="session")
//	public Map<String, Object> sessiono(HttpSession session){
////	public String session(@SessionAttribute("xusuario") Persona per){
////		HttpSession sesion=request.getSession(true);
//		
////		Persona xuser=(Persona) sesion.getAttribute("xusuario");
//		System.out.println("Autorizado User: "+session.getAttribute("user"));
////		System.out.println("Autorizado User: "+req.getSession(false).getAttribute("xusuario"));
////		System.out.println("Autorizado User: "+per.toString());
//		Map<String, Object> resp=new HashMap<>(); 
////		try {
////			if(xuser==null) {
////				resp.put("autorize",false);
////			}else {
////				resp.put("autorize", true);
////			}
////		} catch (Exception e) {
////			// TODO: handle exception
////			resp.put("autorize",false);
////		}
//		return resp;
////		return "Accesso Publico";
//	}
//	
	@ResponseBody 
	@RequestMapping(value="MenuRol")
//	public ResponseEntity<List<Modulo>>  ModalAddR(Model model,HttpServletRequest reques,@RequestParam String idrol){
	public ResponseEntity<List<Modulo>>  ModalAddR(Model model,HttpServletRequest reques,@RequestParam String idrol){
		Persona persona=(Persona)reques.getSession().getAttribute("xusuario");	
		System.out.println("idrol: "+idrol);
		int codrol=Integer.parseInt(idrol);
		List<Modulo> xmenus = this.manejadorModulos.LisRolmenus(codrol);
		for (int i = 0; i < xmenus.size(); i++) {
			Modulo mx = xmenus.get(i);
			mx.setProcesos( this.manejadorProcesos.getProcesosByMenu( mx.getIdmod(),codrol));
//			System.out.println("modulos: "+mx);
			for (int j = 0; j < mx.getProcesos().size(); j++) {
				Proceso p=mx.getProcesos().get(j);
				p.setOpciones(this.manejadorOpciones.getOpcionesByRolMenuProc(codrol,mx.getIdmod(),p.getIdproc()));
			}
		}
		List<Modulo> m=new ArrayList<Modulo>();
		m.add(new Modulo(1, "m1", "", 1, null));
		respuesta e= new respuesta();
		e.setMsg("men1");
		e.setStatus(true);
		return new ResponseEntity<List<Modulo>>(xmenus,HttpStatus.OK);
	}
	
	@ResponseBody 
	@RequestMapping(value="obtenerRoles")
	public ResponseEntity<List<Rol>> obtenerRol(HttpServletRequest request,HttpServletResponse response){
		System.out.println("llegue ObtenerRoles");
		HttpSession sesion=request.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		List<Rol> Roles=null;
		try {
			if (xuser==null) {
				System.out.println(" usuarui nullo");
//				"redirect: principal/index.html";
				response.sendRedirect("principal/index.html");
				return new ResponseEntity<List<Rol>>(HttpStatus.NOT_FOUND);
			} else {
				System.out.println("usuario ok");
				Roles=this.manejadorRoles.ListarRolUsuario(xuser.getIdper());
//				Rol r1=Roles.get(0);
//				for (Rol i : Roles) {
					
//				}
				
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		System.out.println("newRol: "+Roles);
		return new ResponseEntity<List<Rol>>(Roles,HttpStatus.OK);
	}
}
