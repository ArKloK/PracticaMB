package com.mycompany.practicamb;

import java.io.IOException;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

public class Consultas {
    public static void main(String[] args) throws SolrServerException, IOException{
        HttpSolrClient solr = new HttpSolrClient.Builder("http://localhost:8983/solr/micoleccion").build();
		
        SolrQuery query = new SolrQuery();
        query.setQuery("What problems and concerns are");
        //query.setQuery("Apple");
        //query.addFilterQuery("cat:electronics");
        //query.setFields("id");
        QueryResponse rsp = solr.query(query);
        
        SolrDocumentList docs = rsp.getResults();
	for (int i = 0; i < docs.size(); ++i) {
            System.out.println(docs.get(i));
        }
    }
}
