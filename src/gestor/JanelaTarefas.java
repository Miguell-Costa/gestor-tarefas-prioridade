package gestor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.text.DateFormatter;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;

public class JanelaTarefas extends JFrame {

         private final TaskManager gestor;
         private final JTextField nomeField;
         private final JTextField prioridadeField;
         private final JTextField dataField;
         private final JTable tabelaTarefas;
         private final DefaultTableModel tabela;

         public JanelaTarefas() {
                  //criação 
                  gestor = new TaskManager();

                  setTitle("Gestor de Tarefas");
                  setSize(600, 500);
                  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                  setLocationRelativeTo(null);
                  // Campo de entrada
                  nomeField = new JTextField(15);
                  prioridadeField = new JTextField(5);
                  // Campo de data com formatação
                  SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                  DateFormatter formatter = new DateFormatter(formato);
                  dataField = new JFormattedTextField(formatter);
                  dataField.setColumns(10);
                  // Botões
                  JButton adicionarBtn = new JButton("Adicionar");
                  JButton pesquisarBtn = new JButton("Pesquisar");
                  JButton editarBtn = new JButton("Editar");
                  // Botão único para remoção
                  JButton removerBtn = new JButton("Remover");
                  JButton ordenarPrioridadeBtn = new JButton("Ordenar por Prioridade");
                  JButton ordenarDataLimiteBtn = new JButton("Ordenar por Data Limite");
                  JButton contarPrioridadeBtn = new JButton("Total Tarefas Prioridade");
                  JButton tarefaMaiorPrioridadeBtn = new JButton("Recuperar Tarefa Maior Prioridade");
                  JButton verificarAtrasoBtn = new JButton("Verificar atraso");
                  JButton limiteProximoBtn = new JButton("Data Limite mais Proxima");
                  JButton duracapTotalBtn = new JButton("Duração Total");

                  // Ações dos botões
                  adicionarBtn.addActionListener(e -> adicionarTarefa());
                  pesquisarBtn.addActionListener(e -> pesquisarTarefa());
                  editarBtn.addActionListener(e -> editarTarefa());

                  // Popup para escolher o tipo de remoção
                  removerBtn.addActionListener(e -> {
                           String[] opcoes = {"Remover pela Coluna", "Remover pelo Nome", "Remover Conjunto por Prioridade", "Remover Tudo"};

                           int escolha = JOptionPane.showOptionDialog(this, "Escolha o tipo de remoção:", "Remover Tarefa", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opcoes, opcoes[0]);

                           switch (escolha) {
                                    case 0 ->
                                             removerTarefaColuna();
                                    case 1 ->
                                             removerTarefaNome();
                                    case 2 ->
                                             removerConjuntoTarefas();
                                    case 3 ->
                                             removerTudo();
                                    default -> {
                                    }
                           }
                  });

                  ordenarPrioridadeBtn.addActionListener(e -> ordenarTarefasPrioridade());
                  ordenarDataLimiteBtn.addActionListener(e -> ordenarTarefasDataLimite());
                  contarPrioridadeBtn.addActionListener(e -> contarPrioridade());
                  tarefaMaiorPrioridadeBtn.addActionListener(e -> maiorPrioridade());
                  verificarAtrasoBtn.addActionListener(e -> verificarAtraso());
                  limiteProximoBtn.addActionListener(e -> limiteProximo());
                  duracapTotalBtn.addActionListener(e -> duracaoTotal());

                  // Painel principal de entrada (campos + botões em duas linhas)
                  JPanel inputPanel = new JPanel();
                  inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

                  // Linha dos campos
                  JPanel camposPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                  camposPanel.add(new JLabel("Nome:"));
                  camposPanel.add(nomeField);
                  camposPanel.add(new JLabel("Prioridade:"));
                  camposPanel.add(prioridadeField);
                  camposPanel.add(new JLabel("Data (yyyy-MM-dd):"));
                  camposPanel.add(dataField);

                  // Linha dos botões
                  JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                  botoesPanel.add(adicionarBtn);
                  botoesPanel.add(pesquisarBtn);
                  botoesPanel.add(editarBtn);
                  botoesPanel.add(removerBtn);  // só o botão único de remoção aqui
                  botoesPanel.add(ordenarPrioridadeBtn);
                  botoesPanel.add(ordenarDataLimiteBtn);
                  botoesPanel.add(contarPrioridadeBtn);
                  botoesPanel.add(tarefaMaiorPrioridadeBtn);
                  botoesPanel.add(verificarAtrasoBtn);
                  botoesPanel.add(limiteProximoBtn);
                  botoesPanel.add(duracapTotalBtn);

                  // Adicionar ambos os painéis ao painel principal
                  inputPanel.add(camposPanel);
                  inputPanel.add(botoesPanel);

                  // Tabela
                  String[] colunas = {"Nome", "Prioridade","Data Inicio", "Data Limite"};
                  tabela = new DefaultTableModel(colunas, 0);
                  tabelaTarefas = new JTable(tabela);
                  JScrollPane scrollPane = new JScrollPane(tabelaTarefas);

                  // Layout
                  setLayout(new BorderLayout());
                  add(inputPanel, BorderLayout.NORTH);
                  add(scrollPane, BorderLayout.CENTER);
         }

