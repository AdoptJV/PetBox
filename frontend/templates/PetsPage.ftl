<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lista de Pets</title>
    <link rel="stylesheet" href="/css/pets.css">
</head>
<body>
<div class="titlebox">Esses pets estão à busca de um lar!</div>
<ul>
    <#list pets as pet>
        <li class="pets">
            <strong>Nome:</strong> ${pet.name} <br>
            <strong>Espécie:</strong> ${pet.species} <br>
            <strong>Idade:</strong> ${pet.age} anos <br>
            <strong>Sexo:</strong> ${pet.sex} <br>
            <strong>Castrado:</strong> ${pet.castrated?string("Sim", "Não")} <br>
        </li>
    </#list>
</ul>
</body>
</html>
