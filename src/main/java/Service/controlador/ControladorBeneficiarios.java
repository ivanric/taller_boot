package Service.controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import Service.manager.ManejadorBeneficiarios;
import Service.models.Documento;
import Service.models.Persona;

import utilidades.GeneradorReportes;

@RequestMapping({"/Beneficiarios/"})
@Controller
public class ControladorBeneficiarios {
	@Autowired
	ManejadorBeneficiarios manejadorBeneficiarios;
	
	@RequestMapping({"Gestion"})
	public String gestion(HttpServletRequest request,Model m){
		HttpSession sesion=request.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		try {
			if (xuser==null) {
				m.addAttribute("mensaje","Usuario no Autorizado..");
				return "principal/cerrarSession";
			} else {

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			m.addAttribute("mensaje","Usuario no Autorizado..");
			return "principal/cerrarSession";
		}
		return "beneficiarios/gestion";
	}
	@RequestMapping({"modal-add"})
	public String modal_add(HttpServletRequest request,Model m){
		List<Documento> listaDocumentos;
		HttpSession sesion=request.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		try {
			if (xuser==null) {
				m.addAttribute("mensaje","Usuario no Autorizado..");
				return "principal/cerrarSession";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			m.addAttribute("mensaje","Usuario no Autorizado..");
			return "principal/cerrarSession";
		}
		return "beneficiarios/modal-adicionar";
	}
	@RequestMapping({"modal-mod"})
	public String modal_mod(HttpServletRequest request,Model m){
		List<Documento> listaDocumentos;
		HttpSession sesion=request.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		try {
			if (xuser==null) {
				m.addAttribute("mensaje","Usuario no Autorizado..");
				return "principal/cerrarSession";
			} else {
				listaDocumentos=this.manejadorBeneficiarios.getDocumentos(Integer.parseInt(request.getParameter("idben")));
				m.addAttribute("listaDoc",listaDocumentos);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			m.addAttribute("mensaje","Usuario no Autorizado..");
			return "principal/cerrarSession";
		}
		return "beneficiarios/modal-modificar";
	}
	
	@Autowired
	DataSource dataSource;
	@RequestMapping("listaReporte")
	public  void ListaR(HttpServletResponse res,HttpServletRequest req){
		String nombreReporte="Lista de Beeneficiarios",tipo="pdf", estado="inline";
		Persona us=(Persona)req.getSession(true).getAttribute("xusuario");
		Map<String, Object> parametros=new HashMap<String, Object>();
		String url="/Service/reportes/reporte1.jasper";	;
		
//		parametros.put("codt", codt);
//		parametros.put("usuario", us.getNombre()+" "+us.getAp()+" "+us.getAm());
			
		GeneradorReportes g=new GeneradorReportes();
		try{
			g.generarReporte(res, getClass().getResource(url), tipo, parametros, dataSource.getConnection(), nombreReporte, estado);	
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	

	
}
