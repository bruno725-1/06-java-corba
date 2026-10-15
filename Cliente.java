// Arquivo Cliente.java

import CalculadoraApp.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import java.util.Scanner;

public class Cliente {
  public static void main(String[] args) {
    try {
      /*
       * Cria e inicializa o ORB. O segundo argumento é um objeto Properties
       * com configurações adicionais do ORB. O null significa que nenhuma
       * propriedade extra foi informada. O ORB usará apenas os argumentos
       * da linha de comando (args: host e port).
       */
      ORB orb = ORB.init(args, null);

      /*
       * Gera o root naming context:
       * O método abaixo não busca qualquer nome arbitrário do projeto.
       * Ele consulta uma chave interna do ORB e a chave correta do serviço
       * de nomes CORBA é: "NameService".
       */
      org.omg.CORBA.Object objeto = orb.resolve_initial_references("NameService");
      NamingContext contextoNome = NamingContextHelper.narrow(objeto);

      /*
       * Resolve o Object Reference in Naming. O segundo parâmetro é a
       * categoria ou tipo (kind) do nome. A string vazia significa que
       * nenhum tipo foi especificado.
       */
      NameComponent componenteNome = new NameComponent("Calculadora", "");
      NameComponent nome[] = { componenteNome };
      Calculadora calculadora = CalculadoraHelper.narrow(contextoNome.resolve(nome));

      Scanner scanner = new Scanner(System.in);
      System.out.print("Digite o primeiro número: ");
      double numero1 = scanner.nextDouble();
      System.out.print("Digite o segundo número: ");
      double numero2 = scanner.nextDouble();

      double resultadoSoma = calculadora.somar(numero1, numero2);
      double resultadoSubtracao = calculadora.subtrair(numero1, numero2);
      double resultadoMultiplicacao = calculadora.multiplicar(numero1, numero2);
      double resultadoDivisao = calculadora.dividir(numero1, numero2);

      System.out.println("Resultado da soma: " + resultadoSoma);
      System.out.println("Resultado da subtração: " + resultadoSubtracao);
      System.out.println("Resultado da multiplicação: " + resultadoMultiplicacao);
      System.out.println("Resultado da divisão: " + resultadoDivisao);
      scanner.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}