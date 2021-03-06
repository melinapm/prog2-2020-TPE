package JuegoCartas.Febbraro.PerezMounet.estrategias;

import JuegoCartas.Febbraro.PerezMounet.Carta;

public class EstrategiaObstinado extends Estrategia {
	
	private String mismoAtributo;

	public EstrategiaObstinado() {
		super();
		this.mismoAtributo = null;
	}

	private String getMismoAtributo() {
		return mismoAtributo;
	}

	private void setMismoAtributo(String mismoAtributo) {
		this.mismoAtributo = mismoAtributo;
	}

	@Override
	public String getAtributo(Carta carta) {
		if (mismoAtributo == null) {
			setMismoAtributo(carta.getAtributoPorPosicion(0).getNombre());
			return getMismoAtributo();
		} else
			return getMismoAtributo();			
	}

}
