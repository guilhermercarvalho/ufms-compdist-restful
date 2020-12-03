package model;

import java.sql.Date;

public class Actor {
    private Integer id;
    private String name;
    private Date birth_date;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getBirth_date() {
        return birth_date;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirth_date(Date birth_date) {
        this.birth_date = birth_date;
    }
}
