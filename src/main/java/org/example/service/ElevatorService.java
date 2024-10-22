package org.example.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.repository.IElevadorService;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class ElevatorService implements IElevadorService {

    private List<Map<String, Object>> apisulJson;

    public ElevatorService(String caminhoJson) {
        Reader in = new InputStreamReader(getClass().getResourceAsStream("/input.json"));
        Gson gson = new Gson();
        Type typeJson = new TypeToken<List<Map<String, Object>>>() {
        }.getType();
        this.apisulJson = gson.fromJson(in, typeJson);
    }

    @Override
    public List<String> andarMenosUtilizado() {
        Double numeroDeAndares = totalDeAndares();

        Map<String, Integer> porteiro = new HashMap<>();
        for (var i = 0; i <= numeroDeAndares; i++) porteiro.put(String.valueOf(i), 0);

        for (Map<String, Object> andarAtual : this.apisulJson) {
            String andar = String.valueOf(((Double) andarAtual.get("andar")).intValue());// Convertendo para String
            porteiro.put(andar, porteiro.get(andar) + 1);
        }

        // Encontra o valor mínimo de utilizações
        Integer menorValor = porteiro.values().stream()
                .min(Integer::compare)
                .orElse(0);

        // Retorna todos os andares que têm o número mínimo de utilizações
        return porteiro.entrySet().stream()
                .filter(f -> f.getValue().equals(menorValor))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> elevadorMaisFrequentado() {
        Map<String, Integer> todosElevadores = totalDeElevadores();

        int maxRegistros = Collections.max(todosElevadores.values());

        List<String> maisFrequentados = todosElevadores.entrySet().stream()
                .filter(e -> e.getValue().equals(maxRegistros))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        return maisFrequentados;
    }

    @Override
    public List<String> periodoMaiorFluxoElevadorMaisFrequentado() {
        Map<String, Integer> totalTurnos = totalDeTurnos();

        int maxTurnos = Collections.max(totalTurnos.values());

        List<String> maioresTurnos = totalTurnos.entrySet().stream()
                .filter(e -> e.getValue().equals(maxTurnos))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        Map<String, Integer> elevadorFrequentadoManha = new HashMap<>();
        apisulJson.stream()
                .filter(registro -> Objects.equals(registro.get("turno"), maioresTurnos.get(0)))
                .forEach(registro -> {
                    String elevador = (String) registro.get("elevador");
                    elevadorFrequentadoManha.put(elevador, elevadorFrequentadoManha.getOrDefault(elevador, 0) + 1);
                });

        int maxRegistros = Collections.max(elevadorFrequentadoManha.values());

        List<String> maisFrequentadoPorTurno = elevadorFrequentadoManha.entrySet().stream()
                .filter(e -> e.getValue().equals(maxRegistros))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        String resposta = "Turno mais frequentado: " + maioresTurnos.get(0) + ", Elevador mais utilizado por turno: " + maisFrequentadoPorTurno.get(0);

        return List.of(resposta);
    }

    @Override
    public List<String> elevadorMenosFrequentado() {
        Map<String, Integer> totalElevadores = new HashMap<>();
        for (Map<String, Object> registros : apisulJson) {
            String elevadores = (String) registros.get("elevador");
            totalElevadores.put(elevadores, totalElevadores.getOrDefault(elevadores, 0) + 1);
        }
        int minRegistro = Collections.min(totalElevadores.values());

        List<String> minFrequentado = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : totalElevadores.entrySet()) {
            if (entry.getValue().equals(minRegistro)) {
                minFrequentado.add(entry.getKey());
            }
        }
        return minFrequentado;
    }

    @Override
    public List<Character> periodoMenorFluxoElevadorMenosFrequentado() {
        // Elevador menos frequentado
        List<String> elevadoresMesnosFreq = elevadorMenosFrequentado();

        // Map para armazenar a contagem de cada turnopara os elevadores menos frenquetado
        Map<String, Integer> turnosPorElevador = new HashMap();

        // Para cada elevador menos frequentado, contamos as ocorrências por turno
        for (Map<String, Object> registro : apisulJson) {
            String elevador = (String) registro.get("elevador");
            String turno = (String) registro.get("turno");

            if (elevadoresMesnosFreq.contains(elevador)) {
                turnosPorElevador.put(turno, turnosPorElevador.getOrDefault(turno, 0) + 1);
            }
        }

        // Se não houver registros, retornamos uma lista vazia
        if (turnosPorElevador.isEmpty()) {
            return new ArrayList<>();
        }

        // Encontramos  (menor fluxo)
        int menorFluxo = Collections.min(turnosPorElevador.values());

        // Coletamos todos os turnos que têm esse menor fluxo
        return turnosPorElevador.entrySet().stream()
                .filter(f -> f.getValue() == menorFluxo)
                .map(f -> f.getKey().charAt(0))
                .collect(Collectors.toList());
    }

    @Override
    public List<Character> periodoMaiorUtilizacaoConjuntoElevadores() {
        //Map para contar a frequêmcia de cada turno
        Map<String, Integer> turnosFrequencia = new HashMap<>();

        //Ocorrência de cada turno
        for (Map<String, Object> registros : apisulJson) {
            String turno = (String) registros.get("turno");
            turnosFrequencia.put(turno, turnosFrequencia.getOrDefault(turno, 0) + 1);
        }

        //Se não houver registro, retorna vazio
        if (turnosFrequencia.isEmpty()) {
            return new ArrayList<>();
        }

        //Encontra o maior valor (maior utilização)
        int maiorFluxo = Collections.max(turnosFrequencia.values());

        return turnosFrequencia.entrySet().stream()
                .filter(f -> f.getValue() == maiorFluxo)
                .map(f -> f.getKey().charAt(0))
                .collect(Collectors.toList());
    }

    @Override
    public float percentualDeUsoElevadorA() {

        return calcularPercentualUsoElevador("A");
    }

    @Override
    public float percentualDeUsoElevadorB() {

        return calcularPercentualUsoElevador("B");
    }

    @Override
    public float percentualDeUsoElevadorC() {

        return calcularPercentualUsoElevador("C");
    }

    @Override
    public float percentualDeUsoElevadorD() {

        return calcularPercentualUsoElevador("D");
    }

    @Override
    public float percentualDeUsoElevadorE() {

        return calcularPercentualUsoElevador("E");
    }

    //Método para calcular o percentual de uso de um elevador específico
    private float calcularPercentualUsoElevador(String elevador) {

        long viagensElevador = apisulJson.stream()
                .filter(registro -> elevador.equals(registro.get("elevador")))
                .count();

        long totalViagens = apisulJson.size();

        if (totalViagens == 0) {
            System.out.printf("Percentual de uso do elevador %s: 0%%%n", elevador);
            return 0;
        }

        // Calcula o percentual de uso do elevador
        float percentual = (viagensElevador / (float) totalViagens) * 100;

        // Arredonda para duas casas decimais
        percentual = Math.round(percentual * 100) / 100.0f;

        // Imprime a frase com o resultado
        System.out.printf("Percentual de uso do elevador %s: %.2f%%%n", elevador, percentual);

        // Retorna o valor percentual arredondado
        return percentual;
    }

    private Map<String, Integer> totalDeElevadores() {
        return apisulJson.stream()
                .collect(Collectors.groupingBy(
                        registro -> (String) registro.get("elevador"),
                        Collectors.summingInt(registro -> 1)
                ));
    }

    private double totalDeAndares() {
        return apisulJson.stream()
                .mapToDouble(registro -> (double) registro.get("andar"))// mapeia elementos e converte para double
                .max()// encontra o valor máximo
                .orElse(0.0); //retorna 0.0 caso esteja vazio, se não houver elementos no json
    }

    private Map<String, Integer> totalDeTurnos() {
        return apisulJson.stream()
                .collect(Collectors.groupingBy(
                        registro -> (String) registro.get("turno"),
                        Collectors.summingInt(registro -> 1)
                ));
    }

}


