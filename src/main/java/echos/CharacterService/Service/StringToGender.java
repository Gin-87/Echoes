package echos.CharacterService.Service;

import echos.CharacterService.CharacterGender;
import echos.UserService.UserLanguage;

public class StringToGender {

    public static CharacterGender StringToGenderMethod(String gender) {
        if (gender != null) {
            if (gender.equals("Male") || gender.equals("male")) {
                return CharacterGender.Male;
            }
            else if (gender.equals("female") || gender.equals("Female")) {
                return CharacterGender.Female;
            }
            else if(gender.equals("unknown") || gender.equals("Unknown")) {
                return CharacterGender.Unknown;}

            else{
                throw new IllegalArgumentException("Gender  does not exist");
            }
        }
        else{
            throw new IllegalArgumentException("Gender does not exist");
        }
    }
}