         public static void main(String[] args) {
                  SwingUtilities.invokeLater(() -> new JanelaTarefas().setVisible(true));
         }

         //criar uma tarefa
         public void adicionarTarefa() {
                  String nome = nomeField.getText();
                  String prioridadeTexto = prioridadeField.getText();
                  String dataTexto = dataField.getText();

                  if (nomeField.getText().isEmpty() || prioridadeField.getText().isEmpty() || dataField.getText().isEmpty()) {
                           JOptionPane.showMessageDialog(this, "Todos os Campos têm de estar preenchidos", "Campos inválidos", JOptionPane.WARNING_MESSAGE);
                           return;
                  }

                  try {
                           int prioridade = Integer.parseInt(prioridadeTexto);
                           LocalDate dataInserida = LocalDate.parse(dataTexto);
                           LocalDate hoje = LocalDate.now();

                           //verificar se os campos estao vazios
                           if (!dataInserida.isAfter(hoje)) {
                                    JOptionPane.showMessageDialog(this, "A data tem de ser uma data futura", "Data inválida", JOptionPane.WARNING_MESSAGE);
                                    return;
                           }
                           
                           
                           if (gestor.adicionarTarefa(nome.trim(), prioridade, dataInserida)) {
                                    nomeField.setText("");
                                    prioridadeField.setText("");
                                    dataField.setText("");
                                    listarTarefas();
                                    JOptionPane.showMessageDialog(this, "Tarefa inserida com sucesso!", "Adicionar Tarefa", JOptionPane.INFORMATION_MESSAGE);
                           } else {
                                    JOptionPane.showMessageDialog(this, "Já existe uma tarefa comm esse nome", "Nome  inválido", JOptionPane.WARNING_MESSAGE);
                           }

                  } catch (NumberFormatException e) {
                           JOptionPane.showMessageDialog(this, "Prioridade inválida (tem de ser um número)", "Erro", JOptionPane.ERROR_MESSAGE);
                  } catch (DateTimeParseException e) {
                           JOptionPane.showMessageDialog(this, "Data inválida (use o formato yyyy-MM-dd)", "Erro", JOptionPane.ERROR_MESSAGE);
                  }
         }

         //pesquisar uma tarefa
         public void pesquisarTarefa() {
                  //Pedir e guardar o nome da tarefa 
                  String nome = JOptionPane.showInputDialog(this, "Insira o nome da tarefa ", "Pesquisar Tarefa", JOptionPane.QUESTION_MESSAGE);

                  //se cancelar sai
                  if (nome == null) {
                           return;
                  }

                  //verificar se o campo esta vazio
                  if (nome.isEmpty()) {
                           JOptionPane.showMessageDialog(this, "O campo tem de estar preenchido", "Campo inválido", JOptionPane.WARNING_MESSAGE);
                           return;
                  }

                  //Usar variavel para depois veridicar se esta nula
                  Task tarefa = gestor.pesquisarTarefa(nome);

                  if (tarefa != null) {
                           JOptionPane.showMessageDialog(this, gestor.pesquisarTarefa(nome));
                  } else {
                           JOptionPane.showMessageDialog(this, "Tarefa não encontrada", "Erro", JOptionPane.ERROR_MESSAGE);
                  }
         }

