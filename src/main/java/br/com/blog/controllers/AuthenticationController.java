package br.com.blog.controllers;


import br.com.blog.dtos.forms.AuthenticationDTO;
import br.com.blog.dtos.forms.LoginResponseDTO;
import br.com.blog.dtos.forms.RegisterDTO;
import br.com.blog.models.User;
import br.com.blog.models.UserRole;
import br.com.blog.repositories.UserRepository;
import br.com.blog.security.TokenService;
import br.com.blog.service.impl.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private EmailService emailService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data){
        if(this.repository.findByLogin(data.getLogin()) != null) return ResponseEntity.badRequest().body("Login ja existente!");
        if(this.repository.findByEmail(data.getEmail()) != null) return ResponseEntity.badRequest().body("Email ja existente!");

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());
        User newUser = new User(data.getLogin(), encryptedPassword, UserRole.USER, data.getEmail());
        User save = this.repository.save(newUser);

        enviarEmail(save);
        return ResponseEntity.ok().build();
    }

    public void enviarEmail(User user) {
        String destinatario = user.getEmail();
        String assunto = "Pronto para Iniciar? Sua Conta em nosso Blog-Desafio ja está ativada!";
        String corpo = "Agradecemos por se juntar à nossa comunidade no Blog-Desafio. Estamos ansiosos para ver suas contribuições!";

        emailService.sendSimpleMessage(destinatario, assunto, corpo);
    }
}
