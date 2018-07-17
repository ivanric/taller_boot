package Service.manager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import Service.manager.ManejadorServicios.objServicios;
import Service.models.Modulo;
import Service.models.Opcion;
import Service.models.Persona;
import Service.models.Proceso;
import Service.models.Rol;
import Service.models.Servicio;



//@Service indica que la clase es un bean de la capa de negocio
@Service
public class ManejadorRoles{
	
	private JdbcTemplate db;
	@Autowired
	public void setDataSource(DataSource dataSource){
		db = new JdbcTemplate(dataSource);
	}
	public class objRol implements RowMapper<Rol>{
		@Override
		public Rol mapRow(ResultSet rs, int arg1) throws SQLException {
			Rol r= new Rol();
			r.setIdrol(rs.getInt("idrol"));
			r.setNombre(rs.getString("nombre"));
			r.setEstado(rs.getInt("estado"));
			return r;
	    }
	}
	public class objModulo implements RowMapper<Modulo>{
		@Override
		public Modulo mapRow(ResultSet rs, int arg1) throws SQLException {
			Modulo m= new Modulo();
			m.setIdmod(rs.getInt("idmod"));
			m.setNombre(rs.getString("nombre"));
			m.setIcono(rs.getString("icono"));
			m.setEstado(rs.getInt("estado"));
			return m;
	    }
	}
	public class objProceso implements RowMapper<Proceso>{
		@Override
		public Proceso mapRow(ResultSet rs, int arg1) throws SQLException {
			Proceso p= new Proceso();
			p.setIdproc(rs.getInt("idproc"));
			p.setNombre(rs.getString("nombre"));
			p.setEnlace(rs.getString("enlace"));
			p.setIcono(rs.getString("icono"));
			p.setEstado(rs.getInt("estado"));
			return p;
	    }
	}
	public class objOpcion implements RowMapper<Opcion>{
		@Override
		public Opcion mapRow(ResultSet rs, int arg1) throws SQLException {
			Opcion o= new Opcion();
			o.setIdopc(rs.getInt("idopc"));
			o.setNombre(rs.getString("nombre"));
			o.setCodigo(rs.getString("codigo"));
			o.setEstado(rs.getInt("estado"));
			return o;
	    }
	}
	public List<Rol> ListarRolUsuario(int idper){
		String sql="select r.* from rol r,usuario us,rolusu rs where r.idrol=rs.idrol and us.login=rs.login and us.idper=?";
		return this.db.query(sql,new objRol(),idper);	
	}
	public List<Rol> ControlRoles(int codper){
		System.out.println("codper: "+codper);
		String sql="select r.* from rol r,usuario us,rolusu rs where r.idrol=rs.idrol and rs.login=us.login and us.idper=?";
		return this.db.query(sql,new objRol(),codper);
	}
	public List<Rol> ListaRoles(){
		String sql="select * from rol  where estado=1";
		return this.db.query(sql,new objRol());
	}
	public List<Modulo> ListaMenus(){
		String sql="select * from modulo  where estado=1";
		return this.db.query(sql,new objModulo());
	}
	public List<Proceso> ListaProcesos(){
		String sql="select * from proceso  where estado=1";
		return this.db.query(sql,new objProceso());
	}
	public List<Opcion> ListaOpciones(){
		String sql="select * from opcion where  estado=1 ";
		return this.db.query(sql,new objOpcion());
	}
	public List<Opcion> ListaOpcionesByIdproc(int idproc){
		System.out.println("idProc: "+idproc);
		String sql="select o.* from opcion o ,procopc po,proceso pc  where  o.idopc=po.idopc AND pc.idproc=po.idproc and o.estado=1 AND pc.idproc=?";
		return this.db.query(sql,new objOpcion(),idproc);
	}
	public List<Modulo> ListaMenuRol(int codr){
		String sql="select DISTINCT m.* from modulo m,permiso p  where p.idmod=m.idmod and p.idrol=?";
		return this.db.query(sql,new objModulo(),codr);
	}
	public List<Proceso> ListaProcMod(int idRol,int idMod){
		String sql="select DISTINCT p.* from proceso p,permiso pr  where p.idproc=pr.idproc and pr.idrol=? and pr.idmod=?";
		return this.db.query(sql,new objProceso(),idRol,idMod);
	}

	public List<Opcion> ListaOpcProc(int idRol,int idMod,int idProc){
		String sql="select DISTINCT op.* from opcion op,permiso pr  where op.idopc=pr.idopc and pr.idrol=? and pr.idmod=? and  pr.idproc=?";
		return this.db.query(sql,new objOpcion(),idRol,idMod,idProc);
	}
	
	
	//RESTROLES
	public List<Rol> ListarRoles(HttpServletRequest req){
		String filtro=req.getParameter("filtro");
		int estado=Integer.parseInt(req.getParameter("estado"));
		String sql="SELECT r.* FROM rol r where (r.nombre LIKE ?) and (r.estado=? or ?=-1) ORDER BY r.idrol ASC";
		return this.db.query(sql, new objRol(),"%"+filtro+"%",estado,estado);
	}
	
	public boolean registrar(HttpServletRequest req,Persona xuser) {
		String sql="";
		int idrol=generarIdRol();
		try {
			sql="INSERT INTO rol(idrol,nombre) VALUES(?,?)";
			this.db.update(sql,idrol,req.getParameter("nombre").toUpperCase());
			return true;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	public Rol datosModificar(HttpServletRequest req){
		String sql="";
		Rol r=null;
		int idrol=Integer.parseInt(req.getParameter("idrol"));
		System.out.println("idrol: "+idrol);
		try {
			sql="SELECT r.* FROM rol r WHERE r.idrol=?";
			r=this.db.queryForObject(sql,new objRol(),idrol);
		} catch (Exception e) {
			r=null;
		}
		return r;
	}
	public boolean modificar(HttpServletRequest req,Persona xuser){
		String sql="";
		int idrol=Integer.parseInt(req.getParameter("idrol"));
		
		try {
			sql="UPDATE rol SET nombre=? WHERE idrol=?";
			this.db.update(sql,req.getParameter("nombre").toUpperCase(),idrol);
			
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	public boolean eliminar(Integer id){
		String sql="";
		try {
			sql="UPDATE rol  SET estado=0 WHERE idrol=?";
			int a=this.db.update(sql,id);
			this.db.update(sql,id);
			System.out.println("sql_elimino: "+a);
			if (a==1) {
				return true;	
			} else {
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public boolean habilitar(Integer id){
		String sql="";
		try {
			sql="UPDATE rol SET estado=1 WHERE idrol=?";
			int a=this.db.update(sql,id);
			this.db.update(sql,id);
			System.out.println("sql_habilito: "+a);
			if (a==1) {
				return true;	
			} else {
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	public int generarIdRol(){
		String sql="select COALESCE(max(idrol),0)+1 as idrol from rol";
		return db.queryForObject(sql, Integer.class);
	}

	

}