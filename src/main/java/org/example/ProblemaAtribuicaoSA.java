package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ProblemaAtribuicaoSA {
    private final int [][] matrizCusto;
    private final int numTarefas;
    private static final double TEMPERATURA_INICIAL = 5000;
    private static final double TAXA_RESFRIAMENTO = 0.0005;
    private static final int MAX_ITERACOES = 100000;

    public ProblemaAtribuicaoSA(int[][] matrizCusto) {
        this.matrizCusto = matrizCusto;
        this.numTarefas = matrizCusto.length;
    }

    public int[] resolver() {
        int[] solucaoAtual = gerarSolucaoAleatoria();
        int custoAtual = calcularCusto(solucaoAtual);

        int[] melhorSolucao = solucaoAtual.clone();
        int melhorCusto = custoAtual;

        double temperatura = TEMPERATURA_INICIAL;
        Random rand = new Random();

        System.out.println("Iniciando o Simulated Annealing...");

        for (int i = 0; i < MAX_ITERACOES; i++) {
            if (temperatura > 1) {
                int[] novaSolucao = perturbarSolucao(solucaoAtual.clone());
                int novoCusto = calcularCusto(novaSolucao);

                System.out.println("Iteração: " + i + ", Avaliação da nova solução: " + novoCusto);

                if (probabilidadeAceitacao(custoAtual, novoCusto, temperatura) > rand.nextDouble()) {
                    solucaoAtual = novaSolucao;
                    custoAtual = novoCusto;
                    System.out.println("Nova solução aceita com custo: " + custoAtual);
                }

                if (custoAtual < melhorCusto) {
                    melhorSolucao = solucaoAtual.clone();
                    melhorCusto = custoAtual;
                    System.out.println("Nova melhor solução encontrada com custo: " + melhorCusto);
                }

                System.out.printf("Temperatura antes do resfriamento: %.2f%n", temperatura);
                temperatura *= (1 - TAXA_RESFRIAMENTO);
                System.out.printf("Temperatura após o resfriamento: %.2f%n", temperatura);
            }
        }

        System.out.println("Solução final: " + java.util.Arrays.toString(melhorSolucao));
        System.out.println("Custo da melhor solução: " + melhorCusto);
        return melhorSolucao;
    }

    private int[] gerarSolucaoAleatoria() {
        int[] solucao = new int[numTarefas];
        List<Integer> tarefas = new ArrayList<>();
        for (int i = 0; i < numTarefas; i++) tarefas.add(i);
        Collections.shuffle(tarefas);
        for (int i = 0; i < numTarefas; i++) solucao[i] = tarefas.get(i);
        return solucao;
    }

    private int[] perturbarSolucao(int[] solucao) {
        Random rand = new Random();
        int idx1 = rand.nextInt(numTarefas);
        int idx2 = rand.nextInt(numTarefas);
        int temp = solucao[idx1];
        solucao[idx1] = solucao[idx2];
        solucao[idx2] = temp;
        return solucao;
    }

    private int calcularCusto(int[] solucao) {
        int custo = 0;
        for (int i = 0; i < numTarefas; i++) {
            custo += matrizCusto[i][solucao[i]];
        }
        return custo;
    }

    private double probabilidadeAceitacao(int custoAtual, int novoCusto, double temperatura) {
        if (novoCusto < custoAtual) {
            return 1.0;
        }
        return Math.exp((custoAtual - novoCusto) / (2 * temperatura));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random rand = new Random();

        System.out.println("Escolha a instância do problema para resolver:");
        System.out.println("1 - Instância pequena (N=2500)");
        System.out.println("2 - Instância média (N=12500)");
        System.out.println("3 - Instância grande (N=25000)");
        System.out.print("Sua escolha: ");
        int escolha = scanner.nextInt();

        ProblemaAtribuicaoSA sa ;

        switch (escolha) {
            case 1:
                int[][] matrizCustoPequena = new int[2500][2500];
                for (int i = 0; i < 2500; i++) {
                    for (int j = 0; j < 2500; j++) {
                        matrizCustoPequena[i][j] = rand.nextInt(1000) + 1;
                    }
                }
                sa = new ProblemaAtribuicaoSA(matrizCustoPequena);
                System.out.println("Resultado para N=2500:");
                break;
            case 2:
                int[][] matrizCustoMedia = new int[12500][12500];
                for (int i = 0; i < 12500; i++) {
                    for (int j = 0; j < 12500; j++) {
                        matrizCustoMedia[i][j] = rand.nextInt(1000) + 1;
                    }
                }
                sa = new ProblemaAtribuicaoSA(matrizCustoMedia);
                System.out.println("Resultado para N=12500:");
                break;
            case 3:
                int[][] matrizCustoGrande = new int[25000][25000];
                for (int i = 0; i < 25000; i++) {
                    for (int j = 0; j < 25000; j++) {
                        matrizCustoGrande[i][j] = rand.nextInt(1000) + 1;
                    }
                }
                sa = new ProblemaAtribuicaoSA(matrizCustoGrande);
                System.out.println("Resultado para N=25000:");
                break;
            default:
                System.out.println("Escolha inválida.");
                return;
        }

        sa.resolver();

        scanner.close();
    }
}
