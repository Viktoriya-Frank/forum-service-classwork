package ait.cohort55.accounting.service;

import ait.cohort55.accounting.dao.UserAccountRepository;
import ait.cohort55.accounting.dto.RolesDto;
import ait.cohort55.accounting.dto.UserDto;
import ait.cohort55.accounting.dto.UserEditDto;
import ait.cohort55.accounting.dto.UserRegisterDto;
import ait.cohort55.accounting.dto.exceptions.InvalidDataException;
import ait.cohort55.accounting.dto.exceptions.UserAlreadyExistsException;
import ait.cohort55.accounting.dto.exceptions.userNotFoundException;
import ait.cohort55.accounting.model.Role;
import ait.cohort55.accounting.model.UserAccount;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService, CommandLineRunner {
    private final UserAccountRepository userAccountRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        if(userAccountRepository.existsById(userRegisterDto.getLogin())) {
            throw new UserAlreadyExistsException();
        }
        UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
        String password = passwordEncoder.encode(userRegisterDto.getPassword());
        userAccount.setPassword(password);
        userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto getUser(String login) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(userNotFoundException::new);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto removeUser(String login) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(userNotFoundException::new);
        userAccountRepository.delete(userAccount);
        return modelMapper.map(userAccount, UserDto.class);
    }

    @Override
    public UserDto updateUser(String login, UserEditDto userEditDto) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(userNotFoundException::new);

        if (userEditDto.getFirstName() !=null) {
            userAccount.setFirstName(userEditDto.getFirstName());
        }
        if (userEditDto.getLastName() !=null) {
            userAccount.setLastName(userEditDto.getLastName());
        }
        userAccount = userAccountRepository.save(userAccount);
        return modelMapper.map(userAccount, UserDto.class);

    }

    @Override
    public RolesDto changeRolesList(String login, String role, boolean isAddRole) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(userNotFoundException::new);

        boolean res;
        role = role.toLowerCase();

        try {
            if(isAddRole) {
                res = userAccount.addRole(role);
            } else {
                res = userAccount.removeRole(role);
            }
        } catch (Exception e) {
            throw new InvalidDataException("Invalid role" + role);
        }
            if (res) {
                userAccountRepository.save(userAccount);
            }
            return modelMapper.map(userAccount, RolesDto.class);
    }

        @Override
    public void changePassword(String login, String newPassword) {
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(userNotFoundException::new);
        String password = passwordEncoder.encode(newPassword);
        userAccount.setPassword(newPassword);
        userAccountRepository.save(userAccount);

    }

    @Override
    public void run(String... args) throws Exception {
        if (!userAccountRepository.existsById("admin")) {
            UserAccount userAccount = new UserAccount("admin", passwordEncoder.encode("admin"), "", "");
            userAccount.addRole(Role.MODERATOR.name());
            userAccount.addRole(Role.ADMINISTRATOR.name());
            userAccountRepository.save(userAccount);
        }
    }
}
