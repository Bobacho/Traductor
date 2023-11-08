package org.example.controllers;
import org.example.models.Tipo;
import org.jetbrains.annotations.NotNull;
import org.example.models.Tokens;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Parser {

    private List<String> Vt;
    private Stack<String> pila;
    private List<Tokens> resultados;
    private Map<Map<String,String>,List<Object>> tabla;
    public Parser(@NotNull Scanner scanner) throws IOException {

        resultados=scanner.getListaToken();
        Vt=new ArrayList<>();
        for(Tokens token: scanner.plantilla)
        {
            Vt.add(getIdentificadorOvt(token));
        }
        Tokens delimitador=new Tokens();
        delimitador.setNombre("$");
        delimitador.setTipo(Tipo.DELIMITADOR);
        delimitador.setDescripcion("FIN DE CODIGO");
        resultados.add(delimitador);
        pila= new Stack<>();
        initialize();
    }

    private void initialize() throws IOException {
        File listaParser=new File("listaParser.json");
        String bufferParser="";
        if(!listaParser.exists())
        {
            listaParser.createNewFile();
        }
        if(listaParser.canRead())
        {
            java.util.Scanner parser=new java.util.Scanner(listaParser);
            while(parser.hasNext())
            {
                bufferParser+=parser.nextLine();
            }
        }
        JSONArray jsonParser=new JSONArray(bufferParser);
        tabla=new HashMap<>();
        for(int i=0;i<jsonParser.length();i++)
        {
            tabla.put(Map.of(jsonParser.getJSONObject(i).getString("Vn"),
                    jsonParser.getJSONObject(i).getString("Vt")),
                    jsonParser.getJSONObject(i).getJSONArray("Produccion").toList());
        }
        pila.push("$");
        pila.push("S");
    }
    public void run()
    {
        int sim=0;
        String x="";
        List<String> cadenaEntrada=new ArrayList<>();
        for(int i=0;i<resultados.size();i++)
        {
            cadenaEntrada.add((resultados.get(i).getTipo()==Tipo.IDENTIFICADOR)?resultados.get(i).getDescripcion():resultados.get(i).getNombre());
        }
        do {
            System.out.println("---------------------------");
            System.out.println("Pila:"+pila.toString());
            x=pila.peek();
            System.out.println("Entrada:"+cadenaEntrada);
            System.out.println("X->"+x+"; sim->"+getIdentificadorOvt(resultados.get(sim)));
            if(Vt.contains(x) || x.equals("$"))
            {
                System.out.println("X es terminal");
                if (x.equals(getIdentificadorOvt(resultados.get(sim)))) {
                    pila.pop();
                    sim++;
                    continue;
                }
                else {
                    throw new RuntimeException("Error");
                }
            }
            else if(tabla.get(Map.of(x,getIdentificadorOvt(resultados.get(sim))))!=null)
            {
                System.out.println("X es no terminal");
                pila.pop();
                if(tabla.get(Map.of(x,getIdentificadorOvt(resultados.get(sim)))).get(0).toString().isBlank())
                {
                    continue;
                }
                for(int i=tabla.get(Map.of(x,getIdentificadorOvt(resultados.get(sim)))).size()-1;i>=0;i--)
                {
                    pila.push((String)tabla.get(Map.of(x,getIdentificadorOvt(resultados.get(sim)))).get(i));
                }
            }
            else
            {
                throw new RuntimeException("Error");
            }
        }while(!x.equals("$"));
        System.out.println("\nSENTENCIA ACEPTADA");
    }

    private String getIdentificadorOvt(Tokens token)
    {
        if(token.getTipo().equals(Tipo.IDENTIFICADOR) || token.getTipo().equals(Tipo.NUMERO) || token.getTipo().equals(Tipo.CADENA))
        {
            return token.getDescripcion();
        }
        else
        {
            return token.getNombre();
        }
    }
}
