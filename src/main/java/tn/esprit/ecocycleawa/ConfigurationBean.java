package tn.esprit.ecocycleawa;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

@Configuration
public class ConfigurationBean {
    @Bean
    public OntModel ontModel() {
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        try {
            InputStream in = new ClassPathResource("static/ecocycle4.owl").getInputStream();
            model.read(in, null, "RDF/XML");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}
