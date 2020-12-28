package br.com.integracaojr.teste;

import br.com.integracaojr.dao.DotacaoDao;

public class TesteDotacao {

	public static void main(String[] args) {
		DotacaoDao dotacao = new DotacaoDao("fpterraroxa");
		
		dotacao.getDotacao(1, 2020, 326);      
		

	}

}
