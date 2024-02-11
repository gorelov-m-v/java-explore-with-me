package ru.practicum.main.comment.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @Column(name = "created_at")
    private Long created;
    @Column(name = "updated_at")
    private Long updated;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "event_id")
    private Long eventId;
    @Column(name = "is_deleted")
    private Boolean isDeleted;
}