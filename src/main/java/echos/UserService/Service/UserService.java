package echos.UserService.Service;

import echos.AuthorizationService.Service.RoleService;
import echos.CharacterService.DTO.CharacterMapper;
import echos.CharacterService.Entity.Character;
import echos.CharacterService.Repository.UserFavoriteRepository;
import echos.CharacterService.Service.CharacterService;
import echos.UserService.DTO.UserDTO;
import echos.UserService.DTO.UserMapper;
import echos.UserService.Entity.User;
import echos.UserService.Repository.UserPreferenceRepository;
import echos.UserService.Repository.UserRepository;
import echos.UtilityService.ExceptionManagement.UserAlreadyExistsException;
import echos.UtilityService.PasswordUtil;
import echos.UtilityService.RSADecryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import echos.UserService.UserStatus;
import org.springframework.transaction.annotation.Transactional;


import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserPreferenceService userPreferenceService;
    @Autowired
    private UserPreferenceRepository userPreferenceRepository;
    @Autowired
    private UserFavoriteRepository userFavoriteRepository;

    @Lazy
    @Autowired
    private CharacterService characterService;


    // 根据条件搜索用户
    public List<User> searchUsers(String phone, String email, String nickname, UserStatus status) {
        return userRepository.findUsersByCriteria(phone, email, nickname, status);
    }

    // 根据 ID 获取用户
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    //根据email获取用户
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    //根据手机号获取用户
    public User getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }




    // 检查手机号是否存在
    private boolean phoneExists(String phone) {

        if (phone == null) {

            return false;
        }
        return userRepository.findByPhone(phone) != null;
    }

    // 检查邮箱是否存在
    private boolean emailExists(String email) {
        if (email == null) {
            return false;
        }
        return userRepository.findByEmail(email) != null;
    }

    //根据手机号与rsa加密密码验证登录
    public boolean userLoginCheckByPhone(String phone, String rsaPassword) {
        // 解密前端加密的密码
        String decryptedPassword = RSADecryptUtil.decrypt(rsaPassword);
        User user = userRepository.findByPhone(phone);

        if (user == null) {
            return false;
        }

        String UserPassword = user.getPassword();
        return PasswordUtil.matches(decryptedPassword,UserPassword);
    }



