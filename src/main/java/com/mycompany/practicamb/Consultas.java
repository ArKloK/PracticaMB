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
        String fileName = "src\\main\\java\\ejemplo\\CISI.QRY"; //Ruta relativa hacia el fichero
        Scanner scan = new Scanner(new File(fileName));
        String texto = "";

        HttpSolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/micoleccion").build();

        while (scan.hasNext()) {
            String line = scan.nextLine();

            if (line.equals(".W")) {
                for (int j = 0; j < 5; j++) {
                    line = scan.next();
                    if (line.startsWith("(")) {
                        line = line.substring(1);
                    }
                    if (line.endsWith(")")) {
                        line = line.substring(0, line.length()-1);
                    }
                    texto += " " + line;
                }
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
        }
    }
}