         //editar uma tarefa
         public void editarTarefa() {

                  //Pedir e guardar o nome da tarefa 
                  String nome = JOptionPane.showInputDialog(this, "Insira o nome da tarefa ", "Pesquisar Tarefa", JOptionPane.QUESTION_MESSAGE);

                  //se cancelar sai
                  if (nome == null) {

                           //verificar se o campo esta vazio
                  } else if (nome.isEmpty()) {
                           JOptionPane.showMessageDialog(this, "O campo tem de estar preenchido", "Campo inválido", JOptionPane.WARNING_MESSAGE);

                  } else {
                           //Pedir a nova prioridade
                           String novaPrioridade = JOptionPane.showInputDialog(this, "Insira a nova prioridave da tarefa ", "Editar Prioridade Tarefa", JOptionPane.QUESTION_MESSAGE);

                           //Pedir a nova data
                           String novaData = JOptionPane.showInputDialog(this, "Insira a nova data limite da tarefa ", "Editar Data Limite Tarefa", JOptionPane.QUESTION_MESSAGE);

                           //verifica se os campos foram preenchidos 
                           if (novaPrioridade.isEmpty() || novaData.isEmpty()) {
                                    JOptionPane.showMessageDialog(this, "Os campos têm de estar preenchidos", "Campos inválidos", JOptionPane.WARNING_MESSAGE);

                           } else if (gestor.verificarTarefa(nome)) { //verifica se a tarefa existe
                                    try {
                                             //Passar as variaveis para os dados corretos
                                             int prioridade = Integer.parseInt(novaPrioridade);
                                             LocalDate dataInserida = LocalDate.parse(novaData);
                                             LocalDate hoje = LocalDate.now();

                                             //Validar a data
                                             if (!dataInserida.isAfter(hoje)) {
                                                      JOptionPane.showMessageDialog(this, "A data tem de ser uma data futura", "Data inválida", JOptionPane.WARNING_MESSAGE);
                                                      return;
                                             }

                                             //Editar a tarefa
                                             gestor.editarTarefa(nome, prioridade, dataInserida);
                                             listarTarefas();

                                             JOptionPane.showMessageDialog(this, "Tarefa editada com sucesso!", "Editar Tarefa", JOptionPane.INFORMATION_MESSAGE);

                                    } catch (NumberFormatException e) {
                                             JOptionPane.showMessageDialog(this, "Prioridade inválida (tem de ser um número)", "Erro", JOptionPane.ERROR_MESSAGE);
                                    } catch (DateTimeParseException e) {
                                             JOptionPane.showMessageDialog(this, "Data inválida (use o formato yyyy-MM-dd)", "Erro", JOptionPane.ERROR_MESSAGE);
                                    }
                           } else {
                                    JOptionPane.showMessageDialog(this, "Tarefa não encontradas", "Erro", JOptionPane.ERROR_MESSAGE);
                           }
                  }
         }

         //listar tarefas
         public void listarTarefas() {
                  tabela.setRowCount(0); // limpa a tabela

                  // Copiar a PriorityQueue atual para garantir ordem ao listar
                  PriorityQueue<Task> copia = new PriorityQueue<>(new TaskPriorityComparator());
                  copia.addAll(gestor.getTodasTarefas());

                  while (!copia.isEmpty()) {
                           Task t = copia.poll(); // remove o próximo na ordem
                           tabela.addRow(new Object[]{t.getNome(), t.getPrioridade(), t.getDataInicio(), t.getDueDate()});
                  }
         }

         //remover uma tarela pela coluna
         public void removerTarefaColuna() {
                  //guardar coluna selecionada
                  int linhaSelecionada = tabelaTarefas.getSelectedRow();

                  //validar se algum linha está selecionada
                  if (linhaSelecionada == -1) {
                           JOptionPane.showMessageDialog(this, "Selecione uma linha da tabela primeiro", "Erro", JOptionPane.ERROR_MESSAGE);
                           return;
                  }

                  //guardar o nome da tarefa da linha selecionada
                  String nome = tabelaTarefas.getValueAt(linhaSelecionada, 0).toString();

                  //remover a tarefa e verificar se foi eleminada
                  if (gestor.removerTarefa(nome)) {
                           DefaultTableModel model = (DefaultTableModel) tabelaTarefas.getModel();
                           model.removeRow(linhaSelecionada);
                           JOptionPane.showMessageDialog(this, "Tarefa removida com sucesso!");
                  } else {
                           JOptionPane.showMessageDialog(this, "Tarefa não encontrada.");
                  }
         }

         //remover uma tarefa pelo nome
         public void removerTarefaNome() {
                  //Pedir e guardar o nome da tarefa a eleminar
                  String nome = JOptionPane.showInputDialog(this, "Insira o nome da tarefa a remover", "Remover Trefa", JOptionPane.QUESTION_MESSAGE);

                  //se cancelar sai
                  if (nome == null) {
                           return;
                           //verificar se o campo esta vazio
                  } else if (nome.isEmpty()) {
                           JOptionPane.showMessageDialog(this, "O campo tem de estar preenchido", "Campo inválido", JOptionPane.WARNING_MESSAGE);
                           return;
                  }

                  //eleminar a tarefa
                  boolean remover = gestor.removerTarefa(nome.trim());

                  //veridicar se a tarefa foi eleminada
                  if (remover) {
                           listarTarefas();
                           JOptionPane.showMessageDialog(this, "Tarefa removida com sucesso!");
                  } else {
                           JOptionPane.showMessageDialog(this, "Tarefa não encontrada.");
                  }
         }

