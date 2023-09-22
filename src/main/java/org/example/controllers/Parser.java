package org.example.controllers;
import org.jetbrains.annotations.NotNull;
import org.example.models.Tokens;

import java.util.List;

public class Parser {
    private List<Tokens> resultados;
    public Parser(@NotNull Scanner scanner)
    {
        resultados=scanner.getListaToken();
    }

    public void run()
    {
        
    }
}
