import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {
    String citySelector = "[data-test-id=city] input";
    String cityInvalidSelector = "[data-test-id=city].input_invalid .input__sub";
    String dateSelector = "[data-test-id=date] input";
    String dateInvalidSelector = "[data-test-id=date] .input_invalid .input__sub";
    String nameSelector = "[data-test-id=name] input";
    String nameInvalidSelector = "[data-test-id=name].input_invalid .input__sub";
    String phoneSelector = "[data-test-id=phone] input";
    String phoneInvalidSelector = "[data-test-id=phone].input_invalid .input__sub";
    String agreementSelector = "[data-test-id=agreement] span.checkbox__box";
    String agreementInvalidSelector = "[data-test-id=agreement].input_invalid";
    String buttonSelector = "button .button__text";
    String notificationTitleSelector = "[data-test-id=notification] .notification__title";
    String notificationContentSelector = "[data-test-id=notification] .notification__content";
    String cityPopupSelector = ".input__popup .menu";

    String emptyInputField = "Поле обязательно для заполнения";
    String sendButtonText = "Забронировать";
    String notificationTitle = "Успешно!";
    String notificationContent = "Встреча успешно забронирована на ";
    String invalidCityMsg = "Доставка в выбранный город недоступна";
    String notADateMsg = "Неверно введена дата";
    String invalidDateMsg = "Заказ на выбранную дату невозможен";
    String invalidNameMsg = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
    String invalidPhoneMsg = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";

    String city = "Москва";
    int shiftDate = 3;
    String name = "Иванов Иван";
    String phone = "+71111222333";

    String calculateDate(int amount) {
        return LocalDate.now().plusDays(amount).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setUpAll() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldSendApplication() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(notificationTitleSelector)
                .should(visible, Duration.ofSeconds(15)).shouldHave(text(notificationTitle));
        $(notificationContentSelector).shouldHave(visible, text(notificationContent + calculateDate(shiftDate)));

    }

    @Test
    void shouldNotPassValidationEmptyFields() {
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(cityInvalidSelector).shouldHave(text(emptyInputField));
        $(dateInvalidSelector).shouldNot(exist);
        $(nameInvalidSelector).shouldNot(exist);
        $(phoneInvalidSelector).shouldNot(exist);
        $(agreementInvalidSelector).shouldNot(exist);
        $(notificationTitleSelector).should(hidden);
    }

    @Test
    void shouldNoPassValidationWithoutAgreement() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(agreementInvalidSelector).should(visible);
        $(notificationTitleSelector).should(hidden);
    }

    @Test
    void shouldValidateFieldCityEmpty() {
        $(citySelector).setValue("");
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(cityInvalidSelector).should(visible).shouldHave(text(emptyInputField));
    }

    @Test
    void shouldValidateFieldCityLowerCase() {
        $(citySelector).setValue(city.toLowerCase());
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(cityInvalidSelector).shouldNot(exist);
    }

    @Test
    void shouldValidateFieldCityUpperCase() {
        $(citySelector).setValue(city.toUpperCase());
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(cityInvalidSelector).shouldNot(exist);
    }

    @Test
    void shouldValidateFieldCityNotACentre() {
        $(citySelector).setValue("Стерлитамак");
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(cityInvalidSelector).shouldHave(text(invalidCityMsg));
    }

    @Test
    void shouldValidateFieldCityMissedAHyphen() {
        $(citySelector).setValue("Ростов на дону");
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(cityInvalidSelector).shouldHave(text(invalidCityMsg));
    }

    @Test
    void shouldValidateFieldCityLatin() {
        $(citySelector).setValue("Chelyabinsk");
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(cityInvalidSelector).shouldHave(text(invalidCityMsg));
    }

    @Test
    void shouldValidateFieldCityNumbers() {
        $(citySelector).setValue("Москва99");
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(cityInvalidSelector).shouldHave(text(invalidCityMsg));
    }

    @Test
    void shouldValidateFieldCitySpecialChar() {
        $(citySelector).setValue("Москва№;%:");
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(cityInvalidSelector).shouldHave(text(invalidCityMsg));
    }

    @Test
    void shouldValidateFieldDateEmpty() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue("");
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(dateInvalidSelector).shouldHave(text(notADateMsg));
    }

    @Test
    void shouldValidateFieldDateBeforeAvailableDate() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate-1));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(dateInvalidSelector).shouldHave(text(invalidDateMsg));
    }

    @Test
    void shouldValidateFieldDateAfterAvailableDate() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate+1));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(dateInvalidSelector).shouldNot(exist);
    }

    @Test
    void shouldValidateFieldDateInvalidDay() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue("32.11.2024");
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(dateInvalidSelector).shouldHave(text(notADateMsg));
    }

    @Test
    void shouldValidateFieldDateInvalidMonth() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue("20.30.2024");
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(dateInvalidSelector).shouldHave(text(notADateMsg));
    }

    @Test
    void shouldValidateFieldDateInvalidYear() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue("01.06.24");
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(dateInvalidSelector).shouldHave(text(notADateMsg));
    }

    @Test
    void shouldValidateFieldDateOnlyNumbers() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue("222");
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(dateInvalidSelector).shouldHave(text(notADateMsg));
    }

    @Test
    void shouldValidateFieldDateLatinLetters() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue("03 may 2024");
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(dateInvalidSelector).shouldHave(text(notADateMsg));
    }

    @Test
    void shouldValidateFieldDateCyrillicLetters() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue("03 май 2024");
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(dateInvalidSelector).shouldHave(text(notADateMsg));
    }

    @Test
    void shouldValidateFieldDateSpecialChar() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(";%:?*(");
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(dateInvalidSelector).shouldHave(text(notADateMsg));
    }

    @Test
    void shouldValidateFieldNameEmpty() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue("");
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(nameInvalidSelector).shouldHave(text(emptyInputField));
    }

    @Test
    void shouldValidateFieldNameWithHyphen() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue("Иванова Анна-Мария");
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(nameInvalidSelector).shouldNot(exist);
    }

    @Test
    void shouldValidateFieldNameLowerCase() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name.toLowerCase());
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(nameInvalidSelector).shouldNot(exist);
    }

    @Test
    void shouldValidateFieldNameUpperCase() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name.toUpperCase());
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(nameInvalidSelector).shouldNot(exist);
    }

    @Test
    void shouldValidateFieldNameLatin() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue("Ivanov Ivan");
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(nameInvalidSelector).shouldHave(text(invalidNameMsg));
    }

    @Test
    void shouldValidateFieldNameNumbers() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue("Иванов Иван36");
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(nameInvalidSelector).shouldHave(text(invalidNameMsg));
    }

    @Test
    void shouldValidateFieldNameSpecialChar() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue("%^&*()");
        $(phoneSelector).setValue(phone);
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(nameInvalidSelector).shouldHave(text(invalidNameMsg));
    }

    @Test
    void shouldValidateFieldPhoneEmpty() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue("");
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(phoneInvalidSelector).shouldHave(text(emptyInputField));
    }

    @Test
    void shouldValidateFieldPhoneLess11Digits() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue("+7911112233");
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(phoneInvalidSelector).shouldHave(text(invalidPhoneMsg));
    }

    @Test
    void shouldValidateFieldPhoneMore11Digits() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue(phone + "9");
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(phoneInvalidSelector).shouldHave(text(invalidPhoneMsg));
    }

    @Test
    void shouldValidateFieldPhoneWithoutPlus() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue("79111122333");
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(phoneInvalidSelector).shouldHave(text(invalidPhoneMsg));
    }

    @Test
    void shouldValidateFieldPhonePlusAtTheEnd() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue("79111122333+");
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(phoneInvalidSelector).shouldHave(text(invalidPhoneMsg));
    }

    @Test
    void shouldValidateFieldPhoneLatin() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue("tel79111122333");
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(phoneInvalidSelector).shouldHave(text(invalidPhoneMsg));
    }

    @Test
    void shouldValidateFieldPhoneCyrillic() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue("тел79111122333");
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(phoneInvalidSelector).shouldHave(text(invalidPhoneMsg));
    }

    @Test
    void shouldValidateFieldPhoneSpecialChar() {
        $(citySelector).setValue(city);
        $(dateSelector).doubleClick().sendKeys(Keys.BACK_SPACE);
        $(dateSelector).setValue(calculateDate(shiftDate));
        $(nameSelector).setValue(name);
        $(phoneSelector).setValue("%^&*(79111122333");
        $(agreementSelector).click();
        $(buttonSelector).shouldHave(text(sendButtonText)).click();
        $(phoneInvalidSelector).shouldHave(text(invalidPhoneMsg));
    }

}