         //remover um conjunto de tarefas de acordo com a prioridade
         public void removerConjuntoTarefas() {
                  //guardar o numero da prioridade para remover as tarefas
                  try {
                           String prioridadeRemover = JOptionPane.showInputDialog(this, "Insira o numero da priridade das tarefas a remover", "Remover Trefa", JOptionPane.QUESTION_MESSAGE);

                           if (prioridadeRemover == null) {
                                    return;
                                    //verificar se o campo esta vazio
                           } else if (prioridadeRemover.isEmpty()) {
                                    JOptionPane.showMessageDialog(this, "O campo tem de estar preenchido", "Campo inválido", JOptionPane.WARNING_MESSAGE);
                                    return;
                           }

                           //converter o valor para int
                           int prioridade = Integer.parseInt(prioridadeRemover);

                           //remover as tarefas
                           boolean remover = gestor.removerConjunto(prioridade);

                           //verificar se as tarefas foram removidas
                           if (remover) {
                                    listarTarefas();
                                    JOptionPane.showMessageDialog(this, "Tarefa/s removida/s com sucesso!");
                           } else {
                                    JOptionPane.showMessageDialog(this, "Tarefa/s não encontrada/s.");
                           }
                  } catch (NumberFormatException e) {
                           JOptionPane.showMessageDialog(this, "Prioridade inválida (tem de ser um número)", "Erro", JOptionPane.ERROR_MESSAGE);

                  }
         }
         //remover todas as tarefas

         public void removerTudo() {
                  if (gestor.removerTodasTarefas()) {
                           JOptionPane.showMessageDialog(this, "Não existem tarefas", "Erro", JOptionPane.ERROR_MESSAGE);
                  } else {
                           JOptionPane.showMessageDialog(this, "Tarefas removidas", "Remover Tudo", JOptionPane.INFORMATION_MESSAGE);
                           listarTarefas();
                  }
         }

         //prdenar tarefas por prioridade
         public void ordenarTarefasPrioridade() {
                  // Copiar as tarefas para uma nova lista
                  ArrayList<Task> listaTarefas = new ArrayList<>(gestor.getTodasTarefas());

                  // Usar o comparador para ordenar pela prioridade
                  listaTarefas.sort(new TaskPriorityComparator());

                  // Limpar a tabela para mostrar as tarefas ordenadas
                  tabela.setRowCount(0);

                  // Adicionar as tarefas ordenadas à tabela
                  for (Task t : listaTarefas) {
                           tabela.addRow(new Object[]{t.getNome(), t.getPrioridade(), t.getDataInicio(), t.getDueDate()});
                  }
         }

         //ordenar tarefas por data limite
         public void ordenarTarefasDataLimite() {
                  // Copiar as tarefas  para uma nova lista
                  ArrayList<Task> listaTarefas = new ArrayList<>(gestor.getTodasTarefas());

                  // Usar o comparador para ordenar pela data limite
                  listaTarefas.sort(new TaskDueDateComparator());

                  // Limpar a tabela para mostrar as tarefas ordenadas
                  tabela.setRowCount(0);

                  // Adicionar as tarefas ordenadas à tabela
                  for (Task t : listaTarefas) {
                           tabela.addRow(new Object[]{t.getNome(), t.getPrioridade(), t.getDataInicio(), t.getDueDate()});
                  }
         }

         //contar quantas tarefas existem em cada prioridade
         public void contarPrioridade() {
                  // Obter o mapa que conta quantas tarefas existem por prioridade
                  Map<Integer, Integer> contagem = gestor.contarPrioridades();
                  // Verificar se  existem tarefas
                  if (contagem.isEmpty()) {
                           JOptionPane.showMessageDialog(this, "Nao existem tarefas", "Contagem de Tarefas", JOptionPane.INFORMATION_MESSAGE);
                           return;
                  }

                  //declaração de string 
                  String mensagem = "";
                  // Percorrer todas as prioridades
                  for (Integer prioridade : contagem.keySet()) {
                           // Adicionar à mensagem o texto "Prioridade X: Y" onde X é a prioridade e Y o número de tarefas
                           mensagem += "Prioridade " + prioridade + ": " + contagem.get(prioridade) + "\n";
                  }
                  JOptionPane.showMessageDialog(this, mensagem, "Contagem de Tarefas", JOptionPane.INFORMATION_MESSAGE);
         }

