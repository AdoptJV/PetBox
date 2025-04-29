<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login</title>
    <link rel="stylesheet" href="/css/register.css">
</head>
<body>
    <#if failedRegister>
        <div class="failedRegister">
            <p> Registration failed!!!!</p>
        </div>
    </#if>
    <form method="post" action="/register-user" enctype="multipart/form-data" class="formbox">
        <div class="titlebox">
            <h1 style="text-align: center">Registro de usuário</h1> <br>
            <div>
                <label for="name">Nome Completo:</label>
                <input type="text" id="name" name="name" required/>
            </div>
            <br>
            <div>
                <label for="username">Nome de usuário:</label>
                <input type="text" id="username" name="username" required/>
            </div>
            <br>
            <div>
                <label for="password">Senha:</label>
                <input type="password" id="password" name="password" required/>
                <label for="password">Confirmar Senha:</label>
                <input type="password" id="password" name="password" required/>
            </div>
            <br>

            <div>
                <label for="birthday">Data de nascimento:</label>
                <input type="date" id="birthday" name="birthday" required/>
            </div>
            <br>

            <div>
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" required/>
                <label for="phone number">Número de telefone:</label>
                <input type="tel" id="phone number" name="phone" required/>
            </div>
            <br>

            <div>
                <label for="cep">CEP:</label>
                <input type="text" id="cep" name="cep" required/>
            </div>
            <br>

            <div>
                <label for="description">Descrição (Opcional):</label>
                <textarea id="description" name="description" rows="4" cols="50"></textarea>
            </div>
            <br>

            <div class="button">
                <input type="submit" value="Registre">
            </div>
        </div>
    </form>
</body>
</html>