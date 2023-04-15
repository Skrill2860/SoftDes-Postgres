import com.Skrill2860.models.Student;
import com.Skrill2860.service.dao.StudentDao;
import com.Skrill2860.service.db.DbInit;
import com.Skrill2860.service.db.JdbcTemplate;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static com.Skrill2860.utils.EnsuredInput.getIntEnsured;
import static com.Skrill2860.utils.EnsuredInput.getLongEnsured;


/**
 * Это Java-приложение для случайного выбора студента на паре. После выбора студента и ответа на вопрос преподавателя
 * студенту выставляется оценка.
 * Программа должна уметь показать список студентов и оценок за семинар (список отвечавших),
 * а также подсказку по использованию программы. Если был выбран уже отвечавший студент,
 * должен быть снова произведен выбор отвечащего. Перед проставлением оценки необходимо проставить
 * флаг присутствия студента.
 */
public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static final Random rand = new Random();

    public static void printStudentList(List<Student> students) {
        if (!students.isEmpty()) {
            for (Student s : students) {
                System.out.println(s);
            }
        } else {
            System.out.println("No students");
        }
    }

    public static void printHelp() {
        System.out.println("Available options:");
        printMenu();
    }

    public static void printMenu() {
        System.out.println("""
                /h - help
                /c - create new student table
                /add - add student
                /del - delete student
                /all - list of all students
                /p [id] - print student with id
                /r - choose random student to answer
                /l - list of students who have answered
                /q - quit""");
    }

    public static void main(String[] args) {
        HikariConfig hikariConfig = new HikariConfig("resources/hikari.properties");
        HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(hikariDataSource);
        StudentDao studentDao = new StudentDao(jdbcTemplate);

        printMenu();
        while (true) {
            switch (in.nextLine()) {
                case "/h" -> printHelp();
                case "/c" -> {
                    try {
                        DbInit dbInit = new DbInit(jdbcTemplate);
                        dbInit.create();
                        System.out.println("Таблица создана");
                    } catch (SQLException | IOException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Таблица не создана");
                    }
                }
                case "/add" -> {
                    System.out.println("Введите имя:");
                    String firstName = in.nextLine();
                    System.out.println("Введите фамилию:");
                    String lastName = in.nextLine();
                    Student student = new Student(firstName, lastName);
                    try {
                        studentDao.saveStudent(student);
                        System.out.println("Студент добавлен: " + student.getFullName());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Студент не добавлен");
                    }
                }
                case "/del" -> {
                    Long id = getLongEnsured(in, "Введите идентификатор:");
                    try {
                        studentDao.deleteById(id);
                        System.out.println("Студент удален");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("Студент не удален");
                    }
                }
                case "/all" -> {
                    try {
                        printStudentList(studentDao.getAllStudents());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "/p" -> {
                    Long id = getLongEnsured(in, "Введите идентификатор:");
                    try {
                        System.out.println(studentDao.getStudentById(id));
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "/r" -> {
                    List<Student> students;
                    try {
                        students = studentDao.getStudentsWhoHaveNotAnswered();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                    if (students.size() == 0) {
                        System.out.println("Студентов, которые не отвечали на вопросы, больше нет");
                        break;
                    }
                    int index = rand.nextInt(students.size());
                    Student student = students.get(index);
                    System.out.println("Отвечает " + student.getFullName());
                    System.out.println("Присутствует ли на паре? (y/n)");
                    student.setPresent(in.nextLine().equalsIgnoreCase("y"));
                    if (student.isPresent()) {
                        int grade = getIntEnsured(in, "Введите оценку:");
                        student.setGrade(grade);
                        student.setAnswered(true);
                        System.out.println(student.getFullName() + " присутствовал(а) и добавлен(а) в ведомость");
                    } else {
                        System.out.println(student.getFullName() + " не присутствовал(а) и добавлен(а) в ведомость");
                    }
                    try {
                        studentDao.updateStudent(student);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "/l" -> {
                    try {
                        List<Student> students = studentDao.getStudentsWhoAnswered();
                        printStudentList(students);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                case "/q" -> System.exit(0);
                default -> System.out.println("Такой команды не существует. Введите /h для просмотра списка команд.");
            }
        }
    }
}