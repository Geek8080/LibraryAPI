package io.learning.library.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.learning.library.assembler.BookModelAssembler;
import io.learning.library.assembler.user.StudentModelAssembler;
import io.learning.library.entities.Author;
import io.learning.library.entities.Book;
import io.learning.library.entities.user.Student;
import io.learning.library.repository.BookRepository;
import io.learning.library.repository.user.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    StudentRepository studentRepository;

    @MockBean
    BookRepository bookRepository;

    @SpyBean
    StudentModelAssembler studentModelAssembler;

    @SpyBean
    BookModelAssembler bookModelAssembler;

    @Captor
    ArgumentCaptor<Student> studentCaptor;

    @Autowired
    ObjectMapper mapper;

    List<Student> students;
    List<Book> books;

    @BeforeEach
    void setUp() {
        students = new ArrayList<>();
        books = new ArrayList<>();

        Book book1 = new Book();
        book1.setId(1L);
        book1.setName("The Book Thief");
        book1.setAuthors(new ArrayList<>());
        book1.getAuthors().add(new Author("Markus Zusak"));
        book1.setEdition(1.0f);
        book1.setAvailableCopies(4);
        book1.setIssuedCopies(1);

        Book book2 = new Book();
        book2.setId(2L);
        book2.setName("Diary of a Young Girl");
        book2.setAuthors(new ArrayList<>());
        book2.getAuthors().add(new Author("Anne Frank"));
        book2.setEdition(1.0f);
        book2.setAvailableCopies(6);
        book2.setIssuedCopies(2);


        Student student1 = new Student();
        student1.setId(3L);
        student1.setName("Alpha Pandey");
        student1.setIssueDetails(new HashMap());
        student1.getIssueDetails().put(1L, LocalDate.now().plusMonths(3));
        student1.getIssueDetails().put(2L, LocalDate.now().plusMonths(3));

        Student student2 = new Student();
        student2.setId(4L);
        student2.setName("Beta Pandey");
        student2.setIssueDetails(new HashMap());
        student2.getIssueDetails().put(2L, LocalDate.now().plusMonths(3));

        books.add(book1);
        books.add(book2);
        students.add(student1);
        students.add(student2);

        given(studentRepository.findAll()).willReturn(students);
        given(studentRepository.findById(3L)).willReturn(Optional.of(student1));
        given(studentRepository.findById(4L)).willReturn(Optional.of(student2));
        given(studentRepository.findById(any(Long.class))).willReturn(null);

    }

    @Test
    void all() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/students").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.students", hasSize(2)))
                .andExpect(jsonPath("$._embedded.students[0].id", is(3)))
                .andExpect(jsonPath("$._embedded.students[0].issueDetails").isMap());
    }

    @Test
    void addNew() throws Exception {
        Student student = new Student();
        student.setId(6L);
        student.setName("Gamma Pandey");
        student.setIssueDetails(new HashMap<>());

        given(studentRepository.save(studentCaptor.capture())).willAnswer(i -> i.getArgument(0));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/students").contentType(MediaType.APPLICATION_JSON_VALUE)
                                    .accept(MediaType.APPLICATION_JSON)
                                    .characterEncoding("UTF-8").content(mapper.writeValueAsBytes(student)))
                        .andExpect(status().isCreated());

        students.add(studentCaptor.getValue());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/students").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.students", hasSize(3)))
                .andExpect(jsonPath("$._embedded.students[2].id").value(6));
    }

    @Test
    void one() {
        mockMvc.perform(MockMvcRequestBuilders.get("/students/{id}", ))
    }

    @Test
    void updateStudent() {
    }

    @Test
    void issueBook() {
    }

    @Test
    void books() {
    }

    @Test
    void reissueAllBooks() {
    }

    @Test
    void reissueBook() {
    }

    @Test
    void returnAllBooks() {
    }

    @Test
    void returnBook() {
    }

    @Test
    void deleteStudent() {
    }
}