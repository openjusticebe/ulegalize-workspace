package com.ulegalize.lawfirm.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ulegalize.enumeration.DriveType;
import com.ulegalize.enumeration.EnumRefCurrency;
import com.ulegalize.lawfirm.model.entity.converter.RefCurrencyConverter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "T_VIRTUALCAB")
public class LawfirmEntity implements Serializable {

    @Id
    @Column(name = "`key`")
    @Getter
    @Setter
    private String vckey;

    @Column(name = "NAME")
    @Getter
    @Setter
    private String name;

    //TODO map to column
    @Column(name = "alias")
    @Getter
    @Setter
    private String alias;

    @Column(name = "abbreviation")
    @Getter
    @Setter
    private String abbreviation = "";

    @Column(name = "doc_path")
    @Getter
    @Setter
    private String docPath = "";
    @Column(name = "objetsocial")
    @Getter
    @Setter
    private String objetsocial = "";

    @Column(name = "name_admin")
    @Getter
    @Setter
    private String nameAdmin = "";
    @Column(name = "email_admin")
    @Getter
    @Setter
    private String emailAdmin = "";
    @Column(name = "pass_admin")
    @Getter
    @Setter
    private String passAdmin = "";

    @Column(name = "STREET")
    @Getter
    @Setter
    private String street = "";
    @Column(name = "CITY")
    @Getter
    @Setter
    private String city;
    @Column(name = "CP")
    @Getter
    @Setter
    private String postalCode = "";
    @Column(name = "ID_COUNTRY_ALPHA2")
    @Getter
    @Setter
    private String countryCode;

    @Column(name = "email")
    @Getter
    @Setter
    private String email;
    @Column(name = "NUMENTREPRISE")
    @Getter
    @Setter
    private String companyNumber;
    @Column(name = "TELEPHONE")
    @Getter
    @Setter
    private String phoneNumber = "";
    @Column(name = "FAX")
    @Getter
    @Setter
    private String fax = "";
    @Column(name = "WEBSITE")
    @Getter
    @Setter
    private String website = "";
    @Column(name = "LICENSE_ID")
    @Getter
    @Setter
    private Integer licenseId = 2;
    @Column(name = "TEMPORARY_VC")
    @Getter
    @Setter
    private Boolean temporaryVc;

    @Column(name = "LOGO")
    @Lob
    @Getter
    @Setter
    private byte[] logo;

    @Column(name = "is_notification")
    @Getter
    @Setter
    private Boolean notification = Boolean.TRUE;

    @Enumerated(EnumType.STRING)
    @Column(name = "DRIVETYPE")
    @Getter
    @Setter
    private DriveType driveType;
    @Column(name = "dropbox_token")
    @Getter
    @Setter
    private String dropboxToken;
    @Column(name = "onedrive_token")
    @Getter
    @Setter
    private String onedriveToken;

    @Column(name = "expire_token")
    @Getter
    @Setter
    private LocalDateTime expireToken;
    @Column(name = "refresh_token")
    @Getter
    @Setter
    private String refreshToken;
    @Column(name = "user_upd")
    @Getter
    @Setter
    private String userUpd;
    @Column(name = "date_upd")
    @UpdateTimestamp
    @Getter
    @Setter
    private LocalDateTime date_upd;

    @Column(name = "currency")
    @Convert(converter = RefCurrencyConverter.class)
    @Getter
    @Setter
    private EnumRefCurrency currency = EnumRefCurrency.EUR;

    @Column(name = "couthoraire")
    @Getter
    @Setter
    private int couthoraire;

    @Column(name = "start_invoice_number")
    @Getter
    @Setter
    private Integer startInvoiceNumber;

    @OneToMany(mappedBy = "lawfirm", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JsonIgnore
    @Getter
    @Setter
    private List<LawfirmUsers> lawfirmUsers;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "`key`", referencedColumnName = "VC_KEY")
    @JsonIgnore
    @Getter
    @Setter
    private LawfirmWebsiteEntity lawfirmWebsite;

    @OneToMany(mappedBy = "lawfirm", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    @JsonIgnore
    @Getter
    @Setter
    private List<TVirtualcabVat> tVirtualcabVatList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LawfirmEntity that = (LawfirmEntity) o;
        return couthoraire == that.couthoraire && Objects.equals(vckey, that.vckey) && Objects.equals(name, that.name) && Objects.equals(alias, that.alias) && Objects.equals(abbreviation, that.abbreviation) && Objects.equals(docPath, that.docPath) && Objects.equals(objetsocial, that.objetsocial) && Objects.equals(nameAdmin, that.nameAdmin) && Objects.equals(emailAdmin, that.emailAdmin) && Objects.equals(passAdmin, that.passAdmin) && Objects.equals(street, that.street) && Objects.equals(city, that.city) && Objects.equals(postalCode, that.postalCode) && Objects.equals(countryCode, that.countryCode) && Objects.equals(email, that.email) && Objects.equals(companyNumber, that.companyNumber) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(fax, that.fax) && Objects.equals(website, that.website) && Objects.equals(licenseId, that.licenseId) && Objects.equals(temporaryVc, that.temporaryVc) && Arrays.equals(logo, that.logo) && driveType == that.driveType && Objects.equals(dropboxToken, that.dropboxToken) && Objects.equals(onedriveToken, that.onedriveToken) && Objects.equals(expireToken, that.expireToken) && Objects.equals(refreshToken, that.refreshToken) && Objects.equals(userUpd, that.userUpd) && Objects.equals(date_upd, that.date_upd) && currency == that.currency && Objects.equals(startInvoiceNumber, that.startInvoiceNumber) && Objects.equals(notification, that.notification);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(vckey, name, alias, abbreviation, docPath, objetsocial, nameAdmin, emailAdmin, passAdmin, street, city, postalCode, countryCode, email, companyNumber, phoneNumber, fax, website, licenseId, temporaryVc, driveType, dropboxToken, onedriveToken, expireToken, refreshToken, userUpd, date_upd, currency, couthoraire, startInvoiceNumber, notification);
        result = 31 * result + Arrays.hashCode(logo);
        return result;
    }
}
