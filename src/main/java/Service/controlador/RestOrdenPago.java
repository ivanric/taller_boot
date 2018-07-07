package Service.controlador;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Service.manager.ManejadorActaRecepcion;import Service.manager.ManejadorInstalacionKit;
import Service.manager.ManejadorServicios;
import Service.manager.ManejadorSolicitudes;
import Service.models.ActaRecepcion;
import Service.models.Combustible;
import Service.models.OrdenPago;
import Service.models.OrdenServicio;
import Service.models.Persona;
import Service.models.Servicio;
import Service.models.Solicitud;
import Service.models.Telefono;
import utilidades.GeneradorReportes;


@RequestMapping("/RestOrdenPago/")
@RestController
public class RestOrdenPago {
	@Autowired
	ManejadorInstalacionKit manejadorInstalacionKit;
	@Autowired
	ManejadorActaRecepcion manejadorActaRecepcion;
	@Autowired
	ManejadorServicios manejadorServicios;
	@Autowired 
	ManejadorSolicitudes manejadorSolicitudes;
	
	@RequestMapping(value="listar")
	public ResponseEntity<List<OrdenPago>> listarOP(HttpServletRequest req,HttpServletResponse res){	
		List<OrdenPago> ListOrdenPago=this.manejadorActaRecepcion.ListaOP(req);
		System.out.println("listaPRE: "+ListOrdenPago.toString());
		for (int i = 0; i < ListOrdenPago.size(); i++) {
			ListOrdenPago.get(i).getActaRecepcion().setOrdenServicio(this.manejadorServicios.getOrdenServicioAR(ListOrdenPago.get(i).getActaRecepcion().getIdordserv()));
			ListOrdenPago.get(i).getActaRecepcion().getOrdenServicio().setSolicitud(this.manejadorSolicitudes.getSoltByOrdServ(ListOrdenPago.get(i).getActaRecepcion().getIdordserv()));			
		}
		System.out.println("listaPOST: "+ListOrdenPago.toString());
		return new ResponseEntity<List<OrdenPago>>(ListOrdenPago,HttpStatus.OK);
	}
	@RequestMapping(value="datosModal")
	public ResponseEntity<List<Object>> dataOrdenServicio(){
		List<Object> lista=new ArrayList<Object>();
		Map<String, Object> mapa=new HashMap<>();
		Date date = new Date();
		String fecha= new SimpleDateFormat("yyyy-MM-dd").format(date);
		mapa.put("numOrdPago", this.manejadorActaRecepcion.numeroOrdPago());
		mapa.put("fechaOrdPago",fecha);

		lista.add(mapa);
		System.out.println("Lista: "+lista.toString());
		return new ResponseEntity<List<Object>>(lista,HttpStatus.OK);
	}
	@RequestMapping(value="FiltroActaRecepcion")
	public ResponseEntity<List<ActaRecepcion>> FiltroSolicitud(HttpServletRequest req,HttpServletResponse res){
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		String texto=req.getParameter("texto");
		System.out.println("texto: "+texto);
		 
		List<ActaRecepcion> ListaAR=null;
		try {
			System.out.println("entro try");
			ListaAR=this.manejadorActaRecepcion.FiltroActaRecepcionOP(req.getParameter("texto"));
			System.out.println("Lista: "+ListaAR.toString());
			for (int i = 0; i < ListaAR.size(); i++) {
				ListaAR.get(i).setRegistroKit(this.manejadorInstalacionKit.getRegistroKitOP(ListaAR.get(i).getIdordserv()));
				ListaAR.get(i).getRegistroKit().setCilindros(this.manejadorInstalacionKit.ListaCilindro(ListaAR.get(i).getRegistroKit().getIdregistroKit()));
				ListaAR.get(i).setOrdenServicio(this.manejadorServicios.getOrdenServicioAR(ListaAR.get(i).getIdordserv()));
				ListaAR.get(i).getOrdenServicio().setSolicitud(this.manejadorSolicitudes.getSoltByOrdServ(ListaAR.get(i).getIdordserv()));		
			}
			System.out.println("TAM: "+ListaAR.size());
			System.out.println("KU: "+ListaAR);
		} catch (Exception e) {
			System.out.println("entro catch");
			ListaAR=null;
		}
		return new ResponseEntity<List<ActaRecepcion>>(ListaAR,HttpStatus.OK);
	}
	
