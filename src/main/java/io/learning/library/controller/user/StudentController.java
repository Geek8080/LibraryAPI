package io.learning.library.controller.user;

import io.learning.library.assembler.BookModelAssembler;
import io.learning.library.assembler.user.StudentModelAssembler;
import io.learning.library.entities.Book;
import io.learning.library.entities.BookIssueDetails;
import io.learning.library.entities.user.Student;
import io.learning.library.exception.BookAlreadyIssuedException;
import io.learning.library.exception.BookNotAvailableException;
import io.learning.library.exception.BookNotFoundException;
import io.learning.library.exception.NoSuchBookIssuedException;
import io.learning.library.exception.user.MaximumBooksIssuedException;
import io.learning.library.exception.user.StudentNotFoundException;
import io.learning.library.repository.BookRepository;
import io.learning.library.repository.user.StudentRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api")
public class StudentController {

    StudentRepository studentRepository;
    StudentModelAssembler studentModelAssembler;

    BookRepository bookRepository;
    BookModelAssembler bookModelAssembler;

    public StudentController(StudentRepository studentRepository, StudentModelAssembler studentModelAssembler, BookRepository bookRepository, BookModelAssembler bookModelAssembler){
        this.studentRepository = studentRepository;
        this.studentModelAssembler = studentModelAssembler;
        this.bookRepository = bookRepository;
        this.bookModelAssembler = bookModelAssembler;
    }

    @GetMapping("/students")
    public CollectionModel<EntityModel<Student>> all(){
        List<EntityModel<Student>> students = studentRepository.findAll().stream()
                .map(studentModelAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(students,
                linkTo(methodOn(StudentController.class).all()).withSelfRel()
        );
    }

    @PostMapping("/students")
    ResponseEntity<?> addNew(@RequestBody Student student){
        EntityModel<Student> studentEntityModel = studentModelAssembler.toModel(studentRepository.save(student));

        return ResponseEntity
                .created(studentEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .build();
    }

    @GetMapping("/students/{id}")
    public EntityModel<Student> one(@PathVariable Long id){
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));

        return studentModelAssembler.toModel(student);
    }

    @PutMapping("/students/{id}")
    ResponseEntity<?> updateStudent(@RequestBody Student newStudent, @PathVariable Long id){
        Student student = studentRepository.findById(id).map(st -> {
            if (!newStudent.getName().equals("")){
                st.setName(newStudent.getName());
            }
            if (!newStudent.getIssueDetails().isEmpty()){
                st.setIssueDetails(newStudent.getIssueDetails());
            }
            return st;
        }).orElseGet(() -> {
            Student std = studentRepository.save(newStudent);
            newStudent.setId(std.getId());
            return newStudent;
        });

        EntityModel<Student> studentEntityModel = studentModelAssembler.toModel(studentRepository.save(student));

        return ResponseEntity
                .created(studentEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(studentEntityModel);
    }

    @PutMapping("/students/{id}/issueBook/{bookId}")
    ResponseEntity<?> issueBook(@PathVariable Long id, @PathVariable Long bookId){
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));

        if (book.getAvailableCopies()==0){
            throw new BookNotAvailableException(bookId);
        }
        if (student.getIssueDetails().size()==5){
            throw new MaximumBooksIssuedException();
        }
        if (student.getIssueDetails().containsKey(book)){
            throw new BookAlreadyIssuedException();
        }

        book.setIssuedCopies(book.getIssuedCopies()+1);
        book.setAvailableCopies(book.getAvailableCopies()-1);

        student.getIssueDetails().put(book.getId(), LocalDate.now());

        EntityModel<Student> studentEntityModel = studentModelAssembler.toModel(studentRepository.save(student));

        bookRepository.save(book);

        return ResponseEntity
                .created(studentEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(studentEntityModel);
    }

    @GetMapping("/students/{id}/books")
    public List<BookIssueDetails> books(@PathVariable Long id){

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        List<BookIssueDetails> bookIssueDetailsList = new ArrayList<>();

        for (Map.Entry<Long, LocalDate> entry: student.getIssueDetails().entrySet()){
            bookIssueDetailsList.add(
                    new BookIssueDetails(
                            bookRepository.findById(entry.getKey()).orElseThrow(() -> new BookNotFoundException(entry.getKey())),
                            entry.getValue()));
        }

        return bookIssueDetailsList;
    }

    @PutMapping("/students/{id}/books/reissue/all")
    ResponseEntity<?> reissueAllBooks(@PathVariable Long id){
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));

        Set<Map.Entry<Long, LocalDate>> issued = student.getIssueDetails().entrySet();

        student.setIssueDetails(new HashMap<>());

        for (Map.Entry<Long, LocalDate> entry : issued){
            student.getIssueDetails().put(entry.getKey(), LocalDate.now());
        }

        EntityModel<Student> studentEntityModel = studentModelAssembler.toModel(studentRepository.save(student));

        return ResponseEntity
                .created(studentEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(studentEntityModel);
    }

    @PutMapping("/students/{id}/books/reissue/{bookId}")
    ResponseEntity<?> reissueBook(@PathVariable Long id, @PathVariable Long bookId) throws NoSuchBookIssuedException {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));

        if (!student.getIssueDetails().containsKey(bookId)){
            throw new NoSuchBookIssuedException(id);
        }

        student.getIssueDetails().put(bookId, LocalDate.now());

        EntityModel<Student> studentEntityModel = studentModelAssembler.toModel(studentRepository.save(student));

        return ResponseEntity
                .created(studentEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(studentEntityModel);

    }

    @PutMapping("/students/{id}/books/return/all")
    ResponseEntity<?> returnAllBooks(@PathVariable Long id){
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));

        Set<Long> bookSet = student.getIssueDetails().keySet();
        List<Book> books = bookRepository.findAllById(bookSet);
        books.forEach(book -> {
            book.setAvailableCopies(book.getAvailableCopies()+1);
            book.setIssuedCopies(book.getIssuedCopies()-1);
        });


        student.setIssueDetails(new HashMap<>());

        bookRepository.saveAll(books);
        EntityModel<Student> studentEntityModel = studentModelAssembler.toModel(studentRepository.save(student));

        return ResponseEntity
                .created(studentEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(studentEntityModel);
    }

    @PutMapping("/students/{id}/books/return/{bookId}")
    ResponseEntity<?> returnBook(@PathVariable Long id, @PathVariable Long bookId) throws NoSuchBookIssuedException {
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
        if (!student.getIssueDetails().containsKey(bookId)){
            throw new NoSuchBookIssuedException(bookId);
        }
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        student.getIssueDetails().remove(book.getId());
        book.setIssuedCopies(book.getIssuedCopies()-1);
        book.setAvailableCopies(book.getAvailableCopies()+1);
        bookRepository.save(book);
        studentRepository.save(student);

        EntityModel<Student> studentEntityModel = studentModelAssembler.toModel(student);

        return ResponseEntity
                .created(studentEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(studentEntityModel);
    }

    @DeleteMapping("/students/{id}")
    ResponseEntity<?> deleteStudent(@PathVariable Long id){
        Student student = studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));

        reissueAllBooks(id);

        studentRepository.delete(student);

        return ResponseEntity
                .noContent().build();
    }

}
