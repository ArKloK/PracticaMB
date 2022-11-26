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
        String diccionario = "src\\main\\java\\ejemplo\\diccionario.txt";

        //Fichero donde guardamos los resultados de las consultas
        File fichero = new File("C:\\Users\\Carlos\\Documents\\UHU\\MB\\queryresults.txt");
        //File fichero = new File("D:\\Documentos\\UHU\\MB\\queryresults.txt");
        fichero.createNewFile();
        //Indicamos el documento donde vamos a escribir
        BufferedWriter bw = new BufferedWriter(new FileWriter(fichero));
        Scanner scandiccionarioaux = new Scanner(new File(diccionario));
        Scanner scan = new Scanner(new File(fileName));
        String texto = "", indice = "";

        HttpSolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/micoleccion").build();

        while (scan.hasNext()) {
            String line = scan.nextLine();

            if (line.startsWith(".I")) {
                indice = line.substring(3);
            } else if (line.equals(".W")) {
                while (!line.startsWith(".I") && !line.equals(".B")) {
                    line = scan.next();
                    Scanner scandiccionario = new Scanner(new File(diccionario));
                    
                    while(scandiccionario.hasNext()){
                        String tokendiccionario = scandiccionario.next();
                        if (line.startsWith(tokendiccionario)) {
                            line = line.substring(1);
                        }else if (line.endsWith(tokendiccionario)) {
                            line = line.substring(0, line.length()-1);
                        }
                    }
                    /*if (line.endsWith(":")) {
                        line = line.substring(0, line.length()-1);
                    }*/
                    texto += line + " ";
                    if (scan.hasNext(".I") || scan.hasNext(".B")) {
                        break;
                    }
                }
                System.out.println(indice + " " + texto);
                SolrQuery query = new SolrQuery();
                query.setQuery("texto:" + texto);
                query.setFields("id", "score");
                QueryResponse rsp = solr.query(query);
                SolrDocumentList docs = rsp.getResults();
                for (int i = 0; i < docs.size(); ++i) {
                    bw.write(indice + " Q0 " + docs.get(i).getFieldValue("id") + " " + i + " " + docs.get(i).getFieldValue("score") + " UHU\n");
                }
                texto = "";
            }
        }

        bw.close();
    }
}
