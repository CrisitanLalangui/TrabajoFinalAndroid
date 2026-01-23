from django.contrib.auth.base_user import AbstractBaseUser, BaseUserManager
from django.contrib.auth.models import PermissionsMixin
from django.db import models
from django.utils.text import slugify

NOT_ALLOWED_DOMAIN = [".ru", ".xyz"]


# Solo la utilizaremos para la validación de la creación de un usuario
class UserManager(BaseUserManager):
    def create_user(self, email=None, password=None, **extra_fields):
        if not email:
            raise ValueError("El correo electrónico no puede estar vacío")

        if "@" not in email:
            raise ValueError("Correo no válido")

        if any(domain in email for domain in NOT_ALLOWED_DOMAIN):
            raise ValueError("El dominio del correo no está permitido")

        if not password:
            raise ValueError("La contraseña no puede estar vacía")

        if len(password) < 6:
            raise ValueError("La contraseña debe de tener mínimo 6 caracteres")

        if not any(caracter.isdigit() for caracter in password):
            raise ValueError("La contraseña debe tener al menos un dígito")

        email = self.normalize_email(email)  # convertirlo a unicode -> Normalizarlo
        user = self.model(email=email, **extra_fields)  # crear el usuario
        user.set_password(password)  # para encriptar  la contaseña
        user.save(using=self._db)
        return self.create_user(email, password, **extra_fields)

    def create_superuser(self, email, password=None, **extra_fields):

        extra_fields.setdefault("is_staff", True)
        extra_fields.setdefault("is_superuser", True)

        return self.create_user(email, password, **extra_fields)


class User(PermissionsMixin, AbstractBaseUser):
    email = models.EmailField(max_length=100, unique=True, null=False, blank=False, verbose_name="correo Electrónico",
                              help_text="(Obligatorio)")
    username = models.CharField(max_length=50, unique=True, null=False, blank=False)

    first_name = models.CharField(max_length=100, null=False, blank=False, verbose_name="nombre",
                                  help_text="(Obligatorio)")

    last_name = models.CharField(max_length=100, null=False, blank=False, verbose_name="apellido",
                                 help_text="(Obligatorio)")
    is_active = models.BooleanField(default=True)
    is_staff = models.BooleanField(default=False)
    is_superuser = models.BooleanField(default=False)

    role = models.ForeignKey("Role", on_delete=models.SET_NULL, null=True, blank=True, verbose_name="Rol de usuario",
                             help_text="(Obligatorio)")  # Si se borrar un rol, el campo ahora esta vacío
    objects = UserManager()

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = []

    class Meta:
        db_table = 'users'
        ordering = ('-email',)
        verbose_name = 'Usuario'
        verbose_name_plural = 'Usuarios'

    def save(self, *args, **kwargs):
        if not self.username:
            self.username = f"{slugify(self.first_name)}--{slugify(self.last_name)}--{self.email[:3].upper()} "
        super().save(*args, **kwargs)
