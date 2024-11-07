package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Optional;  // <-- This is the import for Optional

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

    @Override
    public Instructor addInstructorAndAssignToCourse(Instructor instructor, Long numCourse) {
        Course course = courseRepository.findById(numCourse).orElse(null);
        Set<Course> courseSet = new HashSet<>();
        courseSet.add(course);
        instructor.setCourses(courseSet);
        return instructorRepository.save(instructor);
    }
    @Override
    // New method to get instructors by course
    public List<Instructor> getInstructorsByCourse(Long numCourse) {
        Course course = courseRepository.findById(numCourse).orElse(null);
        if (course != null) {
            return instructorRepository.findAll().stream()
                    .filter(instructor -> instructor.getCourses().contains(course))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    public Instructor addInstructorAndAssignCourses(Instructor instructor, Set<Long> courseIds) {
        LocalDate currentDate = LocalDate.now();
        long experienceYears = ChronoUnit.YEARS.between(instructor.getDateOfHire(), currentDate);
        int maxCourses = experienceYears > 5 ? 5 : 3;  // Max 5 courses if more than 5 years of experience, otherwise max 3

        if (courseIds.size() > maxCourses) {
            throw new IllegalArgumentException("Instructor cannot be assigned more than " + maxCourses + " courses");
        }

        // Fetch courses from the repository
        Set<Course> courses = courseIds.stream()
                .map(courseRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        if (courses.size() != courseIds.size()) {
            throw new IllegalArgumentException("Course not found");
        }

        // Check for schedule conflicts
        if (hasScheduleConflict(courses)) {
            throw new IllegalStateException("Schedule conflict detected for the selected courses");
        }

        // Assign the courses to the instructor
        instructor.setCourses(courses);

        // Save the instructor and return the saved entity
        return instructorRepository.save(instructor);
    }

    private boolean hasScheduleConflict(Set<Course> courses) {
        // Check for schedule conflicts using the timeSlot property of courses
        for (Course course1 : courses) {
            for (Course course2 : courses) {
                if (!course1.equals(course2) && course1.getTimeSlot() == course2.getTimeSlot()) {
                    return true;  // Conflict detected if the time slots are the same
                }
            }
        }
        return false;  // No conflicts found
    }
    public boolean assignInstructorToCourse(Long instructorId, Long courseId) {
        Instructor instructor = instructorRepository.findById(instructorId).orElse(null);
        Course course = courseRepository.findById(courseId).orElse(null);

        if (instructor == null || course == null) {
            return false; // Instructor or Course not found
        }

        // Calculate the instructor's years of experience based on the date of hire
        LocalDate hireDate = instructor.getDateOfHire();
        if (hireDate == null) {
            return false; // If the hire date is null, return false
        }

        int yearsOfExperience = Period.between(hireDate, LocalDate.now()).getYears();

        // Business logic: Check if the instructor has at least 2 years of experience
        if (yearsOfExperience < 2) {
            return false; // Instructor has less than 2 years of experience
        }

        instructor.getCourses().add(course); // Assuming there's a 'courses' list in Instructor

        // Save the updated instructor object (which will also update the relationship)
        instructorRepository.save(instructor);
        return true;
    }
}
