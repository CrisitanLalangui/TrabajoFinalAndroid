from django.db import models
from django.db.models.enums import TextChoices

from TiendaOnline import settings



class PaisesChoices(models.TextChoices):
    SPAIN = "ES", "ESPAÑA"
    FRANCE = "FR", "FRANCIA"


# 1Crear la clase heradea de djanbo.db

# 2 Crear la Tabla con sus datos, (celdas)
class InfoPersonal(models.Model):
    user = models.OneToOneField(settings.AUTH_USER_MODEL, on_delete=models.CASCADE, related_name="info-personal")
    document = models.CharField(max_length=15, unique=True, null=False, blank=False,
                                verbose_name="Documento (DNI, NIE, PASAPORTE, OTROS)", help_text="(Obligatorio)")
    adress = models.TextField(null=False, blank=False, verbose_name="Dirección", help_text="(Obligatorio)")
    birthday = models.DateField(verbose_name="Fecha de nacimiento", help_text="(Obligatorio)")
    phone = models.CharField(max_length=11, verbose_name="Teléfono", help_text="(Opcional)")
    age = models.PositiveBigIntegerField(
        null=False, blank=False, default=18, choices=[(n, n) for n in range(1, 101)], verbose_name="Edad"
    )
    city = models.ForeignKey("ciudades.model", on_delete=models.SET_NULL, null=True, blank=True, verbose_name="Ciudad",
                             help_text="(Obligatorio)")

    country = models.CharField(max_length=3, choices=PaisesChoices.choices, default=PaisesChoices.SPAIN,
                               verbose_name="País", help_text="True")

    class Meta:
        db_table = "info-personal",
        verbose_name = "Info_personal"
        verbose_name_plural = "Datos Personales"
        ordering = ("-country",)


    ##Queda
    def __str__(self):
        return f"{self.document} - ({self.city} / {self.country})"
