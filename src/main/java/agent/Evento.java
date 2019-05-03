package agent;

public class Evento {
	
	String origem;
	String destino;
	MeioTransporte meio_transporte;
	float duracao;
	
	public Evento (String origem, String destino, MeioTransporte meio_transporte, float duracao) {
		this.origem = origem;
		this.destino = destino;
		this.meio_transporte = meio_transporte;
		this.duracao = duracao;
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

	public MeioTransporte getMeio_transporte() {
		return meio_transporte;
	}

	public void setMeio_transporte(MeioTransporte meio_transporte) {
		this.meio_transporte = meio_transporte;
	}

	public float getDuracao() {
		return duracao;
	}

	public void setDuracao(float duracao) {
		this.duracao = duracao;
	}
	
}
