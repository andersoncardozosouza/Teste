package br.com.integracaojr.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import br.com.integracaojr.data.ConexaoElotech;
import br.com.integracaojr.data.ConexaoTerraRoxa;




public class UploadTodos   {
	
	private static Connection connection; 
	

	public UploadTodos(String dataBase) {
		if (dataBase == "fpterraroxa") {
		    this.connection = ConexaoTerraRoxa.getConnection(dataBase);
		}else {
			dataBase = "elotech";
			this.connection = ConexaoElotech.getConnection(dataBase);
		}	
		
		
	}
	
	
	public  boolean getTodosUpload(Integer entidade, Integer exercicio, Integer dMes, Integer cliente) throws FileNotFoundException {
		
		if(entidade != null && exercicio != null && dMes != null) {   
			      
        
        
        if (entidade == 326) {
        	 cliente = 326;
        	 entidade = 1;// Fundo de Terra Roxa - Previsterra
        }else  if (entidade == 12634) {
           	 cliente = 12634;// Câmara de Guaíra  
           	 entidade = 1;
        }else if (entidade == 8787) {
              	 cliente = 8787;// Câmara de Arapongas
              	 entidade = 1;
           }
        
        
     
		Integer mesLoa = dMes;
		Integer anoLoa = exercicio;

		String ano = null;
		String mes = null;
		String meses = null;

		if (dMes == 1) {
			mes = "01";
			meses = "Janeiro";
		} else if (dMes == 2) {
			mes = "02";
			meses = "Fevereiro";
		} else if (dMes == 3) {
			mes = "03";
			meses = "Março";
		} else if (dMes == 4) {
			mes = "04";
			meses = "Abril";
		} else if (dMes == 5) {
			mes = "05";
			meses = "Maio";
		} else if (mesLoa == 6) {
			mes = "06";
			meses = "Junho";
		} else if (mesLoa == 7) {
			mes = "07";
			meses = "Julho";
		} else if (mesLoa == 8) {
			mes = "08";
			meses = "Agosto";
		} else if (mesLoa == 9) {
			mes = "09";
			meses = "Setembro";
		} else if (mesLoa == 10) {
			mes = "10";
			meses = "Outubro";
		} else if (mesLoa == 11) {
			mes = "11";
			meses = "Novembro";
		} else if (mesLoa == 12) {
			mes = "12";
			meses = "Dezembro";
		}
        String merda = mes;
        
        mes = merda;
        
		File fileRaiz = new File("c:\\integracao");
		File fileAno = new File("c:\\integracao\\" + anoLoa);
		File fileMes = new File("c:\\integracao\\" + anoLoa + "\\" + mes);

		File conFileRaiz[] = fileRaiz.listFiles();
		
		if (conFileRaiz == null) {
			File cFile = new File("c:\\integracao");
			cFile.mkdir();
		}
		File conFileAno[] = fileAno.listFiles();
		if (conFileAno == null) {
			File cFile = new File("c:\\integracao\\" + anoLoa);
			cFile.mkdir();
		}
		File conFileMes[] = fileMes.listFiles();
		if (conFileMes == null) {
			File cFile = new File("c:\\integracao\\" + anoLoa + "\\" + mes);
			cFile.mkdir();
		}
/////xxxxxxx Insere Fonte Recurso  xxxx\\\\
		
//////// xxxxxxx Insere Fonte Recurso xxxxxxxxx\\\\\\\\\

		
		
		String sql = 
				"select lpad('"+cliente+"',6,'0') clicodigo,  \r\n"
				+ "				       exercicio loaano,  \r\n"
				+ "				       lpad(cast(empenho as varchar(8)),8,'0') empnro, \r\n"
				+ "				       lpad('0',3,'0')  empsub,  \r\n"
				+ "				       tipo empespecie,  \r\n"
				+ "				       lpad('0',4,'0') empanoinscresto, \r\n"
				+ "				       lpad('0',4,'0') empanobaixaresto,  \r\n"
				+ "				       cast(data as varchar(10)) empdataemissao, \r\n"
				+ "				       cast(data as varchar(10)) empdatavencimento,  \r\n"
				+ "				       lpad(cast(cast(valor as numeric(17,2))as varchar(17)),17,'0') empvalor, \r\n"
				+ "				       substring(regexp_replace(historiconad, E'[\n\r]+', ' - ', 'g' ),1,500) emphistorico ,  \r\n"
				+ "				       rpad((select case when cnpj = '58460160904000' then '07890935000130' else cnpj end cnpj from siscop.fornecedor where fornecedor = siscop.empenho.fornecedor),14,' ') unicpfcnpj, \r\n"
				+ "				       rpad((select nome from siscop.fornecedor where fornecedor = siscop.empenho.fornecedor),200,' ') uninomerazao, \r\n"
				+ "				       2 plntipoplano, \r\n"
				+ "				       rpad((select 3||d.ipmelemento from intdotacao d\r\n"
				+ "					      where siscop.empenho.exercicio = d.eloexercicio \r\n"
				+ "					        and siscop.empenho.programatica = d.eloprogramatica\r\n"
				+ "					        and siscop.empenho.fonterecurso = cast(d.elofonterecurso as double precision) \r\n"
				+ "					        and siscop.empenho.despesa = d.elodespesa \r\n"
				+ "					        and siscop.empenho.desdobradesp = d.elodesdobramento\r\n"
				+ "					        and siscop.empenho.subdesdobramento = d.elosubdesdobramento),20,'0')  plncodigo,  \r\n"
				+ "				       lpad(substring(programatica,1,2),2,'0') orgcodigo, \r\n"
				+ "				       substring(programatica,3,3) undcodigo, \r\n"
				+ "				       lpad(substring(programatica,6,2),4,'0') tfccodigo, \r\n"
				+ "				       lpad(substring(programatica,8,3),4,'0') tsfcodigo, \r\n"
				+ "				       substring(programatica,11,4) pgrcodigo,   \r\n"
				+ "				       lpad((select d.ipmtipoacao from intdotacao d\r\n"
				+ "					      where siscop.empenho.exercicio = cast(d.eloexercicio as double precision) \r\n"
				+ "					        and siscop.empenho.programatica = d.eloprogramatica\r\n"
				+ "					        and siscop.empenho.fonterecurso = cast(d.elofonterecurso as double precision)\r\n"
				+ "					        and siscop.empenho.despesa = d.elodespesa \r\n"
				+ "					        and siscop.empenho.desdobradesp = d.elodesdobramento\r\n"
				+ "					        and siscop.empenho.subdesdobramento = d.elosubdesdobramento),2,'0') acotipo, \r\n"
				+ "				       substring(programatica,15,4)acocodigo, \r\n"
				+ "				       lpad((select distinct cast(ipmvinculo as varchar(8)) from intfonterecurso ifr where ifr.elofonterecurso =  siscop.empenho.fonterecurso ),8,'0') vincodigo, \r\n"
				+ "				       rpad('3'||despesa,20,'0') plncodigodotacao,  \r\n"
				+ "				       lpad(cast(idlancamento as varchar(10)),10,'0') ctlsequencia,despesa||desdobradesp||subdesdobramento despesa \r\n"
				+ "				   from siscop.empenho  \r\n"
				+ "				     where entidade = ?  \r\n"
				+ "				    and exercicio = ? \r\n"
				+ "					and cast(substring(cast(data as varchar(10)),6,2)as numeric(10,0)) = ? \r\n"
				+ "					 order by empenho";
		
		try {
			PreparedStatement ps = connection.prepareStatement(sql);	
			
			ps.setInt(1,entidade);
			ps.setInt(2,exercicio);
			ps.setInt(3,dMes);
			
			ResultSet rs = ps.executeQuery();
			
            File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\Empenho.txt");
			
			PrintWriter pw = new PrintWriter(file);
			
			while (rs.next()) {
				
				String fimLinha = "\r\n";
				
				String cnpj = rs.getString("unicpfcnpj");	
				String elemento = rs.getString("plncodigo");
				pw.printf("%6s%4s%8s%3s%1s%4s%4s%10s%10s%17s%-500s%-14s%-200s%1s%20s%2s%3s%4s%4s%4s%2s%4s%8s%-20s%10s%4s", 
						rs.getString("clicodigo"), 
						rs.getString("loaano"), 
						rs.getString("empnro"),
						rs.getString("empsub"), 
						rs.getString("empespecie"), 
						rs.getString("empanoinscresto"),
						rs.getString("empanobaixaresto"), 
						rs.getString("empdataemissao"), 
						rs.getString("empdatavencimento"),
				        rs.getString("empvalor"),
				        rs.getString("emphistorico"),
				        cnpj,
				        rs.getString("uninomerazao"),
				        rs.getString("plntipoplano"),
				        elemento,
				        rs.getString("orgcodigo"),
				        rs.getString("undcodigo"),
				        rs.getString("tfccodigo"),
				        rs.getString("tsfcodigo"),
				        rs.getString("pgrcodigo"),
				        rs.getString("acotipo"),
				        rs.getString("acocodigo"),
				        rs.getString("vincodigo"),
				        rs.getString("plncodigodotacao"),
				        rs.getString("ctlsequencia"),fimLinha);		
		}
			pw.flush();
			pw.close();
			
		} catch (SQLException e) {
			e.printStackTrace();			
		}
		//Fim Empenho 
		String sqlee = 
        "select lpad('"+cliente+"',6,'0') clicodigo,  \r\n" + 
		"       exercicio loaano, \r\n" + 
		"       exercicio loaanoemp, \r\n" + 
		"       lpad(cast(empenho as varchar(8)),8,'0') empnro, \r\n" + 
		"       lpad('0',3,'0') empsub, \r\n" + 
		"       lpad(cast(idlancamento as varchar(10)),10,'0') estsequencia, \r\n" + 
		"       motivo esthistorico, \r\n" + 
		"       1 esttipocancresto, \r\n" + 
		"       cast(data as varchar(10)) estdata, \r\n" + 
		"       lpad(cast(cast(valor as numeric(17,2)) as varchar(17)),17,'0') estvalor, \r\n" + 
		"       lpad(cast(idlancamento as varchar(10)),10,'0')ctlsequencia \r\n" + 
		"  from siscop.anulacaoempenho \r\n" + 
		"   where entidade = ?  \r\n" + 
		"     and exercicio = ?  \r\n" + 
		"     and cast(substring(cast(data as varchar(10)),6,2)as numeric(10,0)) = ? order by data";

try {
PreparedStatement ps = connection.prepareStatement(sqlee);	

ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);

ResultSet rs = ps.executeQuery();	

File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\EstornoEmpenho.txt");

PrintWriter pw = new PrintWriter(file);
while (rs.next()) {
int empsub = rs.getString("empsub").length();	
String vEmpsub = null;
if (empsub == 1) {
	vEmpsub = "000";	
}	
String fimLinha = "\r\n";

pw.printf("%6s%4s%4s%8s%3s%10s%-500s%1s%10s%17s%10s%4s", 
		rs.getString("clicodigo"), 
		rs.getString("loaano"), 
		rs.getString("loaanoemp"),
		rs.getString("empnro"),
		rs.getString("empsub"), 						 
		rs.getString("estsequencia"), 
		rs.getString("esthistorico"),
        rs.getString("esttipocancresto"),
        rs.getString("estdata"),
        rs.getString("estvalor"),
        rs.getString("ctlsequencia"),fimLinha);
}
pw.flush();
pw.close();
} catch (SQLException e) {
e.printStackTrace();
}
//Fim Estorno Empenho

String sqll =  
"select lpad('"+cliente+"',6,'0') clicodigo,  \r\n" + 
"       exercicio loaano, \r\n" + 
"       case when anodocorigem < exercicio then 2 \r\n" + 
"       else 1 end liqtipo, \r\n" + 
"       lpad(cast(idlancamento as varchar(10)),10,'0') liqsequencia, \r\n" + 
"       anodocorigem loaanoemp, \r\n" + 
"       lpad(cast(nodocorigem as varchar(8)),8,'0') empnro, \r\n" + 
"       lpad('',3,'0') empsub, \r\n" + 
"      cast(data as varchar(10)) liqdata, \r\n" + 
"      cast(data as varchar(10)) liqdatavencto, \r\n" + 
"      lpad(cast(cast(valor as numeric(17,2)) as varchar(17)),17,'0') liqvalor, \r\n" + 
"      substring(regexp_replace(historicoobs, E'[\\n\\r]+', ' - ', 'g' ),1,499) liqhistorico, \r\n" + 
"      lpad(cast(idlancamento as varchar(10)),10,'0')ctlsequencia \r\n" + 
"	 from siscop.liquidacao \r\n" + 
"	   where entidade = ?  \r\n" + 
"	     and exercicio = ? and contabilizado = 'S'  \r\n" + 
"	     and cast(substring(cast(data as varchar(10)),6,2)as numeric(10,0)) = ? order by nodocorigem";

try {
PreparedStatement ps = connection.prepareStatement(sqll);	

ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);

ResultSet rs = ps.executeQuery();				

File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\Liquidacao.txt");

PrintWriter pw = new PrintWriter(file);
while (rs.next()) {
int empsub = rs.getString("empsub").length();	
String vEmpsub = null;
if (empsub == 1) {
vEmpsub = "000";	
}	
String fimLinha = "\r\n";

pw.printf("%6s%4s%1s%10s%4s%8s%3s%10s%10s%17s%-500s%10s%4s", 
	rs.getString("clicodigo"), 
	rs.getString("loaano"), 
	rs.getString("liqtipo"),
	rs.getString("liqsequencia"),
	rs.getString("loaanoemp"),
	rs.getString("empnro"),
	rs.getString("empsub"),
    rs.getString("liqdata"),
    rs.getString("liqdatavencto"),
    rs.getString("liqvalor"),
    rs.getString("liqhistorico"),
    rs.getString("ctlsequencia"),fimLinha);		
}
pw.flush();
pw.close();
} catch (SQLException e) {
e.printStackTrace();
}
//Fim da Liquidação

String sqlel = 
"select lpad('"+cliente+"',6,'0') clicodigo, \r\n" + 
"				       exercicio loaano, \r\n" + 
"				       exercicioliq loaanoliq, \r\n" + 
"				       case when anodocorigem < exercicio then 2  \r\n" + 
"				       else 1 end liqtipo, \r\n" + 
"				       lpad(cast((select l.idlancamento from siscop.liquidacao l \r\n" + 
"				           where l.entidade = siscop.estornoliquidacao.entidade \r\n" + 
"				             and l.exercicio = siscop.estornoliquidacao.exercicio  \r\n" + 
"				             and l.anodocorigem = siscop.estornoliquidacao.anodocorigem \r\n" + 
"				             and l.nodocorigem = siscop.estornoliquidacao.nodocorigem \r\n" + 
"				             and l.noliquidacao = siscop.estornoliquidacao.noliquidacao) as varchar(10)),10,'0') liqsequencia, \r\n" + 
"				      lpad(cast(estorno as varchar(10)),10,'0') lessequencia, \r\n" + 
"				      cast(data as varchar(10)) lesdata, \r\n" + 
"				      lpad(cast(cast(valor as numeric(17,2)) as varchar(17)),17,'0') lesvalor, \r\n" + 
"				      case when motivo is null then 'hitorico não informado' else motivo end leshistorico, \r\n" + 
"				      lpad(cast(idlancamento as varchar(10)),10,'0') ctlsequencia \r\n" + 
"				 from siscop.estornoliquidacao \r\n" + 
"				   where entidade = ?  \r\n" + 
"				     and exercicio = ?  \r\n" + 
"				     and cast(substring(cast(data as varchar(10)),6,2)as numeric(10,0)) = ? order by data";

try {
PreparedStatement ps = connection.prepareStatement(sqlel);			

ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);

ResultSet rs = ps.executeQuery();	


File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\EstornoLiquidacao.txt");
PrintWriter pw = new PrintWriter(file);

while (rs.next()) {


String fimLinha = "\r\n";

pw.printf("%6s%4s%4s%1s%10s%10s%10s%17s%-500s%10s%4s", 
	rs.getString("clicodigo"), 
	rs.getString("loaano"), 
	rs.getString("loaanoliq"),
	rs.getString("liqtipo"),
	rs.getString("liqsequencia"),
	rs.getString("lessequencia"),
    rs.getString("lesdata"),
    rs.getString("lesvalor"),
    rs.getString("leshistorico"),
    rs.getString("ctlsequencia"),fimLinha);
}
pw.flush();
pw.close();
} catch (SQLException e) {
e.printStackTrace();
}
//Fim Estorno Liquidação

