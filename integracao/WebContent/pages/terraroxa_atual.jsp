<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<form action="ArapongasServlet" method="post">
		<h1>Integração Câmara de Terra Roxa com Município de Terra Roxa</h1>
		<p>
			<label>Entidade: </label> <select name="entidade">
				<option value="326">Terra Roxa</option>
			</select>
		<p>
			<label>Exercício: </label> <select name="exercicio">
				<option value="2020">2020</option>
				<option value="2019">2019</option>
				<option value="2018">2018</option>
				<option value="2017">2017</option>
				<option value="2016">2016</option>
				<option value="2015">2015</option>
				<option value="2014">2014</option>
				<option value="2013">2013</option>
			</select>
		<p>
			<label>Mês: </label> <select name="mes">
				<option value="01">Janeiro</option>
				<option value="02">Fevereiro</option>
				<option value="03">Março</option>
				<option value="04">Abril</option>
				<option value="05">Maio</option>
				<option value="06">Junho</option>
				<option value="07">Julho</option>
				<option value="08">Agosto</option>
				<option value="09">Setembro</option>
				<option value="10">Outubro</option>
				<option value="11">Novembro</option>
				<option value="12">Dezembro</option>
			</select>
		<p>
			<input type="submit" value="Gerar Arquivos">
	</form>

</body>
</html>