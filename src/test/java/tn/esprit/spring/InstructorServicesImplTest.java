package tn.esprit.spring;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;
import tn.esprit.spring.services.InstructorServicesImpl;

@SpringBootTest  // Load the Spring context with MySQL
@TestMethodOrder(OrderAnnotation.class)  // Define the order of tests
@Transactional  // Each test is isolated in a transaction
@Rollback(true)  // Changes are rolled back after each test
public class InstructorServicesImplTest {
    @Autowired
    private InstructorServicesImpl instructorServices;

    @Autowired
    private IInstructorRepository instructorRepository;

    @Autowired
    private ICourseRepository courseRepository;


    private Instructor instructor;
    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        instructor = new Instructor(1L, "John", "Doe", LocalDate.now(), new HashSet<>());
        course = new Course(1L,1, TypeCourse.COLLECTIVE_CHILDREN, null,10F, 1, Collections.emptySet());
    }

    @Test
    void testAddInstructor() {
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);
        Instructor savedInstructor = instructorServices.addInstructor(instructor);
        assertEquals(instructor, savedInstructor);
        verify(instructorRepository, times(1)).save(instructor);
    }}