String sqlrl = "select lpad('"+cliente+"',6,'0') as clicodigo,  \r\n"
+ "       rl.exercicio as loaano,  \r\n"
+ "       case when rl.anodocorigem < rl.exercicio then 2  \r\n"
+ "       else 1 end as liqtipo,  \r\n"
+ "       lpad(cast(l.idlancamento as varchar(10)),10,'0') as liqsequencia,  \r\n"
+ "       3 as plntipoplano,  \r\n"
+ "       (select pc.ipmconta from intplanocontas pc\r\n"
+ "          where pc.entidade = '"+cliente+"' \r\n"
+ " 	        and pc.eloconta = rl.contacredito\r\n"
+ "            and pc.elocontacorrente = rl.idcontacorrente) plncodigo,  \r\n"
+ "       lpad(cast(cast(rl.valor as numeric(17,2)) as character varying(17)),17,'0')  rtlvalor, \r\n"
+ "       lpad(cast(rl.idlancamento as varchar(10)),10,'0') as ctlsequencia   \r\n"
+ "   from siscop.retencoesliquidacao rl   \r\n"
+ "   	 left join siscop.liquidacao l  \r\n"
+ "   			on rl.entidade = l.entidade   \r\n"
+ "   		   and rl.exercicio = l.exercicio  \r\n"
+ "   		   and rl.noliquidacao = l.noliquidacao  \r\n"
+ "   		   and rl.nodocorigem = l.nodocorigem  \r\n"
+ "   	   	   and rl.anodocorigem = l.anodocorigem  \r\n"
+ "   	where rl.entidade = ?  \r\n"
+ "      and rl.exercicio = ?  \r\n"
+ "      and cast(substring(cast(rl.datacontabilizado as varchar(10)),6,2) as numeric(10,0)) = ? ";

try  {
PreparedStatement ps = connection.prepareStatement(sqlrl);	


ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);

ResultSet rs = ps.executeQuery();	

File file = new File("c:\\integracao\\"+anoLoa+"\\" +mes+ "\\RetencaoLiquidacao.txt");

PrintWriter pw = new PrintWriter(file);

while (rs.next()) {

String fimLinha = "\r\n";

pw.printf("%6s%4s%1s%10s%1s%20s%17s%10s%4s", 
	rs.getString("clicodigo"), 
	rs.getString("loaano"), 
	rs.getString("liqtipo"),
	rs.getString("liqsequencia"),
	rs.getString("plntipoplano"),
	rs.getString("plncodigo"),
    rs.getString("rtlvalor"),
    rs.getString("ctlsequencia"),fimLinha);			
}
pw.flush();
pw.close();
} catch (SQLException e) {
e.printStackTrace();
}


//Fim Retenção Liquidação

String sqlerl = 
"select lpad('"+cliente+"',6,'0') clicodigo,   \r\n"
+ "       substring(cast(rl.dataestcontabilizado as varchar(10)),1,4) as loaano,  \r\n"
+ "       case when rl.anodocorigem < rl.exercicio then 2  \r\n"
+ "       else 1 end as liqtipo,  \r\n"
+ "       lpad(cast(rl.idlancamento as varchar(10)),10,'0') liqsequencia,  \r\n"
+ "       lpad(cast(rl.idlancamento as varchar(10)),10,'0') lessequencia, \r\n"
+ "       rl.exercicio loaanoret, \r\n"
+ "       cast(rl.dataestcontabilizado as varchar(10)) rledata, \r\n"
+ "       lpad(cast(cast(rl.valor as numeric(17,2))as varchar(17)),17,'0') rlevalor, \r\n"
+ "       3 as plntipoplano,  \r\n"
+ "       (select pc.ipmconta from intplanocontas pc\r\n"
+ "                        where pc.entidade = '"+cliente+"'\r\n"
+ "                          and pc.eloconta = rl.contacredito\r\n"
+ "                       and pc.elocontacorrente = rl.idcontacorrente) plncodigo \r\n"
+ " 	from siscop.retencoesliquidacao rl   \r\n"
+ "	    left join siscop.liquidacao l  \r\n"
+ "  	  		   on rl.entidade = l.entidade   \r\n"
+ "  			  and rl.exercicio = l.exercicio  \r\n"
+ "  			  and rl.noliquidacao = l.noliquidacao  \r\n"
+ "  			  and rl.nodocorigem = l.nodocorigem  \r\n"
+ "  			  and rl.anodocorigem = l.anodocorigem  \r\n"
+ "      where rl.entidade = ?  \r\n"
+ "        and rl.exercicio = ? \r\n"
+ "        and rl.dataestcontabilizado is not null  \r\n"
+ "        and cast(substring(cast(rl.datacontabilizado as varchar(10)),6,2)as numeric(10,0)) = ? \r\n"
+ "	  order by rl.datacontabilizado ";

