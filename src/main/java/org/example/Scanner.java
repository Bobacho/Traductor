package org.example;

import org.example.models.Tipo;
import org.example.models.Tokens;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.List;

public class Scanner {
    private String sentencia;
    private List<Tokens> plantilla=new ArrayList<>();

    private List<Tokens> resultados=new ArrayList<>();


    public Scanner(String sentencia) throws IOException {
        initialize();
        this.sentencia=sentencia;
    }
    private void initialize() throws IOException {
        File reservadas=new File("listaTokens.json");
        String bufferReservadas="";
        if(!reservadas.exists())
        {
            reservadas.createNewFile();
        }
        if(reservadas.canRead())
        {
            java.util.Scanner reader=new java.util.Scanner(reservadas);
            while(reader.hasNext()) {
                bufferReservadas+= reader.nextLine();
            }
        }
        JSONArray jsontokens=new JSONArray(bufferReservadas);
        plantilla=new ArrayList<>();
        for(int i=0;i<jsontokens.length();i++)
        {
            Tokens temp=new Tokens();
            temp.setId(jsontokens.getJSONObject(i).getInt("ID"));
            temp.setNombre(jsontokens.getJSONObject(i).getString("Nombre"));
            temp.setDescripcion(jsontokens.getJSONObject(i).getString("Descripcion"));
            temp.setTipo(jsontokens.getJSONObject(i).getEnum(Tipo.class,"Tipo"));
            plantilla.add(temp);
        }
    }
    public void compile()
    {
        sentencia+="$";
        String token="";
        for(int i=0;i<sentencia.length();i++)
        {
            char pointer=sentencia.toCharArray()[i];
            BuscarIdentificadoresReservadas:
            if((pointer>64 && pointer<91) || (pointer>96 && pointer<123) || pointer==95)
            {
                int j=i;
                for(;j<sentencia.length() && validarCaracterParaID(sentencia.toCharArray()[j]);j++)
                {
                    token+=sentencia.toCharArray()[j];
                }
                i=j-1;
                pointer=sentencia.toCharArray()[i];
                Tokens tokens=buscarEnLista(token,Tipo.IDENTIFICADOR);
                for(Tokens t:plantilla)
                {
                    if(token.equals(t.getNombre()))
                    {
                        tokens.setId(t.getId());
                        tokens.setNombre(t.getNombre());
                        tokens.setDescripcion(t.getDescripcion());
                        tokens.setTipo(t.getTipo());
                        break;
                    }
                }
                resultados.add(tokens);
                token="";
            }
            BuscarNumerosEnteros:
            if(pointer>47 && pointer<58)
            {
                int j=i;
                for(;j<sentencia.length() && validarNumero(sentencia.toCharArray()[j]);j++)
                {
                    token+=sentencia.toCharArray()[j];
                }
                Tokens tokens=buscarEnLista(token,Tipo.NUMERO);
                resultados.add(tokens);
                token="";
                i=j-1;
                pointer=sentencia.toCharArray()[i];
            }
            BuscarOperadores:
            if(existeOperador(0,pointer))
            {
                int j=i;
                int indice;
                for(indice=j-i;j<sentencia.length() && existeOperador(indice,sentencia.toCharArray()[j]);j++)
                {
                    token+=sentencia.toCharArray()[j];
                }
                Tokens tokens=buscarEnLista(token,Tipo.OPERADOR);
                resultados.add(tokens);
                token="";
                i=j-1;
                pointer=sentencia.toCharArray()[i];
            }
        }
    }
    private boolean existeOperador(int indice,char pointer)
    {
        for(Tokens t:plantilla)
        {
            if(t.getTipo()!=Tipo.OPERADOR) continue;
            if(indice<t.getNombre().length() && t.getNombre().toCharArray()[indice]==pointer)
            {
                System.out.println(t.getNombre()+""+pointer);
                return true;
            }
        }
        return false;
    }
    public String getResultados()
    {
        compile();
        return resultados.toString();
    }
    private boolean validarNumero(char pointer)
    {
        return (pointer>47 && pointer<57);
    }
    private boolean validarCaracterParaID(char pointer)
    {
        return ((pointer>64 && pointer<91) || (pointer>96 && pointer<123)) || (pointer>47 && pointer<57);
    }

    private Tokens buscarEnLista(String token,Tipo tipo)
    {
        Tokens tokens=new Tokens();
        for(Tokens l:plantilla)
        {
            if(l.getTipo().equals(tipo))
            {
                tokens.setId(l.getId());
                tokens.setNombre(token);
                tokens.setDescripcion(l.getDescripcion());
                tokens.setTipo(l.getTipo());
            }
        }
        return tokens;
    }
}
