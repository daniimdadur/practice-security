package org.security.practice.basic.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.security.practice.basic.util.CommonUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_user")
public class UserEntity implements UserDetails {
    //interface UserDetails digunakan untuk menyediakan informasi tentang pengguna yang diperlukan untuk autentikasi dan otorisasi.
    private static final long serialVersionUID = -7513004010560492767L;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "first_name", length = 64)
    private String firstName;

    @Column(name = "last_name", length = 64)
    private String lastName;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "password", length = 64)
    private String password;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER) //Menentukan hubungan many-to-many dengan entitas RoleEntity, dan semua peran akan dimuat segera setelah entitas pengguna dimuat.
    @JoinTable(name = "t_user_role", //Menentukan nama tabel penghubung yang akan digunakan dalam basis data.
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), //Anotasi ini mendefinisikan kolom-kolom di tabel penghubung yang merujuk ke entitas utama (dalam hal ini, UserEntity).
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id") //Anotasi ini mendefinisikan kolom-kolom di tabel penghubung yang merujuk ke entitas terkait (dalam hal ini, RoleEntity).
    )
    private List<RoleEntity> roles = new ArrayList<>();

    public UserEntity(String firstName, String lastName, String email, String password, List<RoleEntity> roles) {
        this.id = CommonUtil.getUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    @Override //Mengembalikan daftar otoritas (hak akses) yang dimiliki pengguna.
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = this.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}
