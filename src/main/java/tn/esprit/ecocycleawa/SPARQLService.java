package tn.esprit.ecocycleawa;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.jena.update.*;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.FileOutputStream;
import java.io.OutputStream;

@Service
public class SPARQLService {
    @Autowired
    private OntModel model;

    public String executeSparqlQuery(String queryString) throws UnsupportedEncodingException {
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, model);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.outputAsJSON(outputStream, results);
            return outputStream.toString("UTF-8");
        } finally {
            qexec.close();
        }
    }

    public void addZoneCollecte(String nom, String adresse) {
        // Création d'un URI propre à chaque ZoneCollecte basé sur son nom
        String cleanNom = nom.replace(" ", "_"); // Remplace les espaces par des underscores pour un URI valide
        String zoneCollecteUri = "http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#" + cleanNom;

        String queryString = "" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "INSERT DATA {\n" +
                "  <" + zoneCollecteUri + "> rdf:type :ZoneCollecte ;\n" + // Utilisation de l'URI
                "            :nom \"" + nom + "\" ;\n" +
                "            :adresse \"" + adresse + "\" .\n" +
                "}";

        UpdateRequest updateRequest = UpdateFactory.create(queryString);
        Dataset dataset = DatasetFactory.create(model);
        UpdateProcessor updateProcessor = UpdateExecutionFactory.create(updateRequest, dataset);
        updateProcessor.execute();

        // Sauvegarder le modèle modifié dans le fichier
        saveModel();
    }

    public void deleteZoneCollecte(String nom) {
        String queryString = "" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "DELETE WHERE {\n" +
                "  ?zone rdf:type :ZoneCollecte ;\n" +
                "        :nom \"" + nom + "\" .\n" +
                "}";

        UpdateRequest updateRequest = UpdateFactory.create(queryString);
        // Conversion de OntModel en Dataset pour l'exécution
        Dataset dataset = DatasetFactory.create(model);
        UpdateProcessor updateProcessor = UpdateExecutionFactory.create(updateRequest, dataset);
        updateProcessor.execute();

        // Sauvegarder le modèle modifié dans le fichier
        saveModel();
    }


    private void saveModel() {
        try {
            // Assurez-vous que le chemin d'accès est correct et que l'application a les droits d'écriture sur le fichier
            OutputStream out = new FileOutputStream(new ClassPathResource("static/ecocycle4.owl").getFile());
            model.write(out, "RDF/XML");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