try  {
PreparedStatement ps = connection.prepareStatement(sqlerl);	

ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);

ResultSet rs = ps.executeQuery();			

File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\EstornoRetencaoLiquidacao.txt");

PrintWriter pw = new PrintWriter(file);
while (rs.next()) {

String fimLinha = "\r\n";

pw.printf("%6s%4s%1s%10s%10s%4s%10s%17s%1s%20s%4s", 
	rs.getString("clicodigo"), 
	rs.getString("loaano"), 
	rs.getString("liqtipo"),
	rs.getString("liqsequencia"),
	rs.getString("lessequencia"),
	rs.getString("loaanoret"),
    rs.getString("rledata"),
    rs.getString("rlevalor"),
    rs.getString("plntipoplano"),
    rs.getString("plncodigo"),fimLinha);
}
pw.flush();
pw.close();
} catch (SQLException e) {
e.printStackTrace();
}

//Fim Estorno Retenção Liquidação

String sqlp = 
"(SELECT lpad('"+cliente+"',6,'0') clicodigo,    \r\n"
+ "        CP.EXERCICIO loaano,   \r\n"
+ "        CASE WHEN ICP.ANODOCORIGEM < ICP.EXERCICIO THEN 2    \r\n"
+ "        ELSE 1 END pagtipoemp,   \r\n"
+ "        lpad(cast(P.NOPAGAMENTO as varchar(10)),10,'0')pagnumero,   \r\n"
+ "        lpad(CAST(CASE WHEN CP.TIPODOCUMENTO = 'C' THEN '1'     \r\n"
+ "                       WHEN CP.TIPODOCUMENTO = 'B' THEN '3'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'O' THEN '10'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'N' THEN '10'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'D' THEN '10'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'E' THEN '5'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'F' THEN '8'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'Z' THEN '6'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'T' THEN '7'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'U' THEN '8'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'Y' THEN '9'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'X' THEN '10'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'K' THEN '11'     \r\n"
+ " 	                   ELSE '2'     \r\n"
+ " 	              END AS VARCHAR(50)),2,'0') pagtipo,   \r\n"
+ " 	   0 pagtiporetencao,   \r\n"
+ " 	   CAST(CP.DATALANCAMENTO AS VARCHAR(10)) pagdata,   \r\n"
+ " 	   rpad(CAST(CP.NRDOCUMENTO AS VARCHAR(15)),15,' ') pagdocumento,   \r\n"
+ " 	   lpad(CAST(CAST(ICP.VALOR AS NUMERIC(17,2)) AS VARCHAR(10)),17,'0') pagvalor,   \r\n"
+ " 	   case when CP.OBSERVACAO = '' then 'Historico não informado' else CP.OBSERVACAO end paghistorico,    \r\n"
+ " 	   cast(OPL.EXERCICIOLIQ as varchar(4)) loaanoliq,   \r\n"
+ " 	   CASE WHEN ICP.ANODOCORIGEM < ICP.EXERCICIO THEN 2   \r\n"
+ " 	   ELSE 1 END liqtipo,   \r\n"
+ " 	   lpad(cast((select l.idlancamento from siscop.liquidacao l \r\n"
+ "	                 where l.entidade = icp.entidade  \r\n"
+ " 					   and l.exercicio = icp.EXERCICIOLIQUIDACAO and l.noliquidacao = icp.nrliquidacao  \r\n"
+ " 					   and l.nodocorigem = icp.nrdocorigem \r\n"
+ "					   and l.anodocorigem = icp.anodocorigem ) as varchar(10)),10,'0') liqsequencia,   \r\n"
+ " 	   lpad('0',4,'0') loaanolix,   \r\n"
+ " 	   lpad('0',10,'0') lixsequencia,   \r\n"
+ " 	   lpad('3',1,'0') plntipoplano,\r\n"
+ "       (select pc.ipmconta from intplanocontas pc\r\n"
+ " 		  where pc.entidade = '"+cliente+"'\r\n"
+ " 		    and pc.eloexercicio = p.exercicio\r\n"
+ " 			and pc.eloconta = p.contadebito\r\n"
+ " 			and pc.elocontacorrente = p.idcontacorrente) plncodigo,   \r\n"
+ "	   lpad(cast(OPL.IDLANCAMENTO as varchar(10)),10,'0')ctlsequencia   \r\n"
+ " FROM SISCOP.CENTRALPAGAMENTOS CP     \r\n"
+ "  JOIN SISCOP.ITEMCENTRALPAGAMENTOS ICP     \r\n"
+ "    ON CP.ENTIDADE   = ICP.ENTIDADE     \r\n"
+ "   AND CP.EXERCICIO  = ICP.EXERCICIO     \r\n"
+ "   AND CP.LANCAMENTO = ICP.LANCAMENTO     \r\n"
+ "   AND ICP.TIPODOCORIGEM = 'O'   \r\n"
+ "  LEFT JOIN SISCOP.PAGAMENTO P     \r\n"
+ "    ON ICP.ENTIDADE      = P.ENTIDADE     \r\n"
+ "   AND ICP.EXERCICIO     = P.EXERCICIO     \r\n"
+ "   AND ICP.NOPAGAMENTO   = P.NOPAGAMENTO     \r\n"
+ "   AND ICP.TIPODOCORIGEM = 'O'     \r\n"
+ "  LEFT JOIN SISCOP.ORDEMPAGAMENTO OP     \r\n"
+ "    ON P.ENTIDADE          = OP.ENTIDADE     \r\n"
+ "   AND P.ANOORDEMPAGAMENTO = OP.EXERCICIO     \r\n"
+ "   AND P.NOORDEMPAGAMENTO  = OP.NOORDEM     \r\n"
+ "  LEFT JOIN SISCOP.FORNECEDOR F     \r\n"
+ "    ON OP.FORNECEDOR = F.FORNECEDOR     \r\n"
+ "  LEFT JOIN SISCOP.ORDEMPAGAMENTO_LIQUIDACAO OPL     \r\n"
+ "    ON OP.ENTIDADE   = OPL.ENTIDADE     \r\n"
+ "   AND OP.EXERCICIO = OPL.EXERCICIO     \r\n"
+ "   AND OP.NOORDEM   = OPL.NOORDEM    \r\n"
+ "  LEFT JOIN SISCOP.LANCAMENTOSEQUENCIA LS    \r\n"
+ "         ON LS.ENTIDADE = OPL.ENTIDADE    \r\n"
+ "        AND LS.ID       = OPL.IDLANCAMENTO     \r\n"
+ "  LEFT JOIN SISCOP.LANCAMENTOSEQUENCIA LSE    \r\n"
+ "         ON LSE.ENTIDADE = OPL.ENTIDADE    \r\n"
+ "        AND LSE.ID       = OPL.IDLANCAMENTOESTORNO    \r\n"
+ " WHERE CP.ENTIDADE  = ?  \r\n"
+ "   AND CP.EXERCICIO = ?  \r\n"
+ "   AND cast(substring(cast(CP.DATALANCAMENTO as varchar(10)),6,2)as numeric(10,0)) = ? order by  CP.DATALANCAMENTO) \r\n"
+ "union all  \r\n"
+ "(SELECT lpad('"+cliente+"',6,'0') clicodigo,    \r\n"
+ "        CP.EXERCICIO loaano,   \r\n"
+ "        3 pagtipoemp,   \r\n"
+ "        lpad(cast(P.NOPAGAMENTO as varchar(10)),10,'0')pagnumero,   \r\n"
+ "        lpad(CAST(CASE WHEN CP.TIPODOCUMENTO = 'C' THEN '1'     \r\n"
+ "                       WHEN CP.TIPODOCUMENTO = 'B' THEN '3'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'O' THEN '10'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'N' THEN '10'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'D' THEN '10'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'E' THEN '5'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'F' THEN '8'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'Z' THEN '6'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'T' THEN '7'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'U' THEN '8'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'Y' THEN '9'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'X' THEN '10'     \r\n"
+ " 	                   WHEN CP.TIPODOCUMENTO = 'K' THEN '11'     \r\n"
+ " 	                   ELSE '2'     \r\n"
+ " 	              END AS VARCHAR(50)),2,'0') pagtipo,   \r\n"
+ " 	    0 pagtiporetencao,   \r\n"
+ " 	    CAST(CP.DATALANCAMENTO AS VARCHAR(10)) pagdata,   \r\n"
+ " 	    rpad(CAST(CP.NRDOCUMENTO AS VARCHAR(15)),15,' ') pagdocumento,   \r\n"
+ " 	    lpad(CAST(CAST(ICP.VALOR AS NUMERIC(17,2)) AS VARCHAR(10)),17,'0') pagvalor,   \r\n"
+ " 	    case when CP.OBSERVACAO = '' then 'Historico não informado  na nota extra' else CP.OBSERVACAO end paghistorico,    \r\n"
+ " 	    lpad('0',4,'0') loaanoliq,   \r\n"
+ " 	    CASE WHEN ICP.ANODOCORIGEM < ICP.EXERCICIO THEN 2   \r\n"
+ " 	    ELSE 1 END liqtipo,   \r\n"
+ " 	    lpad('0',10,'0') liqsequencia,   \r\n"
+ " 	    lpad(cast(p.exercicionotaextra as varchar(10)),4,'0') loaanolix,   \r\n"
+ " 	    lpad(cast(p.nonotaextra as varchar(10)),10,'0') lixsequencia,   \r\n"
+ " 	    lpad('3',1,'0') plntipoplano, \r\n"
+ " 	    (select pc.ipmconta from intplanocontas pc\r\n"
+ "           where pc.entidade = '"+cliente+"'\r\n"
+ "             and pc.eloexercicio = p.exercicio\r\n"
+ "          	 and pc.eloconta = p.contacredito\r\n"
+ "         	 and pc.elocontacorrente = p.idcontacorrentecred) plncodigo,   \r\n"
+ "		lpad(cast(P.IDLANCAMENTO as varchar(10)),10,'0')ctlsequencia   \r\n"
+ "  FROM SISCOP.CENTRALPAGAMENTOS CP     \r\n"
+ "          JOIN SISCOP.ITEMCENTRALPAGAMENTOS ICP     \r\n"
+ "            ON CP.ENTIDADE   = ICP.ENTIDADE     \r\n"
+ "           AND CP.EXERCICIO  = ICP.EXERCICIO     \r\n"
+ "           AND CP.LANCAMENTO = ICP.LANCAMENTO     \r\n"
+ "           AND ICP.TIPODOCORIGEM = 'E'   \r\n"
+ "     LEFT JOIN SISCOP.PAGAMENTOEXTRA P     \r\n"
+ "            ON ICP.ENTIDADE      = P.ENTIDADE     \r\n"
+ "           AND ICP.EXERCICIO     = P.EXERCICIO     \r\n"
+ "           AND ICP.NOPAGAMENTO   = P.NOPAGAMENTO     \r\n"
+ "           AND ICP.TIPODOCORIGEM = 'E'   \r\n"
+ "     LEFT JOIN SISCOP.LANCAMENTOSEQUENCIA LS    \r\n"
+ "            ON LS.ENTIDADE = P.ENTIDADE    \r\n"
+ "           AND LS.ID       = P.IDLANCAMENTO      				          \r\n"
+ " WHERE CP.ENTIDADE  = ? \r\n"
+ "   AND CP.EXERCICIO = ?  \r\n"
+ "   AND cast(substring(cast(CP.DATALANCAMENTO as varchar(10)),6,2)as numeric(10,0)) = ? order by CP.DATALANCAMENTO) \r\n"
+ "union all \r\n"
+ "(select lpad('"+cliente+"',6,'0') as clicodigo,  \r\n"
+ "        rl.exercicio as loaano,  \r\n"
+ "        4 pagtipoemp,  \r\n"
+ "        lpad(cast(rl.idlancamento as varchar(10)),10,'0') as pagnumero,  \r\n"
+ "        cast(99 as varchar(2)) as pagtipo, \r\n"
+ "        0 pagtiporetencao, \r\n"
+ "        cast(rl.datacontabilizado as varchar(10)) pagdata, \r\n"
+ "        rpad('',15,' ')  pagdocumento, \r\n"
+ "        lpad(cast(cast(rl.valor as numeric(17,2)) as character varying(17)),17,'0')  pagvalor, \r\n"
+ "        'nao possui informaçao na retençao' paghistorico, \r\n"
+ "        cast(rl.exercicio as varchar(10)) loaanoliq, \r\n"
+ "        case when rl.anodocorigem < rl.exercicio then 2  \r\n"
+ "        else 1 end as liqtipo, \r\n"
+ "        lpad(cast(l.idlancamento as varchar(10)),10,'0') liqsequencia, \r\n"
+ "        lpad('0',4,'0') loaanolix, \r\n"
+ "        lpad('0',10,'0') lixsequencia, \r\n"
+ "        lpad('3',1,'0') plntipoplano,\r\n"
+ "        (select pc.ipmconta from intplanocontas pc\r\n"
+ "           where pc.entidade = '"+cliente+"'\r\n"
+ "		     and pc.ipmvinculo like '%94%') plncodigo,         \r\n"
+ "        lpad(cast(rl.idlancamento as varchar(10)),10,'0') as ctlsequencia   \r\n"
+ "    from siscop.retencoesliquidacao rl   \r\n"
+ "       left join siscop.liquidacao l  \r\n"
+ " 	     on rl.entidade = l.entidade   \r\n"
+ " 	    and rl.exercicio = l.exercicio  \r\n"
+ "   	    and rl.noliquidacao = l.noliquidacao  \r\n"
+ "  	    and rl.nodocorigem = l.nodocorigem  \r\n"
+ " 	    and rl.anodocorigem = l.anodocorigem  \r\n"
+ "    where rl.entidade = ? \r\n"
+ "      and rl.exercicio = ?  \r\n"
+ "      and cast(substring(cast(rl.datacontabilizado as varchar(10)),6,2) as numeric(10,0)) = ? order by rl.datacontabilizado)";
try  {
PreparedStatement ps = connection.prepareStatement(sqlp);			


ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);
ps.setInt(4,entidade);
ps.setInt(5,exercicio);
ps.setInt(6,dMes);
ps.setInt(7,entidade);
ps.setInt(8,exercicio);
ps.setInt(9,dMes);