//    public boolean userLoginCheckByPhoneMicroService(String phone, String rsaPassword) {
//        try {
//            // Step 1: 调用 RSA 服务解密密码
//            String rsaDecryptUrl = UriComponentsBuilder
//                    .fromHttpUrl(serviceDomain + "/rsa/decrypt")
//                    .queryParam("encryptedPassword", rsaPassword)
//                    .toUriString();
//            String decryptedPassword = restTemplate.getForObject(rsaDecryptUrl, String.class);
//
//            // Step 2: 调用 Password 服务进行哈希加密
//            String hashEncryptUrl = UriComponentsBuilder
//                    .fromHttpUrl(serviceDomain + "/rsa/hashEncrypt")
//                    .queryParam("password", decryptedPassword)
//                    .toUriString();
//            String hashedPassword = restTemplate.getForObject(hashEncryptUrl, String.class);
//
//            // Step 3: 数据库查询验证
//            return userRepository.findByPhoneAndHashedPassword(phone, hashedPassword) != null;
//
//        } catch (Exception e) {
//            // 捕获并处理远程服务调用异常
//            throw new RuntimeException("Failed to authenticate user", e);
//        }
//    }



    //根据邮箱与rsa加密密码验证登录
    public boolean userLoginCheckByEmail(String email, String rsaPassword) {
        // 解密前端加密的密码
        String decryptedPassword = RSADecryptUtil.decrypt(rsaPassword);

        // 对解密后的密码进行哈希加密
        String hashedPassword = PasswordUtil.encode(decryptedPassword);
        User user = userRepository.findByEmail(email);

        //判断用户名是否正确
        if (user == null) {
            return false;
        }


        String UserPassword = user.getPassword();
        return PasswordUtil.matches(decryptedPassword,UserPassword);
    }

    private boolean validatePassword(String decryptedPassword) {
        // 添加后端密码验证
        return decryptedPassword != null && decryptedPassword.length() >= 3;
    }

    //创建新用户
    @Transactional
    public User createUser(UserDTO userDTO) throws GeneralSecurityException, UnsupportedEncodingException {
        // 先进行存在性检查

        if (emailExists(userDTO.getEmail())) {

            throw new UserAlreadyExistsException("User with email " + userDTO.getEmail() + " already exists");
        }

        if (phoneExists(userDTO.getPhone())) {

            throw new UserAlreadyExistsException("User with phone " + userDTO.getPhone() + " already exists");
        }

        User user = new User();
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setNickname(userDTO.getNickname());
        user.setStatus(userDTO.getStatus());
        if (userDTO.getRole_id() == null) {
            user.setRoleId(1L);
        }
        else{
            user.setRoleId(userDTO.getRole_id());
        }

        // 解密前端加密的密码
        String decryptedPassword = RSADecryptUtil.decrypt(userDTO.getPassword());
        
        // 添加后端验证
        if (!validatePassword(decryptedPassword)) {
            throw new IllegalArgumentException("Invalid password format");
        }

        System.out.println("注册时解密密码");

        System.out.println(decryptedPassword);


        // 对解密后的密码进行哈希加密
        String hashedPassword = PasswordUtil.encode(decryptedPassword);

        System.out.println("数据库密码");

        System.out.println(hashedPassword);

        user.setPassword(hashedPassword);

        try {
            // 直接返回保存结果
            return userRepository.save(user);
        } catch (DataAccessException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e); // 通用异常包装
        }
    }



    // 更新用户
    @Transactional
    public User updateUser(Long id, UserDTO userDTO) throws GeneralSecurityException, UnsupportedEncodingException {
        // 查找用户是否存在
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        //更新字段
        if (userDTO.getNickname() != null) {
            existingUser.setNickname(userDTO.getNickname());
        }

        if (userDTO.getStatus() != null) {
            existingUser.setStatus(userDTO.getStatus());
        }

        if (userDTO.getPassword() != null) {
            // 解密前端加密的密码
            String decryptedPassword = RSADecryptUtil.decrypt(userDTO.getPassword());

            // 对解密后的密码进行哈希加密
            String hashedPassword = PasswordUtil.encode(decryptedPassword);

            existingUser.setPassword(hashedPassword);
        }

        // 保存更新后的用户
        return userRepository.save(existingUser);
    }

    // 删除用户
    @Transactional
    public void deleteUser(Long UserId, Long operator_role_id) {

        if (!roleService.isAdmin(operator_role_id)){
            throw new IllegalArgumentException("Required admin permission");
        }

        //先删除用户对应的preference
        userPreferenceService.deleteUserPreferenceByUserId(UserId);
        System.out.println("顺利删除用户偏好设置");

        //删除用户和角色关联关系
        userRepository.deleteUserFavoritesByUserId(UserId);
        System.out.println("顺利删除用户和角色关联关系");

        User user = getUserById(UserId);

        //把所有角色的所有权移交给admin
        List<Character> characters = characterService.getOriginCharactersByOwnerId(UserId);
        Long admin_role_id = roleService.getAdminRoleId();
        Long target_owner = userRepository.findByRoleId(admin_role_id).getFirst().getId();
        for (Character character : characters) {
            characterService.updateOwner(character, target_owner);
        }

        userRepository.delete(user);
        System.out.println("顺利删除用户");

    }

    //管理员的用户列表
    public List<UserDTO> adminGetAllUsers(Long role_id) {

        System.out.println("服务方法" + role_id);
        if (!roleService.isAdmin(role_id)){
            throw new IllegalArgumentException("Required admin permission");
        }

        List<UserDTO> dtos = UserMapper.toDTOList(userRepository.findAll());
        for (UserDTO dto : dtos) {
            dto.setRole_name(roleService.getRoleById(dto.getRole_id()).getRoleName());
        }

        return dtos;
    }
}
