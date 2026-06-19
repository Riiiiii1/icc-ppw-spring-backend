package ec.edu.ups.icc.fundamentos01.students.controllers;
import java.util.ArrayList;
import java.util.List;

import ec.edu.ups.icc.fundamentos01.students.models.Student;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private List<Student> estudiantes = new ArrayList<>();

    public StudentController(){
        estudiantes.add(new Student(1L,"Alice ",20));
        estudiantes.add(new Student(2L,"Bob ",22));
        estudiantes.add(new Student(3L,"Charlie ",19));
    }

    @GetMapping()
    public List<Student> getStudents(){
        return estudiantes;
    }
    @GetMapping("/count")
    public String getCount() {
        return " Total de estudiantes : " + estudiantes.size();
    }
}
