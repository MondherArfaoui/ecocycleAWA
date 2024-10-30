package tn.esprit.ecocycleawa;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Dechet {
    String uri;
    String nom;
    double poids;
    double tauxRecyclage;
    CentreCollecte centreCollecte;
    CentreRecyclage centreRecyclage;
}
