package tn.esprit.spring;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.*;

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

    private IInstructorRepository instructorRepository;
    private ICourseRepository courseRepository;
    private InstructorServicesImpl instructorService;
    private Instructor instructor;

    @BeforeEach
    public void setUp() {
        // Manually initializing instructor and repositories for the test
        instructorRepository = mock(IInstructorRepository.class);
        courseRepository = mock(ICourseRepository.class);
        instructorService = new InstructorServicesImpl(instructorRepository, courseRepository);

        instructor = new Instructor();
        instructor.setFirstName("John");
        instructor.setLastName("Doe");
        instructor.setDateOfHire(LocalDate.of(2015, 1, 1));  // 5+ years of experience
    }

    @Test
    public void shouldThrowException_whenInstructorAssignedMoreThanMaxCourses() {
        // Test: More than 5 courses for an instructor with > 5 years of experience
        Set<Long> courseIds = Set.of(1L, 2L, 3L, 4L, 5L, 6L);  // Too many courses

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            instructorService.addInstructorAndAssignCourses(instructor, courseIds);
        });

        // Check the exception message
        assertEquals("Instructor cannot be assigned more than 5 courses", exception.getMessage());
    }

    @Test
    public void shouldThrowException_whenCourseNotFoundForAssignment() {
        // Test: Course does not exist
        Set<Long> courseIds = Set.of(1L, 2L);

        // Mock the course repository to return empty for both courses
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            instructorService.addInstructorAndAssignCourses(instructor, courseIds);
        });

        // Check the exception message
        assertEquals("Course not found", exception.getMessage());
    }
}
