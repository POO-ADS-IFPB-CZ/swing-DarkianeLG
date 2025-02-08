import dao.ProdutoDao;
import model.Produto;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;

public class Main {

    public static void main(String[] args){
        ProdutoDao produtoDao = new ProdutoDao();

        while (true) {
            String[] opcoes = {"Salvar", "Listar", "Remover", "Sair"};

            int escolha = JOptionPane.showOptionDialog(
                    null,
                    "O que deseja fazer?",
                    "Gerenciar Produtos",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opcoes,
                    opcoes[0] // Padrão: "Salvar"
            );

            switch (escolha) {
                case 0 -> salvarProduto(produtoDao);
                case 1 -> listarProdutos(produtoDao);
                case 2 -> removerProduto(produtoDao);
                case 3, -1 -> { // "Sair" ou fechar a janela
                    JOptionPane.showMessageDialog(null, "Saindo...");
                    System.exit(0);
                }
            }
        }
    }

    private static void salvarProduto(ProdutoDao produtoDao) {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Informe o ID do produto:"));
            String descricao = JOptionPane.showInputDialog("Informe a descrição do produto:");
            float preco = Float.parseFloat(JOptionPane.showInputDialog("Informe o preço do produto:"));
            String validadeStr = JOptionPane.showInputDialog("Informe a data de validade (AAAA-MM-DD):");

            LocalDate validade = LocalDate.parse(validadeStr);

            Produto produto = new Produto(id, descricao, preco, validade);
            boolean sucesso = produtoDao.adicionarProduto(produto);

            if (sucesso) {
                JOptionPane.showMessageDialog(null, "Produto salvo com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao salvar o produto! Verifique se já existe.");
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            JOptionPane.showMessageDialog(null, "Erro: Formato inválido. Tente novamente.");
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Erro ao acessar o arquivo.");
        }
    }

    private static void listarProdutos(ProdutoDao produtoDao) {
        try {
            Set<Produto> produtos = produtoDao.getProdutos();
            if (produtos.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum produto cadastrado.");
            } else {
                StringBuilder lista = new StringBuilder("Lista de Produtos:\n");
                for (Produto p : produtos) {
                    lista.append(p).append("\n");
                }
                JOptionPane.showMessageDialog(null, lista.toString());
            }
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Erro ao acessar os produtos.");
        }
    }

    private static void removerProduto(ProdutoDao produtoDao) {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Informe o ID do produto para remover:"));


            Set<Produto> produtos = produtoDao.getProdutos();
            Produto produtoParaRemover = null;

            for (Produto p : produtos) {
                if (p.getId() == id) {
                    produtoParaRemover = p;
                    break;
                }
            }

            if (produtoParaRemover != null) {
                boolean sucesso = produtoDao.removerProduto(produtoParaRemover);
                if (sucesso) {
                    JOptionPane.showMessageDialog(null, "Produto removido com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Erro ao remover o produto.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Produto não encontrado!");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro: ID inválido.");
        } catch (IOException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Erro ao acessar o arquivo.");
        }
    }

}