ResultSet rs = ps.executeQuery();			

File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\Pagamento.txt");

PrintWriter pw = new PrintWriter(file);
while (rs.next()) {	

String fimLinha = "\r\n";

pw.printf("%6s%4s%1s%10s%2s%1s%10s%-15s%17s%-500s%4s%1s%10s%4s%10s%1s%20s%10s%4s", 
	rs.getString("clicodigo"), 
	rs.getString("loaano"), 
	rs.getString("pagtipoemp"),
	rs.getString("pagnumero"),
	rs.getString("pagtipo"),
	rs.getString("pagtiporetencao"),						
    rs.getString("pagdata"),
    rs.getString("pagdocumento"),
    rs.getString("pagvalor"),
    rs.getString("paghistorico"),
    rs.getString("loaanoliq"),
    rs.getString("liqtipo"),
    rs.getString("liqsequencia"),
    rs.getString("loaanolix"),
    rs.getString("lixsequencia"),
    rs.getString("plntipoplano"),
    rs.getString("plncodigo"),//18
    rs.getString("ctlsequencia"),fimLinha);
}
pw.flush();
pw.close();
} catch (SQLException e) {
e.printStackTrace();
}

//Fim Pagamento
String sqlep = "select lpad('"+cliente+"',6,'0') clicodigo, \r\n" + 
"       exercicio loaano, \r\n" + 
"       exercicio loaanopag, \r\n" + 
"       1 pagtipoemp, \r\n" + 
"       lpad(cast(nopagamento as varchar(10)),10,'0') pagnumero, \r\n" + 
"       lpad(cast(estorno as varchar(10)),10,'0') petsequencia, \r\n" + 
"       cast(data as varchar(10)) petdata, \r\n" + 
"       lpad(cast(cast(valor as numeric(17,2)) as varchar(10)),17,'0') petvalor,\r\n" + 
"       motivo pethistorico,\r\n" + 
"       lpad(cast(estorno as varchar(10)),10,'0') ctlsequencia       \r\n" + 
" from siscop.estornopagamento\r\n" + 
"  where entidade = ? \r\n" + 
"    and exercicio = ? \r\n" + 
"    and cast(substring(cast(data as varchar(10)),6,2)as numeric(10,0)) = ? order by data ";

try {
PreparedStatement ps = connection.prepareStatement(sqlep);	

ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);

ResultSet rs = ps.executeQuery();		

File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\EstornoPagamento.txt");

PrintWriter pw = new PrintWriter(file);
while (rs.next()) {	

String fimLinha = "\r\n";

pw.printf("%6s%4s%4s%1s%10s%10s%10s%17s%-500s%10s%4s", 
	rs.getString("clicodigo"), 
	rs.getString("loaano"), 
	rs.getString("loaanopag"),
	rs.getString("pagtipoemp"),
	rs.getString("pagnumero"),
	rs.getString("petsequencia"),
    rs.getString("petdata"),
    rs.getString("petvalor"),
    rs.getString("pethistorico"),
    rs.getString("ctlsequencia"),fimLinha);
}
pw.flush();


pw.close();
} catch (SQLException e) {
e.printStackTrace();
}

//Fim Estorno Pagamento

String sqlpr = 
"select lpad('"+cliente+"',6,'0') as clicodigo,   \r\n"
+ "       rl.exercicio as loaano,   \r\n"
+ "       4 pagtipoemp,   \r\n"
+ "       lpad(cast(rl.idlancamento as varchar(10)),10,'0') as pagnumero, \r\n"
+ "       lpad(cast(cast(rl.valor as numeric(17,2)) as character varying(17)),17,'0')  prtvalor,  \r\n"
+ "       3 plntipoplano,\r\n"
+ "       (select pc.ipmconta from intplanocontas pc\r\n"
+ "          where pc.entidade = '"+cliente+"'\r\n"
+ "            and pc.eloexercicio = rl.exercicio\r\n"
+ "            and pc.eloconta = rl.contacredito\r\n"
+ "            and pc.elocontacorrente = rl.idcontacorrente) plncodigo \r\n"
+ "   from siscop.retencoesliquidacao rl    \r\n"
+ "      left join siscop.liquidacao l   \r\n"
+ "	     on rl.entidade = l.entidade    \r\n"
+ "	    and rl.exercicio = l.exercicio   \r\n"
+ "  	    and rl.noliquidacao = l.noliquidacao   \r\n"
+ " 	    and rl.nodocorigem = l.nodocorigem   \r\n"
+ "	    and rl.anodocorigem = l.anodocorigem   \r\n"
+ "   where rl.entidade = ? \r\n"
+ "     and rl.exercicio = ?\r\n"
+ "     and cast(substring(cast(rl.datacontabilizado as varchar(10)),6,2) as numeric(10,0)) = ?";

try {
PreparedStatement ps = connection.prepareStatement(sqlpr);

ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);

ResultSet rs = ps.executeQuery();		

File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\PagamentoRetencao.txt");

PrintWriter pw = new PrintWriter(file);

while (rs.next()) {	

String fimLinha = "\r\n";

pw.printf("%6s%4s%1s%10s%17s%1s%20s%4s", 
	rs.getString("clicodigo"), 
	rs.getString("loaano"), 
	rs.getString("pagtipoemp"),
	rs.getString("pagnumero"),
	rs.getString("prtvalor"),
	rs.getString("plntipoplano"),
    rs.getString("plncodigo"),fimLinha);
}
pw.flush();		
pw.close();
} catch (SQLException e) {
e.printStackTrace();
}

//Fim Pagamento Retenção

String sqlepr = "select lpad('"+cliente+"',6,'0') clicodigo, \r\n" + 
"       exercicio loaano,\r\n" + 
"       (select exercicio from siscop.pagamentoextra p \r\n" + 
"          where p.entidade = siscop.estornopagamentoextra.entidade   \r\n" + 
"            and p.exercicio = siscop.estornopagamentoextra.exercicio \r\n" + 
"            and p.nopagamento = siscop.estornopagamentoextra.nopagamento) loaanopag,\r\n" + 
"       3 pagtipoemp,\r\n" + 
"       lpad(cast((select idlancamento from siscop.pagamentoextra p \r\n" + 
"          where p.entidade = siscop.estornopagamentoextra.entidade   \r\n" + 
"            and p.exercicio = siscop.estornopagamentoextra.exercicio \r\n" + 
"            and p.nopagamento = siscop.estornopagamentoextra.nopagamento) as varchar(10)),10,'0') pagnumero,\r\n" + 
"       lpad(cast(idlancamento as varchar(10)),10,'0') petsequencia,\r\n" + 
"       lpad(cast(cast(valor as numeric(17,2))as varchar(17)),17,'0') pervalor,\r\n" + 
"       3 plntipoplano,\r\n" + 
"       rpad('',20,'0')plncodigo\r\n" + 
"       from siscop.estornopagamentoextra\r\n" + 
"  where entidade = ? \r\n" + 
"    and exercicio = ? \r\n" + 
"    and cast(substring(cast(data as varchar(10)),6,2) as numeric(10,0)) = ? ";

