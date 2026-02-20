from django.contrib.auth.base_user import AbstractBaseUser, BaseUserManager
from django.contrib.auth.models import PermissionsMixin
from django.db import models
from django.utils.text import slugify

# Lista de dominios de correo que NO están permitidos para crear usuarios
NOT_ALLOWED_DOMAIN = [".ru", ".xyz"]


# ===============================================================
# UserManager: Clase encargada de crear y administrar usuarios
# ===============================================================
# Hereda de BaseUserManager, que es la clase base de Django para
# manejar la creación de usuarios de manera personalizada.
class UserManager(BaseUserManager):

    # -----------------------------------------------------------
    # Método para crear un usuario "normal" (no superusuario)
    # -----------------------------------------------------------
    def create_user(self, email=None, password=None, **extra_fields):
        # Validación: el correo electrónico no puede estar vacío
        if not email:
            raise ValueError("El correo electrónico no puede estar vacío")

        # Validación: el correo debe tener el carácter '@'
        if "@" not in email:
            raise ValueError("Correo no válido")

        # Validación: el correo no puede contener dominios prohibidos
        if any(domain in email for domain in NOT_ALLOWED_DOMAIN):
            raise ValueError("El dominio del correo no está permitido")

        # Validación: la contraseña no puede estar vacía
        if not password:
            raise ValueError("La contraseña no puede estar vacía")

        # Validación: la contraseña debe tener al menos 6 caracteres
        if len(password) < 6:
            raise ValueError("La contraseña debe de tener mínimo 6 caracteres")

        # Validación: la contraseña debe contener al menos un dígito
        if not any(caracter.isdigit() for caracter in password):
            raise ValueError("La contraseña debe tener al menos un dígito")

        # Normaliza el correo (convierte a minúsculas y formato estándar)
        email = self.normalize_email(email)

        # Crea una instancia del modelo User con el correo y campos extra
        user = self.model(email=email, **extra_fields)

        """
          @@@@@@@@@@@@@@@@@@@@@@@@@@Encriptacion contraseña###############################
        """
        # Encripta la contraseña usando el sistema de Django
        user.set_password(password)

        # Guarda el usuario en la base de datos
        user.save(using=self._db)

        return user

    # -----------------------------------------------------------
    # Método para crear un superusuario
    # -----------------------------------------------------------
    # Este método se utiliza normalmente para el comando 'createsuperuser'
    def create_superuser(self, email, password=None, **extra_fields):

        # Por defecto, un superusuario debe tener is_staff y is_superuser en True
        extra_fields.setdefault("is_staff", True)  # Si no se ha especificado 'is_staff' en extra_fields, se establece en True.
        # 'is_staff=True' permite que el usuario acceda al panel de administración de Django (/admin).

        extra_fields.setdefault("is_superuser", True)  # Si no se ha especificado 'is_superuser' en extra_fields, se establece en True.
        # 'is_superuser=True' otorga todos los permisos automáticamente al usuario.

        # Llama al método create_user para crear el usuario
        return self.create_user(email, password, **extra_fields)


# ===============================================================
# User: Modelo personalizado de usuario
# ===============================================================
# Hereda de:
# - PermissionsMixin: para manejar permisos y grupos de usuarios
# - AbstractBaseUser: para definir un modelo de usuario completamente personalizado
class User(PermissionsMixin, AbstractBaseUser):
    # -----------------------------------------------------------
    # Campos obligatorios, donde se pone el texto
    # -----------------------------------------------------------
    email = models.EmailField(
        max_length=100,
        unique=True,
        null=False,
        blank=False,
        verbose_name="correo Electrónico",
        help_text="(Obligatorio)"
    )

    username = models.CharField(
        max_length=50,
        unique=True,
        null=True,
        blank=True
    )

    first_name = models.CharField(
        max_length=100,
        null=True,
        blank=True,
        verbose_name="nombre",
        help_text="(Obligatorio)"
    )

    last_name = models.CharField(
        max_length=100,
        null=True,
        blank=True,
        verbose_name="apellido",
        help_text="(Obligatorio)"
    )

    # -----------------------------------------------------------
    # Campos de estado del usuario
    # -----------------------------------------------------------
    is_active = models.BooleanField(default=True)  # Indica si el usuario puede iniciar sesión
    is_staff = models.BooleanField(default=False)  # Indica si el usuario puede acceder al admin
    is_superuser = models.BooleanField(default=False)  # Indica si el usuario es superusuario

    # -----------------------------------------------------------
    # Relación con otro modelo (Role)
    # -----------------------------------------------------------
    # Cada usuario puede tener un rol asignado. Si se borra el rol, se pone en NULL
    role = models.ForeignKey(
        # "Role" hace referencia a la **clase del modelo** Role, NO al nombre de la tabla en la base de datos.
        # Debe coincidir exactamente con el nombre de la clase (mayúsculas/minúsculas).
        # Django se encarga de convertir esto en la columna role_id en la tabla de la base de datos.
        "Role",

        # Qué hacer si el objeto relacionado (Role) se borra
        # SET_NULL → el campo role_id se pone a NULL, pero el objeto actual (User) NO se borra
        on_delete=models.SET_NULL,

        # Permite que el campo role_id en la base de datos sea NULL
        null=True,

        # Permite que el campo esté vacío en formularios/admin
        blank=True,

        # Nombre legible para mostrar en el admin de Django
        verbose_name="Rol de usuario",

        # Texto de ayuda que aparece en formularios/admin
        # Nota: aunque dice "(Obligatorio)", realmente no lo es porque null=True y blank=True
        help_text="(Obligatorio)"
    )

    # ===========================================================
    # Especificamos que esta clase utiliza nuestro UserManager
    # ===========================================================
    objects = UserManager()

    # -----------------------------------------------------------
    # Configuración de autenticación
    # -----------------------------------------------------------
    USERNAME_FIELD = 'email'  # Django usará el email como identificador
    REQUIRED_FIELDS = []  # Campos obligatorios al crear superusuario desde terminal

    # -----------------------------------------------------------
    # Configuración adicional de la tabla en la base de datos
    # -----------------------------------------------------------
    class Meta:
        db_table = 'users'  # Nombre de la tabla en la base de datos
        ordering = ('-email',)  # Orden por defecto (descendente por email)
        verbose_name = 'Usuario'
        verbose_name_plural = 'Usuarios'

    # -----------------------------------------------------------
    # Método save sobrescrito para generar username automáticamente
    # -----------------------------------------------------------
    def save(self, *args, **kwargs):
        # Si el username está vacío, se genera uno automáticamente
        # usando slugify del nombre, apellido y las primeras 3 letras del email
        if not self.username:
            self.username = f"{slugify(self.first_name)}--{slugify(self.last_name)}--{self.email[:3].upper()} "
        super().save(*args, **kwargs)  # Llamada al save original-> guarda los datos
