package com.mycompany.practicamb;

import java.io.*;
import java.util.*;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;

public class IndexClass {

    public static void main(String[] args) throws SolrServerException, IOException {
        //Creo una sesion de Solr
        final SolrClient client = new HttpSolrClient.Builder("http://localhost:8983/solr").build();

        //Forma alternativa de crear core en Solr 
            //String coreName = "micoleccion";
            //String solrDir = "/../solr-9.0.0/server/solr/micoleccion";
            //CoreAdminRequest.Create createRequest = new CoreAdminRequest.Create();
            //createRequest.setCoreName(coreName);
            //createRequest.setInstanceDir(solrDir);
            //createRequest.process(client); //Se ejecuta solo la primera vez que arranco Slor
        //String fileName = "src\\main\\java\\ejemplo\\ejemplo.txt"; //Ruta relativa hacia el fichero
        String fileName = "D:\\Documentos\\UHU\\MB\\CISI.ALL";
        Scanner scan = new Scanner(new File(fileName));
        String titulo = "", autor = "", texto = "", indice = "";//Variables donde irán los valores temporales del documento
        SolrInputDocument doc = new SolrInputDocument();
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            if (line.contains(".I")) {
                indice = line.substring(3);//Leemos justo el valor que hay en el indice 3 cortando el String por esa parte
                doc.addField("id", indice);
            }
            if (line.contains(".T")) {
                while (!line.contains(".A")) {
                    line = scan.nextLine();
                    if (!line.contains(".A")) {
                        titulo = titulo + " " + line;
                    } else {
                        doc.addField("titulo", titulo);
                        titulo = "";
                    }
                }
            }
            if (line.contains(".A")) {
                while (!line.equals(".W")) {
                    line = scan.nextLine();
                    if (!line.equals(".W")) {
                        doc.addField("autor", line);
                    } else {
                        autor = "";
                    }
                }
            }
            if (line.contains(".W")) {
                while (!line.contains(".X")) {
                    line = scan.nextLine();
                    if (!line.contains(".X")) {
                        texto = texto + " " + line;//Una vez haya identificado .W leemos y concatenamos la cadena de caracteres hasta que nos encontramos .X
                    } else {
                        doc.addField("texto", texto);
                        texto = "";
                    }
                }
            }
            if (line.contentEquals(".X")) {
                final UpdateResponse updateResponse = client.add("micoleccion", doc);//Subimos el documento completo
                doc = new SolrInputDocument();//Luego creamos otro documento independiente
            }
        }
        client.commit("micoleccion");
    }
}
