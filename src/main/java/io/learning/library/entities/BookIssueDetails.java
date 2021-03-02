package io.learning.library.entities;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@RequiredArgsConstructor
public class BookIssueDetails{
    private final Book book;

    private final LocalDate issueDate;
}