try {
PreparedStatement ps = connection.prepareStatement(sqlepr);

ps.setInt(1,10);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);

ResultSet rs = ps.executeQuery();			

File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\EstornoPagamentoRetencao.txt");

PrintWriter pw = new PrintWriter(file);

while (rs.next()) {

String fimLinha = "\r\n";

pw.printf("%6s%4s%4s%1s%10s%10s%17s%1s%20s%4s", 
rs.getString("clicodigo"), 
rs.getString("loaano"), 
rs.getString("loaanopag"),
rs.getString("pagtipoemp"),
rs.getString("pagnumero"),
rs.getString("petsequencia"),
rs.getString("pervalor"),
rs.getString("plntipoplano"),
rs.getString("plncodigo"),fimLinha);
}
pw.flush();
pw.close();
} catch (SQLException e) {
e.printStackTrace();
}
//Fim Estorno Pagamento Extra
String sqlne = 
"select lpad('"+cliente+"',6,'0') clicodigo,   \r\n"
+ "       exercicio loaano, \r\n"
+ "       lpad(cast(noextraorcamentario as varchar(10)),10,'0') extnumero, \r\n"
+ "       1 exttipoorigem, \r\n"
+ "       cast(data as varchar(10)) extdataemissao, \r\n"
+ "       cast(data as varchar(10)) extdatavencto, \r\n"
+ "       lpad(cast(cast(valor as numeric(17,2)) as varchar(10)),17,'0') extvalor, \r\n"
+ "       complemento exthistorico, \r\n"
+ "       lpad('',10,' ') extdataestorno, \r\n"
+ "       0 extflagestorno, \r\n"
+ "       (select case when cnpj = '58460160904000' then '07890935000130' else cnpj end cnpj  from siscop.fornecedor where fornecedor = siscop.extraorcamentario.fornecedor ) unicpfcnpj, \r\n"
+ "       (select nome from siscop.fornecedor where fornecedor = siscop.extraorcamentario.fornecedor ) uninomerazao, \r\n"
+ "       3 plntipoplano, \r\n"
+ "       (select pc.ipmconta from intplanocontas pc\r\n"
+ "          where pc.entidade = '"+cliente+"'\r\n"
+ "            and pc.eloexercicio = siscop.extraorcamentario.exercicio\r\n"
+ "            and pc.eloconta = siscop.extraorcamentario.contadebito\r\n"
+ "            and pc.elocontacorrente = siscop.extraorcamentario.idcontacorrentedeb)  plncodigo, \r\n"
+ "       lpad('94',8,'0') vincodigo \r\n"
+ "  from siscop.extraorcamentario  \r\n"
+ "  where entidade = ?  \r\n"
+ "    and exercicio = ?  \r\n"
+ "    and cast(substring(cast(data as varchar(10)),6,2) as numeric(10,0)) = ?";

try {
PreparedStatement ps = connection.prepareStatement(sqlne);

ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);

ResultSet rs = ps.executeQuery();		


File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\NotaExtraorcamentaria.txt");

PrintWriter pw = new PrintWriter(file);
while (rs.next()) {

String fimLinha = "\r\n";

pw.printf("%6s%4s%10s%1s%10s%10s%17s%-500s%10s%1s%-14s%-200s%1s%20s%8s%4s", 
rs.getString("clicodigo"), 
rs.getString("loaano"), 
rs.getString("extnumero"),
rs.getString("exttipoorigem"),
rs.getString("extdataemissao"),
rs.getString("extdatavencto"),
rs.getString("extvalor"),
rs.getString("exthistorico"),
rs.getString("extdataestorno"),
rs.getString("extflagestorno"),
rs.getString("unicpfcnpj"),
rs.getString("uninomerazao"),
rs.getString("plntipoplano"),
rs.getString("plncodigo"),
rs.getString("vincodigo"),fimLinha);
}
pw.flush();

pw.close();
} catch (SQLException e) {
e.printStackTrace();
}

//Fim Nota Extra Orçamentario

String sqllnto = "select lpad('"+cliente+"',6,'0') clicodigo,  \r\n" + 
"		       exercicio loaano, \r\n" + 
"		       lpad(cast(noextraorcamentario as varchar(10)),10,'0')extnumero, \r\n" + 
"		       lpad(cast(noextraorcamentario as varchar(10)),10,'0') lixsequencia, \r\n" + 
"		       cast(data as varchar(10)) lixdata, \r\n" + 
"		       cast(data as varchar(10)) lixdatavencto, \r\n" + 
"		       lpad(cast(cast(valor as numeric(17,2)) as varchar(10)),17,'0') lixvalor, \r\n" + 
"		       complemento lixhistorico  \r\n" + 
"		       from siscop.extraorcamentario \r\n" + 
"		  where entidade = ?  \r\n" + 
"		    and exercicio = ?\r\n" + 
"		    and cast(substring(cast(data as varchar(10)),6,2) as numeric(10,0)) = ?";

try {
PreparedStatement ps = connection.prepareStatement(sqllnto);		

ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);

ResultSet rs = ps.executeQuery();			

File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\LiquidacaoNotaExtraorcamentaria.txt");

PrintWriter pw = new PrintWriter(file);
while (rs.next()) {	

String fimLinha = "\r\n";

pw.printf("%6s%4s%10s%10s%10s%10s%17s%-500s%4s", 
rs.getString("clicodigo"), 
rs.getString("loaano"), 
rs.getString("extnumero"),
rs.getString("lixsequencia"),
rs.getString("lixdata"),
rs.getString("lixdatavencto"),
rs.getString("lixvalor"),
rs.getString("lixhistorico"),fimLinha);
}
pw.flush();


pw.close();
} catch (SQLException e) {
e.printStackTrace();
}

//Fim Liquidação Extra orçamentaria 

String sqlreo = 
"select lpad('"+cliente+"',6,'0') clicodigo,  \r\n"
+ "       lc.exercicio loaano, \r\n"
+ "       lpad(cast(lc.idlancamentodebito as varchar(10)),10,'0') rxtsequencia, \r\n"
+ "       cast(lc.data as varchar(10)) rxtdata, \r\n"
+ "       lpad(cast(cast(lc.valor as numeric(17,2)) as varchar(17)),17,'0') rxtvalor, \r\n"
+ "       lc.complemento rxthistorico, \r\n"
+ "       lpad('',2,'0') rxttipodocumento, \r\n"
+ "       lc.nrodocumentoorigem rxtnrodocumento, \r\n"
+ "       3 plntipoplanobanco, \r\n"
+ "	   (select pc.ipmconta from intplanocontas pc\r\n"
+ "	      where pc.entidade = '"+cliente+"'\r\n"
+ "	        and pc.eloexercicio = lc.exercicio\r\n"
+ "		    and pc.eloconta = lc.contadebito\r\n"
+ "		    and pc.elocontacorrente = lc.idcontacorrentedebito) plncodigobanco, \r\n"
+ "       3 plntipoplano, \r\n"
+ "	   (select pc.ipmconta from intplanocontas pc\r\n"
+ "          where pc.entidade = '"+cliente+"'\r\n"
+ "            and pc.eloexercicio = lc.exercicio\r\n"
+ "            and pc.eloconta = lc.contacredito\r\n"
+ "            and pc.elocontacorrente = lc.idcontacorrentecredito) plncodigo, \r\n"
+ "	   (select pc.ipmvinculo from intplanocontas pc\r\n"
+ "          where pc.entidade = '"+cliente+"'\r\n"
+ "            and pc.eloexercicio = lc.exercicio\r\n"
+ "            and pc.eloconta = lc.contadebito\r\n"
+ "            and pc.elocontacorrente = lc.idcontacorrentedebito) vincodigo, \r\n"
+ "       lpad(cast(lc.idlancamentodebito as varchar(10)),10,'0') ctlsequencia  \r\n"
+ "     from siscop.lancamentoscontabeis lc\r\n"
+ "  where lc.entidade = ?  \r\n"
+ "    and lc.exercicio = ?  \r\n"
+ "    and cast(substring(cast(lc.data as varchar(10)),6,2) as numeric(10,0)) = ?  \r\n"
+ "    and lc.grupoevento = 60 \r\n"
+ "    and lc.evento = 1000000";
try {
PreparedStatement ps = connection.prepareStatement(sqlreo);


ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);

ResultSet rs = ps.executeQuery();	

File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\ReceitaExtraorcamentaria.txt");

PrintWriter pw = new PrintWriter(file);
while (rs.next()) {	

String fimLinha = "\r\n";

pw.printf("%6s%4s%10s%10s%17s%-500s%2s%-15s%1s%20s%1s%20s%8s%10s%4s", 
rs.getString("clicodigo"), 
rs.getString("loaano"), 
rs.getString("rxtsequencia"),
rs.getString("rxtdata"),
rs.getString("rxtvalor"),
rs.getString("rxthistorico"),
rs.getString("rxttipodocumento"),
rs.getString("rxtnrodocumento"),
rs.getString("plntipoplanobanco"),
rs.getString("plncodigobanco"),
rs.getString("plntipoplano"),
rs.getString("plncodigo"),
rs.getString("vincodigo"),
rs.getString("ctlsequencia"),fimLinha);
}
pw.flush();


pw.close();
} catch (SQLException e) {
e.printStackTrace();
}

//Fim Receita Extraorcamentaria	

