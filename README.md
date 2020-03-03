
# Introdução

  

Esse projeto serve de base para o curso de java a ser ministrado no [Women Can Code](http://conteudo.sharerh.com/womencancodebrasil).

  

# Estrutura

  

O progresso esperado em cada dia da semana está disponibilizado em um branch especifico no GIT. Isso permite que as alunas aproveitem todos os dias do curso mesmo quando tenham empacado em quaisquer tarefas pedidas durante o curso. Abaixo segue a lista dos branches referente à cada dia da semana, bem como os principais temas abordados.

  

- 2a Feira
    -  [Aula](https://github.com/bsofiato/wcc-project/tree/master)
    -  [Tarefa](https://github.com/bsofiato/wcc-project/tree/tarefas/segunda)

- 3a Feira
    -  [Aula](https://github.com/bsofiato/wcc-project/tree/aulas/terca)
    -  [Tarefa](https://github.com/bsofiato/wcc-project/tree/tarefas/terca)

- 4a Feira

    -  [Aula](https://github.com/bsofiato/wcc-project/tree/aulas/quarta)
    -  [Tarefa](https://github.com/bsofiato/wcc-project/tree/tarefas/quarta)

- 5a Feira

    -  [Aula](https://github.com/bsofiato/wcc-project/tree/aulas/quinta)
    -  [Tarefa](https://github.com/bsofiato/wcc-project/tree/tarefas/quinta)

- 6a Feira

    -  [Aula](https://github.com/bsofiato/wcc-project/tree/aulas/sexta)
    -  [Tarefa](https://github.com/bsofiato/wcc-project/tree/tarefas/sexta)

- Sábado

    -  [Aula](https://github.com/bsofiato/wcc-project/tree/aulas/sabado)
    -  [Término](https://github.com/bsofiato/wcc-project/tree/termino)


# Pre-requisitos

  

- 4Gb de RAM

- 60Gb de HD

-  [VirtualBox](https://www.virtualbox.org/)

-  [Vagrant](https://www.vagrantup.com/)

  

# Inicialização

Esse projeto utiliza uma maquina virtual [Vagrant](https://www.vagrantup.com/) para desenvolvimento. No momento do provisionamento dessa maquina virtual, é construido um ambiente com pré-configurado, como todos as ferramentas necessárias para a realização do curso. Entre essas ferramentas, encontram-se:

-  [open-jdk](https://openjdk.java.net/) - JDK open-source
-  [maven](http://maven.apache.org/) - Ferramenta para compilação
-  [intellij](https://www.jetbrains.com/idea/) - IDE para desenvolvimento
-  [dbeaver](https://dbeaver.io/) - IDE para acesso a base de dados
-  [postman](https://www.postman.com/) - Ferramenta para chamada de APIs REST
-  [git-cola](https://git-cola.github.io/) - Cliente gráfico para o GIT
-  [docker](https://www.docker.com/) - Plataforma para execução de containers (como se fossem VMs lights)
-  [docker-compose](https://docs.docker.com/compose/) - Gerencia e orquestra os containers da aplicação


Para incializar o ambiente de desenvolvimento, basta executar `vagrant up` no prompt de comando (o comando pode demorar um pouquinho). O comando `vagrant halt`, por sua vez, desliga o ambiente de desenvolvimento. Para ligá-lo novamente, basta executar `vagrant up` novamente. Pode-se também destruir o ambiente de desenvolvimento através do comando `vagrant destroy`.

  

# Desenvolvendo

## Compilação

  

Dentro da máquina vagrant. Rodar o comando `mvn package` no diretório `/home/vagrant/project`. Esse processo rodará os testes automatizados e gerará a imagem docker contendo o projeto.

  

## Execução

  

Após a compilação, bata rodar o comando `docker-compose up` no diretório `/home/vagrant/project/etc/docker`. Esse comando sobe todos o projeto e suas dependências (i.e. banco de dados e o servidor de autenticação).

## Perfis de acesso

### Grupos

Os usuários estâo distribuidos nos seguintes grupos:

|Grupo| Descrição |
|--|--|
| Supervisores | Supervisores têm acesso à todas as operações relativas à veículos. No que tange operações de entrada e saída. Supervisores podem somente visualizar entradas e saídas e cancelar entradas. |
| Operadores | Operadores não podem realizar qualquer tipo de manipulação de veículos (somente a visualização). Quanto a entrada e saida, operadores podem visualizar e criar tanto entradas quanto saídas. O cancelamento de entrada porém é vetado. |

### Personas

Para faciliar o dialogo durante o desenvolvimento, foram desenvolvidas 5 [personas]([https://pt.wikipedia.org/wiki/Persona_(marketing)](https://pt.wikipedia.org/wiki/Persona_(marketing))). Abaixo encontram-se as personas definidas:

| Nome | Login | Senha | Perfil |
|--|--| -- | -- |
| Maria  | maria  | x  | Operadora |
| Davi| davi  | x  | Operador |
| Renata| renata  | x  | Operadora |
| Jéssica| jessica  | x  | Supervisora |
| Calebe| calebe  | x  | Supervisor |