         //devolve e remove a tarefa com mais prioridade
         public void maiorPrioridade() {
                  Task tarefaMaiorPrioridade = gestor.obterMaiorPrioridade();

                  if (tarefaMaiorPrioridade != null) {
                           JOptionPane.showMessageDialog(this, "Tarefa removida:\n" + tarefaMaiorPrioridade);
                           listarTarefas();
                  } else {
                           JOptionPane.showMessageDialog(this, "Não existem tarefas.");
                  }

         }

         //verificar atraso
         public void verificarAtraso() {
                  String nome = JOptionPane.showInputDialog(this, "Insira o nome da tarefa ", "Pesquisar Tarefa", JOptionPane.QUESTION_MESSAGE);

                  //se cancelar sai
                  if (nome == null) {
                           return;
                  }

                  //verificar se o campo esta vazio
                  if (nome.isEmpty()) {
                           JOptionPane.showMessageDialog(this, "O campo tem de estar preenchido", "Campo inválido", JOptionPane.WARNING_MESSAGE);
                           return;
                  }
                  
                  if(!gestor.verificarTarefa(nome)){
                           JOptionPane.showMessageDialog(this, "Tarefa não encontrada", "Erro", JOptionPane.ERROR_MESSAGE);
                           return;
                  }
                  
                  if (gestor.atrasoTarefa(nome)) {
                           JOptionPane.showMessageDialog(this, "A tarefa devia ser entregue no dia " + gestor.pesquisarTarefa(nome).getDueDate() + " e já é dia " + LocalDate.now(), "Tarefa Atrasada", JOptionPane.INFORMATION_MESSAGE);
                  } else {
                           JOptionPane.showMessageDialog(this, "A tarefa \"" + gestor.pesquisarTarefa(nome).getNome() + "\" ainda está dentro do prazo (até " + gestor.pesquisarTarefa(nome).getDueDate() + ").", "Sem Atraso", JOptionPane.INFORMATION_MESSAGE);
                  }
         }
         
         //verificar a tarefa que tem a data limite mais proxima
         public void limiteProximo() {
                  ordenarTarefasDataLimite();
                  String nomeTarefa = (String) tabelaTarefas.getValueAt(0, 0); // coluna do nome
                  int prioridade = (Integer) tabelaTarefas.getValueAt(0, 1);  // coluna da prioridade
                  LocalDate dataLimite = (LocalDate) tabelaTarefas.getValueAt(0, 2);  // coluna da data limite

                  if (tabelaTarefas.getRowCount() < 1) {
                           JOptionPane.showMessageDialog(this, "Nao existem tarefas", "Data Limite Proxima", JOptionPane.INFORMATION_MESSAGE);
                  } else {
                           JOptionPane.showMessageDialog(this, "Nome : " + nomeTarefa + " Prioridade: " + prioridade + " Data Limite: " + dataLimite, "Data Limite Proxima", JOptionPane.INFORMATION_MESSAGE);
                  }
         }

         public void duracaoTotal() {
                  // Verifica se existem tarefas na lista
                  if (gestor.getTodasTarefas().isEmpty()) {
                           // Se não houver tarefas, mostra uma mensagem ao utilizador
                           JOptionPane.showMessageDialog(this, "Não existem tarefas", "Duração por Tarefa", JOptionPane.INFORMATION_MESSAGE);
                  } else {
                           // Inicializa uma variável para construir a mensagem a mostrar
                           String mensagem = "";

                           // Percorre todas as tarefas disponíveis
                           for (Task t : gestor.getTodasTarefas()) {
                                    // Obtém a data de início e a data de término da tarefa
                                    LocalDate inicio = t.getDataInicio();
                                    LocalDate fim = t.getDueDate();

                                    // Verifica se ambas as datas são válidas e se a data de fim não é antes da de início
                                    if (inicio != null && fim != null && !fim.isBefore(inicio)) {
                                             // Calcula a diferença de dias entre o início e o fim
                                             long dias = ChronoUnit.DAYS.between(inicio, fim);

                                             // Adiciona a informação à mensagem
                                             mensagem += "Tarefa \"" + t.getNome() + "\" tem duração de " + dias + " dias.\n";
                                    } else {
                                             // Se os dados forem inválidos, avisa o utilizador
                                             mensagem += "Tarefa \"" + t.getNome() + "\" tem datas inválidas.\n";
                                    }
                           }

                           // Mostra a mensagem com a duração de cada tarefa
                           JOptionPane.showMessageDialog(this, mensagem, "Duração por Tarefa", JOptionPane.INFORMATION_MESSAGE);
                  }
         }

}