String sqlero =
"select lpad('\"+cliente+\"',6,'0') clicodigo,  \r\n"
+ "               lc.exercicio loaano, \r\n"
+ "               lpad(cast(lc.idlancamentodebito as varchar(10)),10,'0') rxtsequencia, \r\n"
+ "               cast(lc.data as varchar(10)) rxtdata, \r\n"
+ "               lpad(cast(cast(lc.valor as numeric(17,2)) as varchar(17)),17,'0') rxtvalor, \r\n"
+ "               lc.complemento rxthistorico, \r\n"
+ "               lpad('',2,'0') rxttipodocumento, \r\n"
+ "               lc.nrodocumentoorigem rxtnrodocumento, \r\n"
+ "               3 plntipoplanobanco, \r\n"
+ "        	   (select pc.ipmconta from intplanocontas pc\r\n"
+ "        	      where pc.entidade = '"+cliente+"'\r\n"
+ "        	        and pc.eloexercicio = lc.exercicio\r\n"
+ "        		    and pc.eloconta = lc.contadebito\r\n"
+ "        		    and pc.elocontacorrente = lc.idcontacorrentedebito) plncodigobanco, \r\n"
+ "               3 plntipoplano, \r\n"
+ "        	   (select pc.ipmconta from intplanocontas pc\r\n"
+ "                  where pc.entidade = '"+cliente+"'\r\n"
+ "                    and pc.eloexercicio = lc.exercicio\r\n"
+ "                    and pc.eloconta = lc.contacredito\r\n"
+ "                    and pc.elocontacorrente = lc.idcontacorrentecredito) plncodigo, \r\n"
+ "        	   (select pc.ipmvinculo from intplanocontas pc\r\n"
+ "                  where pc.entidade = '"+cliente+"'\r\n"
+ "                    and pc.eloexercicio = lc.exercicio\r\n"
+ "                    and pc.eloconta = lc.contadebito\r\n"
+ "                    and pc.elocontacorrente = lc.idcontacorrentedebito) vincodigo, \r\n"
+ "               lpad(cast(lc.idlancamentodebito as varchar(10)),10,'0') ctlsequencia \r\n"
+ "   from siscop.lancamentoscontabeis lc\r\n"
+ "          where lc.entidade = ?  \r\n"
+ "            and lc.exercicio = ?  \r\n"
+ "            and cast(substring(cast(lc.data as varchar(10)),6,2) as numeric(10,0)) = ? \r\n"
+ "            and lc.grupoevento = 60 \r\n"
+ "            and lc.evento = 100 ";//Fazer estorno Receita Extra Orçamentária
try {
PreparedStatement ps = connection.prepareStatement(sqlero);

ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);

ResultSet rs = ps.executeQuery();	

File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\EstornoReceitaExtraOrcamentaria.txt");

PrintWriter pw = new PrintWriter(file);
while (rs.next()) {
/*	
pw.printf("%6s%4s%1s%10s%10s%2s%17s%-500s%2s%15s%1s%20s%1s%20s%8s%10s\n", 
rs.getString("clicodigo"), 
rs.getString("loaano"), 
//rs.getString("rectipo"),
rs.getString("recsequencia"),
rs.getString("recdata"),
rs.getString("rectipodeducao"),
rs.getString("recvalor"),
rs.getString("rechistorico"),
rs.getString("rectipodocumento"),
rs.getString("recnrodocumento"),
rs.getString("plntipoplanobanco"),
rs.getString("plncodigobanco"),
rs.getString("plntipoplano"),
rs.getString("plncodigo"),
rs.getString("vincodigo"),
rs.getString("ctlsequencia"));
*/
}
pw.flush();


pw.close();
} catch (SQLException e) {
e.printStackTrace();
}
//Fim Estorno Receita Orcamentaria	

String sqltf = 
"select  lpad('"+cliente+"',6,'0') clicodigo,   \r\n"
+ "        lc.exercicio loaano,  \r\n"
+ "        lpad(cast(lc.id as varchar(10)),10,'0') trnsequencia,  \r\n"
+ "        case when lc.grupoevento = 60 and lc.evento = 100 then 4   \r\n"
+ "             when lc.grupoevento = 60 and lc.evento = 2 then 1  \r\n"
+ "             when lc.grupoevento = 60 and lc.evento = 1 then 2  \r\n"
+ "        else 3 end trntipo,  \r\n"
+ "        cast(lc.data as varchar(10)) trndata,  \r\n"
+ "        lpad(cast(cast(lc.valor as numeric(17,2)) as varchar(17)),17,'0') trnvalor,  \r\n"
+ "        lpad('',10,' ') trnhistorico,  \r\n"
+ "        '09' trntipodocumento,  \r\n"
+ "        lpad('',10,' ') trnnrodocumento,  \r\n"
+ "        3 plntipoplano,\r\n"
+ " 	   (select pc.ipmconta from intplanocontas pc\r\n"
+ "                  where pc.entidade = '"+cliente+"'\r\n"
+ "                    and pc.eloexercicio = lc.exercicio\r\n"
+ "                 and pc.eloconta = lc.contadebito\r\n"
+ "                 and pc.elocontacorrente = lc.idcontacorrentedebito) plncodigobanco, \r\n"
+ " 	  (select pc.ipmconta from intplanocontas pc\r\n"
+ "                  where pc.entidade = '"+cliente+"'\r\n"
+ "                    and pc.eloexercicio = lc.exercicio\r\n"
+ "                 and pc.eloconta = lc.contacredito\r\n"
+ "                 and pc.elocontacorrente = lc.idcontacorrentecredito)  plncodigo,  \r\n"
+ "        lpad((select pc.ipmvinculo from intplanocontas pc\r\n"
+ "                       where pc.entidade = '"+cliente+"'\r\n"
+ "                         and pc.eloexercicio = lc.exercicio\r\n"
+ "                      and pc.eloconta = lc.contadebito\r\n"
+ "                      and pc.elocontacorrente = lc.idcontacorrentedebito),8,'0') vincodigo,  \r\n"
+ "        lpad(cast(lc.idlancamentodebito as varchar(10)),10,'0')ctlsequencia         \r\n"
+ "  from siscop.lancamentoscontabeis  lc   \r\n"
+ "      where lc.entidade = ? \r\n"
+ "        and lc.exercicio = ? \r\n"
+ "        and lc.grupoevento = 60 \r\n"
+ "        and lc.evento = 10 \r\n"
+ "        and cast(substring(cast(lc.data as varchar(10)),6,2) as numeric(10,0)) = ?";
try {
PreparedStatement ps = connection.prepareStatement(sqltf);			

ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);

ResultSet rs = ps.executeQuery();			

File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\TransferenciaFinanceira.txt");

PrintWriter pw = new PrintWriter(file);
while (rs.next()) {

//int vTrnHistorico = rs.getString("trnhistorico").length();	
String vTrnHistorico = "";
if (rs.getString("trnhistorico") == null) {
vTrnHistorico = "";	
} else {
vTrnHistorico = rs.getString("trnhistorico");
}
String fimLinha = "\r\n";
pw.printf("%6s%4s%10s%1s%10s%17s%-500s%2s%-15s%1s%-20s%-20s%8s%10s%4s", 
rs.getString("clicodigo"), 
rs.getString("loaano"), 
rs.getString("trnsequencia"),
rs.getString("trntipo"),
rs.getString("trndata"),
rs.getString("trnvalor"),
rs.getString("trnhistorico"),
rs.getString("trntipodocumento"),
rs.getString("trnnrodocumento"),
rs.getString("plntipoplano"),
rs.getString("plncodigobanco"),
rs.getString("plncodigo"),
rs.getString("vincodigo"),
rs.getString("ctlsequencia"),fimLinha);
}
pw.flush();
pw.close();
} catch (SQLException e) {
e.printStackTrace();
}
//Fim Transferencias Financeiras




String sqllan = 
"select clicodigo,  \r\n"
+ "       loaano,  \r\n"
+ "	   lpad(cast((select 4736+coalesce(count(*),0) as valor from siscop.eventoslancados el  \r\n"
+ "		            left join siscop.eventoslancadosconta elc  \r\n"
+ "		                   on el.entidade = elc.entidade   \r\n"
+ "		                  and el.exercicio = elc.exercicio  \r\n"
+ "		                  and el.tipoevento = elc.tipoevento  \r\n"
+ "		                  and el.grupoevento = elc.grupoevento   \r\n"
+ "		                  and el.evento = elc.evento  \r\n"
+ "		                  and el.nrolancamento = elc.nrolancamento  \r\n"
+ "		             where el.entidade = ?  \r\n"
+ "		               and el.exercicio = ?  \r\n"
+ "		               and cast(substring(cast(el.data as varchar(10)),6,2) as numeric(10,0)) <= ?-1)+ \r\n"
+ "					   row_number ()over (order by clicodigo) as varchar(10)),10,'0')  \r\n"
+ "	   ctlsequencia, lansequencia, landata, lanhistorico,   \r\n"
+ "	   lanvalor,  \r\n"
+ "	   lanflagestorno,  \r\n"
+ "	   tlccodigo,  \r\n"
+ "	   plntipoplano,  \r\n"
+ "	   plncodigo, \r\n"
+ "	   plntipoplanocontra,  \r\n"
+ "	   plncodigocontra,  \r\n"
+ "	   vincodigo from(\r\n"
+ "select lpad('"+cliente+"',6,'0') clicodigo,     \r\n"
+ "	          e.exercicio loaano,    \r\n"
+ "	          0 ctlsequencia,    \r\n"
+ "	          lpad(cast(ec.nrolancamento as varchar(10)),10,'0') lansequencia,    \r\n"
+ "	          cast(e.data as varchar(10)) landata,    \r\n"
+ "	          e.nrodocumentoorigem lanhistorico,    \r\n"
+ "	          lpad(cast(cast(case when ec.debcred = 'C' then (ec.valor)*-1 else ec.valor end as numeric(17,2))as varchar(17)),17,'0') lanvalor,    \r\n"
+ "	          0 lanflagestorno,    \r\n"
+ "	          lpad(cast(case when ec.grupoevento = 15 then 17     \r\n"
+ "                 when ec.grupoevento = 17 then 100      \r\n"
+ "                 when ec.grupoevento in (20,21,22) then 7     \r\n"
+ "                 when ec.grupoevento in (30,31,32) then 8     \r\n"
+ "                 when ec.grupoevento in (40,41) then 9     \r\n"
+ "                 when ec.grupoevento = 50 then 2     \r\n"
+ "                 when ec.grupoevento = 50 and ec.evento = 2 then 13     \r\n"
+ "                 when ec.grupoevento = 60 and ec.evento = 1 then 26     \r\n"
+ "                 when ec.grupoevento = 60 and ec.evento in (3,5) then 11     \r\n"
+ "                 when ec.grupoevento = 60 and ec.evento = 100 then 35     \r\n"
+ "                 when ec.grupoevento = 70 then 3     \r\n"
+ "                 when ec.grupoevento = 71 then 4     \r\n"
+ "                 when ec.grupoevento = 80 then 99     \r\n"
+ "                 when ec.grupoevento = 81 then 98     \r\n"
+ "                 when ec.grupoevento = 85 then 98    \r\n"
+ "            else 11 end as varchar(3)),3,'0')  tlccodigo,    \r\n"
+ "  3 plntipoplano,   \r\n"
+ "  (select pc.ipmconta from intplanocontas pc\r\n"
+ " 		                  where pc.entidade = '"+cliente+"'\r\n"
+ " 		                    and pc.eloexercicio = ec.exercicio\r\n"
+ " 			                and pc.eloconta = ec.conta\r\n"
+ " 			                and pc.elocontacorrente = ec.idcontacorrente) plncodigo,    \r\n"
+ "  lpad('',1,' ') plntipoplanocontra,    \r\n"
+ "  lpad('',20,' ') plncodigocontra,  \r\n"
+ "  (select lpad(cast(coalesce((select ipmvinculo \r\n"
+ "      from intfonterecurso ifr where ifr.entidade = '"+cliente+"' and ifr.elofonterecurso in (cast(pc.ipmvinculo as numeric(8,0)))),0) as varchar(8)),8,'0')  from intplanocontas pc\r\n"
+ " 		                  where pc.entidade = '"+cliente+"'\r\n"
+ " 		                    and pc.eloexercicio = ec.exercicio\r\n"
+ " 			                and pc.eloconta = ec.conta\r\n"
+ " 			                and pc.elocontacorrente = ec.idcontacorrente)vincodigo         \r\n"
+ " from siscop.eventoslancados e    \r\n"
+ "  left join siscop.eventoslancadosconta ec    \r\n"
+ "         on e.entidade = ec.entidade     \r\n"
+ "        and e.exercicio = ec.exercicio    \r\n"
+ "        and e.tipoevento = ec.tipoevento    \r\n"
+ "        and e.grupoevento = ec.grupoevento     \r\n"
+ "        and e.evento = ec.evento    \r\n"
+ "        and e.nrolancamento = ec.nrolancamento    \r\n"
+ "   where e.entidade = ?    \r\n"
+ "     and e.exercicio = ?    \r\n"
+ "     and cast(substring(cast(e.data as varchar(10)),6,2) as numeric(10,0)) = ?)x";

