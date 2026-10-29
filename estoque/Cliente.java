import org.omg.CORBA.ORB;
import org.omg.CosNaming.*;
import java.util.Locale;
import java.util.Scanner;
import EstoqueApp.*;
public class Cliente {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);

            org.omg.CORBA.Object referenciaNomes =
                orb.resolve_initial_references("NameService");

            NamingContextExt contexto =
                NamingContextExtHelper.narrow(referenciaNomes);

            Estoque estoque =
                EstoqueHelper.narrow(contexto.resolve_str("Estoque"));

            int opcao;

            do {
                System.out.println("\n SISTEMA DE ESTOQUE");
                System.out.println("1 - Listar produtos");
                System.out.println("2 - Alterar quantidade em estoque");
                System.out.println("3 - Alterar preço");
                System.out.println("0 - Sair");
                System.out.print("Escolha uma opção: ");

                opcao = lerInteiro();

                switch (opcao) {
                    case 1:
                        listarProdutos(estoque);
                        break;

                    case 2:
                        alterarQuantidade(estoque);
                        break;

                    case 3:
                        alterarPreco(estoque);
                        break;

                    case 0:
                        System.out.println("Encerrando o cliente...");
                        break;

                    default:
                        System.out.println("Opção inválida.");
                }

            } while (opcao != 0);

        } catch (Exception e) {
            System.err.println("Erro no cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void listarProdutos(Estoque estoque) {
        Produto[] produtos = estoque.listarProdutos();

        System.out.println("\nPRODUTOS EM ESTOQUE");

        if (produtos.length == 0) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        for (Produto produto : produtos) {
            System.out.printf(
                Locale.forLanguageTag("pt-BR"),
                "Produto: %s | Quantidade: %d | Preço: R$ %.2f%n",
                produto.nome,
                produto.quantidade,
                produto.preco
            );
        }
    }

    private static void alterarQuantidade(Estoque estoque) {
        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine();

        System.out.print("Nova quantidade: ");
        int quantidade = lerInteiro();

        if (quantidade < 0) {
            System.out.println("A quantidade não pode ser negativa.");
            return;
        }

        boolean alterado = estoque.alterarQuantidade(nome, quantidade);

        if (alterado) {
            System.out.println("Quantidade alterada com sucesso!");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    private static void alterarPreco(Estoque estoque) {
        System.out.print("Nome do produto: ");
        String nome = scanner.nextLine();

        System.out.print("Novo preço: ");
        double preco = lerDouble();

        if (!Double.isFinite(preco) || preco < 0) {
            System.out.println("Informe um preço válido e não negativo.");
            return;
        }

        boolean alterado = estoque.alterarPreco(nome, preco);

        if (alterado) {
            System.out.println("Preço alterado com sucesso!");
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    private static int lerInteiro() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Digite um número inteiro válido: ");
            }
        }
    }

    private static double lerDouble() {
        while (true) {
            try {
                String entrada = scanner.nextLine()
                    .trim()
                    .replace(',', '.');

                return Double.parseDouble(entrada);
            } catch (NumberFormatException e) {
                System.out.print("Digite um preço válido: ");
            }
        }
    }
}
