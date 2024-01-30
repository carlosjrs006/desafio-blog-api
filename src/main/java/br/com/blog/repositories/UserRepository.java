package br.com.blog.repositories;

import br.com.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;


public interface UserRepository extends JpaRepository<User, String> {
    UserDetails findByLogin(String login);
    UserDetails findByEmail(String email);
}
