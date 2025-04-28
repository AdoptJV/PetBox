<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lista de Pets</title>
</head>
<body>
<h1>Pets disponíveis</h1>
<ul>
    <#list pets as pet>
        <li>
            <strong>Nome:</strong> ${pet.name} <br>
            <strong>Espécie:</strong> ${pet.species} <br>
            <strong>Idade:</strong> ${pet.age} anos <br>
            <strong>Sexo:</strong> ${pet.sex} <br>
            <strong>Castrado:</strong> ${pet.castrated?string("Sim", "Não")} <br>
            <hr>
        </li>
    </#list>
</ul>
</body>
</html>
