<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Pagina inicial PetBox</title>
</head>
<body>
<h1>Seja Bem-vindo ao PetBox!</h1>
<div id="welcome-message">
    <#if isUserLoggedIn>
        Olá, ${username}!
    <#else>
        <p>
        Por favor, faça login, ou registre-se.
        </p>
    </#if>
</div>

<div id="login-logout-actions">
    <#if isUserLoggedIn>

        <div id="register-pet">
            <a href="/register-pet"> <button type="button">Cadastrar pet:</button></a>
        </div>
        <br>
        <form action="/logout" method="post">
            <button type="submit">Logout</button>
        </form>
    <#else>
            <a href="/login"><button type="submit">Login:</button></a>
            <br>
            <p>
                Não possui cadastro?
            </p>
            <a href="/register-user"><button type="button">Registre-se:</button></a>
    </#if>
</div>
