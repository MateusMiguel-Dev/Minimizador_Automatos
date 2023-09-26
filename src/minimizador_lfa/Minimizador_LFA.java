package minimizador_lfa;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Minimizador_LFA {//FUNÇÃO MINIMIZADOR
    //FUNÇÃO ANALISE PAR - BUSCA NO PROGRAMA SE OS ESTADOS SELECIONADOS SÃO ESTADOS FINAIS OU NÃO, E COMPARA PARA VERIFICAR SE SÃO EQUIVALENTES
    //ESSA FUNÇÃO É O REFERENTE A PRIMEIRA MATRIZ DE EQUIVALENCIAS
    public static boolean analise_Par(String simbolo, ArrayList<Transicoes> transic, Equivalencias equiv, ArrayList<Estados_Finais> finais){
        int i, j;
        boolean parA = true, parB = true;
        for(i = 0; i < transic.size(); i++){
            if(transic.get(i).getEstadoA().equals(equiv.getEstadoA()) && transic.get(i).getSimbolo().equals(simbolo)){
               for(j = 0; j < finais.size(); j ++){
                   parA = transic.get(i).getEstadoB().equals(finais.get(j).getEl_final());
               }
            }
            if(transic.get(i).getEstadoA().equals(equiv.getEstadoB()) && transic.get(i).getSimbolo().equals(simbolo)){
                for(j = 0; j < finais.size(); j++){
                    parB = transic.get(i).getEstadoB().equals(finais.get(j).getEl_final());
                }
            }
            break;
        }
        //SE AMBOS FOREM FINAIS OU NÃO FINAIS, RETORNA O VALOR SE SÃO EQUIVALENTES
        if(parA == true && parB == true){
            return true;
        }else return !((parA == true && parB == false) || (parA == false && parB == true));   
    }
    //FUNÇÃO PARA VERIFICAR, AO RECEBER UM ESTADO E UM SIMBOLO, QUAL É O ESTADO DESTINO PRESENTE NAS TRANSIÇÕES 
    public static String retorne_Estado(ArrayList<Transicoes> t, String e, String a){
        int i;
        String estado = null;
        for(i = 0; i < t.size(); i++){
            if(e.equals(t.get(i).getEstadoA()) && a.equals(t.get(i).getSimbolo())){
                estado = t.get(i).getEstadoB();
            }
        }
        return estado;
    }
    //FUNÇÃO PARA VERIFICAR SE O ESTADO É FINAL
    public static boolean retorne_Final(ArrayList<Estados_Finais> e_final, String estado, String simbolo){
        int i;
        boolean resultado = false;
        for(i = 0; i < e_final.size(); i++){
            if(e_final.get(i).getEl_final().equals(estado)){
                resultado = true;
            }
        }
        return resultado;
    }
    //FUNÇÃO REFERENTE AO PASSO 2 OU T2 DAS COMPARAÇÕES, ONDE SE BUSCA OS ESTADOS DESTINO DE CADA UM, E NOVAMENTE VERIFICANDO SE SÃO EQUIVALENTES OU NÃO
    public static boolean analise_Passo2(ArrayList<Transicoes> transic, Equivalencias eq, ArrayList<Estados_Finais> finais, ArrayList<Alfabeto_Passo2> alfa){
        int i;
        int soma = 0;
        boolean parA;
        boolean parB;
        Estados estadoA = new Estados(null, "0");
        Estados estadoB = new Estados(null, "0");
        for(i = 0; i < 4; i++){
            //RECEBE UMA FUNÇÃO QUE RETORNA QUAL O ESTADO DESTINO
            estadoA.setEstado(retorne_Estado(transic, eq.getEstadoA(), alfa.get(i).getSimboloA()));
            estadoB.setEstado(retorne_Estado(transic, eq.getEstadoB(), alfa.get(i).getSimboloA()));
            //RECEBE UMA FUNÇÃO QUE VERIFICA SE O ESTADO DESTINO DO ESTADO ANTERIOR É FINAL OU NÃO
            parA = retorne_Final(finais, estadoA.getEstado(), alfa.get(i).getSimboloB());
            parB = retorne_Final(finais, estadoB.getEstado(), alfa.get(i).getSimboloB());
            //IMPRESSÃO NA TELA
            //System.out.println(eq.getEstadoA() + " " + alfa.get(i).getSimboloA() + alfa.get(i).getSimboloB() + " " + parA + " " + retorne_Estado(transic, estadoA.getEstado(), alfa.get(i).getSimboloA()));
            //System.out.println(eq.getEstadoB() + " " + alfa.get(i).getSimboloA() + alfa.get(i).getSimboloB() + " " + parB + " " + retorne_Estado(transic, estadoB.getEstado(), alfa.get(i).getSimboloB()));
            //System.out.println("------");
            if(parA && parB || !(parA && parB)){
                soma++;   
            }else{
                break;
            }
        }
        return (soma%2 == 0);
    }   
    //VERIFICA SE É UM AFND
    public static void verifica_afnd(ArrayList<Transicoes> t){
        int compare_det = 0, j, c;
        for(c = 0; c < t.size(); c++){
            for(j = c+1; j < t.size(); j++){
                if(t.get(c).getEstadoA().equals(t.get(j).getEstadoA()) && t.get(c).getSimbolo().equals(t.get(j).getSimbolo())){
                    compare_det++;
                }
            }
        }
        if(compare_det > 0){
            System.out.println("O AUTOMATO É UM AFND, IMPOSSÍVEL MINIMIZAR");
        }
    }
    
    public static void main(String[] args) throws IOException {
        String diretorio = "automato2.dat";
        ArrayList<Estados> estado = new ArrayList();
        ArrayList<Alfabeto> alfabeto = new ArrayList();
        ArrayList<Transicoes> transicoes = new ArrayList();
        ArrayList<Estados_Finais> estados_finais = new ArrayList();
        Estado_Inicial estado_inicio = new Estado_Inicial(null);
        //ABRINDO O ARQUIVO E DEFININDO VARIÁVEIS GLOBAIS
        try{
            BufferedReader buffRead = new BufferedReader(new FileReader(diretorio));
            String linha;
            String[] tokens;
            int i;
            int cont = 0;
            //INICIO DA LEITURA DAS LINHAS DO ARQUIVO
            while(buffRead.ready()){
                linha = buffRead.readLine();
                tokens = linha.split("\\,");
                //ESTADOS DO MINIMIZADOR
                if((tokens[0].charAt(0) != '>') && (tokens[0].charAt(0) != '*') && cont == 0){
                    for(i = 0; i < tokens.length; i++){
                        Estados estado_classe = new Estados(null, "0");
                        estado_classe.setEstado(tokens[i]);
                        estado_classe.setEstado_fim("0");
                        estado.add(estado_classe);
                        //System.out.print(estado.get(i).getEstado() + " ");
                    }
                    cont++;
                //ALFABETO DO MINIMIZADOR
                }else if(cont == 1 && (tokens[0].charAt(0) != '>') && (tokens[0].charAt(0) != '*')){
                    System.out.print("\n");
                    for(i = 0; i < tokens.length; i++){
                        Alfabeto alfabeto_classe = new Alfabeto(null);
                        alfabeto_classe.setSimbolo(tokens[i]);
                        alfabeto.add(alfabeto_classe);
                        //System.out.print(alfabeto.get(i).getSimbolo() + " ");
                    }
                    System.out.print("\n");
                    cont++;
                //TRANSIÇÕES DO MINIMIZADOR
                }else if(cont >= 2 && (tokens[0].charAt(0) != '>') && (tokens[0].charAt(0) != '*') && (tokens[0].charAt(0) != '*')){
                    Transicoes transicao_classe = new Transicoes(null, "0", null);
                    transicao_classe.setEstadoA(tokens[0]);
                    transicao_classe.setSimbolo(tokens[1]);
                    transicao_classe.setEstadoB(tokens[2]);
                    transicoes.add(transicao_classe);
                //ESTADO INICIAL DO MINIMIZADOR
                }else if(tokens[0].charAt(0) == '>'){
                    estado_inicio.setInicial(tokens[0].replaceAll("\\>", ""));
                    //System.out.println(estado_inicial);     
                //ESTADOS FINAIS DO MINIMIZADOR
                }else if(tokens[0].charAt(0) == '*'){
                    tokens[0] = tokens[0].replaceAll("\\*", "");
                    for(i = 0; i < tokens.length; i++){
                        Estados_Finais elemento = new Estados_Finais("0");
                        elemento.setEl_final(tokens[i]);
                        estados_finais.add(elemento);
                        //System.out.print(estados_finais.get(i) + " ");
                    }
                }
            }
        }catch(IOException ioe){
            System.out.println("ERRO AO ABRIR O ARQUIVO!!!");
        }
        //VERIFICANDO SE É FINAL E ADICIONANDO UMA VARIÁVEL DE COMPARAÇÃO
        for(int i = 0; i < estado.size(); i++){
            for(int j = 0; j < estados_finais.size(); j++){
                if(estado.get(i).getEstado().equals(estados_finais.get(j).getEl_final())){
                    estado.get(i).setEstado_fim("1");
                    break;
                }else{
                    estado.get(i).setEstado_fim("0");
                }
            }
        }
        int i, j;
        for(i = 0; i < transicoes.size(); i++){
            System.out.println(transicoes.get(i).getEstadoA() + " " + transicoes.get(i).getSimbolo() + " " + transicoes.get(i).getEstadoB());
        }
        for(i = 0; i < estado.size(); i++){
            System.out.println(estado.get(i).getEstado() + " " + estado.get(i).getEstado_fim());
        }
        //VERIFICAÇÃO PARA VER SE OS ESTADOS SÃO EQUIVALENTES OU NÃO
        ArrayList<Equivalencias> equiv_estados = new ArrayList();
        for(i = 1; i < estado.size(); i++){
            for(j = 0; j < estado.size()-1; j++){
                if(j == i){
                    break;
                }
                Equivalencias equiv_classe = new Equivalencias();
                if(estado.get(i).getEstado_fim().equals("0") && estado.get(j).getEstado_fim().equals("0") && !(estado.get(i).getEstado().equals(estado.get(j).getEstado()))){
                    equiv_classe.setEstadoA(estado.get(i).getEstado());
                    equiv_classe.setEquivalencia(true);
                    equiv_classe.setEstadoB(estado.get(j).getEstado());
                    equiv_estados.add(equiv_classe);
                    //System.out.println(equiv_classe.getEstadoA());
                }else if(estado.get(i).getEstado_fim().equals("1") && estado.get(j).getEstado_fim().equals("0") && !(estado.get(i).getEstado().equals(estado.get(j).getEstado()))){
                    equiv_classe.setEstadoA(estado.get(i).getEstado());
                    equiv_classe.setEquivalencia(false);
                    equiv_classe.setEstadoB(estado.get(j).getEstado());
                    equiv_estados.add(equiv_classe);
                    //System.out.println(equiv_classe.getEstadoA());
                }else if(estado.get(i).getEstado_fim().equals("0") && estado.get(j).getEstado_fim().equals("1") && !(estado.get(i).getEstado().equals(estado.get(j).getEstado()))){
                    equiv_classe.setEstadoA(estado.get(i).getEstado());
                    equiv_classe.setEquivalencia(false);
                    equiv_classe.setEstadoB(estado.get(j).getEstado());
                    equiv_estados.add(equiv_classe);
                    //System.out.println(equiv_classe.getEstadoA());
                }else if(estado.get(i).getEstado_fim().equals("1") && estado.get(j).getEstado_fim().equals("1") && !(estado.get(i).getEstado().equals(estado.get(j).getEstado()))){
                    equiv_classe.setEstadoA(estado.get(i).getEstado());
                    equiv_classe.setEquivalencia(true);
                    equiv_classe.setEstadoB(estado.get(j).getEstado());
                    equiv_estados.add(equiv_classe);
                }
            }
        }
        
        for(i = 0; i < equiv_estados.size(); i++){
            if(equiv_estados.get(i).isEquivalencia()){
                System.out.print(equiv_estados.get(i).getEstadoA() + " ");
                System.out.print(equiv_estados.get(i).isEquivalencia() + " ");
                System.out.println(equiv_estados.get(i).getEstadoB());
            }    
        }
        //ADIÇÃO E TRANSFORMAÇÃO DAS SEQUENCIAS DO ALFABETO PARA O PASSO 2/T2
        ArrayList<Alfabeto_Passo2> alfa = new ArrayList();
        OUTER:
        for (int aux = 0; aux < 4; aux++) {
            switch (aux) {
                case 0 -> {
                    Alfabeto_Passo2 af = new Alfabeto_Passo2();
                    af.setSimboloA(alfabeto.get(0).getSimbolo());
                    af.setSimboloB(alfabeto.get(0).getSimbolo());
                    alfa.add(af);
                }
                case 1 -> {
                    Alfabeto_Passo2 af = new Alfabeto_Passo2();
                    af.setSimboloA(alfabeto.get(0).getSimbolo());
                    af.setSimboloB(alfabeto.get(1).getSimbolo());
                    alfa.add(af);
                }
                case 2 -> {
                    Alfabeto_Passo2 af = new Alfabeto_Passo2();
                    af.setSimboloA(alfabeto.get(1).getSimbolo());
                    af.setSimboloB(alfabeto.get(0).getSimbolo());
                    alfa.add(af);
                }
                case 3 -> {
                    Alfabeto_Passo2 af = new Alfabeto_Passo2();
                    af.setSimboloA(alfabeto.get(1).getSimbolo());
                    af.setSimboloB(alfabeto.get(1).getSimbolo());
                    alfa.add(af);
                }
                default -> {
                    break OUTER;
                }
            }
        }
        //VERIFICAÇÃO T1 E T2
        for(i = 0; i < equiv_estados.size(); i++){
            if(equiv_estados.get(i).isEquivalencia()){
                Equivalencias eq = new Equivalencias();
                eq.setEstadoA(equiv_estados.get(i).getEstadoA());
                eq.setEstadoB(equiv_estados.get(i).getEstadoB());
                eq.setEquivalencia(equiv_estados.get(i).isEquivalencia());
                boolean verifiqueA, verifiqueB;
                verifiqueA = analise_Par(alfabeto.get(0).getSimbolo(), transicoes, eq, estados_finais);
                verifiqueB = analise_Par(alfabeto.get(1).getSimbolo(), transicoes, eq, estados_finais);
                boolean resposta;
                if(verifiqueA && verifiqueB || (!verifiqueA && !verifiqueB)){
                    resposta = analise_Passo2(transicoes, eq, estados_finais, alfa);
                    equiv_estados.get(i).setEquivalencia(resposta);
                }else if(!(verifiqueA && verifiqueB)){
                    equiv_estados.get(i).setEquivalencia(false);
                }
            }
        }
        for(i = 0; i < equiv_estados.size(); i++){
            if(equiv_estados.get(i).isEquivalencia()){
                System.out.println(equiv_estados.get(i).getEstadoA() + " " + "O" + " " + equiv_estados.get(i).getEstadoB());
            }else{
                System.out.println(equiv_estados.get(i).getEstadoA() + " " + "X" + " " + equiv_estados.get(i).getEstadoB());
            }    
        }
    }//----- FIM DO MAIN
}//----- FIM DA CLASSE PRINCIPAL