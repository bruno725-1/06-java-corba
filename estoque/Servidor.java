import org.omg.CORBA.ORB;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.PortableServer.*;

import java.util.ArrayList;
import java.util.List;

import EstoqueApp.*;

class EstoqueImpl extends EstoquePOA {
    private final List<Produto> produtos = new ArrayList<>();

    public EstoqueImpl() {
        produtos.add(new Produto("Teclado", 15, 120.00));
        produtos.add(new Produto("Mouse", 30, 65.00));
        produtos.add(new Produto("Monitor", 8, 899.90));
    }

    @Override
    public synchronized Produto[] listarProdutos() {
        return produtos.toArray(new Produto[0]);
    }

    @Override
    public synchronized boolean alterarQuantidade(
            String nome, int novaQuantidade) {

        if (novaQuantidade < 0) {
            return false;
        }

        for (Produto produto : produtos) {
            if (produto.nome.equalsIgnoreCase(nome)) {
                produto.quantidade = novaQuantidade;
                return true;
            }
        }

        return false;
    }

    @Override
    public synchronized boolean alterarPreco(
            String nome, double novoPreco) {
        if (novoPreco < 0 || !Double.isFinite(novoPreco)) {
            return false;
        }

        for (Produto produto : produtos) {
            if (produto.nome.equalsIgnoreCase(nome)) {
                produto.preco = novoPreco;
                return true;
            }
        }

        return false;
    }
}

public class Servidor {
    public static void main(String[] args) {
        try {
            ORB orb = ORB.init(args, null);

            POA rootPOA = POAHelper.narrow(
                orb.resolve_initial_references("RootPOA")
            );

            rootPOA.the_POAManager().activate();

            EstoqueImpl estoque = new EstoqueImpl();

            org.omg.CORBA.Object referencia =
                rootPOA.servant_to_reference(estoque);

            Estoque objetoEstoque = EstoqueHelper.narrow(referencia);

            org.omg.CORBA.Object referenciaNomes =
                orb.resolve_initial_references("NameService");

            NamingContextExt contexto =
                NamingContextExtHelper.narrow(referenciaNomes);

            contexto.rebind(
                contexto.to_name("Estoque"),
                objetoEstoque
            );

            System.out.println("Servidor de estoque iniciado.");

            orb.run();

        } catch (Exception e) {
            System.err.println("Erro no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
