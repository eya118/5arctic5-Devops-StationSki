package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class InstructorServicesImpl implements IInstructorServices{

    private IInstructorRepository instructorRepository;
    private ICourseRepository courseRepository;

    @Override
    public Instructor addInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    @Override
    public List<Instructor> retrieveAllInstructors() {
        return instructorRepository.findAll();
    }

    @Override
    public Instructor updateInstructor(Instructor instructor) {
        return instructorRepository.save(instructor);
    }

    @Override
    public Instructor retrieveInstructor(Long numInstructor) {
        return instructorRepository.findById(numInstructor).orElse(null);
    }

// Method to add an instructor and assign courses
    public Instructor addInstructorAndAssignCourses(Instructor instructor, Set<Long> courseIds) {
        LocalDate currentDate = LocalDate.now();
        long experienceYears = ChronoUnit.YEARS.between(instructor.getDateOfHire(), currentDate);
        int maxCourses = experienceYears > 5 ? 5 : 3;  // Instructor can handle max 5 courses if more than 5 years of experience, otherwise max 3

        // Check if the instructor is assigned more than the allowed number of courses
        if (courseIds.size() > maxCourses) {
            throw new IllegalArgumentException("Instructor cannot be assigned more than " + maxCourses + " courses");
        }

        // Fetch the courses from the course repository by their IDs
        Set<Course> courses = courseIds.stream()
                .map(courseRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        // Check for schedule conflicts between the selected courses
        if (hasScheduleConflict(courses)) {
            throw new IllegalStateException("Schedule conflict detected for the selected courses");
        }

        // Assign courses to instructor
        instructor.setCourses(courses);

        // Save the instructor with the assigned courses
        return instructorRepository.save(instructor);
    }

    // Helper method to check if there are any schedule conflicts among the selected courses
    private boolean hasScheduleConflict(Set<Course> courses) {
        // Check for schedule conflicts based on 'timeSlot' (used as a simple conflict detection)
        for (Course course1 : courses) {
            for (Course course2 : courses) {
                if (!course1.equals(course2) && course1.getTimeSlot() == course2.getTimeSlot()) {
                    return true; // Conflict found if courses share the same time slot
                }
            }
        }
        return false; // No conflicts found
    }

}
