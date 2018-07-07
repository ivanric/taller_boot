package Service.manager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import Service.models.Modulo;
import Service.models.Opcion;
import Service.models.Proceso;
import Service.models.Rol;



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
		String sql="select * from opcion  where estado=1";
		return this.db.query(sql,new objOpcion());
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
	
	


	

}