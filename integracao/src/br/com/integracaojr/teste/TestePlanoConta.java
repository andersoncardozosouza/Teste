package br.com.integracaojr.teste;

import br.com.integracaojr.dao.PlanoContaDao;

public class TestePlanoConta {

	public static void main(String[] args) {
		PlanoContaDao planoContaDao = new PlanoContaDao("elotech");
		
		planoContaDao.getPlanoConta(1, 2020, 8787);

	}

}
