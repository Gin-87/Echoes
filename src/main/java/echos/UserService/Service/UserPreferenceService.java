package echos.UserService.Service;

import echos.UserService.Entity.UserPreference;
import echos.UserService.Repository.UserPreferenceRepository;
import echos.UserService.UserLanguage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserPreferenceService {

    @Autowired
    private UserPreferenceRepository userPreferenceRepository;

    //根据user id 查询
    public UserPreference getUserPreferenceByUserId(Long userId) {
        return userPreferenceRepository.findByUserId(userId);
    }

    // 创建方法
    public UserPreference createUserPreference(Long UserId, String LanguagePreference) {

        UserPreference userPreference = new UserPreference();
        userPreference.setUserId(UserId);
        if (LanguagePreference != null) {
            if (LanguagePreference.equals("en")) {
                userPreference.setLanguagePrefer(UserLanguage.valueOf("English"));
            }
            else if (LanguagePreference.equals("zh")) {
                userPreference.setLanguagePrefer(UserLanguage.valueOf("Chinese"));
            }
            else{
                throw new IllegalArgumentException("Language preference does not exist");
            }
        }
        return userPreferenceRepository.save(userPreference);

    }

    // 更新user preference
    public  UserPreference updateUserPreference(Long UserId, String LanguagePreference) {
        UserPreference userPreference = getUserPreferenceByUserId(UserId);

        if (LanguagePreference != null) {
            if (LanguagePreference.equals("en") || LanguagePreference.equals("English")|| LanguagePreference.equals("ENGLISH")) {
                userPreference.setLanguagePrefer(UserLanguage.valueOf("English"));
            }
            else if (LanguagePreference.equals("zh") || LanguagePreference.equals("Chinese") || LanguagePreference.equals("CHINESE") ) {
                userPreference.setLanguagePrefer(UserLanguage.valueOf("Chinese"));
            }
            else{
                throw new IllegalArgumentException("Language preference does not exist");
            }
            return userPreferenceRepository.save(userPreference);

        }
        else{
            throw new IllegalArgumentException("User Preference does not exist");
        }

    }

    //根据userId删除
    @Transactional
    public void deleteUserPreferenceByUserId(Long UserId) {

        UserPreference userPreference = getUserPreferenceByUserId(UserId);
        if (userPreference != null) {
            userPreferenceRepository.delete(userPreference);
        }
    }




}