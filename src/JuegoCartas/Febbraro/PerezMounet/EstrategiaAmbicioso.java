package JuegoCartas.Febbraro.PerezMounet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class EstrategiaAmbicioso extends Estrategia {

	public EstrategiaAmbicioso() {
		super();
	}

	@Override
	public String getAtributo(Carta carta) {
		String atributo = null;
		ArrayList<Atributo> atributosCarta = new ArrayList<Atributo>(); 		
		atributosCarta = carta.getAtributos();

		 int maximo = 0;
	       for(int i=0; i<atributosCarta.size(); i++){
	           if(atributosCarta.get(i).getValor() > maximo){
	        	   maximo = atributosCarta.get(i).getValor();
	        	   atributo = atributosCarta.get(i).getNombre();
	           }
	       }
	       return atributo;
	}

}
