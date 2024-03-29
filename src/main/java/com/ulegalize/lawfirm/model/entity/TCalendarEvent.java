package com.ulegalize.lawfirm.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ulegalize.enumeration.EnumCalendarEventType;
import com.ulegalize.lawfirm.model.BaseEntity;
import com.ulegalize.lawfirm.model.entity.converter.EnumCalendarEventTypeConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "T_CALENDAR_EVENT")
public class TCalendarEvent extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @OneToOne
    @JoinColumn(name = "dossier_id")
    @JsonIgnore
    @Getter
    @Setter
    private TDossiers dossier;

    @ManyToOne
    @JoinColumn(columnDefinition = "VARCHAR", name = "user_id")
    @JsonIgnore
    @Getter
    @Setter
    private TUsers tUsers;

    @Column(name = "vc_key")
    @Getter
    @Setter
    private String vcKey;

    @Column(name = "title")
    @Getter
    @Setter
    private String title = " ";

    @Column(name = "note")
    @Getter
    @Setter
    private String note;

    @Column(name = "location")
    @Getter
    @Setter
    private String location;

    @Column(name = "type")
    @Convert(converter = EnumCalendarEventTypeConverter.class)
    @Getter
    @Setter
    private EnumCalendarEventType eventType;

    @Column(columnDefinition = "INTEGER", name = "approved")
    @Getter
    @Setter
    private boolean approved;

    @Column(name = "`start`")
    @Getter
    @Setter
    private Date start;

    @Column(name = "`end`")
    @Getter
    @Setter
    private Date end;

    @Column(name = "url_room")
    @Getter
    @Setter
    private String urlRoom;

    @Column(name = "path_file")
    @Getter
    @Setter
    private String pathFile;

    @Column(name = "micro_text")
    @Getter
    @Setter
    private String microText;

    @Column(name = "audio_text")
    @Getter
    @Setter
    private String audioText;

    @Column(name = "speech_to_text_activated")
    @Getter
    @Setter
    private boolean speechToTextActivated = false;

    @OneToMany(mappedBy = "tCalendarEvent", cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    @Setter
    private List<TCalendarParticipants> tCalendarParticipants;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TCalendarEvent that = (TCalendarEvent) o;
        return approved == that.approved && Objects.equals(id, that.id) && Objects.equals(dossier, that.dossier) && Objects.equals(tUsers, that.tUsers) && Objects.equals(vcKey, that.vcKey) && Objects.equals(title, that.title) && Objects.equals(note, that.note) && Objects.equals(location, that.location) && eventType == that.eventType && Objects.equals(start, that.start) && Objects.equals(end, that.end) && Objects.equals(urlRoom, that.urlRoom) && Objects.equals(pathFile, that.pathFile) && Objects.equals(microText, that.microText) && Objects.equals(audioText, that.audioText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, dossier, tUsers, vcKey, title, note, location, eventType, approved, start, end, pathFile, microText);
    }

    @Override
    public String toString() {
        return "TCalendarEvent{" +
                "id=" + id +
                ", dossier=" + dossier +
                ", tUsers=" + tUsers +
                ", title='" + title + '\'' +
                ", note='" + note + '\'' +
                ", location='" + location + '\'' +
                ", eventType=" + eventType +
                ", approved=" + approved +
                ", start=" + start +
                ", end=" + end +
                ", pathFile=" + pathFile +
                ", microText=" + microText +
                '}';
    }

//	@Column(name="email")
//	private String email;

}
