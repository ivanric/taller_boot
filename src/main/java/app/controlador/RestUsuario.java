package app.controlador;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import app.manager.ManejadorModulos;
import app.manager.ManejadorOpciones;
import app.manager.ManejadorProcesos;
import app.manager.ManejadorRoles;
import app.manager.ManejadorUsuarios;
import app.models.Modulo;
import app.models.Persona;
import app.models.Proceso;
import app.models.Rol;
import app.models.Usuario;
import app.models.respuesta;
import app.service.IUsuarioService;
import app.utilidades.CargarArchivos;

//@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/RestUsuario/")
@RestController
public class RestUsuario {
	@Autowired
	ServletContext context;
	
	@Autowired
	private CargarArchivos appArchivos;
	
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
	
	@RequestMapping(value="listar")
	public ResponseEntity<List<Persona>> listarBneneficiarios(HttpServletRequest req,HttpServletResponse res){	
		List<Persona> lista=this.manejadorUsuarios.Lista(req);
		System.out.println("lista: "+lista.toString());
		return new ResponseEntity<List<Persona>>(lista,HttpStatus.OK);
	}
	
	@RequestMapping(value="datosModal")
	public ResponseEntity<List<Object>> dataOrdenServicio(){
		List<Object> lista=new ArrayList<Object>();
		Map<String, Object> mapa=new HashMap<>();
		Date date = new Date();
		String fecha= new SimpleDateFormat("yyyy-MM-dd").format(date);
		mapa.put("contraseniaDefault", this.manejadorUsuarios.getDefaultPassword());
		mapa.put("fecha",fecha);

		lista.add(mapa);
		System.out.println("Lista: "+lista.toString());
		return new ResponseEntity<List<Object>>(lista,HttpStatus.OK);
	}
	
	@Transactional
	@RequestMapping(value="adicionar")
	public Map<String, Object> adicionar(HttpServletRequest req,HttpServletResponse res,MultipartFile foto){
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		String nombreFoto;
		String[] telefonos=req.getParameterValues("telefono[]");
		Map<String, Object> respuesta=new HashMap<String, Object>();
		System.out.println("foto_status"+foto==null);
		if(xuser!=null){
			try {
				if(foto!=null && foto.getSize()>0){
					nombreFoto="codper-"+this.manejadorUsuarios.generarIdPer()+foto.getOriginalFilename().substring(foto.getOriginalFilename().lastIndexOf('.'));
					appArchivos.saveImagen(foto,nombreFoto);
					System.out.println("DIR: "+context.getRealPath(""));

//					File archivo=new File(req.getSession().getServletContext().getRealPath("/fotos")+"/"+nombreFoto);
//					foto.transferTo(archivo);
				}else {
					nombreFoto="user.png";
				}
			
				
				if(this.manejadorUsuarios.registrar(req,telefonos,nombreFoto)){
					respuesta.put("estado",true);
				}else{
					respuesta.put("estado",false);
				}
			} catch (Exception e) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				System.out.println("error al adicionarrr"+e.toString());
				respuesta.put("estado",false);
			}
		}
		return respuesta;
	}
	
	@Transactional
	@ResponseBody 
	@RequestMapping({"/validar"})	
	public  ResponseEntity<Map<String, Object>> validar(HttpServletRequest request,HttpSession session,Principal principal)  throws IOException  {			
		String xlogin=request.getParameter("login");
		String xpassword=request.getParameter("password");
		Map<String, Object> Data=new HashMap<>();
		try {
			Persona xusuario=this.manejadorUsuarios.iniciarSession(xlogin,xpassword);
			System.out.println(xusuario);
			List<Rol> ListaRoles=this.manejadorRoles.ControlRoles(xusuario.getIdper());
			System.out.println("RolesFuera: "+ListaRoles.toString());
			if(xusuario!=null){
				if(xusuario.getUsuario().getEstado()!=1){
					Data.put("msg","Usuario no esta activo");
					Data.put("status",false);
				}else {
					if(ListaRoles.size()==0){
						Data.put("msg","Este usuario no tiene Roles");
						Data.put("status",false);
					}
					else{
						System.out.println("sessionooo");
						session.setAttribute("xusuario",xusuario);
						Data.put("msg","Usuario registrado con Exito");
						Data.put("status",true);
					}
				}

			}
//			else{
//				Data.put("msg","error serv");
//				Data.put("status",false);
//			}
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			System.out.println(e.getMessage());
			Data.put("msg","Usuario incorrecto, por favor verifique login y clave.");
			Data.put("status",false);

		}


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
//		HttpSession session=req.getSession();
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
//		Persona persona=(Persona)reques.getSession().getAttribute("xusuario");	
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
