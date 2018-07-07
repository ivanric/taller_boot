package Service.models;

public class TransferenciaBeneficiario {
	protected Integer idtrasl,idsolt,idben;
	protected String fechaTraslado,motivoTraslado,login;
	protected Persona personaNBenf;
	protected RegistroKit registroKit;
	public TransferenciaBeneficiario() {

	}
	public TransferenciaBeneficiario(Integer idtrasl, Integer idsolt, Integer idben, String fechaTraslado,
			String motivoTraslado, String login, Persona personaNBenf, RegistroKit registroKit) {
		super();
		this.idtrasl = idtrasl;
		this.idsolt = idsolt;
		this.idben = idben;
		this.fechaTraslado = fechaTraslado;
		this.motivoTraslado = motivoTraslado;
		this.login = login;
		this.personaNBenf = personaNBenf;
		this.registroKit = registroKit;
	}
	public Integer getIdtrasl() {
		return idtrasl;
	}
	public void setIdtrasl(Integer idtrasl) {
		this.idtrasl = idtrasl;
	}
	public Integer getIdsolt() {
		return idsolt;
	}
	public void setIdsolt(Integer idsolt) {
		this.idsolt = idsolt;
	}
	public Integer getIdben() {
		return idben;
	}
	public void setIdben(Integer idben) {
		this.idben = idben;
	}
	public String getFechaTraslado() {
		return fechaTraslado;
	}
	public void setFechaTraslado(String fechaTraslado) {
		this.fechaTraslado = fechaTraslado;
	}
	public String getMotivoTraslado() {
		return motivoTraslado;
	}
	public void setMotivoTraslado(String motivoTraslado) {
		this.motivoTraslado = motivoTraslado;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public Persona getPersonaNBenf() {
		return personaNBenf;
	}
	public void setPersonaNBenf(Persona personaNBenf) {
		this.personaNBenf = personaNBenf;
	}
	public RegistroKit getRegistroKit() {
		return registroKit;
	}
	public void setRegistroKit(RegistroKit registroKit) {
		this.registroKit = registroKit;
	}
	@Override
	public String toString() {
		return "TransferenciaBeneficiario [idtrasl=" + idtrasl + ", idsolt=" + idsolt + ", idben=" + idben
				+ ", fechaTraslado=" + fechaTraslado + ", motivoTraslado=" + motivoTraslado + ", login=" + login
				+ ", personaNBenf=" + personaNBenf + ", registroKit=" + registroKit + "]";
	}
	
}
