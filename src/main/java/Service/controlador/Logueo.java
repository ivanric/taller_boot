package Service.controlador;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import Service.manager.ManejadorModulos;
import Service.manager.ManejadorOpciones;
import Service.manager.ManejadorProcesos;
import Service.manager.ManejadorRoles;
import Service.manager.ManejadorUsuarios;
import Service.models.Modulo;
import Service.models.Persona;
import Service.models.Proceso;
import Service.models.Rol;
/**
 * Handles requests for the application home page.
 */
//@RestController
@RequestMapping("/principal/")
@Controller
public class Logueo {
	
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
	

	@RequestMapping(value = "/index")
	public String home(Model model) {
		System.out.println("INICIA");
//		model.addAttribute("Hello", "Hola Mundo Velocity");
		return "index";
	}
	
	@RequestMapping(value = "/index2")
	public String home2(Model model) {
//		model.addAttribute("Hello", "Hola Mundo Velocity");
		return "demo";
	}
	
	@RequestMapping(value = "/inicio")
	public String inicio(HttpServletRequest request, Model model) {
		
		HttpSession sesion=request.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		System.out.println("Llego Sesion Logueada: "+xuser);
		System.out.println("Llega usuario "+xuser);
		try {
			if (xuser==null){
				return "redirect:index";
			}else{
				List<Rol> Roles=this.manejadorRoles.ListarRolUsuario(xuser.getIdper());
//				for (Object i : Roles) {
//					System.out.println("Roles"+i);
//				}
				Rol r1=Roles.get(0);
//				System.out.println("idrol_1: "+r1.getIdrol());
;
				List<Modulo> xmenus = this.manejadorModulos.menusRol(r1.getIdrol());
//				List<?> xprocesos=this.procesoManager.ListarProcesos();
				for (Modulo modulo : xmenus) {
					System.out.println("Modulos:"+modulo.toString());
				}
				for (int i = 0; i < xmenus.size(); i++) {
					Modulo mx = xmenus.get(i);
//					List <Proceso> lp=this.manejadorProcesos.getProcesosByMenu( mx.getIdmod());
//					System.out.println("lp: "+lp.toString());
					mx.setProcesos( this.manejadorProcesos.getProcesosByMenu( mx.getIdmod(),r1.getIdrol()));
					for (int j = 0; j < mx.getProcesos().size(); j++) {
						Proceso p=mx.getProcesos().get(j);
						p.setOpciones(this.manejadorOpciones.getOpcionesByRolMenuProc(r1.getIdrol(),mx.getIdmod(),p.getIdproc()));
					}
				}

				for (Modulo modulo : xmenus) {
					System.out.println("Procesos_date:"+modulo.getProcesos().toString());
				}
//				System.out.println("Lista de Menus: "+xmenus.toString());
//				System.out.println("lista de Procesos: "+xprocesos);
//				List<?> xsubmenus = this.menuManager.xsubmenus();/*se hace una consulta y se captura todo un objeto de submenus*/
				
//				model.addAttribute("xsubmenus", xsubmenus);
				model.addAttribute("listRoles",Roles);
				model.addAttribute("xmenus", xmenus);
//				model.addAttribute("rolPrincipal", r1);
//				model.addAttribute("xprocesos",xprocesos);
				model.addAttribute("msg","Bienvenido "+xuser.getNombres()+" "+xuser.getAp()+" "+xuser.getAm());
				model.addAttribute("usuario",xuser);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "principal/principal";
	
	}
	@RequestMapping({"/alerta"})
	public String alerta(Model m,HttpServletRequest req,HttpServletResponse res){
		m.addAttribute("mensaje","Usted desea salir del Sistema..?");
		return "principal/desconectar";
	}
	@RequestMapping({"/desconectar"})
	public String desc(Model m,HttpServletRequest req,HttpServletResponse res){
		HttpSession sesion=req.getSession(true);
		sesion.removeAttribute("xusuario");
		return "redirect:index";
	}
	
	
}
