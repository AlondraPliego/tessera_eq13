package com.TESSERA.Eq13Tessera.auth.service;

import com.TESSERA.Eq13Tessera.auth.dto.ActualizarPerfilClienteRequestDTO;
import com.TESSERA.Eq13Tessera.auth.dto.PerfilClienteResponseDTO;
import com.TESSERA.Eq13Tessera.auth.dto.PerfilEmpresaResponseDTO;
import com.TESSERA.Eq13Tessera.auth.dto.RegisterRequestDTO;
import com.TESSERA.Eq13Tessera.auth.entity.Cliente;
import com.TESSERA.Eq13Tessera.auth.entity.Empresa;
import com.TESSERA.Eq13Tessera.auth.entity.Rol;
import com.TESSERA.Eq13Tessera.auth.entity.Usuario;
import com.TESSERA.Eq13Tessera.auth.exception.DatosRegistroInvalidosException;
import com.TESSERA.Eq13Tessera.auth.exception.RolNoEncontradoException;
import com.TESSERA.Eq13Tessera.auth.exception.UsuarioYaExisteException;
import com.TESSERA.Eq13Tessera.auth.repository.ClienteRepository;
import com.TESSERA.Eq13Tessera.auth.repository.EmpresaRepository;
import com.TESSERA.Eq13Tessera.auth.repository.RolRepository;
import com.TESSERA.Eq13Tessera.auth.repository.UsuarioRepository;
import com.TESSERA.Eq13Tessera.common.exception.ResourceNotFoundException;
import com.TESSERA.Eq13Tessera.notificaciones.service.MailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final ClienteRepository clienteRepository;
    private final EmpresaRepository empresaRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                           RolRepository rolRepository,
                           ClienteRepository clienteRepository,
                           EmpresaRepository empresaRepository,
                           PasswordEncoder passwordEncoder,
                           MailService mailService) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.clienteRepository = clienteRepository;
        this.empresaRepository = empresaRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    }

    @Transactional
    public Usuario registrar(RegisterRequestDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new UsuarioYaExisteException("Ya existe una cuenta con ese correo");
        }

        // solo cliente y empresa para crear
        String nombreRol = dto.getRolNombre().toUpperCase();
        if (!nombreRol.equals("CLIENTE") && !nombreRol.equals("EMPRESA")) {
            throw new RolNoEncontradoException(
                    "Rol inválido: " + dto.getRolNombre() + ". Usa EMPRESA o CLIENTE");
        }

        Rol rol = rolRepository.findByNombre(nombreRol)
                .orElseThrow(() -> new RolNoEncontradoException(
                        "El rol " + nombreRol + " no existe en la base de datos"));

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        usuario.setRol(rol);
        usuario = usuarioRepository.save(usuario);

        if (nombreRol.equals("CLIENTE")) {
            crearDatosCliente(usuario, dto);
        } else {
            crearDatosEmpresa(usuario, dto);
        }

        mailService.enviarCorreoBienvenida(usuario.getEmail(), usuario.getNombre());

        return usuario;
    }

    private void crearDatosCliente(Usuario usuario, RegisterRequestDTO dto) {
        if (!StringUtils.hasText(dto.getNombreUsuario())) {
            throw new DatosRegistroInvalidosException("nombreUsuario es obligatorio para el rol CLIENTE");
        }
        if (clienteRepository.existsByNombreUsuario(dto.getNombreUsuario())) {
            throw new DatosRegistroInvalidosException("Ese nombre de usuario ya está en uso");
        }
        clienteRepository.save(new Cliente(usuario, dto.getNombreUsuario(), dto.getTelefono()));
    }

    @Transactional(readOnly = true)
    public PerfilClienteResponseDTO obtenerPerfilCliente(Long usuarioId) {
        Cliente cliente = clienteRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el perfil de cliente"));
        Usuario usuario = cliente.getUsuario();

        return new PerfilClienteResponseDTO(
                usuario.getNombre(),
                cliente.getApellidos(),
                usuario.getEmail(),
                cliente.getTelefono(),
                cliente.getFechaNacimiento()
        );
    }

    @Transactional
    public PerfilClienteResponseDTO actualizarPerfilCliente(Long usuarioId, ActualizarPerfilClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el perfil de cliente"));
        Usuario usuario = cliente.getUsuario();

        if (!usuario.getEmail().equalsIgnoreCase(dto.getCorreo())
                && usuarioRepository.existsByEmail(dto.getCorreo())) {
            throw new UsuarioYaExisteException("Ya existe una cuenta con ese correo");
        }

        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getCorreo());
        cliente.setApellidos(dto.getApellidos());
        cliente.setTelefono(dto.getTelefono());
        cliente.setFechaNacimiento(dto.getFechaNacimiento());

        usuarioRepository.save(usuario);
        clienteRepository.save(cliente);

        return new PerfilClienteResponseDTO(
                usuario.getNombre(),
                cliente.getApellidos(),
                usuario.getEmail(),
                cliente.getTelefono(),
                cliente.getFechaNacimiento()
        );
    }

    @Transactional(readOnly = true)
    public PerfilEmpresaResponseDTO obtenerPerfilEmpresa(Long usuarioId) {
        Empresa empresa = empresaRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el perfil de empresa"));
        Usuario usuario = empresa.getUsuario();

        return new PerfilEmpresaResponseDTO(
                empresa.getNombreEmpresa(),
                empresa.getRfc(),
                usuario.getEmail(),
                empresa.getTelefono(),
                empresa.getSitioWeb()
        );
    }

    private void crearDatosEmpresa(Usuario usuario, RegisterRequestDTO dto) {
        if (!StringUtils.hasText(dto.getNombreEmpresa()) || !StringUtils.hasText(dto.getRfc())) {
            throw new DatosRegistroInvalidosException(
                    "nombreEmpresa y rfc son obligatorios para el rol EMPRESA");
        }
        if (empresaRepository.existsByRfc(dto.getRfc())) {
            throw new DatosRegistroInvalidosException("Ese RFC ya está registrado");
        }
        empresaRepository.save(new Empresa(
                usuario, dto.getNombreEmpresa(), dto.getRfc(), dto.getTelefono(), dto.getSitioWeb()));
    }
}