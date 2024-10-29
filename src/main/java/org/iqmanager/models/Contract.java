package org.iqmanager.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "contract")
@Getter
@Setter
@NoArgsConstructor
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "type")
    private String type; // стадия контракта (на проверке/заключен)

    @Column(name = "date_conclusion_contract")
    private Date dateConclusion;

    @Column(name = "contract_kind")
    private String contractKind; // offline/online

    @Column(name = "activities")
    private String activities; // деятельность

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "country")
    private String country;

    @Column(name = "region")
    private String region;

    @Column(name = "number_contract")
    private long numberContract;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "patronymic")
    private String patronymic; // отчество

    @Column(name = "fullName")
    private String fullName; // ФИО

    @Column(name = "mail")
    private String mail; //почтовый индекс

    @Column(name = "address")
    private String address;// адрес местонахождения

    @Column(name = "series")
    private String series; // серия паспорта

    @Column(name = "number")
    private String number; // номер паспорта

    @Column(name = "who_issued")
    private String whoIssued; // кто выдал

    @Column(name = "when_issued")
    private String whenIssued; // когда выдан

    @Column(name = "unit_code")
    private String unitCode;  // код подразделения

    @Column(name = "ip_name")
    private String ipName;  // наименование ип

    @Column(name = "legal_status")
    private String legalStatus; //юр статус

    @Column(name = "inn")
    private String inn; // ИНН

    @Column(name = "address_org")
    private String organisationAddress;  //  адрес орг.

    @Column(name = "address_registration")
    private String registrationAddress;  // адрес регистрации

    @Column(name = "acting_on")
    private String actingOnTheBasics;  // Действующего на основании

    @Column(name = "full_name_org")
    private String fullNameOrg;  // полное название организации

    @Column(name = "bank")
    private String bank;

    @Column(name = "settlement_account")
    private String settlementAccount;  // расчетный счет

    @Column(name = "correspondent_account")
    private String correspondentAccount; //корреспондентский счет

    @Column(name = "bik")
    private String bik; // БИК

    @Column(name = "ogrn")
    private String ogrn; // ОГРН

    @Column(name = "kpp")
    private String kpp; // КПП

    @Column(name = "phone_org")
    private String phoneOrg; // номер организации

    @Column(name = "passport_main")
    private String passportMain;

    @Column(name = "passport_registration")
    private String passportRegistration;

    @Column(name = "passport_with_person")
    private String passportWithPerson;

    @Column(name = "bank_address")
    private String bankAddress;//адрес банка

    @Column(name = "card_number")
    private String cardNumber;//номер карты

    @Column(name = "ogrnip")
    private String ogrnip;//ОГРНИП

    @Column(name = "card_holder_fio")
    private String cardHolderFio; // ФИО владельца карты

    @Column(name = "signature")
    private String signature;

    @Column(name = "path")
    private String path;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "performer_id", referencedColumnName = "id")
    private PerformerData performerData;

}
