package com.bank.verification;

import com.bank.user.enums.Gender;
import com.bank.user.jpa.dto.UserRequestDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@Log4j2
public class NationalNumberValidator {

    public boolean passed(UserRequestDto user) {
        log.info(" gender: " + genderMatchesIdentificationNumber(user) + " starts with date: " + identificationNumberStartsWithDateOfBirth(user) + " weight is:  " + identificationNumberWeightIsCorrect(user));
        return genderMatchesIdentificationNumber(user)
                && identificationNumberStartsWithDateOfBirth(user)
                && identificationNumberWeightIsCorrect(user);
    }

    private boolean genderMatchesIdentificationNumber(UserRequestDto user) {
        Gender userGender = user.gender();
        Gender genderFromDate;
        if (Integer.parseInt(user.nationalNumber()
                .substring(9, 10)) % 2 == 0) {
            genderFromDate = Gender.FEMALE;
        } else {
            genderFromDate = Gender.MALE;
        }
        return genderFromDate == userGender;
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
