package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;
import tn.esprit.spring.services.InstructorServicesImpl;

@SpringBootTest
public class InstructorServicesImplTest {

    @Mock
    private IInstructorRepository instructorRepository;
    
    @Mock
    private ICourseRepository courseRepository;
    
    @InjectMocks
    private InstructorServicesImpl instructorService;

    private Instructor instructor;

    @BeforeEach
    public void setUp() {
        // Initializing instructor and repositories
        instructor = new Instructor();
        instructor.setFirstName("John");
        instructor.setLastName("Doe");
        instructor.setDateOfHire(LocalDate.of(2015, 1, 1));  // 5+ years of experience
    }

    @Test
    public void shouldThrowException_whenInstructorAssignedMoreThanMaxCourses() {
        Set<Long> courseIds = Set.of(1L, 2L, 3L, 4L, 5L, 6L);  // Too many courses

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            instructorService.addInstructorAndAssignCourses(instructor, courseIds);
        });

        assertEquals("Instructor cannot be assigned more than 5 courses", exception.getMessage());
    }

    @Test
    public void shouldThrowException_whenCourseNotFoundForAssignment() {
        Set<Long> courseIds = Set.of(1L, 2L);

        // Mock course repository to return empty for both courses
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        when(courseRepository.findById(2L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            instructorService.addInstructorAndAssignCourses(instructor, courseIds);
        });

        assertEquals("Course not found", exception.getMessage());
    }
}
