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
    public void testAddInstructor() {
        // Arrange: Mock the save method to avoid actual database interaction
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        // Act: Call the service method
        String result = instructorService.addInstructor(instructor);

        // Assert: Verify the result and interaction
        assertEquals("Instructor added successfully", result);
        verify(instructorRepository, times(1)).save(instructor);
    }
}
