package com.mycompany.practicamb;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class Consultas {

    public static void main(String[] args) throws SolrServerException, IOException {
        String fileName = "src\\main\\java\\ejemplo\\consultas.txt"; //Ruta relativa hacia el fichero
        Scanner scan = new Scanner(new File(fileName));
        String texto = "";

        HttpSolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/micoleccion").build();

        while (scan.hasNext()) {
            String line = scan.nextLine();
            if (line.equals(".W")) {
                while (scan.hasNext() && !line.equals(".I")) {
                    //Controlando que elija las lineas que contengan .W o terminen por . o por ? siempre y cuando haya una siguiente linea para seguir iterando
                    if ((line.equals(".W") || line.endsWith("?") || line.endsWith(".")) && scan.hasNext()) {
                        //Bucle para elegir las 5 primeras palabras de la consulta
                        for (int j = 0; j < 5; j++) {
                            line = scan.next();
                            texto += " " + line;
                        }
                        //Cuerpo de la consulta y mostrando los resultados
                        SolrQuery query = new SolrQuery();
                        query.setQuery("texto:" + texto);
                        query.setFields("id");
                        QueryResponse rsp = solr.query(query);
                        SolrDocumentList docs = rsp.getResults();
                        for (int i = 0; i < docs.size(); ++i) {
                            System.out.println(docs.get(i));
                        }
                        texto = "";
                    }
                    line = scan.nextLine();
                }
            }
        }
    }
}
