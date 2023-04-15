package com.Skrill2860.service.dao;

import com.Skrill2860.models.Student;
import com.Skrill2860.service.db.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Data access object for Student. All interaction with the database takes place through this class.
 * This class directly interacts with the {@link JdbcTemplate} class, which in turn refers to the database.
 * This is done to ensure that there are no leaks in the connections.
 * <p>
 * Check hikari.properties in /src/main/resources/ to configure your connection
 */
public class StudentDao {
    private final JdbcTemplate jdbcTemplate;
    /**
     * Saves a collection of students to the database
     *
     * @param students collection of {@link Student}
     * @throws SQLException
     */
    public void saveStudent(Collection<Student> students) throws SQLException {
        jdbcTemplate.preparedStatement(
                "insert into st_schema.students(firstname, lastname, is_present, has_answered, grade) values (?, ?, ?, ?, ?);",
                insertStudent -> {
                    for (Student student : students) {
                        insertStudent.setString(1, student.getFirstname());
                        insertStudent.setString(2, student.getLastname());
                        insertStudent.setBoolean(3, student.isPresent());
                        insertStudent.setBoolean(4, student.hasAnswered());
                        insertStudent.setInt(5, student.getGrade());
                        insertStudent.execute();
                    }
                });
    }

    public void saveStudent(Student student) throws SQLException {
        jdbcTemplate.preparedStatement(
                "insert into st_schema.students(firstname, lastname, is_present, has_answered, grade) values (?, ?, ?, ?, ?);",
                insertStudent -> {
                    insertStudent.setString(1, student.getFirstname());
                    insertStudent.setString(2, student.getLastname());
                    insertStudent.setBoolean(3, student.isPresent());
                    insertStudent.setBoolean(4, student.hasAnswered());
                    insertStudent.setInt(5, student.getGrade());
                    insertStudent.execute();
                });
    }

    public void updateStudent(Student student) throws SQLException {
        jdbcTemplate.preparedStatement(
                "update st_schema.students set " +
                        "firstname = ?, lastname = ?, is_present = ?, has_answered = ?, grade = ? where id = ?;",
                updateStudent -> {
                    updateStudent.setString(1, student.getFirstname());
                    updateStudent.setString(2, student.getLastname());
                    updateStudent.setBoolean(3, student.isPresent());
                    updateStudent.setBoolean(4, student.hasAnswered());
                    updateStudent.setInt(5, student.getGrade());
                    updateStudent.setLong(6, student.getId());
                    updateStudent.execute();
                });
    }

    /**
     * Removes the student with the given id from the database
     *
     * @param id - Student ID
     * @throws SQLException
     */
    public void deleteById(Long id) throws SQLException {
        jdbcTemplate.preparedStatement("delete from st_schema.students where id = ?;",
                deleteStudent -> {
                    deleteStudent.setLong(1, id);
                    deleteStudent.execute();
                });
    }

    /**
     * Returns an optional student object with the given id from the database
     *
     * @param id student ID
     * @return {@code Optional<Student>}
     * @throws SQLException
     */
    public Optional<Student> getStudentById(Long id) throws SQLException {
        return jdbcTemplate.preparedStatement(
                "select id, firstname, lastname, is_present, has_answered, grade from st_schema.students where id = ?;",
                selectStudent -> {
                    selectStudent.setLong(1, id);
                    ResultSet resultSet = selectStudent.executeQuery();
                    Student student = null;
                    while (resultSet.next()) {
                        student = new Student();
                        student.setId(resultSet.getLong(1));
                        student.setFirstname(resultSet.getString(2));
                        student.setLastname(resultSet.getString(3));
                        student.setPresent(resultSet.getBoolean(4));
                        student.setAnswered(resultSet.getBoolean(5));
                        student.setGrade(resultSet.getInt(6));
                    }

                    return Optional.ofNullable(student);
                });
    }

    /**
     * Returns a list of students who have already answered
     * @return {@code List<Student>}
     * @throws SQLException
     */
    public List<Student> getStudentsWhoAnswered() throws SQLException {
        return jdbcTemplate.preparedStatement(
                "SELECT id, firstname, lastname, is_present, has_answered, grade FROM st_schema.students " +
                        "WHERE has_answered = true;",
                selectStudent -> {
                    ResultSet resultSet = selectStudent.executeQuery();
                    List<Student> students = new ArrayList<>();
                    while (resultSet.next()) {
                        Student student = new Student();
                        student.setId(resultSet.getLong(1));
                        student.setFirstname(resultSet.getString(2));
                        student.setLastname(resultSet.getString(3));
                        student.setPresent(resultSet.getBoolean(4));
                        student.setAnswered(resultSet.getBoolean(5));
                        student.setGrade(resultSet.getInt(6));
                        students.add(student);
                    }
                    return students;
                });
    }

    /**
     * Returns a list of students who have not answered
     * @return {@code List<Student>}
     * @throws SQLException
     */
    public List<Student> getStudentsWhoHaveNotAnswered() throws SQLException {
        return jdbcTemplate.preparedStatement(
                "SELECT id, firstname, lastname, is_present, has_answered, grade FROM st_schema.students " +
                        "WHERE has_answered = false;",
                selectStudent -> {
                    ResultSet resultSet = selectStudent.executeQuery();
                    List<Student> students = new ArrayList<>();
                    while (resultSet.next()) {
                        Student student = new Student();
                        student.setId(resultSet.getLong(1));
                        student.setFirstname(resultSet.getString(2));
                        student.setLastname(resultSet.getString(3));
                        student.setPresent(resultSet.getBoolean(4));
                        student.setAnswered(resultSet.getBoolean(5));
                        student.setGrade(resultSet.getInt(6));
                        students.add(student);
                    }
                    return students;
                });
    }

    /**
     * Returns all students from the database
     *
     * @return {@code List<Student>}
     * @throws SQLException
     */
    public List<Student> getAllStudents() throws SQLException {
        return jdbcTemplate.statement(
                selectStudent -> {
                    List<Student> studentList = new ArrayList<>();
                    ResultSet resultSet = selectStudent.executeQuery(
                            "select id, firstname, lastname, is_present, has_answered, grade from st_schema.students;");
                    while (resultSet.next()) {
                        Student student = new Student();
                        student.setId(resultSet.getLong(1));
                        student.setFirstname(resultSet.getString(2));
                        student.setLastname(resultSet.getString(3));
                        student.setPresent(resultSet.getBoolean(4));
                        student.setAnswered(resultSet.getBoolean(5));
                        student.setGrade(resultSet.getInt(6));
                        studentList.add(student);
                    }
                    return studentList;
                });
    }

    public StudentDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
