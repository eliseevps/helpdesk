package com.example.application.views.login;


import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Авторизация")
@Route(value = "login")
public class LoginView extends LoginOverlay {
    public LoginView() {
        setAction("login");

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());

        i18n.getHeader().setTitle("ИС СП ИТ");
        i18n.getHeader().setDescription("Информационная система службы поддержки отдела ИТ ООО ТПГ Солид");
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Вход в систему");
        i18nForm.setUsername("Введите логин");
        i18nForm.setPassword("Введите пароль");
        i18nForm.setSubmit("Войти");
        i18nForm.setForgotPassword("Забыли пароль?");
        i18n.setForm(i18nForm);
        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Неверное имя пользователя или пароль");
        i18nErrorMessage.setMessage("Пожалуйста, проверьте правильность имени пользователя и пароля и повторите попытку.");
        i18n.setErrorMessage(i18nErrorMessage);
        getElement().setAttribute("no-autofocus", "");
        setI18n(i18n);
        setOpened(true);
    }

}
