package com.bank.verification;

import com.bank.user.enums.Gender;
import com.bank.user.jpa.dto.UserRequestDto;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class NationalNumberValidator {

    public void passed(UserRequestDto user) {
        if (!(genderMatchesIdentificationNumber(user)
                && identificationNumberStartsWithDateOfBirth(user)
                && identificationNumberWeightIsCorrect(user))) {
            throw new InvalidNationalNumberException("Invalid National Number");
        }
    }

    private boolean genderMatchesIdentificationNumber(UserRequestDto user) {
        Gender gender;
        if (Integer.parseInt(user.nationalNumber()
                .substring(9, 10)) % 2 == 0) {
            gender = Gender.FEMALE;
        } else {
            gender = Gender.MALE;
        }
        return gender == user.gender();
    }

    private boolean identificationNumberStartsWithDateOfBirth(UserRequestDto user) {
        String dateOfBirthString = user.birthDate()
                .format(DateTimeFormatter.ofPattern("yyMMdd"));
        if (dateOfBirthString.charAt(0) == '0') {
            int monthNum = Integer.parseInt(dateOfBirthString.substring(2, 4));
            monthNum += 20;
            dateOfBirthString = dateOfBirthString
                    .substring(0, 2) + monthNum + dateOfBirthString.substring(4, 6);
        }
        return dateOfBirthString
                .equals(user.nationalNumber().substring(0, 6));
    }

    private boolean identificationNumberWeightIsCorrect(UserRequestDto user) {
        int[] weights = {1, 3, 7, 9, 1, 3, 7, 9, 1, 3};

        int weightSum = 0;
        for (int i = 0; i < 10; i++) {
            weightSum += Integer.parseInt(user.nationalNumber()
                    .substring(i, i + 1)) * weights[i];
        }

        int actualSum = 10 - weightSum % 10;

        int checkSum = Integer
                .parseInt(user.nationalNumber().substring(10, 11));

        return actualSum == checkSum;
    }
}
