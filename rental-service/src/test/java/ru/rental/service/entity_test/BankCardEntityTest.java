package ru.rental.service.entity_test;

import jakarta.persistence.*;
import org.junit.jupiter.api.Test;
import ru.rental.service.entity.BankCard;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BankCardEntityTest {

    @Test
    void bankCardEntity_shouldHaveCorrectJpaMappings() throws NoSuchFieldException {
        // Проверка аннотации @Entity
        assertThat(BankCard.class.isAnnotationPresent(Entity.class)).isTrue();

        // Проверка аннотации @Table
        Table tableAnnotation = BankCard.class.getAnnotation(Table.class);
        assertThat(tableAnnotation.name()).isEqualTo("bank_cards");

        // Проверка маппинга полей
        checkFieldMapping(BankCard.class, "id", Id.class);
        checkFieldMapping(BankCard.class, "numberCard", Column.class);
        checkFieldMapping(BankCard.class, "expirationDate", Column.class);
        checkFieldMapping(BankCard.class, "secretCode", Column.class);
        checkFieldMapping(BankCard.class, "user", ManyToOne.class);

        // Проверка конкретных параметров аннотаций
        Field numberCardField = BankCard.class.getDeclaredField("numberCard");
        Column numberCardColumn = numberCardField.getAnnotation(Column.class);
        assertThat(numberCardColumn.name()).isEqualTo("number_card");
        assertThat(numberCardColumn.nullable()).isFalse();

        // Проверка связи с User
        Field userField = BankCard.class.getDeclaredField("user");
        JoinColumn joinColumn = userField.getAnnotation(JoinColumn.class);
        assertThat(joinColumn.name()).isEqualTo("user_id");
        assertThat(joinColumn.nullable()).isFalse();

        ManyToOne manyToOne = userField.getAnnotation(ManyToOne.class);
        assertThat(manyToOne.fetch()).isEqualTo(FetchType.LAZY);
    }

    @Test
    void bankCardEntity_shouldHaveWorkingLombokAnnotations() {
        // Проверка работы @Builder, @NoArgsConstructor, @AllArgsConstructor
        BankCard card = BankCard.builder()
                .id(1)
                .numberCard("1234567890123456")
                .expirationDate("12/25")
                .secretCode(123)
                .build();

        assertThat(card.getId()).isEqualTo(1);
        assertThat(card.getNumberCard()).isEqualTo("1234567890123456");

        // Проверка @Data (toString, equals, hashCode)
        BankCard card2 = new BankCard();
        card2.setId(1);
        assertThat(card).isNotEqualTo(card2);
        assertThat(card.toString()).isNotBlank();
    }

    private void checkFieldMapping(Class<?> clazz, String fieldName, Class<? extends Annotation> annotationClass)
            throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        assertThat(field.isAnnotationPresent(annotationClass)).isTrue();
    }
}
