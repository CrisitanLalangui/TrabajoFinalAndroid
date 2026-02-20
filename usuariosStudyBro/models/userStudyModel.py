from django.contrib.auth.base_user import AbstractBaseUser, BaseUserManager
from django.db import models

NOT_ALLOWED_DOMAIN = [".ru", ".xyz"]


class UserManager(BaseUserManager):
    def create_user(self, email=None, password=None, password2=None, **extra_fields):
        if not email:
            raise ValueError("El correo electrónico no puede estar vacío")
        if "@" not in email:
            raise ValueError("Correo no válido")
        if any(domain in email for domain in NOT_ALLOWED_DOMAIN):
            raise ValueError("El dominio del correo no está permitido")
        if not password:
            raise ValueError("La contraseña no puede estar vacía")
        if len(password) < 6 or len(password2) < 6:
            raise ValueError("La contraseña debe de tener mínimo 6 caracteres")

        if not any(c.isdigit() for c in password):
            raise ValueError("La contraseña debe tener al menos un dígito")
        if not any(c.isdigit() for c in password2):
            raise ValueError("La contraseña debe tener al menos un dígito")
        if password2 != password:
            raise ValueError("Las contraseñas no coinciden")

        email = self.normalize_email(email)
        user = self.model(email=email, **extra_fields)
        user.set_password(password)
        user.set_password(password2)
        user.save(using=self._db)
        return user


class UserStudy(AbstractBaseUser):
    email = models.EmailField(max_length=100, unique=True, null=False, blank=False, help_text="Campo Obligatorio")
    objects = UserManager()

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = []

    class Meta:
        db_table = 'usersStudy'
        ordering = ('-email',)
