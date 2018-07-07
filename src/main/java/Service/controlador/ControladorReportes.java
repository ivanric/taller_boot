package Service.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping({"/Reportes/"})
@Controller
public class ControladorReportes {

	@RequestMapping({"reporte"})
	public String reporte(){
		System.out.println("llego reporte ajax");
		return "beneficiarios/gestion";
	}
}
