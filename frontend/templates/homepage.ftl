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
        Olá, ${username}! Bem vindo de volta.
    </#if>
</div>

<div id="login-logout-actions">
    <br>
    <div>
        <p>
            Adote um pet aqui!
        </p>
        <div>
            <a href="/pets"> <button type="button"> Pets </button></a>
        </div>
    </div>
    <br>

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
