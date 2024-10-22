# Análise de Utilização de Elevadores

## 📝 Descrição
Este projeto implementa um sistema de análise de utilização de elevadores em um prédio, permitindo identificar os andares menos utilizados através do processamento de dados de uso armazenados em formato JSON.

## 🎯 Funcionalidades
- Contabilização de acessos por andar
- Identificação de andares menos utilizados
- Análise de padrões de utilização
- Suporte a múltiplos andares com mesma frequência de uso

## 🔧 Estrutura do Projeto
```
src/
├── main/
│   └── java/
│       └── com/elevator/
│           ├── model/
│           │   └── ElevatorAnalysis.java
│           └── utils/
│               └── JsonParser.java
└── test/
    └── java/
        └── com/elevator/
            └── ElevatorAnalysisTest.java
```

## 💻 Tecnologias Utilizadas
- Java 11+
- Jackson (para processamento JSON)
- JUnit 5 (para testes)

## 📊 Formato dos Dados
O sistema espera dados no seguinte formato JSON:
```json
[
  {
    "andar": 1,
    "timestamp": "2024-02-20T10:30:00",
    "outro_campo": "valor"
  }
]
```

## 🚀 Como Usar

### Pré-requisitos
- JDK 11 ou superior
- Maven ou Gradle

### Configuração
1. Clone o repositório
```bash
git clone https://github.com/seu-usuario/elevator-analysis.git
```

2. Instale as dependências
```bash
mvn install
```

### Exemplo de Uso
```java
ElevatorAnalysis analysis = new ElevatorAnalysis(jsonData);
List menosUtilizados = analysis.andarMenosUtilizado();
```

## 🧪 Testes

### Executando os Testes
```bash
mvn test
```

### Exemplo de Teste
```java
@Test
void deveRetornarAndarMenosUtilizado() {
    // given
    String jsonData = "[{\"andar\": 1}, {\"andar\": 2}, {\"andar\": 2}]";
    ElevatorAnalysis analysis = new ElevatorAnalysis(jsonData);

    // when
    List andaresMenosUtilizados = analysis.andarMenosUtilizado();

    // then
    assertEquals(Collections.singletonList("1"), andaresMenosUtilizados);
}
```

## 📈 Lógica do Método Principal

O método `andarMenosUtilizado()` funciona da seguinte forma:

1. Calcula o total de andares no prédio
2. Inicializa um mapa para contar utilizações por andar
3. Processa os dados JSON para contabilizar acessos
4. Identifica o menor número de utilizações
5. Retorna lista de andares com utilização mínima

```java
public List andarMenosUtilizado() {
    Double numeroDeAndares = totalDeAndares();

    // Inicializa o mapa de contagem
    Map porteiro = new HashMap<>();
    for (var i = 0; i <= numeroDeAndares; i++) {
        porteiro.put(String.valueOf(i), 0);
    }

    // Conta as utilizações de cada andar
    for (Map andarAtual : this.apisulJson) {
        String andar = String.valueOf(((Double) andarAtual.get("andar")).intValue());
        porteiro.put(andar, porteiro.get(andar) + 1);
    }

    // Encontra o valor mínimo de utilizações
    Integer menorValor = porteiro.values().stream()
        .min(Integer::compareTo)
        .orElse(0);

    // Retorna todos os andares que têm o número mínimo de utilizações
    return porteiro.entrySet().stream()
        .filter(entry -> entry.getValue().equals(menorValor))
        .map(Map.Entry::getKey)
        .collect(Collectors.toList());
}
```

## ⚠️ Tratamento de Erros
O sistema inclui tratamento para:
- Dados JSON inválidos
- Valores nulos
- Conversões numéricas
- Andares inexistentes

## 🤝 Contribuindo
1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some changes'`)
4. Push para a branch (`git push origin feature/changes`)
5. Abra um Pull Request

## 📝 Notas de Versão
- 1.0.0
    - Implementação inicial
    - Suporte a múltiplos andares menos utilizados
    - Adição de testes unitários

## 👥 Créditos

### Empresa
Este projeto foi desenvolvido como parte de um teste técnico proposto pela [Apisul](https://www.apisul.com.br/).

### Desenvolvedor
* **Rudiney Liemes** - *Implementação da Solução* - [rudineyliemes](https://github.com/rudineyliemes)

### Agradecimentos
* Equipe Apisul pelo desafio proposto
* Um agradecimento especial ao Marcus Witthoeft por todo suporte, mentoria e contribuições valiosas durante o desenvolvimento deste teste

## 📄 Licença
Este projeto está sob a licença MIT - veja o arquivo [LICENSE.md](LICENSE.md) para detalhes.
