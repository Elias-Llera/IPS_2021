package app.tkrun.entities;

import java.sql.Date;

public class InscripcionEntity {

	private String emailAtleta;
	private int idCarrera;
	private String estado;
	private int dorsal;
	private int idCategoria;
	private Date fecha;

	public String getEmailAtleta() {
		return emailAtleta;
	}
	
	public void setEmailAtleta(String email) {
		this.emailAtleta = email;
	}

	public int getIdCarrera() {
		return idCarrera;
	}
	
	public void setIdCarrera(int idCarrera2) {
		this.idCarrera = idCarrera2;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public int getDorsal() {
		return dorsal;
	}

	public void setDorsal(int dorsal) {
		this.dorsal = dorsal;
	}

	public int getIdCategoria() {
		return idCategoria;
	}
	
	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
}
