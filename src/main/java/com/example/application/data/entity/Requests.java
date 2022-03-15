package com.example.application.data.entity;

import com.example.application.data.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Requests extends AbstractEntity {

    @NotNull
    @ManyToOne
    private Priority priority;

    @NotNull
    @ManyToOne
    private Category category;

    @NotNull
    @ManyToOne
    private Status status;

    @NotEmpty
    @NotBlank
    private String nameRequest = "";

    @NotNull
    private LocalDateTime dateStart;

    @NotNull
    private LocalDateTime dateDue;

    private LocalDateTime dateEnd;

    @NotNull
    @ManyToOne
    private Users requester;

    @NotNull
    @ManyToOne
    private Users executor;

    private String descriptionRequest = "";

    private String comment = "";

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getNameRequest() {
        return nameRequest;
    }

    public void setNameRequest(String nameRequest) {
        this.nameRequest = nameRequest;
    }

    public LocalDateTime getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDateTime dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDateTime getDateDue() {
        return dateDue;
    }

    public void setDateDue(LocalDateTime dateDue) {
        this.dateDue = dateDue;
    }

    public LocalDateTime getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDateTime dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Users getRequester() {
        return requester;
    }

    public void setRequester(Users requester) {
        this.requester = requester;
    }

    public Users getExecutor() {
        return executor;
    }

    public void setExecutor(Users executor) {
        this.executor = executor;
    }

    public String getDescriptionRequest() {
        return descriptionRequest;
    }

    public void setDescriptionRequest(String descriptionRequest) {
        this.descriptionRequest = descriptionRequest;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
