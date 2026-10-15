// Arquivo Servidor.java

import CalculadoraApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;

class CalculadoraImpl extends _CalculadoraImplBase {
  public double somar(double numero1, double numero2) {
    return numero1 + numero2;
  }

  public double subtrair(double numero1, double numero2) {
    return numero1 - numero2;
  }

  public double multiplicar(double numero1, double numero2) {
    return numero1 * numero2;
  }

  public double dividir(double numero1, double numero2) {
    if(numero2 == 0)
      throw new ArithmeticException("Não é possível dividir por zero.");

    return numero1 / numero2;
  }
}

public class Servidor {
  public static void main(String args[]) {
    try {
      /*
       * Cria e inicializa o ORB. O segundo argumento é um objeto Properties 
       * com configurações adicionais do ORB. O null significa que nenhuma 
       * propriedade extra foi informada. O ORB usará apenas os argumentos 
       * da linha de comando (args: host e port).
       */
      ORB orb = ORB.init(args, null);

      // Cria e registra o objeto servidor:
      CalculadoraImpl calculadora = new CalculadoraImpl();
      orb.connect(calculadora);

      /* 
       * Obtém uma referência para o root naming context:
       * O método abaixo não busca qualquer nome arbitrário do projeto. 
       * Ele consulta uma chave interna do ORB e a chave correta do serviço 
       * de nomes CORBA é: "NameService".
       */
      org.omg.CORBA.Object objeto = orb.resolve_initial_references("NameService");
      NamingContext contextoNome = NamingContextHelper.narrow(objeto);

      /*
       * Associa a Object Reference em Naming:
       * Resolve o Object Reference in Naming. O segundo parâmetro é a
       * categoria ou tipo (kind) do nome. A string vazia significa que
       * nenhum tipo foi especificado.
       */
      NameComponent componenteNome = new NameComponent("Calculadora", "");
      NameComponent nome[] = { componenteNome };
      contextoNome.rebind(nome, calculadora);

      // Espera requisições dos clientes:
      java.lang.Object sincronizacao = new java.lang.Object();
      synchronized(sincronizacao) {
        sincronizacao.wait();
      }
    } catch(Exception e) {
      System.err.println("ERRO: " + e);
      e.printStackTrace(System.out);
    }
  }
}