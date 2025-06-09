# PetBox

  Esse projeto envolve a criação de um site cujo objetivo é facilitar o processo de adoção
de bichinhos, tanto para ONGs quanto para usuários individuais. O objetivo é implementar uma
interface acessível e eficiente para os usuários visando ajudar os animais desabrigados
a encontrarem um lar.

  Criaremos um sistema de busca com filtros, um algoritmo de recomendação baseado
na localização de cada usuário e um quadro de postagens, onde usuários podem publicar
informações sobre animais de rua que encontrarem e ONGs podem requisitar auxílios e divulgar
campanhas. Além disso, um sistema de chat entre os usuários para possibilitar a comunicação
sem a necessidade de uso de aplicativos externos.

  Os usuários e as ONGs podem adicionar informações a seus perfis de modo que se tornem mais
confiáveis para o processo de adoção. Também podem ocorrer acompanhamentos no perfil dos
usuários sobre os bichinhos que adotaram e das ONGs sobre os bichinhos disponíveis para a adoção.

# Como rodar?

  Por enquanto, a execução ocorre com um servidor local na sua máquina (localhost). Primeiramente, suba a API do backend utilizando
  
  ```./gradlew run```
  
  no diretório PetBox. A API é hosteada na porta 8080, acessível pelo broswer em localhost:8080/api.
  
  Então, rode o frontend utilizando
  
  ```npm run dev```
  
  no diretório PetBox/react-frontend. O frontend é hosteado na porta 5173, acessível em localhost:5173/login (é a landing page).
  Basta criar um usuário e faça o login com as credenciais utilizadas para ganhar acesso ao website.

# Tecnologias utilizadas:
    - Servidor API backend em Ktor (framework de Kotlin)
    - Frontend em React + Vite (com bootstrap)
    - Banco de dados em SQLite
