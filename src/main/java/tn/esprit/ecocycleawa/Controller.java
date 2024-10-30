package tn.esprit.ecocycleawa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@CrossOrigin("*")
@RequestMapping("/api")
public class Controller {
    @Autowired
    private SPARQLService sparqlService;

    @GetMapping("/getUserDetails/{email}/{password}")
    public String getUserDetails(@PathVariable String email, @PathVariable String password) throws UnsupportedEncodingException {
        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "SELECT ?type ?nom ?email ?password ?role ?matricule ?telephone\n" +
                "WHERE {\n" +
                "  ?admin a ?type ;\n" +
                "         :nom ?nom ;\n" +
                "         :email ?email ;\n" +
                "         :password ?password ;\n" +
                "         :role ?role .\n" +
                "  OPTIONAL { ?admin :matricule ?matricule }\n" +
                "  OPTIONAL { ?admin :telephone ?telephone }\n" +
                "  FILTER(?email = \"" + email + "\" && ?password = \"" + password + "\")\n" +
                "  FILTER(?type IN (:Admin, :AdminCentreRecyclage, :AdminCentreCollecte))\n" +
                "}";
        return sparqlService.executeSparqlQuery(query);
    }

    @GetMapping("/getListZoneCollecte")
    public String getListZoneCollecte() throws UnsupportedEncodingException {
        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "SELECT ?zoneCollecte ?nom ?adresse\n" +
                "WHERE {\n" +
                "  ?zoneCollecte rdf:type :ZoneCollecte ;\n" +
                "                :nom ?nom ;\n" +
                "                :adresse ?adresse .\n" +
                "}";
        return sparqlService.executeSparqlQuery(query);
    }

    @GetMapping("/getListAdminCentreCollecte")
    public String getListAdminCentreCollecte() throws UnsupportedEncodingException {
        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "SELECT ?admin ?nom ?email ?password ?telephone ?matricule ?role\n" +
                "WHERE {\n" +
                "  ?admin rdf:type :AdminCentreCollecte ;\n" +
                "         :nom ?nom ;\n" +
                "         :email ?email ;\n" +
                "         :password ?password ;\n" +
                "         :telephone ?telephone ;\n" +
                "         :matricule ?matricule ;\n" +
                "         :role ?role .\n" +
                "}";
        return sparqlService.executeSparqlQuery(query);
    }

    @GetMapping("/getListAdminCentreRecyclage")
    public String getListAdminCentreRecyclage() throws UnsupportedEncodingException {
        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "SELECT ?admin ?nom ?email ?password ?telephone ?matricule ?role\n" +
                "WHERE {\n" +
                "  ?admin rdf:type :AdminCentreRecyclage ;\n" +
                "         :nom ?nom ;\n" +
                "         :email ?email ;\n" +
                "         :password ?password ;\n" +
                "         :telephone ?telephone ;\n" +
                "         :matricule ?matricule ;\n" +
                "         :role ?role .\n" +
                "}";
        return sparqlService.executeSparqlQuery(query);
    }

    @GetMapping("/getCentreCollecteByAdminCentreCollecte/{matricule}")
    public String getCentreCollecteByAdminCentreCollecte(@PathVariable String matricule) throws UnsupportedEncodingException {
        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "SELECT ?centreCollecte ?nomCentre ?capaciteCentre ?adresseCentre ?nomZone ?adresseZone\n" +
                "WHERE {\n" +
                "  ?admin rdf:type :AdminCentreCollecte ;\n" +
                "         :matricule ?adminMatricule .\n" +
                "  ?admin :gèreCentreCollecte ?centreCollecte .\n" +
                "  ?centreCollecte :nom ?nomCentre ;\n" +
                "                   :capacite ?capaciteCentre ;\n" +
                "                   :adresse ?adresseCentre ;\n" +
                "                   :localiseDans ?zoneCollecte .\n" +
                "  ?zoneCollecte :nom ?nomZone ;\n" +
                "                :adresse ?adresseZone .\n" +
                "  FILTER(?adminMatricule = \"" + matricule + "\")\n" +
                "}";
        return sparqlService.executeSparqlQuery(query);
    }

