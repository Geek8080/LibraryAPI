package io.learning.library.entities.user;

import io.learning.library.entities.Book;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ElementCollection
    private Map<Book, LocalDate> issueDetails;

    public float getFine(){
        float fine = 0.0f;

        for(Map.Entry<Book, LocalDate> issued: issueDetails.entrySet()) {
            LocalDate dateIssued = issued.getValue();
            fine += ((getExpectedReturnDate(dateIssued).isBefore(LocalDate.now())) ? ChronoUnit.DAYS.between(LocalDate.now(), getExpectedReturnDate(dateIssued)) : 0);
        }

        return fine*0.5f;
    }

    private LocalDate getExpectedReturnDate(LocalDate issueDate){
        return issueDate.plusMonths(3);
    }
}
