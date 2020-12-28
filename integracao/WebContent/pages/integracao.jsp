<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h1>Integração de Bases</h1>
	<div>
		<form action="IntegracaoServlet" method="POST">
			<div>
				
				<select name="entidade">
					<option value="326">Terra Roxa</option>
					<option value="8787">Arapongas</option>
					<option value="33333">Guaíra</option>
				</select>
				<P>
			</div>
			<div>
			    <select name="exercicio">
					<option value="2020">2020</option>
					<option value="2019">2019</option>
					<option value="2018">2018</option>
					<option value="2017">2017</option>
					<option value="2016">2016</option>
					<option value="2015">2015</option>
					<option value="2014">2014</option>
					<option value="2013">2013</option>
				</select>
			</div>
			<div>
			    <select name="mes">
					<option value="1">Janeiro</option>
					<option value="2">Fevereiro</option>
					<option value="3">Marco</option>
					<option value="4">Abril</option>
					<option value="5">Maio</option>
					<option value="6">Junho</option>
					<option value="7">Julho</option>
					<option value="8">Agosto</option>
					<option value="9">Setembro</option>
					<option value="10">Outubro</option>
					<option value="11">Novembro</option>
					<option value="12">Dezembro</option>
				</select>
			</div>
	</div>
	<p>
		<input type="submit" value="Gerar">
	</form>

</body>
</html>