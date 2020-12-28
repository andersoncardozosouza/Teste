<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h1>Login de Integração</h1>
	<form action="pages/" method="post">
		<p>
			<label>Login: </label> <input type="text" name="login"
				placeholder="Digite o Login"></input>
		<p>
			<label>Senha: </label> <input type="password" name="senha"
				placeholder="Digite a senha"></input>
		<p>
			<label>Entidade: </label> <select name="entidade">
				<option value="8787">Arapongas</option>
				<option value="326">Terra Roxa</option>
				<option value="12565">Guaíra</option>
			</select>
		<p>
			<input type="submit" value="Logar">
		</p>

	</form>

</body>
</html>