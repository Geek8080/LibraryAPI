package io.learning.library;

import io.learning.library.entities.Author;
import io.learning.library.entities.Book;
import io.learning.library.entities.user.Admin;
import io.learning.library.repository.AuthorRepository;
import io.learning.library.repository.BookRepository;
import io.learning.library.repository.user.AdminRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class Helper implements CommandLineRunner {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    AdminRepository adminRepository;

    @Override
    public void run(String... args) {

        Author author1 = new Author("Author1");
        Author author2 = new Author("Author2");
        Author author3 = new Author("Author3");

        Book book1 = new Book("Book1", Arrays.asList(author1), 1.0f, 3, 0);
        Book book2 = new Book("Book2", Arrays.asList(author1, author2), 1.0f, 3, 0);
        Book book3 = new Book("Book3", Arrays.asList(author3), 1.0f, 3, 0);
        Book book4 = new Book("Book4", Arrays.asList(author1, author2, author3), 1.0f, 3, 0);
        Book book5 = new Book("Book5", Arrays.asList(author1, new Author("Author3")), 1.0f, 3, 0);

        bookRepository.saveAll(Arrays.asList(book1, book2, book3, book4, book5));
//        try (BufferedReader reader = new BufferedReader(new FileReader("books.csv"))) {
//            for (int i = 0; i < 99; i++) {
//                String[] bookArray = reader.readLine().trim().split(",");
//                String bookName = bookArray[0].trim();
//                String[] authors = bookArray[1].split("/");
//                //log.info(Arrays.toString(authors));
//                List<Author> authorsList = new ArrayList<>();
//                for (String author : authors) {
//                    Author authorEntity = authorRepository.readAuthorByNameEqualsIgnoringCase(author);
//                    if (authorEntity == null){
//                        authorRepository.save(new Author(author));
//                    }
//                    authorsList.add(authorEntity);
//                }
//                int issued = 0;
//                float edition = (int)(Math.random()*19) + 1;
//                int available = (int)(Math.random()*9) + 1;
//                bookRepository.save(new Book(bookName, authorsList, edition, available, issued));
//            }
//            log.info("Preloading admin: " + adminRepository.save(new Admin("admin", "pass")));
//            //bookRepository.findAll().stream().forEach(book -> log.info("Preloaded book: " + book));
//            //authorRepository.findAll().stream().forEach(author -> log.info("Preloading author: " + author));
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
    }
}
