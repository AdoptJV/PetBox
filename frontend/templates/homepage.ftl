<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Pagina inicial PetBox</title>
    <link rel="stylesheet" href="/css/home.css">
</head>
<body>
<div class="logo">
    <img alt="Logo PetBox" src="/assets/bigLogo.svg">
</div>
<div class="titlebox">
    <#if isUserLoggedIn>
        Olá, ${username}! Bem-vindo(a) de volta.
    <#else>
        Olá! Seja bem-vindo(a) ao PetBox!
    </#if>
    <div><a href="/pets">
            <button type="button"> Pets</button>
        </a></div>
    <#if isUserLoggedIn>
        Registre um pet!<br>
        <div>
            <a href="/register-pet">
                <button type="button">Cadastrar pet</button>
            </a>
            <form action="/logout" method="post">
                <button type="submit">Logout</button>
            </form>
        </div>
    <#else>
        Entre ou faça seu cadastro!<br>
        <div>
            <a href="/login">
                <button type="button">Login</button>
            </a>
            <a href="/register-user">
                <button type="button">Registre-se</button>
            </a>
        </div>
    </#if>
</div>
