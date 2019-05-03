package agent;

import com.google.api.client.util.DateTime;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import exception.GetTimeToEventException;

import java.util.ArrayList;

public class Maps {
	
	ArrayList<Evento> eventosPassados = new ArrayList<>();
	private static final String API_KEY = "AIzaSyDcOZV_h67RGwOFz3CN9hLIcdnYUgWc3EI";
	private static final GeoApiContext context = new GeoApiContext.Builder()
			.apiKey(API_KEY)
			.build();
	
	public Maps () {}
	
	public DistanceMatrix getTimeToEvent(String origem, String destino, String meioTransporte)
			throws GetTimeToEventException {
		if (origem == null && destino == null) {
			throw new GetTimeToEventException();
		}


		for (Evento evento : this.eventosPassados) {
			if (evento.getDestino().equals(destino) && evento.getOrigem().equals(origem) &&
				evento.getMeio_transporte().equals(meioTransporte)) {
				System.out.println("[INFO] : Getting duration from past events.");
				return evento.getDm();
			}
		}

		TravelMode travelMode;
		switch (meioTransporte) {
			case "Carro":
				travelMode = TravelMode.DRIVING;
				break;
			case "Transportes":
				travelMode = TravelMode.TRANSIT;
				break;
			default:
				travelMode = TravelMode.WALKING;
		}

		try {
			DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context);
			// set max arrival time
			//req.arrivalTime(arrival);

			DistanceMatrix matrix = req.origins(origem)
					.destinations(destino)
					.mode(travelMode)
					.language("en-EN")
					.await();

			System.out.println("[INFO] : Getting duration from Google Maps.");
			saveNewEvent(origem, destino, meioTransporte, matrix);
			return matrix;

		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new GetTimeToEventException();
		}
	}


	public void printDistanceMatrix(DistanceMatrix dm) {
		System.out.println("[DM] :");
		System.out.println("\tOrigem: " + dm.originAddresses[0]);
		System.out.println("\tDestino: " + dm.destinationAddresses[0]);
		System.out.println("\tValue: " + dm.rows[0].elements[0].toString());
	}


	private void saveNewEvent(String origem, String destino, String meioTransporte, DistanceMatrix dm) {
		this.eventosPassados.add(new Evento(origem, destino, meioTransporte, dm));
		System.out.println("[INFO] : New event stored.");
	}

}
