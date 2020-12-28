package br.com.integracaojr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.com.integracaojr.data.ConexaoElotech;
import br.com.integracaojr.data.ConexaoTerraRoxa;
import br.com.integracaojr.entidade.FonteRecurso;
import br.com.integracaojr.entidade.PlanoConta;

public class PlanoContaDao {
	private static Connection connection;

	public PlanoContaDao(String dataBase) {
		if (dataBase == "fpterraroxa") {
			this.connection = ConexaoTerraRoxa.getConnection(dataBase);
		} else {
			dataBase = "elotech";
			this.connection = ConexaoElotech.getConnection(dataBase);
		}

	}

	public boolean getPlanoConta(Integer entidade, Integer exercicio, Integer cliente) {

		if (cliente != null) {
			String sqlPlanoconta = "select distinct "+cliente+" entidade, \r\n" + "       elc.exercicio eloexercicio,\r\n"
					+ "       elc.conta eloconta, \r\n" + "       (select distinct descricao from siscop.plano p \r\n"
					+ "           where p.entidade = elc.entidade \r\n"
					+ "             and p.exercicio = elc.exercicio \r\n"
					+ "             and p.conta = elc.conta) elodescricao, \r\n"
					+ "       elc.idcontacorrente as elocontacorrente, \r\n"
					+ "       rpad(elc.conta,20,'0') ipmconta, \r\n"
					+ "       (select distinct descricao from siscop.plano p \r\n"
					+ "           where p.entidade = elc.entidade \r\n"
					+ "             and p.exercicio = elc.exercicio \r\n"
					+ "             and p.conta = elc.conta) ipmdescricao,\r\n"
					+ "       lpad(cast(case when (elc.conta like '1111%' or elc.conta like '1141%') then \r\n"
					+ "            (select (select cb.fonterecurso from siscop.contabancaria cb\r\n"
					+ "                       where cb.entidade = cbv.entidade \r\n"
					+ "                         and cb.reduzido = cbv.reduzido ) \r\n"
					+ "               from siscop.contabancariavinculo cbv\r\n"
					+ "               inner join siscop.contacorrente cc \r\n"
					+ "                       on cbv.entidade = cc.entidade \r\n"
					+ "                      and cbv.id = cc.idcontabancariavinculo\r\n"
					+ "               inner join siscop.planocontacorrente pcc\r\n"
					+ "                       on pcc.entidade = cbv.entidade\r\n"
					+ "                      and pcc.idcontacorrente = cc.idcontacorrente\r\n"
					+ "                where pcc.entidade = elc.entidade\r\n"
					+ "                  and pcc.exercicio = elc.exercicio\r\n"
					+ "                  and pcc.conta = elc.conta\r\n"
					+ "                  and pcc.idcontacorrente = elc.idcontacorrente) else 0 end as varchar(8)),8,'0') ipmvinculo\r\n"
					+ "    from siscop.eventoslancadosconta elc \r\n" + "  inner join siscop.eventoslancados el \r\n"
					+ "          on elc.entidade = el.entidade\r\n" + "         and elc.exercicio = el.exercicio \r\n"
					+ "         and elc.tipoevento = el.tipoevento \r\n"
					+ "         and elc.grupoevento = el.grupoevento\r\n" + "         and elc.evento = el.evento\r\n"
					+ "         and elc.nrolancamento = el.nrolancamento\r\n" + "  where elc.entidade = ? \r\n"
					+ "    and elc.exercicio = ? \r\n"
					+ "    and cast(substring(cast(el.datadocumentoorigem as varchar(10)),6,2) as numeric(2,0)) > 0 \r\n"
					+ "    and not exists \r\n" + "        (select * from intplanocontas ipc\r\n"
					+ "          where elc.conta = ipc.eloconta \r\n"
					+ "            and elc.idcontacorrente = cast(ipc.elocontacorrente as numeric(8,0))\r\n"
					+ "            and ipc.entidade = ?)";
			try {
				PreparedStatement pspl = connection.prepareStatement(sqlPlanoconta);

				pspl.setInt(1, entidade);
				pspl.setInt(2, exercicio);
				pspl.setInt(3, cliente);

				ResultSet rspl = pspl.executeQuery();
				while (rspl.next()) {
					PlanoConta planoconta = new PlanoConta();

					planoconta.setEntidade(cliente);
					planoconta.setEloexercicio(rspl.getInt("eloexercicio"));
					planoconta.setEloconta(rspl.getString("eloconta"));
					planoconta.setElodescricao(rspl.getString("elodescricao"));
					planoconta.setElocontacorrente(rspl.getInt("elocontacorrente"));
					planoconta.setIpmconta(rspl.getString("ipmconta"));
					planoconta.setIpmdescricao(rspl.getString("ipmdescricao"));
					planoconta.setIpmvinculo(rspl.getString("ipmvinculo"));

					String sqlplsalvar = "INSERT INTO intplanocontas(entidade,eloexercicio,eloconta,elodescricao,elocontacorrente,\r\n"
							+ "			                           ipmconta,ipmdescricao,ipmvinculo) VALUES (?,?,?,?,?,?,?,?)";
					try {
						PreparedStatement psipl = connection.prepareStatement(sqlplsalvar);

						psipl.setInt(1, cliente);
						psipl.setInt(2, rspl.getInt("eloexercicio"));
						psipl.setString(3, rspl.getString("eloconta"));
						psipl.setString(4, rspl.getString("elodescricao"));
						psipl.setInt(5, rspl.getInt("elocontacorrente"));
						psipl.setString(6, rspl.getString("ipmconta"));
						psipl.setString(7, rspl.getString("ipmdescricao"));
						psipl.setString(8, rspl.getString("ipmvinculo"));
						psipl.execute();
						psipl.close();
						
						

					} catch (SQLException e) {
						e.printStackTrace();
					}
					

					

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println("Não existe conta a ser cadastrado");
			return false;		

		}
		System.out.println("Gerado Com Sucesso PlanoConta");
		return true;

	}

}
