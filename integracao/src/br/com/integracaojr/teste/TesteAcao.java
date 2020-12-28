package br.com.integracaojr.teste;

import br.com.integracaojr.dao.AcaoDao;

public class TesteAcao {

	public static void main(String[] args) {
		
        AcaoDao acaoDao = new AcaoDao("fpterraroxa");	
		
		acaoDao.getAcao(1,2020,326);

	}

}