try {
PreparedStatement ps = connection.prepareStatement(sqllan);

ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);
ps.setInt(4,entidade);
ps.setInt(5,exercicio);
ps.setInt(6,dMes);

ResultSet rs = ps.executeQuery();		


File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\Lancamento.txt");

PrintWriter pw = new PrintWriter(file);



while (rs.next()) {	

String fimLinha = "\r\n";

pw.printf("%6s%4s%10s%10s%10s%-500s%17s%1s%3s%1s%20s%1s%20s%8s%4s", 
rs.getString("clicodigo"), 
rs.getString("loaano"), 
rs.getString("ctlsequencia"),
rs.getString("lansequencia"),
rs.getString("landata"),
rs.getString("lanhistorico"),
rs.getString("lanvalor"),
rs.getString("lanflagestorno"),
rs.getString("tlccodigo"),
rs.getString("plntipoplano"),
rs.getString("plncodigo"),
rs.getString("plntipoplanocontra"),
rs.getString("plncodigocontra"),
rs.getString("vincodigo"),fimLinha);
}
pw.flush();

pw.close();

} catch (SQLException e) {
e.printStackTrace();
}

//Fim Lancamento
//Receita Orçamentária
String sqlRec =
"select lpad('"+cliente+"',6,'0') clicodigo,  \r\n"
+ "       lpad(cast(r.exercicio as varchar(4)),4,'')loaano, \r\n"
+ "       '1' as rectipo, \r\n"
+ "       lpad(cast(r.idlancamento as varchar(10)),10,'0') recsequencia, \r\n"
+ "       lpad(cast(data as varchar(10)),10,'0') recdata, \r\n"
+ "       lpad('0',2,'0') as rectipodeducao, \r\n"
+ "       lpad(cast(cast(r.valor as numeric(17,2)) as varchar(17)),17,'0') recvalor, \r\n"
+ "       rpad(cast(r.historico as varchar(500)),500,' ') rechistorico, \r\n"
+ "       lpad('1',2,'0') rectipodocumento, \r\n"
+ "       rpad(cast(r.nrodocumento as varchar(15)),15,' ') recnrodocumento, \r\n"
+ "       3 plntipoplanobanco,\r\n"
+ "	   (select pc.ipmconta from intplanocontas pc\r\n"
+ "	                  where pc.entidade = '"+cliente+"'\r\n"
+ "	                    and pc.eloexercicio = r.exercicio\r\n"
+ "		                and pc.eloconta = r.contacontabil\r\n"
+ "		                and pc.elocontacorrente = r.idcontacorrente) plncodigobanco, \r\n"
+ "       1 plntipoplano, \r\n"
+ "       rpad(r.receita,20,'0') plncodigo, \r\n"
+ "       lpad(cast(rr.fonterecurso as varchar(8)),8,'0') vincodigo, \r\n"
+ "       lpad(cast(r.idlancamento as varchar(10)),10,'0') ctlsequencia \r\n"
+ "  from siscop.realizacaoreceita  r\r\n"
+ "      inner join siscop.realizacaoreceitaitem rr\r\n"
+ "              on r.entidade = rr.entidade \r\n"
+ "             and r.exercicio = rr.exercicio \r\n"
+ "             and r.lancamento = rr.lancamento\r\n"
+ "  where r.entidade = ?  \r\n"
+ "    and r.exercicio = ?  \r\n"
+ "    and cast(substring(cast(r.data as varchar(10)),6,2) as numeric(10,0)) = ? ";

try {
PreparedStatement ps = connection.prepareStatement(sqlRec);


ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);

ResultSet rs = ps.executeQuery();		


File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\ReceitaOrcamentaria.txt");

PrintWriter pw = new PrintWriter(file);

while (rs.next()) {	
String fimLinha = "\r\n";


pw.printf("%6s%4s%1s%10s%10s%2s%17s%-500s%2s%-15s%1s%20s%1s%20s%8s%10s%4s", 
rs.getString("clicodigo"), 
rs.getString("loaano"), 
rs.getString("rectipo"),
rs.getString("recsequencia"),
rs.getString("recdata"),
rs.getString("rectipodeducao"),
rs.getString("recvalor"),
rs.getString("rechistorico"),
rs.getString("rectipodocumento"),
rs.getString("recnrodocumento"),
rs.getString("plntipoplanobanco"),
rs.getString("plncodigobanco"),
rs.getString("plntipoplano"),
rs.getString("plncodigo"),
rs.getString("vincodigo"),
rs.getString("ctlsequencia"),fimLinha);

}
pw.flush();

pw.close();

} catch (SQLException e) {
e.printStackTrace();
}

//Fim Receita Orçamentária

//Estorno Receita Orçamentária
String sqlEstRec =
"select lpad('"+cliente+"',6,'0') clicodigo,  \r\n"
+ "       lpad(cast(r.exercicio as varchar(4)),4,'') loaano, \r\n"
+ "       lpad(cast(r.idlancamento as varchar(10)),10,'0') recsequencia, \r\n"
+ "       lpad(cast(r.idlancamento as varchar(10)),10,'0') esrsequencia, \r\n"
+ "       1 esrtipo, \r\n"
+ "       lpad(cast(data as varchar(10)),10,'0') esrdataestorno, \r\n"
+ "       lpad(cast(cast(r.valor as numeric(17,2)) as varchar(17)),17,'0') esrvalor,\r\n"
+ "       rpad(cast(r.historico as varchar(500)),500,' ') esrhistorico, \r\n"
+ "       lpad('1',2,'0') esrtipodocumento, \r\n"
+ "       rpad(cast(r.nrodocumento as varchar(15)),15,' ') esrnrodocumento, \r\n"
+ "       2 plntipoplanobanco,\r\n"
+ "	   (select pc.ipmconta from intplanocontas pc\r\n"
+ "	                  where pc.entidade = '"+cliente+"'\r\n"
+ "	                    and pc.eloexercicio = r.exercicio\r\n"
+ "		                and pc.eloconta = r.contacontabil\r\n"
+ "		                and pc.elocontacorrente = r.idcontacorrente) plncodigobanco, \r\n"
+ "       2 plntipoplano, \r\n"
+ "       rpad(r.receita,20,'0') plncodigo, \r\n"
+ "       lpad((select cast(ipmvinculo as varchar(8)) from intfonterecurso ifr where ifr.entidade = '"+cliente+"' and ifr.elofonterecurso = rr.fonterecurso),8,'0') vincodigo, \r\n" //rr.fonterecurso
+ "       lpad(cast(r.idlancamento as varchar(10)),10,'0') ctlsequencia \r\n"
+ "  from siscop.realizacaoreceita  r\r\n"
+ "      inner join siscop.realizacaoreceitaitem rr\r\n"
+ "              on r.entidade = rr.entidade \r\n"
+ "             and r.exercicio = rr.exercicio \r\n"
+ "             and r.lancamento = rr.lancamento\r\n"
+ "  where r.entidade = ?  \r\n"
+ "    and r.exercicio = ?  \r\n"
+ "    and cast(substring(cast(r.data as varchar(10)),6,2) as numeric(10,0)) = ? \r\n"
+ "	  and r.estorno = 'S' ";

try {
PreparedStatement ps = connection.prepareStatement(sqlEstRec);


ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);

ResultSet rs = ps.executeQuery();		


File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\EstornoReceitaOrcamentaria.txt");

PrintWriter pw = new PrintWriter(file);

while (rs.next()) {	
String fimLinha = "\r\n";


pw.printf("%6s%4s%10s%10s%1s%10s%17s%-500s%2s%-15s%1s%20s%1s%20s%8s%10s%4s", 
rs.getString("clicodigo"), 
rs.getString("loaano"), 
rs.getString("esrtipo"),
rs.getString("recsequencia"),
rs.getString("recdata"),
rs.getString("rectipodeducao"),
rs.getString("recvalor"),
rs.getString("rechistorico"),
rs.getString("rectipodocumento"),
rs.getString("recnrodocumento"),
rs.getString("plntipoplanobanco"),
rs.getString("plncodigobanco"),
rs.getString("plntipoplano"),
rs.getString("plncodigo"),
rs.getString("vincodigo"),
rs.getString("ctlsequencia"),fimLinha);

}
pw.flush();

pw.close();

} catch (SQLException e) {
e.printStackTrace();
}

