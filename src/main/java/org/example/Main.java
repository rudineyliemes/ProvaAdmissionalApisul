package org.example;

import org.example.service.ElevatorService;

public class Main {
    public static void main(String[] args) {

        ElevatorService elevatorService = new ElevatorService("/input.json");
        System.out.println(elevatorService.andarMenosUtilizado());
        System.out.println(elevatorService.elevadorMaisFrequentado());
        System.out.println(elevatorService.elevadorMenosFrequentado());
        System.out.println(elevatorService.periodoMaiorFluxoElevadorMaisFrequentado());
        System.out.println(elevatorService.periodoMenorFluxoElevadorMenosFrequentado());
        System.out.println(elevatorService.periodoMaiorUtilizacaoConjuntoElevadores());
        elevatorService.percentualDeUsoElevadorA();
        elevatorService.percentualDeUsoElevadorB();
        elevatorService.percentualDeUsoElevadorC();
        elevatorService.percentualDeUsoElevadorD();
        elevatorService.percentualDeUsoElevadorE();

    }
}