package com.logistics.shipment_tracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is mandatory")
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;
    public String getEmail() { return this.email; }
    public String getPassword() { return this.password; }
    public Role getRole() { return this.role; }
    public void setPassword(String password) {
        this.password = password;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public void setEmail(String email) { this.email = email; }


    public void setRole(Role role) { this.role = role; }
    
    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name='" + name + '\'' + ", email='" + email + '\'' + ", role=" + role + '}';
    }

}
