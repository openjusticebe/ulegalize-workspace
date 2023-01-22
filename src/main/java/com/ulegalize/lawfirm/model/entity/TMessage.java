package com.ulegalize.lawfirm.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "t_message")
public class TMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(insertable = false, name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;

    @Column(name = "message_fr")
    @Getter
    @Setter
    private String messageFr;

    @Column(name = "message_en")
    @Getter
    @Setter
    private String messageEn;

    @Column(name = "message_nl")
    @Getter
    @Setter
    private String messageNl;

    @Column(name = "message_de")
    @Getter
    @Setter
    private String messageDe;

    @OneToMany(mappedBy = "tMessage")
    @Getter
    @Setter
    private List<TMessageUser> tMessageUserList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TMessage tMessage = (TMessage) o;
        return Objects.equals(id, tMessage.id) && Objects.equals(messageFr, tMessage.messageFr) && Objects.equals(messageEn, tMessage.messageEn) && Objects.equals(messageNl, tMessage.messageNl) && Objects.equals(messageDe, tMessage.messageDe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, messageFr, messageEn, messageNl, messageDe);
    }

    @Override
    public String toString() {
        return "TMessage{" +
                "id=" + id +
                ", messageFr='" + messageFr + '\'' +
                ", messageEn='" + messageEn + '\'' +
                ", messageNl='" + messageNl + '\'' +
                ", messageDe='" + messageDe + '\'' +
                '}';
    }
}
