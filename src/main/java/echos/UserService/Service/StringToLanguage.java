package echos.UserService.Service;

import echos.UserService.UserLanguage;

public class StringToLanguage {



    public static UserLanguage stringToLanguage(String language) {
        if (language != null) {
            if (language.equals("en") || language.equals("English")|| language.equals("ENGLISH")) {
                return UserLanguage.valueOf("English");
            }
            else if (language.equals("zh") || language.equals("Chinese") || language.equals("CHINESE") ) {
                return UserLanguage.valueOf("Chinese");
            }
            else{
                throw new IllegalArgumentException("Language preference does not exist");
            }
        }
        else{
            throw new IllegalArgumentException("User Preference does not exist");
        }
    }

}
