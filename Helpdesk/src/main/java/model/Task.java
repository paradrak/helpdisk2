package main.java.model;

public class Task{
    private int id;
    private String title, datetime, author, contractor, status;

    public Task(int id, String title, String datetime, String author, String contractor, String status){
        this.id = id;
        this.title = title;
        this.datetime = datetime;
        this.author = author;
        this.contractor = contractor;
        this.status = status;
    }

    public Task(){
    }

    public int getId() {
        return id;    }

    public void setId(int id) {
        this.id = id;    }

    public String getTitle() {
        return title;    }

    public void setTitle(String title) {
        this.title = title;    }

    public String getDatetime() {
        return datetime;    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;    }

    public String getAuthor() {
        return author;    }

    public void setAuthor(String author) {
        this.author = author;    }

    public String getContractor() {
        return contractor;    }

    public void setContractor(String contractor) {
        this.contractor = contractor;    }

    public String getStatus() {
        return status;    }

    public void setStatus(String status) {
        this.status = status;    }
}

