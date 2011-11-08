package my.simple.model.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    public void setEmail(String email) {
    }

    public void setFirstName(String firstName) {
    }

    public void setLastName(String lastName) {
    }

    public void setOrganisation(String organisation) {
    }

    private String id;

    @Id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
