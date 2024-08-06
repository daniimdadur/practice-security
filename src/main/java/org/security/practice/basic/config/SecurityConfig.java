package org.security.practice.basic.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private String[] WHITE_LIST_URL = new String[] {
            "/assets/audio/**", "/assets/css/**", "/assets/img/**", "/assets/js/**", "/assets/json/**", "/assets/scripts/**", "/assets/svg/**", "/assets/vendor/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize //Metode ini digunakan untuk mendefinisikan aturan otorisasi untuk berbagai URL atau endpoint dalam aplikasi web.
                        .requestMatchers(WHITE_LIST_URL).permitAll() //Mengizinkan akses tanpa otentikasi ke semua URL yang tercantum dalam WHITE_LIST_URL.
                        .anyRequest().authenticated() //Mengharuskan semua permintaan lainnya untuk diautentikasi.
                )
                .formLogin(form -> form
                        .loginPage("/login") //Menentukan URL dari halaman login yang digunakan oleh pengguna untuk masuk ke aplikasi
                        .defaultSuccessUrl("/home", true) //Parameter pertama ("/home") adalah URL tujuan setelah login berhasil.
                                                                                      //Parameter kedua (true) menunjukkan bahwa setelah login, pengguna akan selalu diarahkan ke URL default ini, tidak peduli halaman mana yang mereka coba akses sebelumnya.
                        .failureUrl("/login?error=true") //Menentukan URL yang akan dialihkan pengguna jika login gagal.
                        .permitAll() //Halaman login dapat diakses oleh siapa saja tanpa memerlukan otentikasi.
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") //Menentukan URL endpoint untuk logout.
                        .logoutSuccessUrl("/login?logout=true") //URL yang akan diarahkan setelah pengguna berhasil logout.
                        .invalidateHttpSession(true) //Menghapus sesi HTTP setelah logout, memastikan bahwa sesi pengguna di server dihentikan.
                        .deleteCookies("JSESSIONID") //JSESSIONID adalah cookie yang digunakan oleh Java EE dan Spring Security untuk mengelola sesi pengguna.
                                                                          //Cookie ini menyimpan ID sesi yang unik untuk setiap pengguna yang mengunjungi aplikasi
                                                                          //Dengan menghapus cookie sesi, Anda memastikan bahwa sesi pengguna benar-benar berakhir setelah logout.
                        .permitAll() //Mengizinkan semua pengguna, terlepas dari status otentikasi mereka, untuk mengakses endpoint logout dan URL sukses logout.
                )
                .csrf(csrf -> csrf //CSRF (Cross-Site Request Forgery) adalah mekanisme keamanan yang melindungi aplikasi dari serangan di mana pengguna yang diautentikasi tidak sengaja melakukan tindakan yang tidak diinginkan di aplikasi web.
                        .ignoringRequestMatchers("/h2-console/**") //Bagian ini mengecualikan URL tertentu dari perlindungan CSRF.
                )
                .sessionManagement(session -> session //Metode ini mengatur kebijakan manajemen sesi dalam aplikasi Spring Security
                        .invalidSessionUrl("/login?invalid-session=true") //Menentukan URL yang akan diarahkan ketika sesi pengguna menjadi tidak valid  (misalnya, karena cookie sesi telah dihapus atau sesi telah kedaluwarsa)
                        .maximumSessions(1) //Dalam contoh ini, hanya satu sesi yang diizinkan per pengguna pada waktu yang bersamaan. Jika pengguna mencoba login dari perangkat lain, sesi yang sebelumnya akan diakhiri.
                        .expiredUrl("/login?session-expired=true") //Menentukan URL yang akan diarahkan ketika sesi pengguna telah kedaluwarsa.
                )
                .rememberMe(rememberMe -> rememberMe //ketika kita memilih opsi remember me
                        .key("uniqueAndSecret") //digunakan untuk enkripsi token "remember me"
                        .tokenValiditySeconds(86400) // tidak perlu login ulang sampai batas waktu yang ditentukan yaitu 24 jam
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //hashing password
    }
}
