package user;

import com.google.maps.model.DistanceMatrix;

import java.io.Serializable;

public class Evento implements Serializable {

	// for serialization
	static final long serialVersionUID = 42L;

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

	public String getDestino() {
		return destino;
	}

	public String getMeio_transporte() {
		return meio_transporte;
	}

	public DistanceMatrix getDm() {
		return dm;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		else {
			Evento e = (Evento) obj;
			return e.origem.equals(this.origem) && e.destino.equals(this.destino) && e.meio_transporte.equals(this.meio_transporte);
		}
	}
}
