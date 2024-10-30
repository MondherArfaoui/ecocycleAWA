package tn.esprit.ecocycleawa;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.query.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

import org.apache.jena.update.*;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.springframework.core.io.ClassPathResource;

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

    public void addAdminCentreRecyclage(String nom, String email, String password, String telephone, String matricule) {
        // Création d'un URI propre à chaque AdminCentreRecyclage basé sur son nom
        String cleanNom = nom.replace(" ", "_"); // Remplace les espaces par des underscores pour un URI valide
        String adminCentreRecyclageUri = "http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#" + cleanNom;

        String queryString = "" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "INSERT DATA {\n" +
                "  <" + adminCentreRecyclageUri + "> rdf:type :AdminCentreRecyclage ;\n" + // Utilisation de l'URI
                "             :nom \"" + nom + "\" ;\n" +
                "             :email \"" + email + "\" ;\n" +
                "             :password \"" + password + "\" ;\n" +
                "             :telephone \"" + telephone + "\" ;\n" +
                "             :matricule \"" + matricule + "\" ;\n" +
                "             :role \"adminCentreRecyclage\" .\n" +
                "}";

        UpdateRequest updateRequest = UpdateFactory.create(queryString);
        // Conversion de OntModel en Dataset pour l'exécution
        Dataset dataset = DatasetFactory.create(model);
        UpdateProcessor updateProcessor = UpdateExecutionFactory.create(updateRequest, dataset);
        updateProcessor.execute();

        // Sauvegarder le modèle modifié dans le fichier
        saveModel();
    }

    public void addAndAssignRecyclingCenter(String adminMatricule, String centerName, int centerCapacite, String centerAddress) {
        // Création d'un URI propre à chaque CentreRecyclage basé sur son nom
        String cleanNom = centerName.replace(" ", "_");
        String centreRecyclageUri = "http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#" + cleanNom;

        // Requête SPARQL pour ajouter et affecter le centre de recyclage
        String queryString = "" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "INSERT DATA {\n" +
                "  <" + centreRecyclageUri + "> rdf:type :CentreRecyclage ;\n" +
                "             :nom \"" + centerName + "\" ;\n" +
                "             :capacite " + centerCapacite + " ;\n" +
                "             :adresse \"" + centerAddress + "\" .\n" +
                "}\n" +
                ";\n" +
                "INSERT {\n" +
                "  ?admin :gèreCentreRecyclage <" + centreRecyclageUri + "> .\n" +
                "}\n" +
                "WHERE {\n" +
                "  ?admin rdf:type :AdminCentreRecyclage ;\n" +
                "         :matricule \"" + adminMatricule + "\" .\n" +
                "}";

        System.out.println("Executing SPARQL Query: " + queryString);

        UpdateRequest updateRequest = UpdateFactory.create(queryString);
        // Conversion de OntModel en Dataset pour l'exécution
        Dataset dataset = DatasetFactory.create(model);
        UpdateProcessor updateProcessor = UpdateExecutionFactory.create(updateRequest, dataset);
        updateProcessor.execute();

        // Sauvegarder le modèle modifié dans le fichier
        saveModel();
    }

    public void addAndAssignMatierePremiere(String adminMatricule, String matierePremiereName, int matierePremiereQuantite, String matierePremiereType) {
        // Création d'un URI propre à chaque MatierePremiere basé sur son nom
        String cleanNom = matierePremiereName.replace(" ", "_");
        String matierePremiereUri = "http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#" + cleanNom;

        // URI pour le TypeRecyclage
        String typeRecyclageUri = "http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#" + matierePremiereType.replace(" ", "_");

        // Requête SPARQL pour ajouter le MatierePremiere et son association TypeRecyclage
        String queryString = "" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "INSERT DATA {\n" +
                "  <" + matierePremiereUri + "> rdf:type :MatierePremiere ;\n" +
                "             :nom \"" + matierePremiereName + "\" ;\n" +
                "             :quantite \"" + matierePremiereQuantite + "\" .\n" +
                "  <" + typeRecyclageUri + "> rdf:type :TypeRecyclage ;\n" +
                "             :estTypeRecyclageDe <" + matierePremiereUri + "> .\n" +  // Correcte direction de la propriété
                "}\n" +
                ";\n" +
                "INSERT {\n" +
                "  ?admin :gèreMatierePremiere <" + matierePremiereUri + "> .\n" +
                "}\n" +
                "WHERE {\n" +
                "  ?admin rdf:type :AdminCentreRecyclage ;\n" +
                "         :matricule \"" + adminMatricule + "\" .\n" +
                "}";

        System.out.println("Executing SPARQL Query: " + queryString);

        UpdateRequest updateRequest = UpdateFactory.create(queryString);
        // Conversion de OntModel en Dataset pour l'exécution
        Dataset dataset = DatasetFactory.create(model);
        UpdateProcessor updateProcessor = UpdateExecutionFactory.create(updateRequest, dataset);
        updateProcessor.execute();

        // Sauvegarder le modèle modifié dans le fichier
        saveModel();
    }

    public void addTypeRecyclage(String adminMatricule, String typeRecyclageName, String typeRecyclageDescription) {
        // Create a URI for the new TypeRecyclage based on its name
        String cleanTypeName = typeRecyclageName.replace(" ", "_");
        String typeRecyclageUri = "http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#" + cleanTypeName;

        // SPARQL query to insert the new TypeRecyclage and assign it to the admin
        String queryString = "" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "INSERT DATA {\n" +
                "  <" + typeRecyclageUri + "> rdf:type :TypeRecyclage ;\n" +
                "                           :nom \"" + typeRecyclageName + "\" ;\n" +
                "                           :description \"" + typeRecyclageDescription + "\" .\n" +
                "}\n" +
                ";\n" +
                "INSERT {\n" +
                "  ?admin :gèreTypeRecyclage <" + typeRecyclageUri + "> .\n" +
                "}\n" +
                "WHERE {\n" +
                "  ?admin rdf:type :AdminCentreRecyclage ;\n" +
                "         :matricule \"" + adminMatricule + "\" .\n" +
                "}";

        System.out.println("Executing SPARQL Query: " + queryString);

        UpdateRequest updateRequest = UpdateFactory.create(queryString);
        // Execute the update query on the ontology model
        Dataset dataset = DatasetFactory.create(model);
        UpdateProcessor updateProcessor = UpdateExecutionFactory.create(updateRequest, dataset);
        updateProcessor.execute();

        // Save the updated model to the file
        saveModel();
    }

    public void deleteTypeRecyclage(String typeRecyclageName) {
        // Création d'un URI propre à chaque TypeRecyclage basé sur son nom pour s'assurer de cibler le bon élément
        String cleanNom = typeRecyclageName.replace(" ", "_");
        String typeRecyclageUri = "http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#" + cleanNom;

        // Requête SPARQL pour supprimer un TypeRecyclage spécifique
        String queryString = "" +
                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "DELETE WHERE {\n" +
                "  <" + typeRecyclageUri + "> ?p ?o .\n" +  // Supprime toutes les triplets où le TypeRecyclage est le sujet
                "}";

        System.out.println("Executing SPARQL Query: " + queryString);

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
            OutputStream out = new FileOutputStream(new ClassPathResource("static/ecocycle4.owl").getFile());
            model.write(out, "RDF/XML");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
