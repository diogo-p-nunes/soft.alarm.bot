package user;

import com.google.maps.model.DistanceMatrix;

import java.io.Serializable;

public class Evento implements Serializable {
	
	private String origem;
	private String destino;
	private String meio_transporte;
	private DistanceMatrix dm;
	
	public Evento (String origem, String destino, String meio_transporte, DistanceMatrix dm) {
		this.origem = origem;
		this.destino = destino;
		this.meio_transporte = meio_transporte;
		this.dm = dm;
	}

	public String getOrigem() {
		return origem;
	}

	public void setOrigem(String origem) {
		this.origem = origem;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getMeio_transporte() {
		return meio_transporte;
	}

	public void setMeio_transporte(String meio_transporte) {
		this.meio_transporte = meio_transporte;
	}

	public DistanceMatrix getDm() {
		return dm;
	}

	public void setDm(DistanceMatrix dm) {
		this.dm = dm;
	}
}
