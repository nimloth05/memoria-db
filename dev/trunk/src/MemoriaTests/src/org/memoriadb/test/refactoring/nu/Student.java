package org.memoriadb.test.refactoring.nu;

public class Student extends Person {

  private String fYear;
  private Rank fRank;
  private String fStudentId;
  
  public Rank getRank() {
    return fRank;
  }
  public String getStudentId() {
    return fStudentId;
  }
  public String getYear() {
    return fYear;
  }
  public void setRank(Rank rank) {
    fRank = rank;
  }
  public void setStudentId(String studentId) {
    fStudentId = studentId;
  }
  public void setYear(String year) {
    fYear = year;
  }
  
}
