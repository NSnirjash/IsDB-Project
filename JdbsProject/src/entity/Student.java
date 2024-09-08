
package entity;

public class Student {
    private int id;
    private String name;
    private String email;
    private String address;
    private String cell;
    private String position;

    public Student() {
    }

    public Student(int id, String name, String email, String address, String cell, String position) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.cell = cell;
        this.position = position;
    }

    public Student(String name, String email, String address, String cell, String position) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.cell = cell;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", name=" + name + ", email=" + email + ", address=" + address + ", cell=" + cell + ", position=" + position + '}';
    }
    
    
}