    @GetMapping("/getCentreRecyclageByAdminCentreRecyclage/{matricule}")
    public String getCentreRecyclageByAdminCentreRecyclage(@PathVariable String matricule) throws UnsupportedEncodingException {
        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "SELECT ?centreRecyclage ?nomCentre ?capaciteCentre ?adresseCentre\n" +
                "WHERE {\n" +
                "  ?admin rdf:type :AdminCentreRecyclage ;\n" +
                "         :matricule ?adminMatricule .\n" +
                "  ?admin :gèreCentreRecyclage ?centreRecyclage .\n" +
                "  ?centreRecyclage :nom ?nomCentre ;\n" +
                "                    :capacite ?capaciteCentre ;\n" +
                "                    :adresse ?adresseCentre .\n" +
                "  FILTER(?adminMatricule = \"" + matricule + "\")\n" +
                "}";
        return sparqlService.executeSparqlQuery(query);
    }

    @GetMapping("/getListTypeDechetByAdminCentreCollecte/{matricule}")
    public String getListTypeDechetByAdminCentreCollecte(@PathVariable String matricule) throws UnsupportedEncodingException {
        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "SELECT ?typeDechet ?typeDechetNom ?typeDechetDangereux ?typeDechetRecyclable ?typeDechetDescription\n" +
                "WHERE {\n" +
                "  ?admin rdf:type :AdminCentreCollecte ;\n" +
                "         :matricule ?adminMatricule ;\n" +
                "         :gèreTypeDechet ?typeDechet .\n" +
                "  ?typeDechet :nom ?typeDechetNom ;\n" +
                "             :dangereux ?typeDechetDangereux ;\n" +
                "             :recyclable ?typeDechetRecyclable ;\n" +
                "             :description ?typeDechetDescription .\n" +
                "  FILTER(?adminMatricule = \"" + matricule + "\")\n" +
                "}";
        return sparqlService.executeSparqlQuery(query);
    }

    @GetMapping("/getListDechetByAdminCentreCollecte/{matricule}")
    public String getListDechetByAdminCentreCollecte(@PathVariable String matricule) throws UnsupportedEncodingException {
        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "SELECT ?dechet ?dechetNom ?quantiteDechet ?typeDechetNom ?typeDechetDangereux ?typeDechetRecyclable ?typeDechetDescription\n" +
                "WHERE {\n" +
                "  ?admin rdf:type :AdminCentreCollecte ;\n" +
                "         :matricule ?adminMatricule ;\n" +
                "         :gèreDechet ?dechet .\n" +
                "  ?dechet :nom ?dechetNom ;\n" +
                "          :quantite ?quantiteDechet .\n" +
                "  ?typeDechet :estTypeDechetDe ?dechet ;\n" +
                "             :nom ?typeDechetNom ;\n" +
                "             :dangereux ?typeDechetDangereux ;\n" +
                "             :recyclable ?typeDechetRecyclable ;\n" +
                "             :description ?typeDechetDescription .\n" +
                "  FILTER(?adminMatricule = \"" + matricule + "\")\n" +
                "}";
        return sparqlService.executeSparqlQuery(query);
    }

    @GetMapping("/getListTypeRecyclageByAdminCentreRecyclage/{matricule}")
    public String getListTypeRecyclageByAdminCentreRecyclage(@PathVariable String matricule) throws UnsupportedEncodingException {
        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "SELECT ?typeRecyclage ?typeRecyclageNom ?typeRecyclageDescription\n" +
                "WHERE {\n" +
                "  ?admin rdf:type :AdminCentreRecyclage ;\n" +
                "         :matricule ?adminMatricule ;\n" +
                "         :gèreTypeRecyclage ?typeRecyclage .\n" +
                "  ?typeRecyclage :nom ?typeRecyclageNom ;\n" +
                "                :description ?typeRecyclageDescription .\n" +
                "  FILTER(?adminMatricule = \"" + matricule + "\")\n" +
                "}";
        return sparqlService.executeSparqlQuery(query);
    }

