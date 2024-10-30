package tn.esprit.ecocycleawa;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MatierePremiere {

    String uri;
    String nom;
}
