from django.db import models
# Importamos models para poder definir modelos (tablas) de Django

from django.db.models.enums import TextChoices
# TextChoices permite definir opciones (choices) de forma limpia y reutilizable

from TiendaOnline import settings
# Importamos settings para poder acceder a AUTH_USER_MODEL
# Esto permite que el modelo funcione aunque el User sea personalizado


# ===============================================================
# Enumeración de países usando TextChoices
# ===============================================================
class PaisesChoices(models.TextChoices):
    # Cada opción tiene:
    # PRIMER valor → lo que se guarda en la base de datos
    # SEGUNDO valor → lo que se muestra al usuario

    SPAIN = "ES", "ESPAÑA"
    FRANCE = "FR", "FRANCIA"


# ===============================================================
# Modelo InfoPersonal
# Almacena información personal adicional de un usuario
# ===============================================================
class InfoPersonal(models.Model):

    # -----------------------------------------------------------
    # Relación uno a uno con el usuario
    # Cada usuario tiene UNA sola información personal
    # -----------------------------------------------------------
    user = models.OneToOneField(
        settings.AUTH_USER_MODEL,     # Modelo de usuario (por defecto o personalizado)
        on_delete=models.CASCADE,     # Si se borra el usuario, se borra su info personal
        related_name="info_personal"  # Permite acceder desde user.info_personal
    )

    # -----------------------------------------------------------
    # Documento de identidad
    # -----------------------------------------------------------
    document = models.CharField(
        max_length=15,                # Máximo 15 caracteres
        unique=True,                  # No se pueden repetir documentos
        null=False,                   # No permite NULL en base de datos
        blank=False,                  # No permite vacío en formularios
        verbose_name="Documento (DNI, NIE, PASAPORTE, OTROS)",
        help_text="(Obligatorio)"
    )

    # -----------------------------------------------------------
    # Dirección completa del usuario
    # -----------------------------------------------------------
    adress = models.TextField(
        null=False,                   # Campo obligatorio en BD
        blank=False,                  # Campo obligatorio en formularios
        verbose_name="Dirección",
        help_text="(Obligatorio)"
    )

    # -----------------------------------------------------------
    # Fecha de nacimiento
    # -----------------------------------------------------------
    birthday = models.DateField(
        verbose_name="Fecha de nacimiento",
        help_text="(Obligatorio)"
    )

    # -----------------------------------------------------------
    # Teléfono del usuario
    # -----------------------------------------------------------
    phone = models.CharField(
        max_length=11,                # Longitud máxima del teléfono
        verbose_name="Teléfono",
        help_text="(Opcional)"
        # Nota: no tiene null=True ni blank=True, así que es obligatorio
        # Si realmente es opcional, habría que añadir null=True, blank=True
    )

    # -----------------------------------------------------------
    # Edad del usuario
    # -----------------------------------------------------------
    age = models.PositiveBigIntegerField(
        null=False,
        blank=False,
        default=18,                   # Valor por defecto
        choices=[(n, n) for n in range(1, 101)],  # Edades permitidas: 1 a 100
        verbose_name="Edad"
    )

    # -----------------------------------------------------------
    # Ciudad (relación con otra tabla)
    # -----------------------------------------------------------
    city = models.ForeignKey(
        "CiudadModel",                # Nombre de la clase del modelo Ciudad
        on_delete=models.SET_NULL,    # Si se borra la ciudad, el campo queda en NULL
        null=True,                    # Permite NULL en la base de datos
        blank=True,                   # Permite dejarlo vacío en formularios
        verbose_name="Ciudad",
        help_text="(Obligatorio)"
    )

    # -----------------------------------------------------------
    # País del usuario
    # -----------------------------------------------------------
    country = models.CharField(
        max_length=3,                         # Longitud máxima del código del país
        choices=PaisesChoices.choices,        # Opciones definidas en TextChoices
        default=PaisesChoices.SPAIN,          # Valor por defecto
        verbose_name="País",
        help_text="(Obligatorio)"
    )

    # -----------------------------------------------------------
    # Configuración adicional del modelo
    # -----------------------------------------------------------
    class Meta:
        db_table = "info_personal"             # Nombre de la tabla en la base de datos
        verbose_name = "Info personal"         # Nombre singular en el admin
        verbose_name_plural = "Datos personales"  # Nombre plural en el admin
        ordering = ("-country",)               # Orden por defecto (descendente por país)

    # -----------------------------------------------------------
    # Representación en texto del objeto
    # -----------------------------------------------------------
    def __str__(self):
        # Define cómo se mostrará el objeto en el admin y en consola
        return f"{self.document} - ({self.city} / {self.country})"
