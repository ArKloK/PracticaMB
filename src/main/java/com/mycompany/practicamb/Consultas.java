package com.mycompany.practicamb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class Consultas {

    public static void main(String[] args) throws SolrServerException, IOException {
        //Ruta relativa hacia el fichero
        String fileName = "src\\main\\java\\ejemplo\\CISI.QRY";

        int contador = 0, j = 0;
        //Fichero donde guardamos los resultados de las consultas
        File fichero = new File("C:\\Users\\Carlos\\Documents\\UHU\\MB\\PracticaMB\\src\\main\\java\\ejemplo\\queryresults.txt");
        fichero.createNewFile();
        //Indicamos el documento donde vamos a escribir
        BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
        Scanner scan = new Scanner(new File(fileName));
        String texto = "", indice = "";

        SolrDocumentList docs = null;

        HttpSolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/micoleccion").build();

        while (scan.hasNext()) {
            String line = scan.nextLine();

            if (line.startsWith(".I")) {
                indice = line.substring(3);
            } else if (line.equals(".W")) {
                for (int i = 0; i < 5; i++) {
                    line = scan.next();
                    if (line.startsWith("(")) {
                        line = line.substring(1);
                    }
                    if (line.endsWith(")")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    if (line.endsWith(".") || line.endsWith("?")) {
                        line = line.substring(0, line.length() - 1);
                        texto += " " + line;
                        break;
                    } else
                        texto += " " + line;
                }
                //System.out.println(indice+ " " + texto);
                SolrQuery query = new SolrQuery();
                query.setQuery("texto:" + texto + " OR titulo:" + texto);
                query.setFields("id", "score");
                QueryResponse rsp = solr.query(query);
                docs = rsp.getResults();
                for (int i = 0; i < docs.size(); ++i) {
                    bw.write(indice + " Q0 " + docs.get(i).getFieldValue("id") + " " + contador + " " + docs.get(i).getFieldValue("score") + " UHU\n");
                    contador++;
                }
                texto = "";
            }
        }

        bw.close();
    }
}
