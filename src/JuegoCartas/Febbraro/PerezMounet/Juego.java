package JuegoCartas.Febbraro.PerezMounet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Juego {

	private Mazo mazoJuego;
	private Jugador jugador1;
	private Jugador jugador2;
	private int maximoRondas;
	
	// Constructor de Juego
	// Desde aqui llamo para cargar el mazoGeneral
	public Juego (Jugador jugador1, Jugador jugador2, String pathCartas, int maximoRondas) {
		this.mazoJuego = new Mazo();
		this.jugador1 = jugador1;
		this.jugador2 = jugador2;
		this.maximoRondas = maximoRondas;
		jugador1.setGanoMano(true);
		jugador2.setGanoMano(false);
		cargarMazo(pathCartas);
	}
	
	// Metodo donde esta la principal logica del juego
	// comparaciones y demas
	public void Jugar() {
		
		repartirCartas(mazoJuego, jugador1, jugador2);
		
		for (int ronda=0 ; ronda <= maximoRondas ; ronda ++ ) {
			System.out.println ("---------- Ronda " + ronda + "----------");
			if (!jugador1.sinCartas() && !jugador2.sinCartas())
				if (jugador1.getGanoMano()) {
					jugarMano(jugador1, jugador2);
					// Imprimir por pantalla
				}
				else {
					jugarMano(jugador2, jugador1); 
					// Imprimir por pantalla
				}
			else if (jugador1.sinCartas())
				System.out.println("Gano jugador 1");
			else if (jugador2.sinCartas())
				System.out.println("Gano jugador 2");
		}

		System.out.println("-----------------------------");
		System.out.println("Se llego al maximo de rondas");	
				
	}
	
	// Reparte las cartas del mazo general a cada jugador
	// todas las cartas en posicion par van para jugador 1
	// cuando se repartieron todas las cartas se limpia el mazoGeneral
	private void repartirCartas(Mazo mazo, Jugador j1, Jugador j2) {
		for (int i=0; i < mazoJuego.tamanioMazo() ; i++) {
			if (i%2!=0) // Si es par, va al J1
				j1.agregarCarta(mazoJuego.tomarUna(i));
			else // Si es impar, va al J2
				j2.agregarCarta(mazoJuego.tomarUna(i));
		}
		mazoJuego.borrarMazo();
	}
	
	// En este metodo el jugador ganador elije el atributo de forma aleatoria
	// se guardan las cartas que van a jugar cada jugador y se compara
	// quien gano
	private void jugarMano(Jugador jugadorGanador, Jugador otroJugador) {
		Carta cartaElegidaJG = jugadorGanador.jugarCarta();
		Carta cartaElegidaOJ = otroJugador.jugarCarta();
		int atributoAletorio = jugadorGanador.valorAleatorio(cartaElegidaJG);
		
		int valorJG = cartaElegidaJG.getAtributo(atributoAletorio).getValor();
		int valorJO = cartaElegidaOJ.getAtributo(atributoAletorio).getValor(); 
		
		if ((cartaElegidaJG.getAtributo(atributoAletorio).getValor()) > (cartaElegidaOJ.getAtributo(atributoAletorio).getValor())) {
			jugadorGanador.agregarCarta(cartaElegidaJG);
			jugadorGanador.agregarCarta(cartaElegidaOJ);
			jugadorGanador.sacarCartaMazo(0);
			otroJugador.sacarCartaMazo(0); 
			jugadorGanador.setGanoMano(true); }
		else if ((cartaElegidaJG.getAtributo(atributoAletorio).getValor()) < (cartaElegidaOJ.getAtributo(atributoAletorio).getValor())) {
			otroJugador.agregarCarta(cartaElegidaOJ);
			otroJugador.agregarCarta(cartaElegidaJG);
			otroJugador.sacarCartaMazo(0);
			jugadorGanador.sacarCartaMazo(0); 
			otroJugador.setGanoMano(true);
			jugadorGanador.setGanoMano(false);}
		else {
			jugadorGanador.agregarCarta(cartaElegidaJG);
			otroJugador.agregarCarta(cartaElegidaOJ);
			jugadorGanador.sacarCartaMazo(0);
			otroJugador.sacarCartaMazo(0); 
			jugadorGanador.setGanoMano(true); 
		}
		
		imprimirPorPantalla(jugadorGanador, otroJugador,
				cartaElegidaJG, cartaElegidaOJ, atributoAletorio);
		
	}
	
	
	// Metodo que se invoca al comenzar el juego y carga el mazo
	// que se encuentra en el path.
	private void cargarMazo(String jsonFile) {
				
        File jsonInputFile = new File(jsonFile);
        InputStream is;
        try {
            is = new FileInputStream(jsonInputFile);
            // Creo el objeto JsonReader de Json.
            JsonReader reader = Json.createReader(is);
            // Obtenemos el JsonObject a partir del JsonReader.
            JsonArray cartas = (JsonArray) reader.readObject().getJsonArray("cartas");
            for (JsonObject carta : cartas.getValuesAs(JsonObject.class)) {
                String nombreCarta = carta.getString("nombre");
                JsonObject atributos = carta.getJsonObject("atributos");
                String nombreAtributoCarta = "";
                int valorAtributoCarta = 0;
                ArrayList<Atributo> atributosCarta = new ArrayList<Atributo>();
                atributosCarta.clear();
                for (String nombreAtributo:atributos.keySet()) {
                	nombreAtributoCarta = nombreAtributo;
                	valorAtributoCarta = atributos.getInt(nombreAtributo);
                	Atributo nuevoAtributo = new Atributo(nombreAtributoCarta, valorAtributoCarta);
                	atributosCarta.add(nuevoAtributo);
                }
                Carta nuevaCarta = new Carta(nombreCarta, atributosCarta);
                mazoJuego.agregarCarta(nuevaCarta);              
            }

            reader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
	
	private void borrarArrayAtributos(ArrayList<Atributo> atributosCarta) {
		for (int i=0 ; i < atributosCarta.size() ; i++)
			atributosCarta.remove(i);
	}


	// Este se saca, es solo para probar si cargo bien el mazo
	public int getTamanioMazoGNR() {
		return mazoJuego.tamanioMazo();
	}
	
	private void imprimirPorPantalla(Jugador j1, Jugador j2,
			Carta cartaElegidaJ1, Carta cartaElegidaJ2, int atributoAletorio) {

		System.out.println("El jugador " + j1.getNombre() 
			+ " selecciona para competir por el atributo " 
			+ cartaElegidaJ2.getAtributo(atributoAletorio).getNombre());
		
		System.out.println("La carta de " + j1.getNombre() 
			+ " es " + cartaElegidaJ2.getNombre()
			+ " con "
			+ cartaElegidaJ1.getAtributo(atributoAletorio).getNombre() + " "
			+ cartaElegidaJ1.getAtributo(atributoAletorio).getValor());
		
		System.out.println("La carta de " + j2.getNombre() 
			+ " es " + cartaElegidaJ2.getNombre()
			+ " con "
			+ cartaElegidaJ2.getAtributo(atributoAletorio).getNombre() + " "
			+ cartaElegidaJ2.getAtributo(atributoAletorio).getValor());
		
		// Ver tema empate
		if (j1.getGanoMano())
			System.out.println("Gana la ronda " + j1.getNombre() + ".");
		else if (j2.getGanoMano())
			System.out.println("Gana la ronda " + j2.getNombre() + ".");
		
		System.out.println(j1.getNombre()
			+ " posee ahora " + j1.getTamanioMazo() + " cartas y "
			+ j2.getNombre() + " posee ahora " + 
			j2.getTamanioMazo() + " cartas.");
	
	}
		
}
