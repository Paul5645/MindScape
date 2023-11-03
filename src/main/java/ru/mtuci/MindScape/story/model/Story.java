package ru.mtuci.MindScape.story.model;

import jakarta.persistence.*;
import lombok.Data;
import ru.mtuci.MindScape.user.model.User;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name = "story")
public class Story {
    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    private String text;

    //@ManyToOne
    //@JoinColumn(name = "author_id")
    //private User author;

    //@OneToMany(mappedBy = "story")
    //private Set<Comment> comments;

    private LocalDateTime time;
}
