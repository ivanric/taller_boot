package Service.controlador;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Service.manager.ManejadorBeneficiarios;
import Service.manager.ManejadorInstalacionKit;
import Service.manager.ManejadorServicios;
import Service.manager.ManejadorSolicitudes;
import Service.models.Documento;
import Service.models.Persona;
import Service.models.TransferenciaBeneficiario;

@RequestMapping("/RestTBeneficiarios/")
@RestController
public class RestTBeneficiarios {
	@Autowired
	ManejadorBeneficiarios manejadorBeneficiarios;
	@Autowired
	ManejadorInstalacionKit manejadorInstalacionKit;
	@Autowired
	ManejadorServicios manejadorServicios;
	@Autowired 
	ManejadorSolicitudes manejadorSolicitudes;
	
	@RequestMapping(value="listar")
	public ResponseEntity<List<TransferenciaBeneficiario>> listarTBneneficiarios(HttpServletRequest req,HttpServletResponse res){	
		List<TransferenciaBeneficiario> Tbeneficiarios=this.manejadorBeneficiarios.ListaTB(req);
		for (int i = 0; i < Tbeneficiarios.size(); i++) {
			Tbeneficiarios.get(i).setRegistroKit(this.manejadorInstalacionKit.getRegistroKitTB(Tbeneficiarios.get(i).getIdsolt()));
			Tbeneficiarios.get(i).getRegistroKit().setOrdenServicio(this.manejadorServicios.getOrdenServicioIK(Tbeneficiarios.get(i).getRegistroKit().getIdordserv()));
			Tbeneficiarios.get(i).getRegistroKit().getOrdenServicio().setSolicitud(this.manejadorSolicitudes.getSoltByOrdServ(Tbeneficiarios.get(i).getRegistroKit().getIdordserv()));			
		}
		System.out.println("listaTBen: "+Tbeneficiarios.toString());
		return new ResponseEntity<List<TransferenciaBeneficiario>>(Tbeneficiarios,HttpStatus.OK);
	}
	@RequestMapping(value="documentosBeneficiario")
	public ResponseEntity<List<Documento>> docuemtosBeneficiario(HttpServletRequest req,HttpServletResponse res){	
		List<Documento> listaDocumentos=this.manejadorBeneficiarios.listaDocumentosTB();
		return new ResponseEntity<List<Documento>>(listaDocumentos,HttpStatus.OK);
	}
}
 