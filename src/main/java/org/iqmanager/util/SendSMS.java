package org.iqmanager.util;


import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.iqmanager.models.PerformerData;

import java.io.IOException;
import java.util.List;


public class SendSMS {

    /** SMS для регистрации заказчика */
    public static void sendAuthorization(String phone, String code) throws IOException {
        Request.Post("https://smsc.ru/sys/send.php")
                .bodyForm(Form.form()
                        .add("login", "iqmanager")
                        .add("psw","IQ#12&manager")
                        .add("phones", phone)
                        .add("mes","Kod podtvergdeniya: " + code)
                        .add("charset", "utf-8")
                        .build())
                .execute();
    }

    /** Оповещение администрации о заявке */
    public static void receivedRequest(String name, String phone, List<String> phonesAdminsList) throws IOException {

        StringBuilder phonesAdmins = new StringBuilder();
        for (String phoneAdmin : phonesAdminsList) {
            phonesAdmins.append(phoneAdmin).append(",");
        }
        phonesAdmins.deleteCharAt(phonesAdmins.length() - 1);


                Request.Post("https://smsc.ru/sys/send.php")
                        .bodyForm(Form.form()
                                .add("login", "iqmanager")
                                .add("psw","IQ#12&manager")
                                .add("phones", phonesAdmins.toString())
                                .add("mes","Postupila novaya zayavka \\n\\r" + name + "\\n\\r" + phone)
                                .add("charset", "utf-8")
                                .build())
                        .execute();

    }

    /** Оповещение исполнителя о новом заказе */
    public static void notifyThePerformerOfNewOrder(List<PerformerData> phone_performer, List<String> phonesAdminsList) throws IOException {

        StringBuilder phonesAdmins = new StringBuilder();
        for (String phoneAdmin : phonesAdminsList) {
            phonesAdmins.append(phoneAdmin).append(",");
        }
        phonesAdmins.deleteCharAt(phonesAdmins.length() - 1);


        StringBuilder phonesPerformers = new StringBuilder();
        for (PerformerData phoneAdmin : phone_performer) {
            phonesAdmins.append(phoneAdmin.getPhone()).append(",");
        }
        phonesAdmins.deleteCharAt(phonesAdmins.length() - 1);

        Request.Post("https://smsc.ru/sys/send.php")
                .bodyForm(Form.form()
                        .add("login", "iqmanager")
                        .add("psw","IQ#12&manager")
                        .add("phones", phonesAdmins.toString())
                        .add("mes","Postupil noviy zakaz\\n\\r https://iqmanager.org/")
                        .add("charset", "utf-8")
                        .build())
                .execute();

        Request.Post("https://smsc.ru/sys/send.php")
                .bodyForm(Form.form()
                        .add("login", "iqmanager")
                        .add("psw","IQ#12&manager")
                        .add("phones", phone_performer.toString())
                        .add("mes","Postupil noviy zakaz\\n\\r Daite otvet na nashem saite \\n\\r https://iqmanager.org/")
                        .add("charset", "utf-8")
                        .build())
                .execute();
    }


    /** Оповещение об автоотказе */
    public static void notifyOfAnAutoReject (String phone) throws IOException {
        Request.Post("https://smsc.ru/sys/send.php")
                .bodyForm(Form.form()
                        .add("login", "iqmanager")
                        .add("psw","IQ#12&manager")
                        .add("phones", phone)
                        .add("mes","Avtootkaz po odnomu iz punktov zakaza\\n\\r Podrobnee:\\n\\r https://iqmanager.org/")
                        .add("charset", "utf-8")
                        .build())
                .execute();
    }


}