    @GetMapping("/getListMatierePremiereByAdminCentreRecyclage/{matricule}")
    public String getListMatierePremiereByAdminCentreRecyclage(@PathVariable String matricule) throws UnsupportedEncodingException {
        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "SELECT ?matierePremiere ?matierePremiereNom ?quantiteMatierePremiere ?typeRecyclageNom ?typeRecyclageDescription\n" +
                "WHERE {\n" +
                "  ?admin rdf:type :AdminCentreRecyclage ;\n" +
                "         :matricule ?adminMatricule ;\n" +
                "         :gèreMatierePremiere ?matierePremiere .\n" +
                "  ?matierePremiere :nom ?matierePremiereNom ;\n" +
                "                  :quantite ?quantiteMatierePremiere .\n" +
                "  ?typeRecyclage :estTypeRecyclageDe ?matierePremiere ;\n" +
                "                :nom ?typeRecyclageNom ;\n" +
                "                :description ?typeRecyclageDescription .\n" +
                "  FILTER(?adminMatricule = \"" + matricule + "\")\n" +
                "}";
        return sparqlService.executeSparqlQuery(query);
    }

    @PostMapping("/addZoneCollecte")
    public String addZoneCollecte(@RequestParam String nom, @RequestParam String adresse) {
        try {
            sparqlService.addZoneCollecte(nom, adresse);
            return "Zone Collecte ajoutée avec succès";
        } catch (Exception e) {
            return "Erreur lors de l'ajout de la Zone Collecte : " + e.getMessage();
        }
    }

    @DeleteMapping("/deleteZoneCollecte/{nom}")
    public String deleteZoneCollecte(@PathVariable String nom) {
        try {
            sparqlService.deleteZoneCollecte(nom);
            return "Zone Collecte supprimée avec succès";
        } catch (Exception e) {
            return "Erreur lors de la suppression de la Zone Collecte : " + e.getMessage();
        }
    }

    @PostMapping("/addAdminCentreRecyclage")
    public String addAdminCentreRecyclage(@RequestParam String nom, @RequestParam String email, @RequestParam String password, @RequestParam String telephone, @RequestParam String matricule) {
        try {
            sparqlService.addAdminCentreRecyclage(nom, email, password, telephone, matricule);
            return "Admin Centre Recyclage ajouté avec succès";
        } catch (Exception e) {
            return "Erreur lors de l'ajout de l'Admin Centre Recyclage : " + e.getMessage();
        }
    }

    @PostMapping("/addRecyclingCenterbyAdminCentreRecyclage")
    public ResponseEntity<String> addRecyclingCenterbyAdminCentreRecyclage(
            @RequestParam("adminMatricule") String adminMatricule,
            @RequestParam("centerName") String centerName, // Assurez-vous que c'est bien le nom attendu dans la requête
            @RequestParam("centerCapacite") int centerCapacite,
            @RequestParam("centerAddress") String centerAddress) {

        try {
            sparqlService.addAndAssignRecyclingCenter(adminMatricule, centerName, centerCapacite, centerAddress);
            return ResponseEntity.ok("Centre de recyclage ajouté avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'ajout du centre de recyclage : " + e.getMessage());
        }
    }

    @GetMapping("/getListRecyclingCenter")
    public String getListRecyclingCenter() throws UnsupportedEncodingException {
        String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX : <http://www.semanticweb.org/arfao/ontologies/2024/8/untitled-ontology-7#>\n" +
                "SELECT ?centreRecyclage ?nom\n" +
                "WHERE {\n" +
                "  ?centreRecyclage rdf:type :CentreRecyclage ;\n" +
                "                :nom ?nom .\n" +
                "}";
        return sparqlService.executeSparqlQuery(query);
    }



}
