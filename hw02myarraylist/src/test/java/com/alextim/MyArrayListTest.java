package com.alextim;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MyArrayListTest {

    @Test void createMyArrayList() {
        List<Student> students = new MyArrayList<>();
        Assertions.assertEquals(0, students.size());
    }

    @Test
    public void getIndexOutOfBoundsException() {
        List<Student> students = new MyArrayList<>();
        students.add(new Student("0"));
        students.add(new Student("1"));
        students.add(new Student("2"));
        students.add(new Student("3"));

        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> students.get(4));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> students.set(4, new Student("4")));
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> students.remove(4));
    }

    @Test
    public void getIllegalArgumentException() {
        List<Student> students = new MyArrayList<>();
        students.add(new Student("0"));
        students.add(new Student("1"));
        students.add(new Student("2"));
        students.add(new Student("3"));

        Assertions.assertThrows(NullPointerException.class, () -> students.add(null));
        Assertions.assertThrows(NullPointerException.class, () -> students.add(1, null));
        Assertions.assertThrows(NullPointerException.class, () -> students.set(2, null));
        Assertions.assertThrows(NullPointerException.class, () -> students.remove(null));
    }

   @Test
    public void addGetTest() {
        List<Student> students = new MyArrayList<>();
        students.add(new Student("0"));
        students.add(new Student("1"));
        students.add(new Student("2"));
        students.add(new Student("3"));

        Assertions.assertEquals(4, students.size());

        for(int i=0; i<4; i++) {
            Assertions.assertEquals(new Student(String.valueOf(i)), students.get(i));
        }
    }

    @Test
    public void addWithIndexTest() {
        MyArrayList<Student> students = new MyArrayList<>();
        students.add(new Student("0"));
        students.add(new Student("1"));
        students.add(new Student("3"));
        students.add(new Student("4"));

        students.add(2, new Student("2"));

        Assertions.assertEquals(5, students.size());

        for(int i=0; i<5; i++) {
            Assertions.assertEquals(new Student(String.valueOf(i)), students.get(i));
        }
    }

    @Test
    public void addAllTest() {
        List<Student> list = Arrays.asList(new Student("2"), new Student("3"), new Student("4"));

        MyArrayList<Student> students = new MyArrayList<>();
        students.add(new Student("0"));
        students.add(new Student("1"));
        students.addAll(list);

        Assertions.assertEquals(5, students.size());

        for(int i=0; i<5; i++) {
            Assertions.assertEquals(new Student(String.valueOf(i)), students.get(i));
        }
    }

    @Test
    public void addAllWithIndexTest() {
        List<Student> list = Arrays.asList(new Student("2"),new Student("3"),new Student("4"));

        MyArrayList<Student> students = new MyArrayList<>();
        students.add(new Student("0"));
        students.add(new Student("1"));
        students.add(new Student("5"));

        students.addAll(2, list);

        Assertions.assertEquals(6, students.size());

        for(int i=0; i<6; i++) {
            Assertions.assertEquals(new Student(String.valueOf(i)), students.get(i));
        }
    }

    @Test
    public void setTest() {
        List<Student> students = new MyArrayList<>();
        students.add(new Student("0"));
        students.add(new Student("0"));
        students.add(new Student("2"));

        students.set(1, new Student("1"));

        for(int i=0; i<3; i++)
            Assertions.assertEquals(new Student(String.valueOf(i)), students.get(i));
    }

    @Test
    public void indexOfTest() {
        List<Student> students = new MyArrayList<>();
        students.add(new Student("0"));
        students.add(new Student("1"));
        students.add(new Student("2"));
        students.add(new Student("3"));
        students.add(new Student("3"));
        students.add(new Student("4"));
        students.add(new Student("5"));

        Assertions.assertEquals(3, students.indexOf(new Student("3")));
        Assertions.assertEquals(-1, students.indexOf(new Student("10")));
    }

    @Test
    public void removeByIndexTest() {
        List<Student> students = new MyArrayList<>();
        students.add(new Student("0"));
        students.add(new Student("Defect"));
        students.add(new Student("1"));
        students.add(new Student("2"));
        students.add(new Student("3"));

        Student removed = students.remove(1);

        Assertions.assertEquals(4, students.size());

        Assertions.assertEquals(new Student("Defect"), removed);

        for(int i=0; i<4; i++)
            Assertions.assertEquals(new Student(String.valueOf(i)), students.get(i));
    }

    @Test
    public void removeByObjectTest() {
        List<Student> students = new MyArrayList<>();
        students.add(new Student("0"));
        students.add(new Student("Defect"));
        students.add(new Student("1"));
        students.add(new Student("2"));
        students.add(new Student("3"));

        boolean defect = students.remove(new Student("Defect"));
        Assertions.assertEquals(4, students.size());
        Assertions.assertTrue(defect);

        for(int i=0; i<4; i++)
            Assertions.assertEquals(new Student(String.valueOf(i)), students.get(i));

        boolean defect2 = students.remove(new Student("Defect2"));
        Assertions.assertFalse( defect2);
    }

    @Test
    public void removeByCollectionTest() {
        List<Student> students = new MyArrayList<>();
        students.add(new Student("0"));
        students.add(new Student("Defect1"));
        students.add(new Student("1"));
        students.add(new Student("2"));
        students.add(new Student("Defect2"));
        students.add(new Student("3"));
        students.add(new Student("Defect3"));

        List<Student> sub = Arrays.asList(new Student("Defect1"), new Student("Defect2"), new Student("Defect3"), new Student("Defect4"));

        boolean modified = students.removeAll(sub);

        Assertions.assertTrue(modified);
        Assertions.assertEquals(4, students.size());

        List<Student> sub2 = Arrays.asList(new Student("Defect5"), new Student("Defect6"));

        boolean modified2 = students.removeAll(sub2);

        Assertions.assertFalse(modified2);
        Assertions.assertEquals(4, students.size());

        for(int i=0; i<4; i++)
            Assertions.assertEquals(new Student(String.valueOf(i)), students.get(i));
    }

    @Test
    public void containsAllTest() {
        List<Student> students = new MyArrayList<>();
        students.add(new Student("0"));
        students.add(new Student("1"));
        students.add(new Student("2"));
        students.add(new Student("3"));
        students.add(new Student("3"));
        students.add(new Student("4"));

        List<Student> sub = Arrays.asList(new Student("1"), new Student("2"), new Student("3"));

        boolean isContains = students.containsAll(sub);
        Assertions.assertTrue(isContains);

        List<Student> sub2 = Arrays.asList(new Student("1"), new Student("2"), new Student("3"),  new Student("5"));
        boolean isContains2 = students.containsAll(sub2);
        Assertions.assertFalse(isContains2);
    }

    @Test
    public void retainAllTest() {
        List<Student> students = new MyArrayList<>();
        students.add(new Student("0"));
        students.add(new Student("8"));
        students.add(new Student("1"));
        students.add(new Student("-4"));
        students.add(new Student("2"));
        students.add(new Student("3"));
        students.add(new Student("9"));
        students.add(new Student("4"));

        List<Student> sub = Arrays.asList(new Student("0"), new Student("1"), new Student("2"), new Student("3"));

        students.retainAll(sub);
        Assertions.assertEquals(4, students.size());

        for(int i=0; i<4; i++)
            Assertions.assertEquals(new Student(String.valueOf(i)), students.get(i));
    }

    @Test
    public void toArrayTest() {
        List<Student> students = new MyArrayList<>();
        students.add(new Student("0"));
        students.add(new Student("1"));
        students.add(new Student("2"));
        students.add(new Student("3"));

        Student[] studentsArr = students.toArray(new Student[4]);

        for(int i=0; i<4; i++)
            Assertions.assertEquals(studentsArr[i], students.get(i));
    }

    @Test
    public void subListTest() {
        List<Student> students = new MyArrayList<>();
        students.add(new Student("0"));
        students.add(new Student("1"));
        students.add(new Student("2"));
        students.add(new Student("3"));
        students.add(new Student("4"));
        students.add(new Student("5"));
        students.add(new Student("6"));

        List<Student> sub = students.subList(1, 4);

        Assertions.assertEquals(3, sub.size());

        for(int i=0; i<3; i++)
            Assertions.assertEquals(new Student(String.valueOf(i+1)), sub.get(i));
    }

    @Test
    public void clearTest() {
        List<Student> students = new MyArrayList<>();
        students.add(new Student("0"));
        students.add(new Student("1"));
        students.add(new Student("2"));
        students.add(new Student("3"));

        students.clear();
        Assertions.assertEquals(0, students.size());
    }

    static class Student {
        String name;

        public Student(String name) {
            this.name = name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Student{" + "name='" + name + '\'' + '}';
        }

        @Override
        public boolean equals(Object object) {
            if (this == object)
                return true;
            if (object == null || getClass() != object.getClass())
                return false;
            Student student = (Student) object;
            return Objects.equals(name, student.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