//Fim Estorno Receita Orçamentária

//Alteração Orçamentária
String sqlaltorc =
"select lpad('"+cliente+"',6,'0') clicodigo, \r\n"
+ "       lpad(cast(di.exercicio as varchar(4)),4,'0') loaano,\r\n"
+ "	   lpad(cast(10+row_number ()over (order by d.exercicio)as varchar(6)),6,'0') alonumero,\r\n"
+ "	   cast(d.data as varchar(10)) alodata,\r\n"
+ "	   lpad(cast(10+row_number ()over (order by d.exercicio)as varchar(6)),6,'0') faosequencia,\r\n"
+ "	   1 faotipo,\r\n"
+ "	   1 faotipofonte,\r\n"
+ "	   1 faotipocredito,\r\n"
+ "	   case when di.operacao = 'R' then (lpad(cast((cast(di.valor as numeric(17,2))*-1)as varchar(17)),17,'0'))\r\n"
+ "	   else lpad(cast(cast(di.valor as numeric(17,2))as varchar(17)),17,'0') end faovalor,\r\n"
+ "	   lpad(substring(di.despesa,1,2),2,'0') orgcodigo,\r\n"
+ "	   lpad(substring(di.despesa,3,2),3,'0') undcodigo,	   \r\n"
+ "	   lpad(substring(di.despesa,5,2),4,'0') tfccodigo,\r\n"
+ "	   lpad(substring(di.despesa,7,3),4,'0') tsfcodigo,\r\n"
+ "	   lpad(substring(di.despesa,10,4),4,'0') pgrcodigo,\r\n"
+ "	   lpad('1',2,'0') acotipo,\r\n"
+ "	   lpad(substring(di.despesa,15,4),4,'0') acocodigo,\r\n"
+ "	   rpad(substring(di.despesa,19,6),20,'0') plncodigo,\r\n"
+ "	   lpad(cast(di.fonterecurso as varchar(8)),8,'0') vincodigo,\r\n"
+ "	   lpad(cast(3+row_number ()over (order by d.exercicio)as varchar(10)),10,'0') ctlsequencia\r\n"
+ " from siscop.decreto d\r\n"
+ "   inner join siscop.itemdecreto di\r\n"
+ "           on d.entidade = di.entidade\r\n"
+ "		  and d.exercicio = di.exercicio\r\n"
+ "		  and d.decreto = di.decreto\r\n"
+ "  where d.entidade = ?\r\n"
+ "    and d.exercicio = ?\r\n"
+ "	and cast(substring(cast(d.data as varchar(10)),6,2) as numeric(10,0)) = ? ";

try {
PreparedStatement ps = connection.prepareStatement(sqlaltorc);


ps.setInt(1,entidade);
ps.setInt(2,exercicio);
ps.setInt(3,dMes);

ResultSet rs = ps.executeQuery();		


File file = new File("c:\\integracao\\" + anoLoa + "\\" + mes + "\\AteracaoOrcamentaria.txt");

PrintWriter pw = new PrintWriter(file);

while (rs.next()) {	
String fimLinha = "\r\n";


pw.printf("%6s%4s%6s%10s%6s%1s%1s%1s%17s%2s%3s%4s%4s%4s%2s%4s%20s%8s%10s%4s", 
rs.getString("clicodigo"), 
rs.getString("loaano"), 
rs.getString("alonumero"),
rs.getString("alodata"),
rs.getString("faosequencia"),
rs.getString("faotipo"),
rs.getString("faotipofonte"),
rs.getString("faotipocredito"),
rs.getString("faovalor"),
rs.getString("orgcodigo"),
rs.getString("undcodigo"),
rs.getString("tfccodigo"),
rs.getString("tsfcodigo"),
rs.getString("pgrcodigo"),
rs.getString("acotipo"),
rs.getString("acocodigo"),
rs.getString("plncodigo"),
rs.getString("vincodigo"),
rs.getString("ctlsequencia"),fimLinha);

}
pw.flush();

pw.close();

} catch (SQLException e) {
e.printStackTrace();
}
		 

//Fim Alteração Orçamentária


		// Início da compactação em Zip
				try {
				    FileOutputStream fos = new FileOutputStream("c:\\integracao\\"+anoLoa+"\\"+meses+".zip");
				          
				    ZipOutputStream zipOut = new ZipOutputStream( fos );
				      
				    File pasta = new File("c:\\integracao\\"+anoLoa+"\\"+mes);
				    for(File arq : pasta.listFiles() ){
				        zipOut.putNextEntry( new ZipEntry( arq.getName().toString() ) );
				                  
				        FileInputStream fis = new FileInputStream( arq );
				                  
				        int content;
				        while ((content = fis.read()) != -1) {
				            zipOut.write( content );
				        }
				                  
				        zipOut.closeEntry();		                  
				    }
				          
				    zipOut.close();
				          
				} catch (IOException e) {
				    e.printStackTrace();
				}
				
				String sqldelete = "delete from intarquivoupload where descricao = ? and ano = '2020'";
				try {
						PreparedStatement ps = connection.prepareStatement(sqldelete);
					
					ps.setString(1, meses);
					ps.execute();
					ps.close();
			    } catch (SQLException ex) {
			        ex.printStackTrace();
			    }
				
				String sqlsalvar = "INSERT INTO intarquivoupload(entidade,descricao,ano,mes,arquivo) VALUES (?,?,?,?,?)";
				try {
						PreparedStatement ps = connection.prepareStatement(sqlsalvar);			
					 
			 
			        //converte o objeto file em array de bytes
			        File f = new File("c:\\integracao\\"+anoLoa+"\\"+meses+".zip");
			        
			        InputStream is = new FileInputStream( f );
			        byte[] bytes = new byte[(int)f.length() ];
			        int offset = 0;
			        int numRead = 0;
			        while (offset < bytes.length
			               && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
			            offset += numRead;
			        }
			        ano = "2020";
			        int cliente1 = Integer.valueOf(cliente);
			        
			        ps.setInt( 1, cliente1 );
			        ps.setString( 2, meses );
			        ps.setString( 3, ano);
			        ps.setString( 4, mes );
			        ps.setBytes( 5, bytes );
			        ps.execute();
			        ps.close();
			 
			    } catch (SQLException ex) {
			        ex.printStackTrace();
			    } catch (IOException ex) {
			        ex.printStackTrace();
			   }
        
				return true;
		}
		return false;
	}
		
	
	private String ParseInt(String string) {
		// TODO Auto-generated method stub
		return null;
	}


	public File getFile(String descricao ){
		String user = "postgres";
		String senha = "jrb@nc0Post";
		String dataBase = "fterraroxaatual";
		
	    String sql = "SELECT id,descricao,ano,mes,arquivo FROM intarquivoupload WHERE descricao = ?";
	    
	    File f = null;
	    
	    try  (Connection conn = DriverManager.getConnection(
				"jdbc:postgresql://127.0.0.1:5432/"+dataBase,user,senha);
				PreparedStatement ps = conn.prepareStatement(sql)){
	        ps.setString(1, descricao);
	        ResultSet rs = ps.executeQuery();
	        if ( rs.next() ){
	            byte [] bytes = rs.getBytes("arquivo");
	            String nome = rs.getString("descricao");
	            String ano = rs.getString("ano");
	            
	 
	            //converte o array de bytes em file
	            f = new File( "c:\\integracao\\" +nome+".zip" );
	            FileOutputStream fos = new FileOutputStream( f);
	            fos.write( bytes );
	            fos.close();
	        }
	        rs.close();
	        ps.close();
	        conn.close();
	        return f;
	} catch (SQLException ex) {
	ex.printStackTrace();
	}
	catch (IOException ex) {
	ex.printStackTrace();
	}
	return null;
	}
	
	public List<Upload> listarTodos(String nome){
		String user = "postgres";
		String senha = "jrb@nc0Post";
		String dataBase = "fterraroxaatual";
		
		String sql = "select * from intarquivoupload where descricao = ? ";
		
		List<Upload> lista = new ArrayList<Upload>();
		try  (Connection conn = DriverManager.getConnection(
				"jdbc:postgresql://127.0.0.1:5432/"+dataBase,user,senha);
				PreparedStatement ps = conn.prepareStatement(sql)){
	        ps.setString(1, nome);
	        ResultSet rs = ps.executeQuery();
	        while ( rs.next() ){
	            Upload upload = new Upload();
	            upload.setId(rs.getInt("id"));
	            upload.setDescricao(rs.getString("descricao"));
	            upload.setAno(rs.getString("ano"));
	            upload.setMes(rs.getString("mes"));
	            upload.setArquivo(rs.getBytes("arquivo"));
	           lista.add(upload);    
	        }
	        rs.close();
	        ps.close();
	        conn.close();
	       
	} catch (SQLException ex) {
	ex.printStackTrace();
	}
		return lista;
	}
	
	///XXXXXXXX------- Inicio Seguencia   ----XXXXXXXXXX\\\\\\
	public void sequencia(int entidade, int exercicio, int dMes) {
	String user = "postgres";
	String senha = "jrb@nc0Post";
	String dataBase = "fterraroxaatual";
	
	String sqlsequencia = "select count(*) as valor from siscop.eventoslancados el\r\n" + 
			"   left join siscop.eventoslancadosconta elc\r\n" + 
			"     on el.entidade = elc.entidade \r\n" + 
			"    and el.exercicio = elc.exercicio\r\n" + 
			"    and el.tipoevento = elc.tipoevento\r\n" + 
			"    and el.grupoevento = elc.grupoevento \r\n" + 
			"    and el.evento = elc.evento\r\n" + 
			"    and el.nrolancamento = elc.nrolancamento\r\n" + 
			"   where el.entidade = ?\r\n" + 
			"     and el.exercicio = ?\r\n" + 
			"     and cast(substring(cast(el.data as varchar(10)),6,2) as numeric(10,0)) = ? ";
	
	try (Connection conn1 = DriverManager.getConnection(
			"jdbc:postgresql://127.0.0.1:5432/"+dataBase,user,senha);
			PreparedStatement ps1 = conn1.prepareStatement(sqlsequencia)){			
		
		ps1.setInt(1,entidade);
		ps1.setInt(2,exercicio);
		ps1.setInt(3,dMes);
		
		ResultSet rs1 = ps1.executeQuery();			
		while (rs1.next()) {	
		int sequencia = Integer.parseInt(rs1.getString("valor"));
		
		}
	} catch (SQLException ex) {
		ex.printStackTrace();
		}
	}
	
}
