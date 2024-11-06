package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;
import tn.esprit.spring.services.InstructorServicesImpl;

public class InstructorServicesImplTestMock {

    @Mock
    private IInstructorRepository instructorRepository;  // Mocked repository for Instructor

    @Mock
    private ICourseRepository courseRepository;  // Mocked repository for Course

    @InjectMocks
    private InstructorServicesImpl instructorService;  // The service class to be tested

    private Instructor instructor;

    @BeforeEach
    public void setUp() {
        // Initializing the instructor object before each test
        instructor = new Instructor();
        instructor.setFirstName("John");
        instructor.setLastName("Doe");
        instructor.setDateOfHire(LocalDate.of(2015, 1, 1));  // 5+ years of experience
    }

    @Test
    public void shouldAddInstructorAndAssignCourses_whenValidData() {
        // Valid course IDs
        Set<Long> courseIds = Set.of(1L, 2L, 3L);

        // Mocking the course repository responses
        Course course1 = new Course();
        course1.setNumCourse(1L);
        course1.setTimeSlot(1);

        Course course2 = new Course();
        course2.setNumCourse(2L);
        course2.setTimeSlot(2);

        Course course3 = new Course();
        course3.setNumCourse(3L);
        course3.setTimeSlot(3);

        // Setting up mocks for course repository
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course1));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(course2));
        when(courseRepository.findById(3L)).thenReturn(Optional.of(course3));

        // Mocking instructor repository save method
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // Service call
        Instructor savedInstructor = instructorService.addInstructorAndAssignCourses(instructor, courseIds);

        // Assertions
        assertNotNull(savedInstructor);  // Ensuring instructor is saved
        assertEquals(3, savedInstructor.getCourses().size());  // Checking number of courses assigned
        verify(instructorRepository, times(1)).save(instructor);  // Verifying save was called once
    }
}
