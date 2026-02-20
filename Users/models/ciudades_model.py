import secrets  # Módulo de Python para generar valores aleatorios seguros
from django.db import models  # Para definir modelos de Django


class CiudadModel(models.Model):
    # Campo de nombre de la ciudad
    name = models.CharField(
        max_length=50,          # Máximo 50 caracteres
        help_text="Obligatorio", # Texto de ayuda en formularios/admin
        verbose_name="name"     # Nombre legible en el admin
    )

    # Campo slug, usado para URLs y debe ser único
    slug = models.SlugField(
        max_length=50,          # Máximo 50 caracteres
        unique=True,            # No se permiten valores repetidos
        help_text="Obligatorio", # Texto de ayuda en formularios/admin
        verbose_name="slug"     # Nombre legible en el admin
    )

    class Meta:
        db_table = "ciudades"           # Nombre real de la tabla en la base de datos
        verbose_name = "Ciudad"         # Nombre legible de un registro
        verbose_name_plural = "Ciudades" # Nombre legible del conjunto de registros

    def __str__(self):
        return self.name  # Representación en string del objeto (útil en admin y relaciones)

    def save(self, *args, **kwargs):
        # Si no se ha asignado un slug, se genera uno automáticamente
        if not self.slug:
            # Genera un token hexadecimal aleatorio de 8 caracteres (4 bytes)
            prov = secrets.token_hex(4)

            # Verifica que el slug generado sea único
            while CiudadModel.objects.filter(slug=prov).exists():
                prov = secrets.token_hex(4)  # Genera otro hasta encontrar uno único

            # Asigna el slug único al objeto
            self.slug = prov

        # Llama al método save original de Django para guardar el objeto en la BD
        super().save(*args, **kwargs)