	@Transactional
	@RequestMapping(value="adicionar")
	public Map<String, Object> adicionar(HttpServletRequest req,HttpServletResponse res){
		HttpSession sesion=req.getSession(true);
		Persona xuser=(Persona) sesion.getAttribute("xusuario");
		Map<String, Object> respuesta=new HashMap<String, Object>();
		try{
			Object[] consulta=this.manejadorActaRecepcion.registrarOP(req,xuser);
			System.out.println("resp: "+consulta);
			//respuesta.put("estado", true);
			respuesta.put("estado", consulta[0]);
			respuesta.put("idOrdPago",Integer.parseInt(consulta[1].toString()));
		}catch (Exception e){
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			respuesta.put("estado",false);
		}
		return respuesta;
	}
	@RequestMapping(value="Ver")
	public ResponseEntity<List<?>> VerSolicitud(HttpServletRequest req,HttpServletResponse res){
		Persona us=(Persona)req.getSession(true).getAttribute("xusuario");
		List<Object> lista=new ArrayList<>();
		String Tramitador=us.getAp().toUpperCase()+" "+us.getAm().toUpperCase()+" "+us.getNombres().toUpperCase();
		String idOrdPago=req.getParameter("idOrdPago");
		System.out.println("idOrdPago: "+idOrdPago);
		

		OrdenPago ordenPago=this.manejadorActaRecepcion.verOrdenPago(Integer.parseInt(idOrdPago));
		ordenPago.getActaRecepcion().setRegistroKit(this.manejadorInstalacionKit.getRegistroKitOP(ordenPago.getActaRecepcion().getIdordserv()));
		ordenPago.getActaRecepcion().getRegistroKit().setCilindros(this.manejadorInstalacionKit.ListaCilindro(ordenPago.getActaRecepcion().getRegistroKit().getIdregistroKit()));
		OrdenServicio ordServ=this.manejadorServicios.getOrdenServicioAR(ordenPago.getActaRecepcion().getIdordserv()); 
		Solicitud solt=this.manejadorSolicitudes.getSoltByOrdServ(ordenPago.getActaRecepcion().getIdordserv());
		ordServ.setSolicitud(solt);
		ordenPago.getActaRecepcion().setOrdenServicio(ordServ);
		
		lista.add(ordenPago);
		return new ResponseEntity<List<?>>(lista,HttpStatus.OK);
	}
	
	@Autowired
	DataSource dataSource;
	@RequestMapping("Imprimir")
	public  void Imprimir(HttpServletResponse res,HttpServletRequest req){
		Persona us=(Persona)req.getSession(true).getAttribute("xusuario");
		String Tramitador=us.getAp().toUpperCase()+" "+us.getAm().toUpperCase()+" "+us.getNombres().toUpperCase();
		String id=req.getParameter("idOrdPago");
		System.out.println("idOrdPago: "+id);
		//int idRegistroKit=this.manejadorActaRecepcion.getIdRegistroKit(Integer.parseInt(id));  
		//System.out.println("idRegistroKitReport: "+idRegistroKit);
		String escudo="/Service/reportes/escudobolivia.png";        
		String nombreReporte="ORDEN DE PAGO",tipo="pdf", estado="inline";
		System.out.println("escudo: "+this.getClass().getResourceAsStream(escudo));
		      
		Map<String, Object> parametros=new HashMap<String, Object>();
		                        
		String url="/Service/reportes/getOrdenPago.jasper"; 	
	                                
		parametros.put("idOrdPago_param",Integer.parseInt(id));
		//parametros.put("idRegistroKit_param",idRegistroKit);
		parametros.put("tramitador_param",Tramitador);
		parametros.put("escudo_param",this.getClass().getResourceAsStream(escudo));
//		parametros.put("usuario", us.getNombre()+" "+us.getAp()+" "+us.getAm());
	     
		GeneradorReportes g=new GeneradorReportes();
		try{
			g.generarReporte(res, getClass().getResource(url), tipo, parametros, dataSource.getConnection(), nombreReporte, estado);	
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
}
