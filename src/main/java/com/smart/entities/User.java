package com.smart.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
// User ki details ka pata krne ke liye spring security uses UserDetails
// Toh humne user se hi implement krva diya userdetails
// Toh jaha bhi user details use krna hai vaha user ka object use kr skte hai
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    form ki input fields bhi same name ki banana jo name yaha use kre hai
    private int id;
    @Size(min=3,max=25,message = "Minimum 3 and Maximum 25 characters allowed")
    private String name;
    private  String password;
    @Column(unique = true,nullable = false)
    private String email;

    private String toDo;


    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<Question> questions=new ArrayList<>();

    private boolean isEnabled=true;
    private boolean emailVerified=false;
    private boolean phoneVerified=false;

    // from where did the user did signup
    // By default sefl hai, means khud se login kra hai
    private Providers provider=Providers.SELF;
    private String providerUserId;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", questions=" + questions +
                '}';
    }


    // methods to override because of implementing UserDetails
    // inhe implement krna padega elsa error aaega
    // isme ek getPassword() bhi override krna tha , but kiuki User class ke khud ke paas getPassword() hai toh vahi call
    // hoga, farak nhi padta dono mai apana ko this.password hi return krna tha

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    // spring security ke according humara username, email hai
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
